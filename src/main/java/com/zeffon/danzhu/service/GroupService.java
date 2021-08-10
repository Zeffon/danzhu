package com.zeffon.danzhu.service;

import com.zeffon.danzhu.bo.RemarkBO;
import com.zeffon.danzhu.core.LocalUser;
import com.zeffon.danzhu.core.enumeration.GroupApplyStatus;
import com.zeffon.danzhu.core.enumeration.Status;
import com.zeffon.danzhu.dto.group.*;
import com.zeffon.danzhu.exception.http.ForbiddenException;
import com.zeffon.danzhu.exception.http.NotFoundException;
import com.zeffon.danzhu.exception.http.ParameterException;
import com.zeffon.danzhu.model.Groups;
import com.zeffon.danzhu.model.User;
import com.zeffon.danzhu.model.UserGroup;
import com.zeffon.danzhu.model.UserGroupApply;
import com.zeffon.danzhu.repository.GroupRepository;
import com.zeffon.danzhu.repository.UserGroupApplyRepository;
import com.zeffon.danzhu.repository.UserGroupRepository;
import com.zeffon.danzhu.repository.UserRepository;
import com.zeffon.danzhu.util.CodeUtil;
import com.zeffon.danzhu.util.EnumUtil;
import com.zeffon.danzhu.vo.ApplyVO;
import com.zeffon.danzhu.vo.GroupDetailVO;
import com.zeffon.danzhu.vo.GroupUserApplyVO;
import com.zeffon.danzhu.vo.GroupUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Create by Zeffon on 2020/10/1
 */
@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserGroupApplyRepository userGroupApplyRepository;
    @Autowired
    private UserRepository userRepository;

    public Page<Groups> listMyCreate(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Integer uid = LocalUser.getUser().getId();
        return groupRepository.findAllByUserId(uid, pageable);
    }

    public Page<Groups> listMyJoin(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Integer uid = LocalUser.getUser().getId();
        return groupRepository.findMyJoinGroup(uid, pageable);
    }

    @Transactional
    public Integer createGroup(CreateDTO createDTO) {
        Integer uid = LocalUser.getUser().getId();
        String code = CodeUtil.markGroupCode();
        Groups groups = Groups.builder()
                .userId(uid)
                .code(code)
                .title(createDTO.getTitle())
                .status(createDTO.getStatus())
                .summary(createDTO.getSummary())
                .userNumber(0)
                .build();
        groups.setRemark(createDTO.getRemark());
        this.groupRepository.save(groups);
        return groups.getId();
    }

    @Transactional
    public void disbandGroup(PureDTO pureDTO) {
        Groups groups = this.hasPermissionToGroup(pureDTO.getId());
        // 用户组编号确认解散
        if (!groups.getCode().equals(pureDTO.getCode())) {
            throw new ParameterException(30005);
        }
        groups.setDeleteTime(new Date());
        this.groupRepository.save(groups);
    }

    @Transactional
    public void updateGroup(UpdateDTO updateDTO) {
        Groups groups = this.hasPermissionToGroup(updateDTO.getId());
        groups.setTitle(updateDTO.getTitle());
        groups.setSummary(updateDTO.getSummary());
        groups.setStatus(updateDTO.getStatus());
        groups.setRemark(updateDTO.getRemark());
        this.groupRepository.save(groups);
    }

    public Boolean adminVerify(Integer gid) {
        Integer uid = LocalUser.getUser().getId();
        return this.groupRepository.findFirstByUserIdAndId(uid, gid).isPresent();
    }

    @Transactional
    public void adminAddUser(AddDTO addDTO) {
        hasPermissionToGroup(addDTO.getGid());
        User user = this.userRepository.findByCode(addDTO.getCode())
                .orElseThrow(() -> new NotFoundException(20002));
        canAddToGroup(user.getId(), addDTO.getGid());
        saveDataToUserGroup(user.getId(), addDTO.getGid(), addDTO.getRemark());
    }

    public Groups getDetail(Integer gid) {
        return this.groupRepository.findFirstById(gid)
                .orElseThrow(() -> new NotFoundException(30002));
    }

    public GroupDetailVO getMoreDetail(Integer gid) {
        return this.groupRepository.getMoreDetail(gid)
                .orElseThrow(() -> new NotFoundException(30002));
    }

    @Transactional
    public void adminModifyUser(IdDTO userDTO) {
        UserGroup userGroup = this.hasPermissionToUserGroup(userDTO.getId());
        userGroup.setRemark(userDTO.getRemark());
        this.userGroupRepository.save(userGroup);
    }

    @Transactional
    public void adminDeleteUser(DeleteDTO deleteDTO) {
        List<UserGroup> userGroups = this.hasPermissionToUserGroupList(deleteDTO.getIds());
        Integer groupId = null;
        for (UserGroup ug : userGroups) {
            groupId = ug.getGroupId();
            ug.setDeleteTime(new Date());
        }
        this.groupRepository.reduceUserNumber(groupId, userGroups.size());
        this.userGroupRepository.saveAll(userGroups);
    }

    public Page<GroupUserApplyVO> listOneGroupAllUserApply(Integer gid, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return this.userGroupApplyRepository.findAllByGroupId(gid, pageable);
    }

    /**
     * 为什么要选择返回int类型而不直接使用void：
     * 主要是因这里业务逻辑而决定的
     * 我们需要简单处理后的返回结果（单单将status设置为1或者2）
     * 但是我们还需要将status设置1时，数据库中已有该记录，
     * 并且我们还有必要将该条记录status设置为1，这样有利于统计未读申请
     * 所以本来是可以抛出异常，但是异常是会触发事务回滚的。
     * @param id
     * @param status
     * @return int
     */
    @Transactional
    public int adminChangeApplyStatus(Integer id, Integer status) {
        GroupApplyStatus applyStatus = EnumUtil.getByValue(status, GroupApplyStatus.class);
        UserGroupApply userGroupApply = this.hasPermissionToUserGroupApply(id);
        boolean isExist;
        if (userGroupApply.getStatus() != 1) {
            userGroupApply.setStatus(status);
            this.userGroupApplyRepository.save(userGroupApply);
            isExist = this.userGroupRepository.findFirstByUserIdAndGroupId
                    (userGroupApply.getUserId(), userGroupApply.getGroupId()).isPresent();
        } else {
            throw new ParameterException(30024);
        }
        switch (applyStatus) {
            case PASS:
                if(isExist) {
                    return 0;
                }
                UserGroup newUserGroup = UserGroup.builder()
                        .userId(userGroupApply.getUserId())
                        .groupId(userGroupApply.getGroupId())
                        .build();
                newUserGroup.setRemark(userGroupApply.getRemark());
                this.userGroupRepository.save(newUserGroup);
                this.groupRepository.addUserNumber(userGroupApply.getGroupId(), 1);
                break;
            case REFUSE:
                break;
        }
        return 1;
    }

    @Transactional
    public void adminJoinGroup(IdDTO joinDTO) {
        Integer uid = LocalUser.getUser().getId();
        Integer groupId = joinDTO.getId();
        hasPermissionToGroup(groupId);

        this.canAddToGroup(uid, groupId);
        this.saveDataToUserGroup(uid, groupId, joinDTO.getRemark());
    }

    @Transactional
    public void adminQuitGroup(Integer groupId) {
        Integer uid = LocalUser.getUser().getId();
        hasPermissionToGroup(groupId);
        UserGroup userGroup = findUserGroupByUserIdAndGroupId(uid, groupId);
        userGroup.setDeleteTime(new Date());
        this.userGroupRepository.save(userGroup);
    }

    @Transactional
    public void userAddGroup(IdDTO joinDTO) {
        Integer uid = LocalUser.getUser().getId();
        Groups groups = this.groupRepository.findById(joinDTO.getId())
                .orElseThrow(() -> new NotFoundException(30002));

        Status status = EnumUtil.getByValue(groups.getStatus(), Status.class);
        switch (status) {
            case ALLOW:
                this.canAddToGroup(uid, groups.getId());
                this.saveDataToUserGroup(uid, groups.getId(), groups.getRemark());
                break;
            case VERIFY:
                this.canAddToGroupApply(uid, groups.getId());
                this.saveDataToUserGroupApply(uid, groups.getId(), groups.getRemark());
                break;
            case REFUSE:
                throw new ForbiddenException(30003);
        }
    }

    public UserGroup getCurUser(Integer gid) {
        Integer uid = LocalUser.getUser().getId();
        return this.userGroupRepository.findFirstByUserIdAndGroupId(uid, gid)
                .orElseThrow(() -> new NotFoundException(30004));
    }

    @Transactional
    public void userQuitGroup(Integer groupId) {
        Integer uid = LocalUser.getUser().getId();
        UserGroup userGroup = findUserGroupByUserIdAndGroupId(uid, groupId);
        userGroup.setDeleteTime(new Date());
        this.groupRepository.reduceUserNumber(groupId, 1);
        this.userGroupRepository.save(userGroup);
    }

    @Transactional
    public void userUpdateInfo(IdDTO idDTO) {
        Integer uid = LocalUser.getUser().getId();
        UserGroup userGroup = this.userGroupRepository.findFirstByUserIdAndId(uid, idDTO.getId())
                .orElseThrow(() -> new NotFoundException(30004));
        userGroup.setRemark(idDTO.getRemark());
        this.userGroupRepository.save(userGroup);
    }

    public Page<GroupUserVO> listOneGroupAllUser(Integer groupId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return this.userGroupRepository.findAllByGroupId(groupId, pageable);
    }

    public Page<ApplyVO> getByStatus(int status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Integer uid = LocalUser.getUser().getId();
        if (status == GroupApplyStatus.ALL.getValue()) {
            return this.userGroupApplyRepository.findByUserId(uid, pageable);
        }
        return this.userGroupApplyRepository.findByUserIdAndStatus(uid, status, pageable);
    }

    private void canAddToGroup(Integer uid, Integer groupId) {
        this.userGroupRepository.findFirstByUserIdAndGroupId(uid, groupId)
                .ifPresent((ug) -> { throw new ParameterException(30007); });
    }

    private void saveDataToUserGroup(Integer uid, Integer groupId, List<RemarkBO> remark) {
        UserGroup userGroup = UserGroup.builder()
                .userId(uid)
                .groupId(groupId)
                .build();
        userGroup.setRemark(remark);
        this.groupRepository.addUserNumber(groupId, 1);
        this.userGroupRepository.save(userGroup);
    }

    private void canAddToGroupApply(Integer uid, Integer groupId) {
        this.userGroupApplyRepository.findUserIdAndGroupIdAndStatus(uid, groupId)
                .ifPresent((ug) -> { throw new ParameterException(30021); });
    }

    private void saveDataToUserGroupApply(Integer uid, Integer groupId, List<RemarkBO> remark) {
        UserGroupApply userGroupApply = UserGroupApply.builder()
                .userId(uid)
                .groupId(groupId)
                .status(GroupApplyStatus.WAIT.getValue())
                .build();
        userGroupApply.setRemark(remark);
        this.userGroupApplyRepository.save(userGroupApply);
    }

    private Groups hasPermissionToGroup(Integer groupId) {
        Integer uid = LocalUser.getUser().getId();
        return this.groupRepository.findFirstByUserIdAndId(uid, groupId)
                .orElseThrow(() -> new ForbiddenException(30003));
    }

    private List<UserGroup> hasPermissionToUserGroupList(List<Integer> ids) {
        Integer uid = LocalUser.getUser().getId();
        return this.userGroupRepository.hasPermissionToUserGroupList(uid, ids)
                .orElseThrow(() -> new ForbiddenException(30006));
    }

    private UserGroup hasPermissionToUserGroup(Integer id) {
        Integer uid = LocalUser.getUser().getId();
        return this.userGroupRepository.hasPermissionToUserGroup(uid, id)
                .orElseThrow(() -> new ForbiddenException(30006));
    }

    private UserGroupApply hasPermissionToUserGroupApply(Integer id) {
        Integer uid = LocalUser.getUser().getId();
        return this.userGroupApplyRepository.hasPermissionToUserGroupApply(uid, id)
                .orElseThrow(() -> new ForbiddenException(30023));
    }

    private UserGroup findUserGroupByUserIdAndGroupId(Integer uid, Integer groupId) {
        return this.userGroupRepository.findFirstByUserIdAndGroupId(uid, groupId)
                .orElseThrow(() -> new ParameterException(30004));
    }
}

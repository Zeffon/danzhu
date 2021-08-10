package com.zeffon.danzhu.api.v1;

import com.zeffon.danzhu.bo.PageCounter;
import com.zeffon.danzhu.core.UnifyResponse;
import com.zeffon.danzhu.core.interceptors.ScopeLevel;
import com.zeffon.danzhu.dto.group.*;
import com.zeffon.danzhu.model.Groups;
import com.zeffon.danzhu.model.UserGroup;
import com.zeffon.danzhu.model.UserGroupApply;
import com.zeffon.danzhu.service.GroupService;
import com.zeffon.danzhu.util.CommonUtil;
import com.zeffon.danzhu.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

/**
 * Create by Zeffon on 2020/10/1
 */
@RequestMapping("/group")
@RestController
@Validated
public class GroupController {
    @Autowired
    protected GroupService groupService;

    @ScopeLevel
    @GetMapping("/list/create")
    public PagingDozer<Groups, GroupVO> listMyCreate(@RequestParam(name = "start", defaultValue = "0") Integer start,
                                                     @RequestParam(name = "count", defaultValue = "10") Integer count) {
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Page<Groups> page = this.groupService.listMyCreate(pageCounter.getPage(), pageCounter.getCount());
        return new PagingDozer<>(page, GroupVO.class);
    }

    @ScopeLevel
    @GetMapping("/list/join")
    public PagingDozer<Groups, GroupVO> listMyJoin(@RequestParam(name = "start", defaultValue = "0") Integer start,
                                                   @RequestParam(name = "count", defaultValue = "10") Integer count) {
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Page<Groups> page = this.groupService.listMyJoin(pageCounter.getPage(), pageCounter.getCount());
        return new PagingDozer<>(page, GroupVO.class);
    }

    @ScopeLevel
    @PostMapping("/create")
    public IdVO createGroup(@RequestBody @Validated CreateDTO createDTO) {
        Integer gid = this.groupService.createGroup(createDTO);
        return new IdVO(gid);
    }

    @GetMapping("/detail/{gid}")
    public Groups getDetail(@PathVariable(name = "gid") @Positive(message = "{id.positive}") Integer gid) {
        return this.groupService.getDetail(gid);
    }

    @GetMapping("/detail/{gid}/more")
    public GroupDetailVO getMoreDetail(@PathVariable(name = "gid") @Positive(message = "{id.positive}") Integer gid) {
        return this.groupService.getMoreDetail(gid);
    }

    @ScopeLevel
    @DeleteMapping("/disband")
    public void disbandGroup(@RequestBody @Validated PureDTO pureDTO) {
        this.groupService.disbandGroup(pureDTO);
        UnifyResponse.deleteSuccess(0);
    }

    @ScopeLevel
    @PostMapping("/update")
    public void updateGroup(@RequestBody @Validated UpdateDTO updateDTO) {
        this.groupService.updateGroup(updateDTO);
        UnifyResponse.updateSuccess(0);
    }

    @ScopeLevel
    @GetMapping("/verify/{gid}")
    public Boolean adminVerify(@PathVariable(name = "gid") @Positive(message = "{id.positive}") Integer gid) {
        return this.groupService.adminVerify(gid);
    }

    @ScopeLevel
    @PostMapping("/admin/add")
    public void adminAddUser(@RequestBody @Validated AddDTO addDTO) {
        this.groupService.adminAddUser(addDTO);
        UnifyResponse.createSuccess(0);
    }

    @ScopeLevel
    @PostMapping("/admin/join")
    public void adminJoinGroup(@RequestBody @Validated IdDTO joinDTO) {
        this.groupService.adminJoinGroup(joinDTO);
        UnifyResponse.createSuccess(0);
    }

    @ScopeLevel
    @DeleteMapping("/admin/quit/{gid}")
    public void adminQuitGroup(@PathVariable(name = "gid") @Positive(message = "{id.positive}") Integer gid) {
        this.groupService.adminQuitGroup(gid);
        UnifyResponse.deleteSuccess(0);
    }

    @ScopeLevel
    @PostMapping("/admin/update")
    public void adminModifyUser(@RequestBody @Validated IdDTO userDTO) {
        this.groupService.adminModifyUser(userDTO);
        UnifyResponse.updateSuccess(0);
    }

    @ScopeLevel
    @DeleteMapping("/admin/delete")
    public void adminDeleteUser(@RequestBody @Validated DeleteDTO deleteDTO) {
        this.groupService.adminDeleteUser(deleteDTO);
        UnifyResponse.deleteSuccess(0);
    }

    @ScopeLevel
    @GetMapping("/admin/user/apply")
    public PagingDozer<GroupUserApplyVO, GroupUserApplyVO> adminGetUserApply(@RequestParam(name = "id") @Positive Integer id,
                                                                             @RequestParam(name = "start", defaultValue = "0") Integer start,
                                                                             @RequestParam(name = "count", defaultValue = "10") Integer count) {
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Page<GroupUserApplyVO> userGroupPage = this.groupService.listOneGroupAllUserApply(id, pageCounter.getPage(), pageCounter.getCount());
        return new PagingDozer<>(userGroupPage, GroupUserApplyVO.class);
    }

    @ScopeLevel
    @PostMapping("/admin/change/apply")
    public void adminChangeApplyStatus(@RequestBody @Validated ApplyDTO applyDTO) {
        int res = this.groupService.adminChangeApplyStatus(applyDTO.getId(), applyDTO.getStatus());
        if (res == 0) {
            UnifyResponse.updateSuccess(30007);
        }
        UnifyResponse.updateSuccess(0);
    }

    @ScopeLevel
    @PostMapping("/user/add")
    public void userAddGroup(@RequestBody @Validated IdDTO joinDTO) {
        this.groupService.userAddGroup(joinDTO);
        UnifyResponse.createSuccess(0);
    }


    @ScopeLevel
    @GetMapping("/user/{gid}")
    public UserGroup getCurUser(@PathVariable(name = "gid") @Positive(message = "{id.positive}") Integer gid) {
        return this.groupService.getCurUser(gid);
    }

    @ScopeLevel
    @PostMapping("/user/quit/{gid}")
    public void userQuitGroup(@PathVariable(name = "gid") @Positive(message = "{id.positive}") Integer gid) {
        this.groupService.userQuitGroup(gid);
        UnifyResponse.deleteSuccess(0);
    }

    @ScopeLevel
    @PostMapping("/user/update")
    public void userUpdateInfo(@RequestBody @Validated IdDTO idDTO) {
        this.groupService.userUpdateInfo(idDTO);
        UnifyResponse.updateSuccess(0);
    }

    @ScopeLevel
    @GetMapping("/list/user")
    public PagingDozer listOneGroupAllUser(@RequestParam(name = "id") @Positive Integer id,
                                                                     @RequestParam(name = "start", defaultValue = "0") Integer start,
                                                                     @RequestParam(name = "count", defaultValue = "10") Integer count) {
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Page<GroupUserVO> userGroupPage = this.groupService.listOneGroupAllUser(id, pageCounter.getPage(), pageCounter.getCount());
        return new PagingDozer<>(userGroupPage, GroupUserVO.class);
    }

    @ScopeLevel
    @GetMapping("/by/status/{status}")
    public PagingDozer getByStatus(@PathVariable int status,
                                   @RequestParam(name = "start", defaultValue = "0") Integer start,
                                   @RequestParam(name = "count", defaultValue = "10") Integer count) {
        PageCounter page = CommonUtil.convertToPageParameter(start, count);
        Page<ApplyVO> applyPage = this.groupService.getByStatus(status, page.getPage(), page.getCount());
        return new PagingDozer<>(applyPage, ApplyVO.class);
    }
}

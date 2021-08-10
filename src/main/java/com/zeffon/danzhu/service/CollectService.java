package com.zeffon.danzhu.service;

import com.zeffon.danzhu.bo.PieBO;
import com.zeffon.danzhu.core.LocalUser;
import com.zeffon.danzhu.core.enumeration.FileType;
import com.zeffon.danzhu.dto.collect.*;
import com.zeffon.danzhu.exception.http.ForbiddenException;
import com.zeffon.danzhu.exception.http.NotFoundException;
import com.zeffon.danzhu.model.Collect;
import com.zeffon.danzhu.model.FileLibrary;
import com.zeffon.danzhu.model.Groups;
import com.zeffon.danzhu.model.UserCollect;
import com.zeffon.danzhu.repository.*;
import com.zeffon.danzhu.util.CodeUtil;
import com.zeffon.danzhu.util.CommonUtil;
import com.zeffon.danzhu.vo.CollectUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Create by Zeffon on 2020/10/1
 */
@Service
public class CollectService {

    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private UserCollectRepository userCollectRepository;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileRepository fileRepository;

    public Page<Collect> listMyCreateCollect(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Integer uid = LocalUser.getUser().getId();
        return collectRepository.findAllByUserId(uid, pageable);
    }

    @Transactional
    public Integer createCollect(CreateDTO createDTO) {
        Integer uid = LocalUser.getUser().getId();
        String code = CodeUtil.markCollectCode();
        Date endTime = this.calculateEndTime(createDTO.getValidPeriod());
        Collect collect = Collect.builder()
                .userId(uid)
                .code(code)
                .title(createDTO.getTitle())
                .summary(createDTO.getSummary())
                .userLimit(createDTO.getUserLimit())
                .groupId(createDTO.getGroupId())
                .validPeriod(createDTO.getValidPeriod())
                .endTime(endTime)
                .userNumber(0) // 默认0，若有用户组，下面语句将会进行覆盖
                .fileType("0") // 文件类型设置不限
                .build();
        collect.setRemark(new ArrayList<>()); // 让数据库不保存NULL，而是[]。若有用户组，下面语句将会进行覆盖
        this.collectRepository.save(collect);
        boolean hasGroup = createDTO.getGroupId() != null;
        if (hasGroup) {
            List<UserCollect> userCollects;
            Groups groups = this.groupService.getDetail(createDTO.getGroupId());
            List<Integer> userIds = this.userGroupRepository.findUserIdsByGroupId(createDTO.getGroupId());
            userCollects = userIds.stream()
                    .map(userId -> {
                        UserCollect userCollect = UserCollect.builder()
                                .collectId(collect.getId())
                                .userId(userId)
                                .status(FileType.YES_GROUP_ING.getValue())
                                .build();
                        userCollect.setRemark(groups.getRemark());
                        return userCollect;
                    }).collect(Collectors.toList());
            collect.setRemark(groups.getRemark());
            collect.setUserNumber(userIds.size());
            this.collectRepository.save(collect);
            this.userCollectRepository.saveAll(userCollects);
        }
        return collect.getId();
    }

    public Collect getCollectDetail(Integer cid) {
        return this.collectRepository.findById(cid)
                .orElseThrow(() -> new NotFoundException(40002));
    }

    @Transactional
    public void disbandCollect(PureDTO pureDTO) {
        Integer uid = LocalUser.getUser().getId();
        this.collectRepository.batchDeleteById(uid, pureDTO.getIds(), new Date());
    }

    @Transactional
    public void updateCollect(UpdateDTO updateDTO) {
        Collect collect = this.hasPermissionToCollect(updateDTO.getCid());

        Calendar endTimeCal = CommonUtil.dataToCalendar(collect.getEndTime());
        Date endTime = CommonUtil.addSomeDays(endTimeCal, updateDTO.getValidPeriod()).getTime();
        collect.setTitle(updateDTO.getTitle());
        collect.setSummary(updateDTO.getSummary());
        collect.setUserLimit(updateDTO.getUserLimit());
        collect.setValidPeriod(collect.getValidPeriod() + updateDTO.getValidPeriod());
        collect.setEndTime(endTime);
        this.collectRepository.save(collect);
    }

    @Transactional
    public void adminDeleteUser(DeleteDTO deleteDTO) {
        this.hasPermissionToCollect(deleteDTO.getCid());
        UserCollect userCollect = this.findUserCollectByUserIdAndCollectId(deleteDTO.getUid(), deleteDTO.getCid());
        userCollect.setDeleteTime(new Date());
        this.userCollectRepository.save(userCollect);
    }

    @Transactional
    public void userAddCollect(JoinDTO joinDTO) {
        Integer uid = LocalUser.getUser().getId();
        UserCollect userCollect = this.userCollectRepository.findFirstByUserIdAndCollectIdAndStatus(uid, joinDTO.getId(), 0);
        if (userCollect != null) {
            return;
        }

        Collect collect = this.getCollectDetail(joinDTO.getId());
        UserCollect userCollectNew = UserCollect.builder()
                .userId(uid)
                .collectId(joinDTO.getId())
                .status(FileType.NO_GROUP_ING.getValue())
                .build();
        userCollectNew.setRemark(collect.getRemark());
        this.userCollectRepository.save(userCollectNew);
        this.collectRepository.addUserNumber(collect.getId(), 1);
    }

    @Transactional
    public void userQuitCollect(Integer collectId) {
        Integer uid = LocalUser.getUser().getId();
        UserCollect userCollect = this.findUserCollectByUserIdAndCollectId(uid, collectId);
        userCollect.setDeleteTime(new Date());
        this.userCollectRepository.save(userCollect);
        this.collectRepository.reduceUserNumber(collectId, 1);
    }

    public Page<CollectUserVO> listOneCollectAllUser(Integer cid, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return this.userCollectRepository.findAllByCollectId(cid, pageable);
    }


    public int[][] getStatByBar(Integer cid) {
        List<UserCollect> userCollectList = this.userCollectRepository.findAllByCollectId(cid);

        int[][] arr = new int[2][2];
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;

        for (UserCollect item : userCollectList) {
            if (item.getStatus().equals(FileType.YES_GROUP_ED.getValue())) {
                a += 1;
            }
            if (item.getStatus().equals(FileType.YES_GROUP_ING.getValue())) {
                b += 1;
            }
            if (item.getStatus().equals(FileType.NO_GROUP_ED.getValue())) {
                c += 1;
            }
            if (item.getStatus().equals(FileType.NO_GROUP_ING.getValue())) {
                d += 1;
            }
        }

        arr[0][0] = a;
        arr[0][1] = b;
        arr[1][0] = c;
        arr[1][1] = d;
        return arr;
    }

    public String getStatByGauge(Integer cid) {
        List<UserCollect> userCollectList = this.userCollectRepository.findAllByCollectId(cid);
        int subNum = 0;
        int unsubNum = 0;
        for (UserCollect item : userCollectList) {
            if (item.getStatus().equals(FileType.YES_GROUP_ED.getValue())) {
                subNum += 1;
            }
            if (item.getStatus().equals(FileType.NO_GROUP_ED.getValue())) {
                unsubNum += 1;
            }
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format((float) subNum / (subNum + unsubNum));
    }

    public LinkedList<Integer> getStatByLine(Integer cid) {
        Collect collect = this.collectRepository.getOne(cid);
        List<FileLibrary> fileLibraryList = this.fileRepository.findAllByCollectIdAndDeleteTimeIsNull(cid);
        LinkedList<Integer> list = new LinkedList<>();
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        int f = 0;
        int g = 0;
        for (FileLibrary file : fileLibraryList) {
            int day = CommonUtil.differentDaysByMillisecond(collect.getCreateTime(), file.getCreateTime());
            if (day == 1) {
                a += 1;
            } else if (day == 2) {
                b += 1;
            } else if (day == 3) {
                c += 1;
            } else if (day == 4) {
                d += 1;
            } else if (day == 5) {
                e += 1;
            } else if (day == 6) {
                f += 1;
            } else {
                g += 1;
            }
        }
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);
        list.add(f);
        list.add(g);
        return list;
    }


    public int[] getStatByPie(Integer cid) {
        List<Integer> counts = this.fileRepository.findAllByCollectIdAndDeleteTimeIsNullAndGroupByUserId(cid);
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;

        for (Integer count : counts) {
            if (count == 1) {
                a += 1;
            } else if (count == 2) {
                b += 1;
            } else if (count == 3) {
                c += 1;
            } else if (count == 4) {
                d += 1;
            } else {
                e += 1;
            }
        }

        int[] arr = new int[]{a, b, c, d, e};
        return arr;
    }

    private Date calculateEndTime(Integer validPeriod) {
        Calendar now = Calendar.getInstance(); // 当前时间
        return CommonUtil.addSomeDays(now, validPeriod).getTime();
    }

    private Collect hasPermissionToCollect(Integer collectId) {
        Integer uid = LocalUser.getUser().getId();
        return this.collectRepository.findFirstByUserIdAndId(uid, collectId)
                .orElseThrow(() -> new ForbiddenException(40003));
    }

    private UserCollect findUserCollectByUserIdAndCollectId(Integer uid, Integer collectId) {
        return this.userCollectRepository.findFirstByUserIdAndCollectId(uid, collectId)
                .orElseThrow(() -> new NotFoundException(40004));
    }

}

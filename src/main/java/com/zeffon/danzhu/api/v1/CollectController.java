package com.zeffon.danzhu.api.v1;

import com.zeffon.danzhu.bo.PageCounter;
import com.zeffon.danzhu.core.UnifyResponse;
import com.zeffon.danzhu.core.interceptors.ScopeLevel;
import com.zeffon.danzhu.dto.collect.*;
import com.zeffon.danzhu.model.Collect;
import com.zeffon.danzhu.service.CollectService;
import com.zeffon.danzhu.util.CommonUtil;
import com.zeffon.danzhu.vo.CollectListVO;
import com.zeffon.danzhu.vo.CollectUserVO;
import com.zeffon.danzhu.vo.IdVO;
import com.zeffon.danzhu.vo.PagingDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.LinkedList;

/**
 * Create by Zeffon on 2020/10/1
 */
@RequestMapping("/collect")
@RestController
public class CollectController {

    @Autowired
    private CollectService collectService;

    @ScopeLevel
    @GetMapping("/list/collect")
    public PagingDozer listMyCreateCollect(@RequestParam(name = "start", defaultValue = "0") Integer start,
                                           @RequestParam(name = "count", defaultValue = "10") Integer count) {
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Page<Collect> page = this.collectService.listMyCreateCollect(pageCounter.getPage(), pageCounter.getCount());
        return new PagingDozer<>(page, CollectListVO.class);
    }

    @GetMapping("/detail/{cid}")
    public Collect getCollectDetail(@PathVariable(name = "cid") @Positive(message = "{id.positive}") Integer cid) {
        return this.collectService.getCollectDetail(cid);
    }

    @ScopeLevel
    @PostMapping("/create")
    public IdVO createCollect(@RequestBody @Validated CreateDTO createDTO) {
        Integer cid = this.collectService.createCollect(createDTO);
        return new IdVO(cid);
    }

    @ScopeLevel
    @DeleteMapping("/disband")
    public void disbandCollect(@RequestBody @Validated PureDTO pureDTO) {
        this.collectService.disbandCollect(pureDTO);
        UnifyResponse.deleteSuccess(0);
    }

    @ScopeLevel
    @PostMapping("/update")
    public void updateCollect(@RequestBody @Validated UpdateDTO updateDTO) {
        this.collectService.updateCollect(updateDTO);
        UnifyResponse.updateSuccess(0);
    }

    @ScopeLevel
    @DeleteMapping("/admin/delete")
    public void adminDeleteUser(@RequestBody @Validated DeleteDTO deleteDTO) {
        this.collectService.adminDeleteUser(deleteDTO);
        UnifyResponse.deleteSuccess(0);
    }

    @ScopeLevel
    @PostMapping("/user/add")
    public void userAddCollect(@RequestBody @Validated JoinDTO joinDTO) {
        this.collectService.userAddCollect(joinDTO);
        UnifyResponse.createSuccess(0);
    }

    @ScopeLevel
    @PostMapping("/user/quit/{cid}")
    public void userQuitCollect(@PathVariable(name = "cid") @Positive(message = "{id.positive}") Integer cid) {
        this.collectService.userQuitCollect(cid);
        UnifyResponse.deleteSuccess(0);
    }

    @ScopeLevel
    @GetMapping("/list/user")
    public PagingDozer listOneCollectAllUser(@RequestParam(name = "cid") @Positive Integer cid,
                                             @RequestParam(name = "start", defaultValue = "0") Integer start,
                                             @RequestParam(name = "count", defaultValue = "10") Integer count) {
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Page<CollectUserVO> userCollectPage = this.collectService.listOneCollectAllUser(cid, pageCounter.getPage(), pageCounter.getCount());
        return new PagingDozer<>(userCollectPage, CollectUserVO.class);
    }

    @GetMapping("/stat/bar/{cid}")
    public int[][] getStatByBar(@PathVariable(name = "cid") @Positive Integer cid) {

        return this.collectService.getStatByBar(cid);
    }

    @GetMapping("/stat/gauge/{cid}")
    public String getStatByGauge(@PathVariable(name = "cid") @Positive Integer cid) {

        return this.collectService.getStatByGauge(cid);
    }

    @GetMapping("/stat/line/{cid}")
    public LinkedList<Integer> getStatByLine(@PathVariable(name = "cid") @Positive Integer cid) {

        return this.collectService.getStatByLine(cid);
    }

    @GetMapping("/stat/pie/{cid}")
    public int[] getStatByPie(@PathVariable(name = "cid") @Positive Integer cid) {

        return this.collectService.getStatByPie(cid);
    }
}

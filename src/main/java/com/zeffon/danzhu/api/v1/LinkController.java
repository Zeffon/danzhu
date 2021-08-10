package com.zeffon.danzhu.api.v1;

import com.zeffon.danzhu.core.interceptors.ScopeLevel;
import com.zeffon.danzhu.dto.link.LinkCreateDTO;
import com.zeffon.danzhu.model.Link;
import com.zeffon.danzhu.model.User;
import com.zeffon.danzhu.service.LinkService;
import com.zeffon.danzhu.vo.FileUserVO;
import com.zeffon.danzhu.vo.LinkVO;
import com.zeffon.danzhu.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by Zeffon on 2020/10/3
 */
@RestController
@RequestMapping("link")
@Validated
public class LinkController {
    @Autowired
    private LinkService linkService;

    @ScopeLevel
    @PostMapping("/create")
    public LinkVO create(@RequestBody @Validated LinkCreateDTO createDTO) {
        Link link = this.linkService.create(createDTO);
        return new LinkVO(link);
    }

    // 检验链接存在或者是否过期
    @GetMapping("/valid/{code}")
    public UserVO valid(@PathVariable @NotBlank String code) {
        User user = this.linkService.valid(code);
        return new UserVO(user);
    }

    @ScopeLevel
    @GetMapping("/user}")
    public UserVO getShareUser() {
        User user = this.linkService.getShareUser();
        return new UserVO(user);
    }

    @ScopeLevel
    @GetMapping("/list/{code}")
    public List<FileUserVO> list(@PathVariable @NotBlank String code) {
        return this.linkService.list(code);
    }
}

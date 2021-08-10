package com.zeffon.danzhu.api.v1;

import com.zeffon.danzhu.bo.WxInfoBO;
import com.zeffon.danzhu.core.UnifyResponse;
import com.zeffon.danzhu.core.interceptors.ScopeLevel;
import com.zeffon.danzhu.model.User;
import com.zeffon.danzhu.service.UserService;
import com.zeffon.danzhu.vo.CodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Create by Zeffon on 2020/10/29
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @ScopeLevel
    @GetMapping("/code")
    public CodeVO getUserCode() {
        User user = this.userService.getUserById();
        return new CodeVO(user.getCode());
    }

    @ScopeLevel
    @PostMapping("/wx_info")
    public void updateUserWxInfo(@RequestBody WxInfoBO wxInfo) {
        this.userService.updateUserWxInfo(wxInfo);
        UnifyResponse.updateSuccess(0);
    }
}

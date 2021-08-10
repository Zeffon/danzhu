package com.zeffon.danzhu.api.v1;

import com.zeffon.danzhu.dto.TokenDTO;
import com.zeffon.danzhu.dto.TokenGetDTO;
import com.zeffon.danzhu.exception.http.NotFoundException;
import com.zeffon.danzhu.service.WxAuthenticationService;
import com.zeffon.danzhu.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by Zeffon on 2020/10/1
 */
@RequestMapping("/token")
@RestController
public class TokenController {

    @Autowired
    private WxAuthenticationService wxAuthenticationService;

    @PostMapping("")
    public Map<String, String> getToken(@RequestBody @Validated TokenGetDTO userData) {
        Map<String, String> map = new HashMap<>();
        String token = null;

        switch (userData.getType()) {
            case USER_WX:
                token = wxAuthenticationService.code2Session(userData);
                break;
            case USER_EMAIL:
                break;
            case LINK_CODE:
                token = wxAuthenticationService.code2Token(userData);
                break;
            default:
                throw new NotFoundException(10003);
        }
        map.put("token", token);

        return map;
    }

    @PostMapping("/verify")
    public Map<String, Boolean> verify(@RequestBody TokenDTO token) {
        Map<String, Boolean> map = new HashMap<>();
        Boolean valid = JwtToken.verifyToken(token.getToken());
        map.put("is_valid", valid);
        return map;
    }

    @GetMapping("/verify2")
    public Map<String, Boolean> verify2(@RequestHeader("Authorization") String bearerToken) {
        Map<String, Boolean> map = new HashMap<>();
        Boolean valid = JwtToken.verifyToken2(bearerToken);
        map.put("is_valid", valid);
        return map;
    }
}

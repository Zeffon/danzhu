package com.zeffon.danzhu.api.v1;

import com.zeffon.danzhu.dto.WxaDTO;
import com.zeffon.danzhu.service.WxAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

/**
 * 小程序码
 * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/qr-code/wxacode.getUnlimited.html
 * Create by Zeffon on 2020/11/28
 */
@RestController
@RequestMapping("wxa")
@RequiredArgsConstructor
public class WxaController {

    private final WxAuthenticationService wxAuthenticationService;

    @PostMapping("")
    public String getWxaCode(@RequestBody WxaDTO wxaDTO) {
        return wxAuthenticationService.getWxaCode(wxaDTO);
    }

    @GetMapping("")
    public String getAccessToken() {
        return wxAuthenticationService.getAccessToken();
    }
}

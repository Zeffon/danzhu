package com.zeffon.danzhu.api.v1;

import com.zeffon.danzhu.core.interceptors.ScopeLevel;
import com.zeffon.danzhu.service.OssService;
import com.zeffon.danzhu.vo.OssCallbackVO;
import com.zeffon.danzhu.vo.OssPolicyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
/**
 * Create by Zeffon on 2020/11/3
 */
@RestController
@RequestMapping("/oss")
public class OssController {

    @Autowired
    private OssService ossService;

    @ScopeLevel
    @GetMapping("/policy")
    public OssPolicyVO policy(){
        return ossService.policy();
    }

    @PostMapping("/callback")
    public OssCallbackVO callback(HttpServletRequest request, String ossCallbackBody){
        System.out.println(ossCallbackBody);
        System.out.println("你回调了吗？");
        return ossService.callback(request);
    }
}

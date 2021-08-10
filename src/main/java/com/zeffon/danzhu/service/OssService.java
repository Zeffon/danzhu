package com.zeffon.danzhu.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.zeffon.danzhu.core.LocalUser;
import com.zeffon.danzhu.dto.OssCallbackDTO;
import com.zeffon.danzhu.util.GenericAndJson;
import com.zeffon.danzhu.vo.OssCallbackVO;
import com.zeffon.danzhu.vo.OssPolicyVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

/**
 * Create by Zeffon on 2020/11/3
 */
@Service
public class OssService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OssService.class);

    @Value("${aliyun.oss.policy.expires}")
    private int EXPIRES;
    @Value(("${aliyun.oss.maxSize}"))
    private int MAX_SIZE;
    @Value("${aliyun.oss.callback}")
    private String CALLBACK;
    @Value("${aliyun.oss.bucketName}")
    private String BUCKET_NAME;
    @Value("${aliyun.oss.endpoint}")
    private String ENDPOINT;
    @Value("${aliyun.oss.dir.prefix}")
    private String DIR_PREFIX;

    @Autowired
    private OSSClient ossClient;

    public OssPolicyVO policy() {
        Integer uid = LocalUser.getUser().getId();

        OssPolicyVO ossPolicyVO = new OssPolicyVO();
        // 存储目录
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String dir = DIR_PREFIX + year + "/" + month +"/" + day + "/" + hour + "/";
        // 签名有效期
        long expireEndTime = System.currentTimeMillis() + EXPIRES * 1000;
        Date expiration = new Date(expireEndTime);
        // 文件大小
        int maxSize = MAX_SIZE * 1024 * 1024;
        // 回调
        OssCallbackDTO ossCallbackDTO = new OssCallbackDTO();
        ossCallbackDTO.setCallbackUrl(CALLBACK);
        ossCallbackDTO.setCallbackBody("filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
        ossCallbackDTO.setCallbackBodyType("application/x-www-form-urlencoded");
        // 提交节点
        String action = "https://"+ ENDPOINT;
        try {
            PolicyConditions policyConditions = new PolicyConditions();
            policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE,0,maxSize);
            policyConditions.addConditionItem(MatchMode.StartWith,PolicyConditions.COND_KEY,dir);
            String  postPolicy = ossClient.generatePostPolicy(expiration,policyConditions);
            byte[] binaryData = new byte[0];
            binaryData = postPolicy.getBytes("utf-8");
            String policy = BinaryUtil.toBase64String(binaryData);
            String signature = ossClient.calculatePostSignature(postPolicy);
            String callbackData = BinaryUtil.toBase64String(GenericAndJson.objectToJson(ossCallbackDTO).toString().getBytes("utf-8"));
            // 返回结果
            ossPolicyVO.setAccessKeyId(ossClient.getCredentialsProvider().getCredentials().getAccessKeyId());
            ossPolicyVO.setPolicy(policy);
            ossPolicyVO.setSignature(signature);
            ossPolicyVO.setDir(dir);
            ossPolicyVO.setCallback(callbackData);
            ossPolicyVO.setHost(action);
        } catch (Exception e) {
            LOGGER.error("签名生成失败",e);
        }
        return ossPolicyVO;
    }

    public OssCallbackVO callback(HttpServletRequest request) {
        LOGGER.info("##################    是否回调了  ##################");
        OssCallbackVO ossCallbackVO = new OssCallbackVO();
        String filename = request.getParameter("filename");
        filename ="https://".concat(BUCKET_NAME).concat(".").concat(ENDPOINT).concat("/").concat(filename);
        ossCallbackVO.setFilename(filename);
        ossCallbackVO.setSize(request.getParameter("size"));
        ossCallbackVO.setMimeType(request.getParameter("mimeType"));
        LOGGER.info(GenericAndJson.objectToJson(ossCallbackVO));
        return ossCallbackVO;
    }
}

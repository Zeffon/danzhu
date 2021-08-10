package com.zeffon.danzhu.api.v1;

import com.zeffon.danzhu.bo.PageCounter;
import com.zeffon.danzhu.core.LocalUser;
import com.zeffon.danzhu.core.UnifyResponse;
import com.zeffon.danzhu.core.interceptors.ScopeLevel;
import com.zeffon.danzhu.dto.file.PureDTO;
import com.zeffon.danzhu.dto.file.Upload2DTO;
import com.zeffon.danzhu.dto.file.UploadDTO;
import com.zeffon.danzhu.model.FileLibrary;
import com.zeffon.danzhu.model.User;
import com.zeffon.danzhu.service.FileService;
import com.zeffon.danzhu.service.UserService;
import com.zeffon.danzhu.util.CommonUtil;
import com.zeffon.danzhu.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by Zeffon on 2020/10/8
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    @ScopeLevel
    @PostMapping("/user/upload")
    public void userUploadFile(@RequestBody @Validated UploadDTO uploadDTO) {

        this.fileService.userUploadFile(uploadDTO);
        UnifyResponse.createSuccess(0);
    }

    @ScopeLevel
    @PostMapping("admin/upload")
    public void adminUploadFile(@RequestBody @Validated Upload2DTO upload2DTO) {

        this.fileService.adminUploadFile(upload2DTO);
        UnifyResponse.createSuccess(0);
    }

    @ScopeLevel
    @DeleteMapping("/admin/delete/{fid}")
    public void adminDeleteFile(@PathVariable @Positive Integer fid) {
        this.fileService.adminDeleteFile(fid);
        UnifyResponse.deleteSuccess(0);
    }

    @ScopeLevel
    @PostMapping("/update/online")
    public void updateOnline(@RequestBody @Validated PureDTO pureDTO) {
        this.fileService.updateOnline(pureDTO);
        UnifyResponse.updateSuccess(0);
    }

    @ScopeLevel
    @GetMapping("/list/{uid}/{cid}")
    public Map listFile(@PathVariable(name = "uid") @Positive(message = "{id.positive}") Integer uid,
                        @PathVariable(name = "cid") @Positive(message = "{id.positive}") Integer cid) {
        User user = this.userService.getUserById(uid);
        List<FileLibrary> list = this.fileService.listFile(uid, cid);
        Map<String, Object> map = new HashMap<>();
        map.put("cid", cid);
        map.put("uid", user.getId());
        map.put("avatar", user.getWxInfo().getAvatarUrl());
        map.put("nickName", user.getWxInfo().getNickName());
        map.put("files", FileVO.getList(list));
        return map;
    }

    @ScopeLevel
    @GetMapping("/page/send")
    public PagingDozer listMySendFile(@RequestParam(name = "start", defaultValue = "0") Integer start,
                                      @RequestParam(name = "count", defaultValue = "20") Integer count) {
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Page<FileLibrary> page = this.fileService.listMySendFile(pageCounter.getPage(), pageCounter.getCount());
        return new PagingDozer<>(page, FileVO.class);
    }

    @ScopeLevel
    @GetMapping("/list/month")
    public LinkedHashMap<String, List<FileVO>> listMonthFile() {
        return this.fileService.listMonthFile();
    }

    @ScopeLevel
    @GetMapping("/list/letter")
    public LinkedHashMap<String, List<FileVO>> listLetterFile() {
        return this.fileService.listLetterFile();
    }

    @ScopeLevel
    @GetMapping("/list/by/{cid}")
    public Map listFileByCid(@PathVariable(name = "cid") Integer cid) {
        return this.fileService.listFileByCid(cid);
    }

    @ScopeLevel
    @GetMapping("/page/trash")
    public PagingDozer pageFileInTrash(@RequestParam(name = "start", defaultValue = "0") Integer start,
                                       @RequestParam(name = "count", defaultValue = "10") Integer count) {
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Page<FileLibrary> page = this.fileService.pageFileInTrash(pageCounter.getPage(), pageCounter.getCount());
        return new PagingDozer<>(page, FileTrashVO.class);
    }

    @ScopeLevel
    @GetMapping("/list/trash")
    public List<FileTrashVO> listFileInTrash() {
        List<FileLibrary> list = this.fileService.listFileInTrash();
        return  FileTrashVO.getList(list);
    }

    @ScopeLevel
    @GetMapping("/list/share/{uid}")
    public Map<String, Object> listShareFile(@PathVariable(name = "uid") Integer uid) {
        User user = this.userService.getUserById(uid);
        List<FileLibrary> list = this.fileService.listShareFileByUserId(uid);
        Map<String, Object> map = new HashMap<>();
        map.put("uid", user.getId());
        map.put("avatar", user.getWxInfo().getAvatarUrl());
        map.put("nickName", user.getWxInfo().getNickName());
        map.put("files", FileVO.getList(list));
        return map;
    }

    @ScopeLevel
    @GetMapping("/list/share/my")
    public Map<String, Object> listMyShareFile() {
        User user = LocalUser.getUser();
        List<FileLibrary> list = this.fileService.listShareFileByUserId(user.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("uid", user.getId());
        map.put("avatar", user.getWxInfo().getAvatarUrl());
        map.put("nickName", user.getWxInfo().getNickName());
        map.put("files", FileVO.getList(list));
        return map;
    }

    @ScopeLevel
    @GetMapping("/count")
    public FileCountVO countFile() {
        return this.fileService.countFile();
    }

    @ScopeLevel
    @PostMapping("/restore/{id}")
    public void restoreFile(@PathVariable(name = "id") @Positive(message = "{id.positive}") Integer fid) {
        this.fileService.restoreFile(fid);
        UnifyResponse.updateSuccess(0);
    }

    @ScopeLevel
    @DeleteMapping("/real/{id}")
    public void realDeleteFile(@PathVariable(name = "id") @Positive(message = "{id.positive}") Integer fid) {
        this.fileService.realDeleteFile(fid);
        UnifyResponse.deleteSuccess(0);
    }



}

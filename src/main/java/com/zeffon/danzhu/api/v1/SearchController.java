package com.zeffon.danzhu.api.v1;

import com.zeffon.danzhu.bo.HotWord;
import com.zeffon.danzhu.core.interceptors.ScopeLevel;
import com.zeffon.danzhu.manager.es7.ES7Service;
import com.zeffon.danzhu.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Create by Zeffon on 2020/10/4
 */
@RequestMapping("/search")
@RestController
@Validated
public class SearchController {

    private final ES7Service es7Service;
    private final SearchService searchService;

    public SearchController(SearchService searchService, ES7Service es7Service) {
        this.searchService = searchService;
        this.es7Service = es7Service;
    }

    @GetMapping("/{q}")
    public Object searchGroupByCode(@PathVariable(name = "q") @NotBlank(message = "关键字不允许为空") String q) {
        return this.searchService.search(q);
    }

    @GetMapping("/hotWord")
    public List<HotWord> hotWord() {
        return this.searchService.getHotWord();
    }

    @PostMapping("/createIndex")
    public String createUserIndex(@RequestParam(value = "index") String index) throws Exception {
        es7Service.createIndex(index);
        return "ok";
    }
}

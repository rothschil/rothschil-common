package io.github.rothschil.web.controller;

import io.github.rothschil.web.compoent.AsyncTask;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Hidden
@Slf4j
@RequestMapping("/iserv")
@RestController
public class IndexController {

    @Value("${github.active:test}")
    protected String active;

    @Autowired
    protected AsyncTask asyncTask;

    @GetMapping("/index")
    public String index(){
        asyncTask.async();
        String uuid = UUID.randomUUID().toString();
        log.info("UUID= {}", uuid);
        return active+":->"+uuid;
    }


}

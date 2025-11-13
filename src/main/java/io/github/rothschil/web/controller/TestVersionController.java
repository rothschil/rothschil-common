package io.github.rothschil.web.controller;

import io.github.rothschil.common.annotation.ApiVersion;
import io.github.rothschil.domain.vo.UserVo;
import io.github.rothschil.web.compoent.TestCompoent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@ApiVersion
@RequestMapping(value = "/{version}/test")
public class TestVersionController {
    TestCompoent testCompoent;

    protected TestVersionController(TestCompoent testCompoent){
        this.testCompoent=testCompoent;
    }



    @GetMapping(value = "one")
    @ApiVersion("1.1")
    public String query2(){
        return "test api v1.1";
    }


    @GetMapping(value = "one")
    @ApiVersion("3.1")
    public String query3(){
        return "test api v3.1";
    }
}

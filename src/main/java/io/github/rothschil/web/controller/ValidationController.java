package io.github.rothschil.web.controller;

import io.github.rothschil.common.constant.Constant;
import io.github.rothschil.common.utils.DateUtils;
import io.github.rothschil.domain.database.entity.TblCdmaHlr;
import io.github.rothschil.domain.database.vo.UserVo;
import io.github.rothschil.web.compoent.AsyncTask;
import io.github.rothschil.web.compoent.TestCompoent;
import io.github.rothschil.web.compoent.host.CdmaHlrResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

/**
 * //todo 添加类描述
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Slf4j
@RestController
public class ValidationController {


    TestCompoent testCompoent;

    @Autowired
    protected AsyncTask asyncTask;

    protected ValidationController(TestCompoent testCompoent){
        this.testCompoent=testCompoent;
    }


    @PostMapping("/addUser")
    public String addUser(@RequestBody @Validated UserVo user) {
        return Constant.NUM_1;
    }

    @GetMapping(value = "one")
    public UserVo query(){
        asyncTask.async();
        int id = new Random().nextInt(90) + 10;
        String acct = DateUtils.getDate(DateUtils.TRANS_PATTERN);
        UserVo userVo = new UserVo();
        userVo.setEmail("wongs@qq.com");
        userVo.setAccount(acct);
        userVo.setPassword("Dog");
        userVo.setId(id);
        userVo.setPhone("18956061234");
        return testCompoent.get(userVo);
    }



    @Operation(summary = "根据手机号获取本地网 不推荐使用，数据存在延迟性，建议使用方慎重使用该功能")
    @Parameters({
            @Parameter(name = "phone", description = "手机号码", required = true)
    })
    @GetMapping(value = "/host/{phone}")
    public CdmaHlrResp testLocalhost(@PathVariable(value = "phone") String phone){
        return testCompoent.testHost(phone);
    }

}

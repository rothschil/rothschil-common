package io.github.rothschil.web.controller;


import cn.hutool.core.util.PhoneUtil;
import io.github.rothschil.common.base.dto.RestBean;
import io.github.rothschil.common.exception.CommonException;
import io.github.rothschil.common.response.enums.Status;
import io.github.rothschil.domain.database.entity.TblCdmaHlr;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "个性化定制服务")
@Slf4j
@RestController
public class ApiController extends BaseController{



    @Operation(summary = "根据手机号获取本地网 不推荐使用，数据存在延迟性，建议使用方慎重使用该功能")
    @Parameters({
            @Parameter(name = "phone", description = "手机号码", required = true)
    })
    @RequestMapping(value = "/hlr/{phone}",method = RequestMethod.GET)
    public TblCdmaHlr hlr(@PathVariable(value = "phone") String phone){
        if(!PhoneUtil.isMobile(phone)){
            throw new CommonException(Status.PARAMS_IS_INVALID,"参数非手机号码类，请重新输入");
        }
        String prefix = phone.substring(0,7);
        return tblCdmaHlrService.getHlrByPhoneprefix(prefix);
    }


    @Hidden
    @RequestMapping(value = "/api/gracefulshutdown",method = RequestMethod.GET)
    public RestBean gracefulShutdown(){
        // 模拟业务耗时处理流程
        try {
            Thread.sleep(15 * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new RestBean(200,"DemoCase");
    }


}

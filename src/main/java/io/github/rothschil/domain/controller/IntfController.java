package io.github.rothschil.domain.controller;

import io.github.rothschil.common.annotation.SelectorDataSource;
import io.github.rothschil.common.constant.DataSourceNamesConstant;
import io.github.rothschil.domain.entity.Intf;
import io.github.rothschil.domain.entity.TblCdmaHlr;
import io.github.rothschil.domain.service.IntfService;
import io.github.rothschil.domain.service.TblCdmaHlrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "测试接口")
@Slf4j
@RestController
@RequestMapping("/intf")
public class IntfController {


    @Autowired
    private IntfService intfService;

    @Autowired
    private TblCdmaHlrService tblCdmaHlrService;

    @RequestMapping(value = "/findOne/{id}",method = RequestMethod.GET)
    public Intf findOne(@PathVariable(value = "id") Long id){
        return intfService.findById(id);
    }


    /**
     * @description: //TODO
     * @param id
     * @return Boolean
     * @date: 2024/12/20 14:35
     **/
    @Operation(summary = "是否存在判断 对象的是否存在")
    @Parameters({
            @Parameter(name = "id", description = "对象属性例如：id", required = true)
    })
    @RequestMapping(value = "/exists/{id}",method = RequestMethod.GET)
    public Boolean exists(@PathVariable(value = "id") Long id){
        return intfService.exists(id);
    }

    /**
     * @param prefix 前缀
     * @return TblCdmaHlr
     **/
    @SelectorDataSource(value=DataSourceNamesConstant.TWO)
    @Operation(summary = "H码前缀")
    @Parameters({
            @Parameter(name = "prefix", description = "H码前缀", required = true)
    })
    @RequestMapping(value = "/hlr/{prefix}",method = RequestMethod.GET)
    public TblCdmaHlr hlr(@PathVariable(value = "prefix") String prefix){
        return tblCdmaHlrService.findOneByAttr("phoneprefix",prefix);
    }
}

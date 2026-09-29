package io.github.rothschil.web.compoent;

import io.github.rothschil.common.config.annotation.Cacheable;
import io.github.rothschil.common.intf.IntfConf;
import io.github.rothschil.common.utils.DateUtils;
import io.github.rothschil.common.utils.RestUtils;
import io.github.rothschil.domain.database.vo.UserVo;
import io.github.rothschil.web.compoent.host.CdmaHlrResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
public class TestCompoent {

    @Autowired
    protected Environment environment;

    public String getPort() {
        return environment.getProperty("local.server.port");
    }


        @Cacheable(key = "#userVo.account",enableCaffeine = true)
    public UserVo get(UserVo userVo) {
        log.info(userVo.getAccount());
        return qryUser();
    }

    protected UserVo qryUser(){
        UserVo vo;
        int id = new Random().nextInt(90) + 10;
        try {
            Thread.sleep(1000);
            vo = UserVo.builder().email("wongs@qq.com").account(DateUtils.getTransId()).password("取你狗命").phone("18912345678").id(id).build();
        } catch (InterruptedException e) {
            vo=UserVo.builder().email("wongs@qq.com").account(DateUtils.getTransId()).password("发生了异常").phone("18912345678").id(id).build();
        }
        log.info("重新查询获取数据为 {}",vo.toString());
        return vo;
    }


    public CdmaHlrResp testHost(String phone){
        String url ="http://localhost:"+getPort()+"/hlr/"+phone;
        IntfConf intfConf = new IntfConf();
        Map<String, Object> map = new HashMap<>();
        intfConf.setAddress(url);
        return RestUtils.get(intfConf,map,CdmaHlrResp.class);
    }


}

package io.github.rothschil.web.compoent;

import io.github.rothschil.common.annotation.Cacheable;
import io.github.rothschil.common.utils.DateUtils;
import io.github.rothschil.domain.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
public class TestCompoent {


//    @Cacheable(cacheNames = {"user","user"},key = "userVo:account")
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
            vo = UserVo.builder().email("wongs@qq.com").account(DateUtils.getTransId()).password("取你狗命").id(id).build();
        } catch (InterruptedException e) {
            vo=UserVo.builder().email("wongs@qq.com").account(DateUtils.getTransId()).password("发生了异常").id(id).build();
        }
        log.info("重新查询获取数据为 {}",vo.toString());
        return vo;
    }
}

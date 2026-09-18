package io.github.rothschil.web.compoent;

import io.github.rothschil.domain.database.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ValidationUserCompoent {


    private static final String KEY_PREFIX = "zoe:asset:";

    @Cacheable
    public UserVo queryWarningResult(UserVo vo) {
        // 模拟耗时操作
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.info("{}",e.getMessage());
        }
        return null;
    }
}

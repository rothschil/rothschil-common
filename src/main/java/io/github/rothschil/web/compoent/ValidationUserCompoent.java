package io.github.rothschil.web.compoent;

import io.github.rothschil.domain.vo.UserVo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class ValidationUserCompoent {


    private static final String KEY_PREFIX = "zoe:asset:";

    @Cacheable
    public UserVo queryWarningResult(UserVo vo) {
        // 模拟耗时操作
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

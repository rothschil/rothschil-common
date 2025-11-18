package io.github.rothschil;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * @description 测试基类
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Slf4j
public abstract class AbstractBaseSimpleCase {


    long beginTime;
    long end;

    @BeforeEach
    public void beforeAll() {
        beginTime = System.currentTimeMillis();
    }

    @AfterEach
    public void after() {
        end = System.currentTimeMillis();
        log.info("[AfterAll] 总共耗时={} 毫秒",(end-beginTime));
    }
}

package io.github.rothschil;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public abstract class BaseTestUnit {


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

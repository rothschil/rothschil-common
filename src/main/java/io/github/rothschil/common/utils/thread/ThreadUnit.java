package io.github.rothschil.common.utils.thread;


import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/** 定义线程的基本单元
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @date 2017/12/2 14:51
 * @since 1.0.0
*/
@Slf4j
@SuppressWarnings("unused")
public class ThreadUnit implements Runnable{

    private String value;

    public ThreadUnit(){

    }

    public ThreadUnit(String value){
        this.value=value;
    }

    @Override
    public void run() {
        LocalDateTime localDateTime = LocalDateTime.now();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            log.info("{}",e.getMessage());
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

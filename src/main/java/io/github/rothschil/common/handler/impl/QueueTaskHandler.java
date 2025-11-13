package io.github.rothschil.common.handler.impl;



import io.github.rothschil.common.handler.IQueueTaskHandler;
import io.github.rothschil.common.handler.IntfLog;
import io.github.rothschil.common.handler.IntfLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 针对队列的任务 消费者
 *
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @date 2021/11/1 - 21:04
 * @since 1.0.0
 */
@Slf4j
@Component
public class QueueTaskHandler implements IQueueTaskHandler {

    private IntfLogService intfLogService;

    /**
     * 异步处理数据的核心方法
     *
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2017/8/18-16:26
     **/
    @Async
    @Override
    public void processData(List<IntfLog> lists) {
        try {
            intfLogService.asyncBatch(lists);
        } catch (Exception e) {
            log.error("[失败重送队列异常] {}",e.getMessage());
        }
    }

    @Autowired
    public void setIntfLogService(IntfLogService intfLogService) {
        this.intfLogService = intfLogService;
    }

    public QueueTaskHandler() {
    }

}

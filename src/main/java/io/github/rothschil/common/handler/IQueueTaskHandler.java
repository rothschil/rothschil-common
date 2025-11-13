package io.github.rothschil.common.handler;


import java.util.List;

/**
 * 针对队列的任务 消费者
 *
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @date 2017/8/18-16:17
 * @since 1.0.0
 */
public interface IQueueTaskHandler {

    /**
     * 异步处理数据的核心方法
     *
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2017/8/18-16:26
     **/
    void processData(List<IntfLog> lists);
}

package io.github.rothschil.common.utils.thread;


import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池的自定义策略
 *
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @date 20/11/19 16:20
 * @since 1.0.0
 */
public class PippinRejectedExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        // 根据实际需要实现，正式场景下应该持久化或者写入日志
        if (r instanceof ThreadUnit) {

        }
    }
}

package io.github.rothschil.common.utils.thread;

import io.github.rothschil.common.utils.ThreadMdcUtil;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

/**
 *
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
public final class CustomThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
    // public CustomThreadPoolTaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
    //                                     BlockingQueue<Runnable> workQueue) {
    //     super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    // }
    //
    // public CustomThreadPoolTaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
    //                                     BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
    //     super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    // }
    //
    // public CustomThreadPoolTaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
    //                                     BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
    //     super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    // }
    //
    // public CustomThreadPoolTaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
    //                                     BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
    //                                     RejectedExecutionHandler handler) {
    //     super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    // }

    @Override
    public void execute(Runnable task) {
        super.execute(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
    }


    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
    }
}

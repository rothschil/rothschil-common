package io.github.rothschil.common.utils.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程池名字
 *
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @date 20/11/19 16:36
 * @since 1.0.0
 */
public class PippinThreadFactory implements ThreadFactory {

    private final ThreadGroup threadGroup;

    private final AtomicInteger threadNumber = new AtomicInteger(1);
    public final String namePrefix;

    public PippinThreadFactory(String name) {
        SecurityManager s = System.getSecurityManager();
        threadGroup = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        if (null == name || "".equals(name.trim())) {
            name = "pool";
        }
        AtomicInteger poolNumber = new AtomicInteger(1);
        namePrefix = name + "-" +
                poolNumber.getAndIncrement() +
                "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(threadGroup, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}

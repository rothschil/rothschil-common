package io.github.rothschil.common.queue;


import io.github.rothschil.common.handler.IQueueTaskHandler;
import io.github.rothschil.common.handler.IntfLog;
import io.github.rothschil.common.utils.thread.PippinRejectedExecutionHandler;
import io.github.rothschil.common.utils.thread.PippinThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 异步处理日志的队列，将需要存储的日志放入这个队列中
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Component
public class AppLogQueue {

    private static final Logger LOG = LoggerFactory.getLogger(AppLogQueue.class);

    private final LinkedBlockingQueue<IntfLog> queue = new LinkedBlockingQueue<>(200);

    @Autowired
    IQueueTaskHandler iQueueTaskHandler;

    /**
     * 检查服务是否运行
     */
    private volatile boolean running = true;

    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        int poolSize = 1;
        // 2、自定义线程名字
        ThreadFactory threadFactory = new PippinThreadFactory(AppLogQueue.class.getName());
        // 3、自定义线程池超出容量的拒绝策略
        RejectedExecutionHandler policy = new PippinRejectedExecutionHandler();
        threadPoolExecutor = new ThreadPoolExecutor(poolSize, 3, 0,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory, policy);
    }

    public AppLogQueue(){

    }

//    /**
//     * 线程状态
//     */
//    private Future<?> threadStatus = null;

    @PostConstruct
    public void init(){
//        threadStatus  = threadPoolExecutor.submit(() -> {
//            while(running){
//                // 队列中不存在元素 则不处理
//                if(!queue.isEmpty()){
//                }
//            }
//        });
    }

    @PreDestroy
    public void destroys() {
        running = false;
        threadPoolExecutor.shutdownNow();
    }

    public void activeService() {
        running = true;
        if (threadPoolExecutor.isShutdown()) {
            init();
            LOG.info("线程池关闭，重新初始化线程池及任务");
        }
//        if (threadStatus.isDone()) {
//            init();
//            LOG.info("线程池任务结束，重新!");
//        }
    }

    public void addQueue(IntfLog intfLog){
//        if(!running){
//            LOG.warn("service is stop");
//            return ;
//        }
        final ReentrantLock putLock = new ReentrantLock();
        putLock.lock();
        try {
            boolean isFull = queue.offer(intfLog);
            if(!isFull){
                LOG.warn("[Failed to add queue,Queue full]");
                List lists = Arrays.asList(queue.toArray());
                iQueueTaskHandler.processData(lists);
                queue.clear();
                // 清理完，把刚才漏掉的补上
                queue.offer(intfLog);
            }
        } catch (Exception e) {
            LOG.warn("[Exception] {}",e.getMessage());
        } finally {
            putLock.unlock();
        }

    }

    public boolean empty(){
        return queue.isEmpty();
    }

}

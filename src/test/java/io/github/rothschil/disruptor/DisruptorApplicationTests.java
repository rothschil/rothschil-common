package io.github.rothschil.disruptor;

import com.google.common.collect.ImmutableMap;
import io.github.rothschil.BaseTestUnit;
import io.github.rothschil.disruptor.service.DisruptorCommServiceImpl;
import io.github.rothschil.disruptor.service.DisruptorIndServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@DisplayName("线程测试")
@Slf4j
public class DisruptorApplicationTests extends BaseTestUnit {
//    public class DisruptorApplicationTests{

    @Autowired
    private DisruptorCommServiceImpl disruptorCommService;
    @Autowired
    private DisruptorIndServiceImpl disruptorIndService;


    /**
     * 独立消费者测试
     * 生产者生产20条消息，由两个消费者各自独立消费20条，两者加起共40
     * @param n
     */
    void disruptorIndTest(int n, Map<String, Object> value){
        disruptorIndService.publish(value);
        log.info("生产者:{}", n);
    }

    /**
     * 共同消费者测试
     * 生产者生产20条消息，由两个消费者共同消费，两者加起共20
     * @param n
     */
    void disruptorCommTest(int n, Map<String, Object> value){
        disruptorCommService.publish(value);
        log.info("生产者:{}", n);
    }

    @DisplayName("共同消费者测试")
    @Test
    public void disruptorTest() throws InterruptedException {

        Map<String, Object> disMap;
        for (int i = 0; i < 20; i++) {
            disMap = ImmutableMap.of("index", i, "uuid", UUID.randomUUID().toString().trim());
            disruptorIndTest(i,disMap);
//            disruptorCommTest(i,disMap);
        }
        log.info("生产者插入结束");
        //因为处理消息是异步的，停一会是为了让所有消息处理完成
        TimeUnit.SECONDS.sleep(5);
        log.info("等待结束");
    }

    @Test
    public void concurrentTest() {
        int count = 50;
        final CountDownLatch countDownLatch = new CountDownLatch(count); // 相当于计数器，当所有都准备好了，再一起执行，模仿多并发，保证并发量
        final CountDownLatch countDownLatch2 = new CountDownLatch(count); // 保证所有线程执行完了再打印atomicInteger的值
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            for (int i = 0; i < count; i++) {
                final int n = i;
                executorService.submit(() -> {
                    try {
                        countDownLatch.await(); //一直阻塞当前线程，直到计时器的值为0,保证同时并发
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(),e);
                    }
                    Map<String, Object> disMap = ImmutableMap.of("index", n, "uuid", UUID.randomUUID().toString().trim());
//                    disruptorCommTest(i,disMap);
                    disruptorCommTest(n,disMap);
                    countDownLatch2.countDown();
                });
                countDownLatch.countDown();
            }

            countDownLatch2.await();// 保证所有线程执行完
            executorService.shutdown();

            //因为处理消息是异步的，停一会是为了让所有消息处理完成
            TimeUnit.SECONDS.sleep(15);
            log.info("等待结束");

        } catch (Exception e){
            log.error(e.getMessage(),e);
        }

    }
}

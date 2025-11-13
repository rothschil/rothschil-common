package io.github.rothschil.disruptor.service;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import io.github.rothschil.common.base.disruptor.factory.DisruptorMsgEventFactory;
import io.github.rothschil.common.base.disruptor.msg.DisruptorMsgEvent;
import io.github.rothschil.disruptor.handler.producer.DisruptorMsgEventProducer;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import jakarta.annotation.PostConstruct;
import java.util.Map;

/**
 * 实例化Disruptor和生产者
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
public abstract class DisruptorMsgEventService {

    /**
     * 环形队列缓冲区大小，为测试看效果所以下面设置得小点，生产上应该配置大些
     * 必须为2的N次方（能将求模运算转为位运算提高效率）
     * 当超过此大小后，再有生产加入时会进行阻塞，
     * 直到有消费者处理完，有空位后则继续加入
     */
    protected int BUFFER_SIZE = 8;

    protected Disruptor<DisruptorMsgEvent> disruptor;

    //生产者
    private DisruptorMsgEventProducer producer;

    @PostConstruct
    private void init() {
        // 实例化，disruptor-handler- 为线程名
        disruptor = new Disruptor<>(new DisruptorMsgEventFactory(), BUFFER_SIZE,
                new CustomizableThreadFactory("disruptor-handler-"), ProducerType.SINGLE, new BlockingWaitStrategy());

        //抽象方法，子类实现
        handleEvents();

        // 启动
        disruptor.start();

        // 实例化生产者
        producer = new DisruptorMsgEventProducer(disruptor.getRingBuffer());
    }

    /**
     * 发布事件
     * @param value
     * @return
     */
    public void publish(Map<String, Object> value) {
        producer.publish(value);
    }

    public long getCursor() {
        return disruptor.getCursor();
    }

    /**
     * 留给子类实现具体的事件消费逻辑
     */
    protected abstract void handleEvents();
}

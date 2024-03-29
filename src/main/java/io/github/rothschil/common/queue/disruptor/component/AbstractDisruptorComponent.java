package io.github.rothschil.common.queue.disruptor.component;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import io.github.rothschil.common.queue.disruptor.event.DisruptorEvent;
import io.github.rothschil.common.queue.disruptor.factory.DisruptorEventFactory;
import io.github.rothschil.common.queue.disruptor.producer.DisruptorEventProducer;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

public abstract class AbstractDisruptorComponent {

    /**
     * 环形队列缓冲区大小，为测试看效果所以下面设置得小点，生产上应该配置大些
     * 必须为2的N次方（能将求模运算转为位运算提高效率）
     * 当超过此大小后，再有生产加入时会进行阻塞，
     * 直到有消费者处理完，有空位后则继续加入
     */
    protected int BUFFER_SIZE = 8;

    protected Disruptor<DisruptorEvent> disruptor;

    private DisruptorEventProducer producer;

    @PostConstruct
    private void init() {
        // 实例化，handler- 为线程名
        disruptor = new Disruptor<>(new DisruptorEventFactory(), BUFFER_SIZE,
                new CustomizableThreadFactory("handler-"), ProducerType.SINGLE, new BlockingWaitStrategy());

        handleEvents();

        // 启动
        disruptor.start();

        // 实例化生产者
        producer = new DisruptorEventProducer(disruptor.getRingBuffer());
    }

    /**
     * 发布事件
     *
     * @param value
     * @return
     */
    public void publish(String value) {
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

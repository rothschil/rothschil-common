package io.github.rothschil.disruptor.handler.producer;

import com.lmax.disruptor.RingBuffer;
import io.github.rothschil.common.base.disruptor.msg.DisruptorMsgEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 *
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Slf4j
public class DisruptorMsgEventProducer {

    private final RingBuffer<DisruptorMsgEvent> ringBuffer;

    public DisruptorMsgEventProducer(RingBuffer<DisruptorMsgEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void publish(Map<String, Object> value){
        // ringBuffer是个队列，其next方法返回的是下最后一条记录之后的位置，这是个可用位置
        long next = ringBuffer.next();
        try {
            DisruptorMsgEvent event = ringBuffer.get(next);
            event.setValue(value);
        } catch (Exception e) {
            log.error("向RingBuffer队列存入数据[{}]出现异常=>{}", value, e.getStackTrace());
        } finally {
            ringBuffer.publish(next);
        }
    }

}

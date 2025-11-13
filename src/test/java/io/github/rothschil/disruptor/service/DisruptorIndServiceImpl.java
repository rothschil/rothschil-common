package io.github.rothschil.disruptor.service;

import io.github.rothschil.disruptor.handler.consumer.simple.DisruptorMsgEventHandlerOne;
import io.github.rothschil.disruptor.handler.consumer.simple.DisruptorMsgEventHandlerTwo;
import org.springframework.stereotype.Service;

/**
 * 独立消费者
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Service
public class DisruptorIndServiceImpl extends DisruptorMsgEventService{


    @Override
    protected void handleEvents() {
        /**
         * 调用handleEventsWith，表示创建的多个消费者，每个都是独立消费的
         * 可以定义不同的消费者处理器，也可使用相同的处理器。
         * 实际场景中应该多数使用不同的处理器，因为正常来讲独立消费者做的应该是不同的事。
         * 所以本例中是定义了两个不同的消费者DisruptorEventIndHandler0和DisruptorEventIndHandler1
         */
        disruptor.handleEventsWith(new DisruptorMsgEventHandlerOne("A"), new DisruptorMsgEventHandlerTwo("B"));
    }
}

package io.github.rothschil.disruptor.service;

import io.github.rothschil.disruptor.handler.consumer.DisruptorEventCommHandler;
import org.springframework.stereotype.Service;

/**
 * 共同消费者
 *
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Service
public class DisruptorCommServiceImpl extends DisruptorMsgEventService {


    @Override
    protected void handleEvents() {
        /**
         * 调用handleEventsWithWorkerPool，表示创建的多个消费者以共同消费的模式消费；
         * 单个消费者时可保证其有序性，多个时无法保证其顺序；
         * 或者说每个消费者是有序的，但每个消费者间是并行执行的，所以无法保证整体的有序
         * 共同消费者做的应该是同个事，所以本例中只定义了一个共同消费者DisruptorEventCommHandler
         */
        disruptor.handleEventsWithWorkerPool(new DisruptorEventCommHandler("A"), new DisruptorEventCommHandler("B"));

    }
}

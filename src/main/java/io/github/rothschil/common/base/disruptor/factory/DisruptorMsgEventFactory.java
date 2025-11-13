package io.github.rothschil.common.base.disruptor.factory;

import com.lmax.disruptor.EventFactory;
import io.github.rothschil.common.base.disruptor.msg.DisruptorMsgEvent;

/**
 * 定义事件模型工厂类
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
public class DisruptorMsgEventFactory implements EventFactory<DisruptorMsgEvent> {
    @Override
    public DisruptorMsgEvent newInstance() {
        return new DisruptorMsgEvent();
    }
}

package io.github.rothschil.disruptor.handler.consumer.simple;

import cn.hutool.json.JSONUtil;
import com.lmax.disruptor.EventHandler;

import io.github.rothschil.common.base.disruptor.msg.DisruptorMsgEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 独立消费者事件处理器1
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Slf4j
public class DisruptorMsgEventHandlerTwo implements EventHandler<DisruptorMsgEvent> {

    private String name;

    public DisruptorMsgEventHandlerTwo(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(DisruptorMsgEvent event, long sequence, boolean endOfBatch) throws Exception {
        TimeUnit.MILLISECONDS.sleep(300);
        log.info("独立消费者{}: {},sequence:{},endOfBatch:{}", name, JSONUtil.parseObj(event), sequence, endOfBatch);
    }
}

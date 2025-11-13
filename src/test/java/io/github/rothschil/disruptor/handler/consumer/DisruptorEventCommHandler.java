package io.github.rothschil.disruptor.handler.consumer;

import cn.hutool.json.JSONUtil;
import com.lmax.disruptor.WorkHandler;

import io.github.rothschil.common.base.disruptor.msg.DisruptorMsgEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Slf4j
public class DisruptorEventCommHandler implements WorkHandler<DisruptorMsgEvent> {

    private String name;

    public DisruptorEventCommHandler(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(DisruptorMsgEvent event) throws Exception {
        TimeUnit.MILLISECONDS.sleep(300);
        log.info("共同消费者{}: {}", name, JSONUtil.parseObj(event));
    }
}
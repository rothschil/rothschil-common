package io.github.rothschil.common.base.disruptor.msg;


import lombok.Data;

import java.util.Map;

/**
 * 定义基础的定义事件数据模型
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Data
public class DisruptorMsgEvent {

    private Map<String, Object> value;
}

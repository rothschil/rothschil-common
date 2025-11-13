package io.github.rothschil.common.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.rothschil.common.base.persistence.entity.BaseEntity;
import lombok.*;

import java.util.Date;

/**
 *  对外/对内 HTTP接口交互日志，便于快速查找问题，上线后可以关闭此功能
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class IntfLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * IVR流水
     */
    private String tranId;

    /**
     * 主叫号码
     */
    private String caller;

    /**
     * 接口编码
     */
    private String intfCode;

    /**
     * 状态
     */
    private String state;

    /**
     * 来源系统
     */
    private String source;

    /**
     * 目标系统
     */
    private String target;

    /**
     * 请求报文
     */
    private String reqData;

    /**
     * 回单报文
     */
    private String respData;

    /**
     * 请求时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reqTime;

    /**
     * 响应时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date respTime;

    /**
     * 错误编码
     */
    private String errCode;

    /**
     * 执行时长，毫秒
     */
    private Long procTime;



}

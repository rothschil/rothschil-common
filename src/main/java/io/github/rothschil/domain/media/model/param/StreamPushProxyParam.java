package io.github.rothschil.domain.media.model.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 推流代理参数
 *
 * @author lidaofu
 * @since 2023/11/29
 **/
@Data
@Schema(name = "StreamPushProxyParam对象", description = "推流代理参数")
public class StreamPushProxyParam {


    @NotBlank(message = "app不为空")
    @Schema(description = "app",required = true)
    private String app;

    @NotBlank(message = "流id不为空")
    @Schema(description = "流id",required = true)
    private String stream;

    @NotBlank(message = "流的协议不为空")
    @Schema(description = "流的协议",required = true)
    private String schema;

    @NotBlank(message = "推流代理流地址不为空")
    @Schema(description = "推流代理流地址",required = true)
    private String url;

    @Schema(description = "rtsp推流时，推流方式，0：tcp，1：udp，2：组播")
    private Integer rtpType=0;

    @Schema(description = "推流代理超时时间，单位秒")
    private Integer timeoutSec;
}

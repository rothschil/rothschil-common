package io.github.rothschil.domain.media.model.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 开启rtp服务
 *
 * @author lidaofu
 * @since 2023/3/30
 **/
@Data
@Schema(name = "OpenRtpServerParam对象", description = "开启rtp服务参数")
public class OpenRtpServerParam implements Serializable {

    private static final long serialVersionUID = 1;


    @NotNull(message = "接收端口，0则为随机端口")
    @Schema(description = "接收端口",required = true)
    private Integer port;

    @NotNull(message = "0 udp 模式，1 tcp 被动模式, 2 tcp 主动模式。 (兼容enable_tcp 为0/1)")
    @Schema(description = "tcp_mode",required = true)
    private Integer tcp_mode;

    @NotBlank(message = "流id不为空")
    @Schema(description = "流id",required = true)
    private String stream;


}

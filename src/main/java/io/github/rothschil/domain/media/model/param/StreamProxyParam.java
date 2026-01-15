package io.github.rothschil.domain.media.model.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 拉流代理参数
 *
 * @author lidaofu
 * @since 2023/11/29
 **/
@Data
@Schema(name = "StreamProxyParam对象", description = "拉流代理参数")
public class StreamProxyParam {


    @NotBlank(message = "app不为空")
    @Schema(description = "app",required = true)
    private String app;

    @NotBlank(message = "流id不为空")
    @Schema(description = "流id",required = true)
    private String stream;

    @NotBlank(message = "代理流地址不为空")
    @Schema(description = "代理流地址",required = true)
    private String url;

    @Schema(description = "rtsp拉流时，拉流方式，0：tcp，1：udp，2：组播")
    private Integer rtpType=0;

    @Schema(description = "拉流重试次数,不传此参数或传值<=0时，则无限重试")
    private Integer retryCount=3;

    @Schema(description = "拉流超时时间，单位秒型")
    private Integer timeoutSec;

    @Schema(description = "开启hls转码")
    private Integer enableHls=1;

    @Schema(description = "开启rtsp/webrtc转码")
    private Integer enableRtsp=1;

    @Schema(description = "开启rtmp/flv转码")
    private Integer enableRtmp=1;

    @Schema(description = "开启ts/ws转码")
    private Integer enableTs=0;

    @Schema(description = "转协议是否开启音频")
    private Integer enableAudio=1;

    @Schema(description = "开启转fmp4")
    private Integer enableFmp4=0;

    @Schema(description = "开启mp4录制")
    private Integer enableMp4=0;

    @Schema(description = "mp4录制切片大小")
    private Integer mp4MaxSecond=3600;

    @Schema(description = "rtsp倍速")
    private BigDecimal rtspSpeed;

    @Schema(description = "自动关流")
    private Integer autoClose = 1;
}

package io.github.rothschil.domain.media.model.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "TestVideoParam对象", description = "测试流参数")
public class TestVideoParam implements Serializable {

    private static final long serialVersionUID = 1;


    @NotBlank(message = "app不为空")
    @Schema(description = "app不为空", example = "1001")
    private String app;

    @NotBlank(message = "流id不为空")
    @Schema(description = "流id不为空", example = "1001")
    private String stream;

    @Schema(description = "视频宽", example = "1001")
    private Integer width = 1920;

    @Schema(description = "视频高", example = "1001")
    private Integer height = 1080;

    @Schema(description = "帧率")
    private Integer fps =25;

    @Schema(description = "比特率")
    private Integer bitRate=5000000;

    @Schema(description = "自动关流")
    private Integer autoClose = 1;

    @Schema(description = "开启hls转码")
    private Integer enableHls = 1;

    @Schema(description = "开启rtsp/webrtc转码")
    private Integer enableRtsp = 1;

    @Schema(description = "开启rtmp/flv转码")
    private Integer enableRtmp = 1;

    @Schema(description = "开启ts/ws转码")
    private Integer enableTs = 0;

    @Schema(description = "开启转fmp4")
    private Integer enableFmp4 = 0;

    @Schema(description = "开启mp4录制")
    private Integer enableMp4 = 0;

    @Schema(description = "mp4录制切片大小")
    private Integer mp4MaxSecond = 3600;

}

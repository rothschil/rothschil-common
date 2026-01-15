package io.github.rothschil.domain.media.model.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
@Data
@Schema(name = "TranscodeParam对象", description = "转码参数")
public class TranscodeParam  implements Serializable {

    private static final long serialVersionUID = 1;

    @NotBlank(message = "url不为空")
    @Schema(description = "url(rtmp协议只支持H264)",required = true)
    private String url;

    @NotBlank(message = "转码后推的app不为空")
    @Schema(description = "转码后推的app",required = true)
    private String app;

    @Schema(description = "是否开启音频",required = true)
    private Boolean enableAudio=true;

    @NotBlank(message = "转码后推的stream不为空")
    @Schema(description = "转码后推的stream",required = true)
    private String stream;

    @Schema(description = "修改分辨率宽")
    private Integer scaleWidth=0;

    @Schema(description = "修改分辨率高")
    private Integer scaleHeight=0;
}

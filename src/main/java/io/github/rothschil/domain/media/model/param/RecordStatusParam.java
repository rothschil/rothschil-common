package io.github.rothschil.domain.media.model.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 录像状态
 *
 * @author lidaofu
 * @since 2023/3/30
 **/
@Data
@Schema(name = "RecordStatusParam对象", description = "录像状态")
public class RecordStatusParam implements Serializable {

    private static final long serialVersionUID = 1;

    @NotBlank(message = "app不为空")
    @Schema(description = "app",required = true)
    private String app;

    @NotBlank(message = "流id不为空")
    @Schema(description = "流id",required = true)
    private String stream;

    @NotNull(message = "录像类型不为空")
    @Schema(description = "0为hls，1为mp4,2:hls-fmp4,3:http-fmp4,4:http-ts 当0时需要开启配置分片持久化",required = true)
    private Integer type;

}

package io.github.rothschil.domain.media.model.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 开始录像
 *
 * @author lidaofu
 * @since 2023/3/30
 **/
@Data
@Schema(name = "StartRecordParam对象", description = "开始录像参数")
public class StartRecordParam implements Serializable {

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

    @Schema(description = "录像保存目录")
    private String customized_path;

    @Schema(description = "mp4录像切片时间大小,单位秒，置0则采用配置项")
    private Long max_second=1L;
}

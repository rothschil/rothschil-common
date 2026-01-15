package io.github.rothschil.domain.media.model.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 流查询参数
 *
 * @author lidaofu
 * @since 2023/3/30
 **/
@Data
@Schema(name = "MediaQueryParam对象", description = "流查询参数")
public class MediaQueryParam implements Serializable {

    private static final long serialVersionUID = 1;


    @NotBlank(message = "app不为空")
    @Schema(description = "app",required = true)
    private String app;

    @NotBlank(message = "流id不为空")
    @Schema(description = "流id",required = true)
    private String stream;

    @NotBlank(message = "流的协议不为空")
    @Schema(description = "流的协议",required = true)
    private String schema;

}

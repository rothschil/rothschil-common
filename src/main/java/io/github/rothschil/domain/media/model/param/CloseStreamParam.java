package io.github.rothschil.domain.media.model.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 关闭流请求参数
 *
 * @author lidaofu
 * @since 2023/3/30
 **/
@Data
@Schema(name = "CloseStreamParam对象", description = "关闭流请求参数")
public class CloseStreamParam  implements Serializable {

    private static final long serialVersionUID = 1;

    @NotBlank(message = "app不为空")
    @Schema(description = "app",required = true)
    private String app;

    @NotBlank(message = "流id不为空")
    @Schema(description = "流id",required = true)
    private String stream;

    @NotNull(message = "是否强制关闭不为空")
    @Schema(description = "是否强制关闭",required = true)
    private Integer force;

    @NotBlank(message = "流的协议不为空")
    @Schema(description = "流的协议",required = true)
    private String schema;


}

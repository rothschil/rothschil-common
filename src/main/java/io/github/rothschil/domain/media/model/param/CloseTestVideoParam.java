package io.github.rothschil.domain.media.model.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "CloseTestVideoParam对象", description = "关闭测试流参数")
public class CloseTestVideoParam implements Serializable {

    private static final long serialVersionUID = 1;


    @NotBlank(message = "app不为空")
    @Schema(description = "app", required = true)
    private String app;

    @NotBlank(message = "流id不为空")
    @Schema(description = "流id", required = true)
    private String stream;

}

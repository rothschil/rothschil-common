package io.github.rothschil.domain.media.model.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 获取流列表
 *
 * @author lidaofu
 * @since 2023/3/30
 **/
@Data
@Schema(name = "GetMediaListParam对象", description = "获取流列表")
public class GetMediaListParam implements Serializable {

    private static final long serialVersionUID = 1;


    @Schema(description = "app")
    private String app;

    @Schema(description = "流id")
    private String stream;

    @Schema(description = "流的协议")
    private String schema;

}

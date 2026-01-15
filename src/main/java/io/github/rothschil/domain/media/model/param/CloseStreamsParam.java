package io.github.rothschil.domain.media.model.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 关闭流请求参数
 *
 * @author lidaofu
 * @since 2023/3/30
 **/
@Data
@Schema(name = "CloseStreamsParam对象", description = "关闭流请求参数")
public class CloseStreamsParam implements Serializable {

    private static final long serialVersionUID = 1;

    @Schema(description = "app")
    private String app;

    @Schema(description = "流id")
    private String stream;

    @Schema(description = "是否强制关闭")
    private Integer force=1;

    @Schema(description = "流的协议")
    private String schema;


}

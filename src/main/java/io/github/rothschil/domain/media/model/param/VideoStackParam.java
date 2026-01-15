package io.github.rothschil.domain.media.model.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(name = "VideoStackParam对象", description = "拼接屏幕参数")
public class VideoStackParam implements Serializable {

    private static final long serialVersionUID = 1;

    @NotBlank(message = "拼接屏任务id不为空")
    @Schema(description = "拼接屏任务id(流id)", required = true)
    private String id;

    @Schema(description = "拼接屏任务流app")
    private String app = "live";

    @Schema(description = "推流地址 如果传了pushUrl将不在本地产生流")
    private String pushUrl;

    @NotNull(message = "拼接屏行数不为空")
    @Schema(description = "拼接屏行数", required = true)
    private Integer row;

    @NotNull(message = "拼接屏列行数不为空")
    @Schema(description = "拼接屏列行数", required = true)
    private Integer col;

    @NotNull(message = "拼接屏宽度不为空")
    @Schema(description = "拼接屏宽度", required = true)
    private Integer width;

    @NotNull(message = "拼接屏高度不为空")
    @Schema(description = "拼接屏高度", required = true)
    private Integer height;

    @Schema(description = "图片链接，为空则填灰色")
    private String fillImgUrl;

    @Schema(description = "默认填充颜色")
    private String fillColor = "BFBFBF";

    @Schema(description = "是否存在分割线")
    private Boolean gridLineEnable = false;

    @Schema(description = "分割线颜色")
    private String gridLineColor = "000000";

    @Schema(description = "分割线宽度")
    private Integer gridLineWidth = 1;

    @Schema(description = "拼接屏内容")
    private List<VideoStackWindowParam> windowList;

}
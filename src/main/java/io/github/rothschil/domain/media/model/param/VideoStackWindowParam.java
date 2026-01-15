package io.github.rothschil.domain.media.model.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(name = "VideoStackWindowParam对象", description = "拼接屏幕地址参数")
public class VideoStackWindowParam implements Serializable {

    private static final long serialVersionUID = 1;

    @Schema(description = "拼接视频地址")
    private String videoUrl;

    @Schema(description = "拼接图片地址,和上面二选一")
    private String imgUrl;

    @Schema(description = "默认填充颜色")
    private String fillColor = "BFBFBF";

    @Schema(description = "所占的格子")
    private List<Integer> span;
}
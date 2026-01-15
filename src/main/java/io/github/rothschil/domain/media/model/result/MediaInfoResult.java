package io.github.rothschil.domain.media.model.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * MediaInfoResult
 *
 * @author lidaofu
 * @since 2023/3/30
 **/
@Data
@Schema(name = "GetMediaListParam对象", description = "流信息")
public class MediaInfoResult implements Serializable {

    private static final long serialVersionUID = 1;

    @Schema(description = "app")
    private String app;

    @Schema(description = "流id")
    private String stream;

    @Schema(description = "本协议观看人数")
    private Integer readerCount;

    @Schema(description = "产生源类型，包括 unknown = 0,rtmp_push=1,rtsp_push=2,rtp_push=3,pull=4,ffmpeg_pull=5,mp4_vod=6,device_chn=7")
    private Integer originType;

    @Schema(description = "产生源的url")
    private String originUrl;

    @Schema(description = "产生源的url的类型")
    private String originTypeStr;

    @Schema(description = "观看总数 包括hls/rtsp/rtmp/http-flv/ws-flv")
    private Integer totalReaderCount;

    @Schema(description = "schema")
    private String schema;

    @Schema(description = "存活时间，单位秒")
    private Long aliveSecond;

    @Schema(description = "数据产生速度，单位byte/s")
    private Integer  bytesSpeed;

    @Schema(description = "GMT unix系统时间戳，单位秒")
    private Long createStamp;

    @Schema(description = "是否录制Hls")
    private Boolean isRecordingHLS;

    @Schema(description = "是否录制mp4")
    private Boolean isRecordingMP4;

    @Schema(description = "虚拟地址")
    private String vhost;

    private List<Track> tracks;


}

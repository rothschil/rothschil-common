//package io.github.rothschil.common.config;
//
//
//import com.aizuda.zlm4j.core.ZLMApi;
//import com.aizuda.zlm4j.structure.MK_EVENTS;
//import com.sun.jna.Native;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @program: rothschil-common
// * @description:
// * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
// **/
//@Configuration
//public class ZlmServerConfig {
//
//    @Bean
//    public ZLMApi zlmApi() {
//        //实例化API
//        ZLMApi ZLM_API = Native.load("mk_api", ZLMApi.class);
//        //初始化SDK配置
//        ZLM_API.mk_env_init1(1, 1, 1, null, 0, 0, null, 0, null, null);
//        //创建http服务器 0:失败,非0:端口号  HTTP服务：用于FLV/HLS播放
//        short http_server_port = ZLM_API.mk_http_server_start((short) 7788, 0);
//        //创建rtsp服务器 0:失败,非0:端口号 RTSP服务：常用于设备推流
//        short rtsp_server_port = ZLM_API.mk_rtsp_server_start((short) 9758, 0);
//        //创建rtmp服务器 0:失败,非0:端口号 RTMP服务：常用于直播平台
//        short rtmp_server_port = ZLM_API.mk_rtmp_server_start((short) 9759, 0);
//        //创建RTP服务器 0:失败,非0:端口号 GB28181 rtp服务建议使用mk_rtp_server_create来创建和管理 用于实时媒体传输
//        short rtp_server_port = ZLM_API.mk_rtp_server_start((short) 32000);
//
//        return ZLM_API;
//    }
//
//    /**
//     * 注册事件监听载体：处理推流鉴权、流状态变化等事件
//     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
//     * @return com.aizuda.zlm4j.structure.MK_EVENTS
//     **/
//    @Bean
//    public MK_EVENTS mkEvents() {
//        return new MK_EVENTS();
//    }
//}

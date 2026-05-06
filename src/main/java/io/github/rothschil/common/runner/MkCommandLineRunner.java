//package io.github.rothschil.common.runner;
//
//
//import com.aizuda.zlm4j.core.ZLMApi;
//import com.aizuda.zlm4j.structure.MK_EVENTS;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
///**
// * @program: rothschil-common
// * @description:
// * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
// **/
//@Slf4j
//@Component
//public class MkCommandLineRunner implements CommandLineRunner {
//
//    @Autowired
//    MK_EVENTS mkEvents;
//
//
//    @Autowired
//    ZLMApi zlmApi;
//
//    @Override
//    public void run(String... args) throws Exception {
//        //推流回调 可控制鉴权、录制、转协议控制等
//        mkEvents.on_mk_media_publish = (url_info, invoker, sender) -> {
//            //这里拿到访问路径后(例如rtmp://xxxx/xxx/xxx?token=xxxx其中?后面就是拿到的参数)的参数
//            // err_msg返回 空字符串表示鉴权成功 否则鉴权失败提示
//            String authParams = zlmApi.mk_media_info_get_params(url_info);
//            log.info("鉴权提醒 推流鉴权参数："+ authParams);
//            boolean isAuthPass = authParams.contains("token=stream2025");
//            // isAuthPass ? 0 : 1
//            zlmApi.mk_publish_auth_invoker_do(invoker, "", 0, 0);
//        };
//        zlmApi.mk_events_listen(mkEvents);
//
//
//        //流状态改变回调
//        mkEvents.on_mk_media_changed = (isOnline, sender) -> {
//            String appName = zlmApi.mk_media_source_get_app(sender);
//            String protocol = zlmApi.mk_media_source_get_schema(sender);
//            String streamName = zlmApi.mk_media_source_get_stream(sender);
//            String status = isOnline == 1 ? "Online" : "Offline";
//            log.info("状态变化 {}://{}/{} {}",protocol,appName,streamName,status);
//        };
//        zlmApi.mk_events_listen(mkEvents);
//    }
//}

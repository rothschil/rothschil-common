//package io.github.rothschil.common.init;
//
//import io.github.rothschil.domain.entity.Intf;
//import io.github.rothschil.domain.service.IntfService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import jakarta.annotation.PostConstruct;
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
///**
// * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
// * @version 1.0
// * @description: Bean初始化后，自动加载需要的数据
// * @date 2025/2/5 14:45
// */
//@Slf4j
//@Service
//public class AllocationDb {
//
//    private static CopyOnWriteArrayList<Intf> intfs = new CopyOnWriteArrayList<>();
//
//    @Autowired
//    private IntfService intfService;
//
//    @PostConstruct
//    public void init(){
//        List<Intf> list = intfService.findAll();
//
//        log.info("The Application is Starting and User Data is loaded,The amount of data is {} ",list.size());
//        list.forEach(v -> {
//            Intf intf = new Intf();
//            intf.setId(v.getId());
//            intf.setName(v.getName());
//            intf.setPassword(v.getPassword());
//            intfs.add(intf);
//        });
//        log.info("The application starts. Loading pre-load data is complete");
//    }
//
//}

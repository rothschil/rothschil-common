package io.github.rothschil.common.handler.impl;


import io.github.rothschil.common.handler.IntfLog;
import io.github.rothschil.common.handler.IntfLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  对外/对内 HTTP接口交互日志，便于快速查找问题，上线后可以关闭此功能
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Slf4j
@Service
public class IntfLogServiceImpl implements IntfLogService {


    /** 批量日志入库，性能一般，推荐使用 {@link IntfLogServiceImpl#asyncBatchOrigin(List< IntfLog >)}
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param intfLogs  集合
     **/
    @Override
    public void asyncBatch(List<IntfLog> intfLogs) {
        try {

        } catch (Exception e) {
            log.error("Description Failed to write logs in batches. {} err {}",intfLogs.size(),e.getMessage());
        }
    }

    /** 批量日志入库，利用Mybatis的原生SqlSession
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param intfLogs  集合
     **/
    @Override
    public void asyncBatchOrigin(List<IntfLog> intfLogs){

    }
}

package io.github.rothschil.common.handler;

import java.util.List;

/**
 *  对外/对内 HTTP接口交互日志，便于快速查找问题，上线后可以关闭此功能
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
public interface IntfLogService {

    /** 批量日志入库，利用Mybatis的 foreach
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param intfLogs  集合
     **/
    @Deprecated
    void asyncBatch(List<IntfLog> intfLogs);

    /** 批量日志入库，利用Mybatis的原生SqlSession
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param intfLogs  集合
     **/
    void asyncBatchOrigin(List<IntfLog> intfLogs);

}

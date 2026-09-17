//package io.github.rothschil.common.config;
//
//
//import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
//import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
//import org.hibernate.engine.jdbc.internal.Formatter;
///**
// *
// * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
// **/
//public class P6SpySqlFormat implements MessageFormattingStrategy {
//
//    // 复用Hibernate自带SQL格式化工具
//    protected static final Formatter FORMATTER = new BasicFormatterImpl();
//
//    @Override
//    public String formatMessage(int connectionId, String now, long elapsed, String category,
//                                String prepared, String sql, String url) {
//        // sql为空直接返回
//        if (sql == null || sql.trim().isEmpty()) {
//            return String.format("[%s] 耗时: %s ms | %s", now, elapsed, prepared);
//        }
//        // 使用Hibernate格式化SQL
//        String prettySql = FORMATTER.format(sql);
//        return String.format("\n[P6Spy SQL]时间:%s | 耗时:%s ms\n%s", now, elapsed, prettySql);
//    }
//}

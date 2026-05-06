package io.github.rothschil.wechat;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
 **/
public class ExcelUtils {

    public static void writeExcel(HttpServletResponse response, List<PrizeDo> list) throws IOException {
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
        WriteSheet sheet = EasyExcel.writerSheet(0, "sheet").head(PrizeDo.class).build();
        excelWriter.write(list, sheet);
        excelWriter.finish();
    }

    public static void writeExcel(String filePath, List<PrizeDo> data, Class<?> clazz){
        EasyExcel.write(filePath, clazz).sheet().doWrite(data);
    }
}

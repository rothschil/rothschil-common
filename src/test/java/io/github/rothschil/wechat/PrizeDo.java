package io.github.rothschil.wechat;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 *
 * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
 **/
@Data
public class PrizeDo {

    @ExcelProperty("路径")
    private String name;

    @ExcelProperty("文件名")
    private String number;

}

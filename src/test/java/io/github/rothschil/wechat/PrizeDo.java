package io.github.rothschil.wechat;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrizeDo {

    @ExcelProperty("文件名")
    private String name;

    @ExcelProperty("路径")
    private String path;

}

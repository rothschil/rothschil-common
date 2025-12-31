package io.github.rothschil.common.enums;

import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyEditorSupport;


/**
* @description: TODO
* @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
* @version 1.0
*/
public class SexEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) {
        if (StringUtils.isBlank(text)) {
            setValue(null);
        } else {
            // 转换为大写后匹配枚举
            SexEnum sex = SexEnum.parse(text);
            setValue(sex);
        }
    }

    @Override
    public String getAsText() {
        SexEnum sex = (SexEnum) getValue();
        return sex != null ? sex.name() : "";
    }
}

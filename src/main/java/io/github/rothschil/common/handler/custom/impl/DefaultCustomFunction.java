package io.github.rothschil.common.handler.custom.impl;



import io.github.rothschil.common.handler.custom.CustomFunction;
import io.github.rothschil.common.handler.custom.enums.FunctionNameEnum;
import org.springframework.stereotype.Component;

/**
 * 定义默认函数
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Component

public class DefaultCustomFunction implements CustomFunction {

    @Override
    public String functionName() {
        return FunctionNameEnum.DEFAULT_NAME.getName();
    }

    @Override
    public String apply(Object value) {
        return null;
    }
}

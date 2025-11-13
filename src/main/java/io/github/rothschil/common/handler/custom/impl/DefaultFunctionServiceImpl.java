package io.github.rothschil.common.handler.custom.impl;

import io.github.rothschil.common.handler.CustomFunctionFactory;
import io.github.rothschil.common.handler.custom.CustomFunction;
import io.github.rothschil.common.handler.custom.FunctionService;
import org.springframework.stereotype.Component;

/**
 *
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Component
public class DefaultFunctionServiceImpl implements FunctionService {

    private final CustomFunctionFactory customFunctionFactory;

    public DefaultFunctionServiceImpl(CustomFunctionFactory customFunctionFactory) {
        this.customFunctionFactory = customFunctionFactory;
    }

    @Override
    public String apply(String functionName, Object value) {
        CustomFunction function = customFunctionFactory.getFunction(functionName);
        if (function == null) {
            return value.toString();
        }
        return function.apply(value);
    }

    @Override
    public boolean executeBefore(String functionName) {
        CustomFunction function = customFunctionFactory.getFunction(functionName);
        return function != null;
    }
}

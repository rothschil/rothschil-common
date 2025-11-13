package io.github.rothschil.common.handler;

import io.github.rothschil.common.handler.custom.CustomFunction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomFunctionFactory {

    private static final Map<String, CustomFunction> customFunctionMap = new ConcurrentHashMap<>();


    public CustomFunctionFactory(List<CustomFunction> customFunctions) {
        for (CustomFunction customFunction : customFunctions) {
            customFunctionMap.put(customFunction.functionName(), customFunction);
        }
    }

    /**
     * 通过函数名获取对应自定义函数
     *
     * @param functionName 函数名
     * @return 自定义函数
     */
    public CustomFunction getFunction(String functionName) {
        return customFunctionMap.get(functionName);
    }
}

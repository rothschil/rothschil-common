package io.github.rothschil.common.handler.custom.enums;

/**
 *
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
public enum FunctionNameEnum {

    GET_ID("getId"),
    DEFAULT_NAME("defaultName"),
    ;
    private final String name;

    FunctionNameEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

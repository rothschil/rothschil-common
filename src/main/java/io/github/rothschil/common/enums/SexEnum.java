package io.github.rothschil.common.enums;

public enum SexEnum {
    MALE("M", "男"),
    FEMALE("F", "女"),
    UNKNOWN("U", "未知");

    private final String code;
    private final String desc;

    SexEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    // 根据 code 或 name 解析枚举
    public static SexEnum parse(String input) {
        if (input == null) {
            return null;
        }
        String str = input.trim().toUpperCase();

        // 优先按 code 匹配（业务常用）
        for (SexEnum sex : values()) {
            if (sex.getCode().equalsIgnoreCase(str)) {
                return sex;
            }
        }

        // 再按枚举名称匹配
        try {
            return SexEnum.valueOf(str);
        } catch (IllegalArgumentException e) {
            // 都不匹配时返回 UNKNOWN 或抛异常，这里选择返回 UNKNOWN 更宽容
            return UNKNOWN;
        }
    }
}

package io.github.rothschil.common.config;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import io.github.rothschil.common.annotation.Sensitive;
import io.github.rothschil.common.enums.SensitiveType;

import java.io.IOException;
import java.util.regex.Pattern;


/**
 *
 * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
 **/
public class SensitiveJsonSerializer extends JsonSerializer<String>
        implements ContextualSerializer {

    // 预编译正则，避免每次序列化都编译，提高并发性能
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("(\\d{3})\\d{4}(\\d{4})");
    private static final Pattern ID_CARD_PATTERN =
            Pattern.compile("(\\d{6})\\d{8}(\\d{4})");
    private static final Pattern BANK_CARD_PATTERN =
            Pattern.compile("\\d+(\\d{4})");

    // 不可变字段，无状态设计，保证线程安全
    private final SensitiveType type;

    public SensitiveJsonSerializer() {
        this.type = SensitiveType.PHONE;
    }

    public SensitiveJsonSerializer(SensitiveType type) {
        this.type = type;
    }

    @Override
    public void serialize(String value, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
        if (value == null || value.isEmpty()) {
            gen.writeString(value);
            return;
        }
        gen.writeString(mask(value));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov,
                                              BeanProperty property)
            throws JsonMappingException {
        // 只有当序列化器直接绑定到字段时，property 才不为 null
        if (property != null) {
            Sensitive sensitive = property.getAnnotation(Sensitive.class);
            if (sensitive != null) {
                return new SensitiveJsonSerializer(sensitive.value());
            }
        }
        return this;
    }

    private String mask(String value) {
        switch (type) {
            case PHONE:
                return PHONE_PATTERN.matcher(value).replaceAll("$1****$2");
            case ID_CARD:
                return ID_CARD_PATTERN.matcher(value).replaceAll("$1********$2");
            case BANK_CARD:
                return BANK_CARD_PATTERN.matcher(value).replaceAll("****$1");
            default:
                return value;
        }
    }
}
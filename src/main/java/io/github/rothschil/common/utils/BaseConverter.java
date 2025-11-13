package io.github.rothschil.common.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BaseConverter {

    public static <S, T> T convert(S source, Class<T> targetClass) {
        T t = null;
        try {
            t = targetClass.newInstance();
            if (ObjectUtils.isNotEmpty(source)) {
                BeanUtils.copyProperties(source, t);
            }
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <S, T> List<T> convertList(List<S> source, Class<T> targetClass) {
        return Optional.ofNullable(source).orElse(Collections.emptyList())
                .stream()
                .map(item -> convert(item, targetClass))
                .filter(ObjectUtils::isNotEmpty)
                .collect(Collectors.toList());
    }
}

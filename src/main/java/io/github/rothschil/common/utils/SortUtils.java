package io.github.rothschil.common.utils;

import com.alibaba.fastjson.JSONObject;
import io.github.rothschil.common.constant.Constant;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.Map;

public class SortUtils {


    /**
     * 表格排序
     *
     * @param tableMap tableMap
     * @param sorterBy 默认按此属性排序
     * @return Sort
     */
    public static Sort sortAttr(Map<String, String> tableMap, String sorterBy) {
        Sort sort;
        if (tableMap.get(Constant.SORTER) != null && !Constant.EMPTY_SORTER.equals(tableMap.get(Constant.SORTER))) {
            JSONObject sorter = JSONObject.parseObject(tableMap.get(Constant.SORTER));
            Iterator<String> iterator = sorter.keySet().iterator();
            String sortAttr = iterator.next();
            if (Constant.ASCEND.equals(sorter.get(sortAttr))) {
                sort = Sort.by(Sort.Direction.ASC, sortAttr);
            } else {
                sort = Sort.by(Sort.Direction.DESC, sortAttr);
            }
        } else {
            sort = Sort.by(Sort.Direction.ASC, sorterBy);
        }
        return sort;
    }
}

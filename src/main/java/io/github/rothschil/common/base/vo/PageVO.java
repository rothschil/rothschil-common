package io.github.rothschil.common.base.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class PageVO<T> implements Serializable {
    private static final long serialVersionUID = -3355752076145642645L;
    /**
     * 总数
     */
    Long total;

    /**
     * 数据
     */
    List<T> data;


    int current;


    int pageSize;
}

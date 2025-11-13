package io.github.rothschil.common.utils;

import io.github.rothschil.common.base.vo.PageVO;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;


public class RepositoryConverter {
    public static <S, D> PageVO<D> convertMultiObjectToPage(Page<S> srcPages, Class<D> destClass) {
        PageVO<D> pageResponse = new PageVO<>();
        List<D> destList = new ArrayList<>();
        if (srcPages != null) {
            srcPages.getContent();
            for (S srcPage : srcPages) {
                destList.add(BaseConverter.convert(srcPage, destClass));
            }
        }
        assert srcPages != null;
        pageResponse.setTotal(srcPages.getTotalElements());
        pageResponse.setData(destList);
        pageResponse.setPageSize(srcPages.getSize());
        pageResponse.setCurrent(srcPages.getNumber() + 1);
        return pageResponse;
    }
}

package io.github.rothschil.domain.service;

import io.github.rothschil.common.annotation.SelectorDataSource;
import io.github.rothschil.common.base.persistence.service.BaseService;
import io.github.rothschil.common.constant.DataSourceNamesConstant;
import io.github.rothschil.common.exception.NullServiceException;
import io.github.rothschil.domain.entity.TblCdmaHlr;
import io.github.rothschil.domain.repository.TblCdmaHlrRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true,rollbackFor = NullServiceException.class)
public class TblCdmaHlrService extends BaseService<TblCdmaHlrRepository, TblCdmaHlr, Long> {

    @SelectorDataSource(DataSourceNamesConstant.TWO)
    public TblCdmaHlr getHlrByPhoneprefix(String prefix){
        return  ((TblCdmaHlrRepository)baseRepository).getTblCdmaHlrByPhoneprefix(prefix);
    }

}
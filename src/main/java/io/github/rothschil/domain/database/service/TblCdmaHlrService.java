package io.github.rothschil.domain.database.service;

import io.github.rothschil.common.annotation.SelectorDataSource;
import io.github.rothschil.common.base.persistence.service.BaseService;
import io.github.rothschil.common.config.annotation.Cacheable;
import io.github.rothschil.common.constant.DataSourceNamesConstant;
import io.github.rothschil.common.exception.NullServiceException;
import io.github.rothschil.domain.database.entity.TblCdmaHlr;
import io.github.rothschil.domain.database.repository.TblCdmaHlrRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true,rollbackFor = NullServiceException.class)
public class TblCdmaHlrService extends BaseService<TblCdmaHlrRepository, TblCdmaHlr, Long> {

    @Cacheable(key = "prefix",enableCaffeine = true)
    @SelectorDataSource(DataSourceNamesConstant.TWO)
    public TblCdmaHlr getHlrByPhoneprefix(String prefix){
        return  ((TblCdmaHlrRepository)baseRepository).getTblCdmaHlrByPhoneprefix(prefix);
    }

}
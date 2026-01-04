 package io.github.rothschil.domain.database.repository;


 import io.github.rothschil.common.base.persistence.repository.BaseRepository;
 import io.github.rothschil.domain.database.entity.TblCdmaHlr;
 import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

 public interface TblCdmaHlrRepository extends BaseRepository<TblCdmaHlr, Long> , JpaSpecificationExecutor<TblCdmaHlr> {

  TblCdmaHlr getTblCdmaHlrByPhoneprefix(String phoneprefix);
 }

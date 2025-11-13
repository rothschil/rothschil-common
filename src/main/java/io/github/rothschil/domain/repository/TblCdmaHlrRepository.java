 package io.github.rothschil.domain.repository;


 import io.github.rothschil.common.base.persistence.repository.BaseRepository;
 import io.github.rothschil.domain.entity.Intf;
 import io.github.rothschil.domain.entity.TblCdmaHlr;
 import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

 public interface TblCdmaHlrRepository extends BaseRepository<TblCdmaHlr, Long> , JpaSpecificationExecutor<TblCdmaHlr> {


 }

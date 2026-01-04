 package io.github.rothschil.domain.database.repository;


 import io.github.rothschil.common.base.persistence.repository.BaseRepository;
 import io.github.rothschil.domain.database.entity.Intf;
 import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

 public interface IntfRepository extends BaseRepository<Intf, Long> , JpaSpecificationExecutor<Intf> {


 }

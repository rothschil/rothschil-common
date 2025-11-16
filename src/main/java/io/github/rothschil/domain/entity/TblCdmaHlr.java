package io.github.rothschil.domain.entity;


import io.github.rothschil.common.base.persistence.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "tbl_cdma_hlr")
@EqualsAndHashCode(callSuper=false)
public class TblCdmaHlr extends AbstractEntity<Long> {


    /**
     * 主键生成策略（主键自增）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;
    }

    String location1;
    String location2;

    String phoneprefix;

    public TblCdmaHlr() {
    }


}

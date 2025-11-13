package io.github.rothschil.common.base.persistence.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Entity基类
 *
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@MappedSuperclass
public abstract class BaseEntity<ID extends Serializable> extends AbstractEntity<ID> {
    private static final long serialVersionUID = 1L;


    /**
     * @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;

    @Override
    public ID getId() {
        return id;
    }

    @Override
    public void setId(ID id) {
        this.id = id;
    }

}

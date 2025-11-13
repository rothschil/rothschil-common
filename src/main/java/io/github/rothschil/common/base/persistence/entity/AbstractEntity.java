package io.github.rothschil.common.base.persistence.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 * JPA实体类基类
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@SuppressWarnings({})
public abstract class AbstractEntity<ID extends Serializable> implements Persistable<ID>, Serializable {

	private static final long serialVersionUID = 1L;


    @Override
    public abstract ID getId();

    /**
     * Sets the id of the entity.
     *
     * @param id the id to set
     */
    public abstract void setId(final ID id);


    @Override
    public boolean isNew() {

        return null == getId();
    }


    @Override
    public boolean equals(Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }

        AbstractEntity<?> that = (AbstractEntity<?>) obj;

        return null != this.getId() && this.getId().equals(that.getId());
    }


    @Override
    public int hashCode() {

        int hashCode = 17;

        hashCode += null == getId() ? 0 : getId().hashCode() * 31;

        return hashCode;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}

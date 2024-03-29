package io.github.rothschil.common.persistence.jpa.entity;

import io.github.rothschil.common.base.entity.BasePo;
import jakarta.persistence.MappedSuperclass;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;


/** 抽象实体基类，如果主键是数据库端自动生成 请使用{@link BasePo}
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @since 1.0.0
 */
@MappedSuperclass
public abstract class AbstractPo<ID extends Serializable> extends BasePo<ID> implements Persistable<ID> {


	@Override
	public abstract ID getId();

    /**
     * Sets the id of the entity.
     * @param id the id to set
     */
    @Override
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
        AbstractPo<?> that = (AbstractPo<?>) obj;
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

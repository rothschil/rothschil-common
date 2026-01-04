package io.github.rothschil.domain.database.entity;


import io.github.rothschil.common.base.persistence.entity.AbstractEntity;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;


/**
 * @description: TODO
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @date 2025/2/5 14:50
 * @version 1.0
 */
@Entity
@Table(name = "INTF")
@EqualsAndHashCode(callSuper=false)
public class Intf extends AbstractEntity<Long> {


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

    String name;
    String password;

    public Intf() {
    }

    public Intf(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package com.newbie.core.entity;
import com.newbie.core.annotations.DomainEntity;

import java.io.Serializable;

@DomainEntity
public interface BaseDomainEntity<K extends Serializable> extends Serializable {
    /**
     * get entity id
     *
     * @return entity id
     */
    public K getId();
}

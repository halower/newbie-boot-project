package com.newbie.core.entity;

import java.io.Serializable;

public abstract class AggregateRoot <K extends Serializable> implements Serializable {
    /**
     * get aggregate root
     *
     * @return root object
     */
    public abstract K getRoot();

    /**
     * get entity id
     *
     * @return entity id
     */
    public K getId() {
        return getRoot();
    }
}
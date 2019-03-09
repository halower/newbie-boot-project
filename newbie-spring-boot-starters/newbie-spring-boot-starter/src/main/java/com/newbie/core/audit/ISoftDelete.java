package com.newbie.core.audit;

public interface ISoftDelete<ID> {
    boolean getIsDeleted();
    void setIsDeleted(boolean deleted);
    ID getId();
}

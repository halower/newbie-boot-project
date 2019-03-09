package com.newbie.core.audit;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@EqualsAndHashCode(callSuper = false)
public abstract class FullAudited implements Serializable {
    @CreatedDate
    @Getter
    @Column(name = "created_date")
    private Date createdDate;

    @CreatedBy
    @Getter
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedDate
    @Getter
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    @LastModifiedBy
    @Getter
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
}

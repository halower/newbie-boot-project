package com.newbie.core.audit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
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
@Data
@Deprecated
/**
 * 已过期，建议使用{@link BaseEntity}
 */
public abstract class BaseAudited implements Serializable {
    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "CJSJ")
    private Date cjsj;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(name = "ZHXGSJ")
    private Date zhxgsj;

    /**
     * 数据标识编号
     */
    @Column(name = "SJBSBH")
    private String sjbsbh;

    /**
     * 是否删除
     */
    @Column(name = "SFSC")
    private String sfsc ="N";
}

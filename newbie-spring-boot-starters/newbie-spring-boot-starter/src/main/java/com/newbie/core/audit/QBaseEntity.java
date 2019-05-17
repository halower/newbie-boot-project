package com.newbie.core.audit;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

/**
 * QFullAudited is a Querydsl query type for FullAudited
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QBaseEntity extends EntityPathBase<BaseEntity> {

    private static final long serialVersionUID = 1610703507L;

    public static final QBaseEntity fullAudited = new QBaseEntity("baseEntity");


    public final DateTimePath<java.util.Date> CJSJ = createDateTime("CJSJ", java.util.Date.class);

    public final StringPath SBBSBH = createString("SBBSBH");

    public final StringPath SFSC = createString("SFSC");

    public final DateTimePath<java.util.Date> ZHXGSJ = createDateTime("ZHXGSJ", java.util.Date.class);

    public QBaseEntity(String variable) {
        super(BaseEntity.class, forVariable(variable));
    }

    public QBaseEntity(Path<? extends BaseEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseEntity(PathMetadata metadata) {
        super(BaseEntity.class, metadata);
    }
}


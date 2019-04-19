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
public class QBaseAudited extends EntityPathBase<BaseAudited> {

    private static final long serialVersionUID = 1610703507L;

    public static final QBaseAudited fullAudited = new QBaseAudited("baseAudited");

    public final StringPath CJRYBM = createString("CJRYBM");

    public final DateTimePath<java.util.Date> CJSJ = createDateTime("CJSJ", java.util.Date.class);

    public final StringPath SBBSBH = createString("SBBSBH");

    public final StringPath SFSC = createString("SFSC");

    public final StringPath ZHXGRYBM = createString("ZHXGRYBM");

    public final DateTimePath<java.util.Date> ZHXGSJ = createDateTime("ZHXGSJ", java.util.Date.class);

    public QBaseAudited(String variable) {
        super(BaseAudited.class, forVariable(variable));
    }

    public QBaseAudited(Path<? extends BaseAudited> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseAudited(PathMetadata metadata) {
        super(BaseAudited.class, metadata);
    }
}


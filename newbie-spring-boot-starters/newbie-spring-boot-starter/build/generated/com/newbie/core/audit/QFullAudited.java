package com.newbie.core.audit;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFullAudited is a Querydsl query type for FullAudited
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QFullAudited extends EntityPathBase<FullAudited> {

    private static final long serialVersionUID = -1093638452L;

    public static final QFullAudited fullAudited = new QFullAudited("fullAudited");

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.util.Date> createdDate = createDateTime("createdDate", java.util.Date.class);

    public final StringPath lastModifiedBy = createString("lastModifiedBy");

    public final DateTimePath<java.util.Date> lastModifiedDate = createDateTime("lastModifiedDate", java.util.Date.class);

    public QFullAudited(String variable) {
        super(FullAudited.class, forVariable(variable));
    }

    public QFullAudited(Path<? extends FullAudited> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFullAudited(PathMetadata metadata) {
        super(FullAudited.class, metadata);
    }

}


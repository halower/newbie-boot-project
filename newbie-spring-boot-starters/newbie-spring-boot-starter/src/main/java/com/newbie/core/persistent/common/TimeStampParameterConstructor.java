package com.newbie.core.persistent.common;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Date;

public class TimeStampParameterConstructor implements ParameterConstructor {

    @Override
    public void drive(Query query, String key, Date value) {
        query.setParameter(key, value,TemporalType.TIMESTAMP);
    }
}

package com.newbie.core.persistent.common;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Author: 谢海龙
 * @Date: 2019/4/24 15:48
 * @Description
 */
public interface ParameterConstructor {
    void drive(Query query, String key, Date value);
}


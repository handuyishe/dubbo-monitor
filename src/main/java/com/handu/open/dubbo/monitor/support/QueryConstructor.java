package com.handu.open.dubbo.monitor.support;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

/**
 * Query Constructor
 *
 * @author Zhiguo.Chen <me@chenzhiguo.cn>
 *         Created on 8/19/15.
 */
public class QueryConstructor {

    private Query query = new Query();


    public static QueryConstructor get() {
        return new QueryConstructor();
    }

    public <T> QueryConstructor addIsAttribute(String name, T t) {
        if (t == null && StringUtils.isEmpty(t)) {
            return this;
        }
        query.addCriteria(Criteria.where(name).is(t));
        return this;
    }

    public <T> QueryConstructor addBetweenAttribute(String name, T before, T after) {
        if (before == null && StringUtils.isEmpty(before)) {
            return this;
        }
        if (after == null && StringUtils.isEmpty(after)) {
            return this;
        }
        query.addCriteria(Criteria.where(name).gte(before).lte(after));
        return this;
    }

    public Query getQuery() {
        return query;
    }
}

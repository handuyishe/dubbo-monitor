/**
 * Copyright 2006-2015 handu.com
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

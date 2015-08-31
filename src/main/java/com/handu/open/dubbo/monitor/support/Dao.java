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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 通用DAO实现类
 *
 * @author Jinkai.Ma
 */
@Repository
public class Dao extends SqlSessionDaoSupport {

    /**
     * 设置工厂类
     *
     * @param sqlSessionFactory sql工厂类
     */
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    /**
     * 插入
     *
     * @param prefix 前缀
     * @param key 关键字
     * @param object 参数对象
     */
    public void insert(String prefix, String key, Object object) {
        getSqlSession().insert(prefix + key, object);
    }

    /**
     * 修改
     *
     * @param prefix 前缀
     * @param key 关键字
     * @param object 参数对象
     */
    public void update(String prefix, String key, Object object) {
        getSqlSession().update(prefix + key, object);
    }

    /**
     * 删除
     *
     * @param prefix 前缀
     * @param key 关键字
     * @param id ID
     */
    public void delete(String prefix, String key, Serializable id) {
        getSqlSession().delete(prefix + key, id);
    }

    /**
     * 删除
     *
     * @param key 关键字
     * @param id ID
     */
    public void delete(String key, Serializable id) {
        getSqlSession().delete(key, id);
    }

    /**
     * 删除
     *
     * @param prefix 前缀
     * @param key 关键字
     * @param object 对象
     */
    public void delete(String prefix, String key, Object object) {
        getSqlSession().delete(prefix + key, object);
    }

    /**
     * 获取单条
     *
     * @param <T> 泛型
     * @param prefix 前缀
     * @param key 关键字
     * @param params 参数
     * @return T 泛型结果
     */
    public <T> T get(String prefix, String key, Object params) {
        return getSqlSession().selectOne(prefix + key, params);
    }

    /**
     * 获取集合
     *
     * @param <T> 泛型
     * @param prefix 前缀
     * @param key 关键字
     * @return T 泛型结果
     */
    public <T> List<T> getList(String prefix, String key) {
        return getSqlSession().selectList(prefix + key);
    }

    /**
     * 按查询条件获取集合
     *
     * @param <T> 泛型
     * @param prefix 前缀
     * @param key 关键字
     * @param params 参数
     * @return T 泛型结果
     */
    public <T> List<T> getList(String prefix, String key, Object params) {
        return getSqlSession().selectList(prefix + key, params);
    }

    /**
     * 获取数量
     *
     * @param prefix 前缀
     * @param key 关键字
     * @param params 参数
     * @return Integer
     */
    public Integer count(String prefix, String key, Object params) {
        return getSqlSession().selectOne(prefix + key, params);
    }

    /**
     * 分页
     *
     * @param prefix 前缀
     * @param pageKey 分页关键字
     * @param countKey 求数量关键字
     * @param params 参数
     * @param offset 偏移量
     * @param limit 限定数量
     * @return Object[]
     */
    public Object[] page(String prefix, String pageKey, String countKey, Object params, int offset, int limit) {
        return new Object[]{
                getSqlSession().selectList(prefix + pageKey, params, new RowBounds(offset, limit)),
                getSqlSession().selectOne(prefix + countKey, params)
        };
    }

    /**
     * 执行SQL语句
     *
     * @param sql SQL语句
     * @return boolean
     */
    public boolean executeSql(String sql) {
        try {
            return getSqlSession().getConnection().prepareStatement(sql).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 执行查询SQL语句
     *
     * @param sql SQL语句
     * @return List<Map>
     */
    public List<Map> querySql(String sql) {
        List<Map> list = Lists.newArrayList();
        try {
            ResultSet rs = getSqlSession().getConnection().prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE).executeQuery();
            try {
                ResultSetMetaData rsm = rs.getMetaData(); //获得列集
                int col = rsm.getColumnCount(); //获得列的个数
                String[] colName = new String[col];
                //取结果集中的表头名称, 放在colName数组中
                for (int i = 0; i < col; i++) {
                    colName[i] = rsm.getColumnName(i + 1);
                }
                rs.beforeFirst();
                while (rs.next()) {
                    Map<String, String> map = Maps.newHashMap();
                    for (String aColName : colName) {
                        map.put(aColName, rs.getString(aColName));
                    }
                    list.add(map);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

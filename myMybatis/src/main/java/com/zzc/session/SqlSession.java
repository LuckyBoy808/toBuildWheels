package com.zzc.session;

import java.util.List;

/**
 * mybatis暴露给外部的接口，实现增删改查
 * 1.对外提供数据访问的api
 * 2.对内将请求转发给executor
 * 3.
 *
 * @author zzc
 * @create 2020-03-04 11:19
 */
public interface SqlSession {

    /**
     * 根据传入的条件查询单一结果
     *
     * @param statement     方法对应的sql语句， namespace + id
     * @param parameter     要传入到sql语句中的查询参数
     * @param <T>
     * @return
     */
    <T> T selectOne(String statement, Object parameter);

    <E> List<E> selectList(String statement, Object parameter);

    <T> T getMapper(Class<T> type);
}

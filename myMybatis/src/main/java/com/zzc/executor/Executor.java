package com.zzc.executor;

import com.zzc.config.MappedStatement;

import java.util.List;

/**
 * @author zzc
 * @create 2020-03-04 11:58
 */
public interface Executor {
    /**
     *查询接口
     *
     * @param ms            封装SQL语句的MapperStatement对象
     * @param parameter     传入SQL语句的参数
     * @param <E>
     * @return
     */
    <E> List<E> query(MappedStatement ms, Object parameter);
}

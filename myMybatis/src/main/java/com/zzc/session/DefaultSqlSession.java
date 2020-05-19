package com.zzc.session;

import com.zzc.binding.MapperProxy;
import com.zzc.config.Configuration;
import com.zzc.config.MappedStatement;
import com.zzc.executor.DefaultExecutor;
import com.zzc.executor.Executor;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 1.对外提供数据访问的api
 * 2.对内将请求转发给Executor
 *
 * @author zzc
 * @create 2020-03-04 11:27
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration) {
        super();
        this.configuration = configuration;
        this.executor = new DefaultExecutor(configuration);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        List<Object> selectList = this.selectList(statement, parameter);
        if (selectList.size() == 1) {
            return (T) selectList.get(0);
        } else if (selectList.size() > 1) {
            throw new RuntimeException("Expected one result (or null) to be returned by selectOne(), but found: " + selectList.size());
        } else {
            return null;
        }
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        MappedStatement ms = configuration.getMappedStatements().get(statement);
        return executor.query(ms, parameter);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        // MapperProxy（实现了InvocationHandler接口）只负责业务逻辑
        MapperProxy mapperProxy = new MapperProxy(this);
        // Proxy只生成代理类
        return (T)Proxy.newProxyInstance(type.getClassLoader(), new Class[] {type}, mapperProxy);
    }
}

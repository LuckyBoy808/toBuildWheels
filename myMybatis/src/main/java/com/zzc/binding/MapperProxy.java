package com.zzc.binding;

import com.zzc.session.SqlSession;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author zzc
 * @create 2020-03-04 12:46
 */
@Data
public class MapperProxy implements InvocationHandler {

    private SqlSession session;

    public MapperProxy(SqlSession session) {
        this.session = session;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Collection.class.isAssignableFrom(method.getReturnType())) {
            return session.selectList(method.getDeclaringClass().getName() +
                    "." + method.getName(), args == null ? null : args[0]);
        } else {
            return session.selectOne(method.getDeclaringClass().getName() +
                    "." + method.getName(), args == null ? null : args[0]);
        }
    }
}

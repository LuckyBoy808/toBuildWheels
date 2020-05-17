package com.zzc.factory;

import com.zzc.pool.IMyPool;
import com.zzc.pool.MyDefaultPool;

/**
 * 数据库连接池工厂
 *      单例模式
 *
 * @author zzc
 * @create 2020-05-17 13:51
 */
public class MyPoolFactory {

    public static class CreatePool {
        public static IMyPool myPool = new MyDefaultPool();
    }

    public static IMyPool getInstance() {
        return CreatePool.myPool;
    }
}
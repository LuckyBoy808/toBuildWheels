package com.zzc.pool;

import com.zzc.MyPooledConnection;

/**
 * 数据库连接管道接口
 *
 * @author zzc
 * @create 2020-05-16 21:52
 */
public interface IMyPool {
    MyPooledConnection getMyPooledConnection();

    void createMyPooledConnection(int count);
}
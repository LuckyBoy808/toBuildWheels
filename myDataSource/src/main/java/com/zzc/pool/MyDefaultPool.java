package com.zzc.pool;

import com.zzc.MyPooledConnection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

/**
 * 数据库连接池
 *      连接管道接口的默认实现类
 *
 * @author zzc
 * @create 2020-05-16 22:01
 */
public class MyDefaultPool implements IMyPool {

    // 基于多线程的考虑，这里使用了Vector。
    private static Vector<MyPooledConnection> connections = new Vector<>();
    private static String jdbcDriver;
    private static String jdbcUrl;
    private static String username;
    private static String password;
    private static Integer initCounts;
    private static Integer maxCounts;
    private static Integer incrementCounts;

    public MyDefaultPool() {
        init();

        try {
            Class.forName(jdbcDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 初始化数据库连接池中的连接管道
        createMyPooledConnection(initCounts);
    }

    @Override
    public MyPooledConnection getMyPooledConnection() {
        if (connections.size() < 1) {
            throw new RuntimeException("连接池初始化错误！");
        }

        MyPooledConnection myPooledConnection = null;
        try {
            myPooledConnection = getRealConnection();
            while (null == myPooledConnection) {
                createMyPooledConnection(incrementCounts);
                myPooledConnection = getRealConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return myPooledConnection;
    }

    /**
     * 数据库连接池在创建管道时，应该去看一下是否达到上限，如果没有，则可以创建。
     * 不仅仅要创建出来，还要标示每一个管道的isBusy标志，并添加到集合容器里面去
     *
     * @param count
     */
    @Override
    public void createMyPooledConnection(int count) {
        if (connections.size() > maxCounts ||
                connections.size() + count > maxCounts) {
            throw new RuntimeException("连接池已满");
        }

        for (int i = 0; i < count; i++) {
            try {
                Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
                connections.add(new MyPooledConnection(connection, false));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 同步方法来获取连接池中可用连接，在多线程情况下，只有一个线程访问该方法来获取连接，
     * 防止由于多线程情况下多个线程获取同一个连接从而引起出错
     *
     * @return
     * @throws SQLException
     */
    private synchronized MyPooledConnection getRealConnection() throws SQLException {
        for (MyPooledConnection myPooledConnection : connections) {
            if (!myPooledConnection.isBusy()) {
                // Connection是有超时机制的，一定不能获取到超时的Connection
                if (myPooledConnection.getConn().isValid(3000)) {
                    myPooledConnection.setBusy(true);
                } else {
                    // 如果连接超时，则新建一个连接管道
                    Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
                    myPooledConnection.setConn(conn);
                    myPooledConnection.setBusy(true);
                }
                return myPooledConnection;
            }
        }
        return null;
    }

    /**
     * 初始化数据库连接池配置
     */
    private void init() {
        // 读取对应的配置文件，加载入properties中，并设置到对应的参数中
        InputStream in = MyDefaultPool.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        jdbcDriver = properties.getProperty("jdbcDriver");
        jdbcUrl = properties.getProperty("jdbcUrl");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        initCounts = Integer.valueOf(properties.getProperty("initCounts"));
        maxCounts = Integer.valueOf(properties.getProperty("maxCounts"));
        incrementCounts = Integer.valueOf(properties.getProperty("incrementCounts"));
    }
}
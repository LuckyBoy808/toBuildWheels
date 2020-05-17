package com.zzc;

import lombok.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库连接管道（封装了Connection）
 *
 * @author zzc
 * @create 2020-05-16 21:38
 */
@Data
public class MyPooledConnection {
    /**
     * 数据库连接
     */
    private Connection conn;

    /**
     * 标记该连接是否可用
     */
    private boolean isBusy;

    public MyPooledConnection(Connection conn, boolean isBusy) {
        this.conn = conn;
        this.isBusy = isBusy;
    }

    public void close() {
        this.isBusy = false;
    }

    public ResultSet query(String sql) {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }
}
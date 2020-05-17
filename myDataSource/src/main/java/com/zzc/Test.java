package com.zzc;

import com.zzc.factory.MyPoolFactory;
import com.zzc.pool.IMyPool;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zzc
 * @create 2020-05-17 13:55
 */
public class Test {
    public static IMyPool myPool = MyPoolFactory.getInstance();

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            MyPooledConnection myPooledConnection = myPool.getMyPooledConnection();
            String sql = "select * from user";
            ResultSet rs = myPooledConnection.query(sql);
            try {
                while (rs.next()) {
                    System.out.println(rs.getString("username") + "使用管道" + myPooledConnection.getConn());
                }
                myPooledConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.zzc.executor;

import com.zzc.config.Configuration;
import com.zzc.config.MappedStatement;
import com.zzc.reflect.ReflectUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zzc
 * @create 2020-03-04 12:01
 */
public class DefaultExecutor implements Executor {

    private final Configuration configuration;

    public DefaultExecutor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter) {
        // 定义返回结果集
        List<E> ret = new ArrayList<>();
        try {
            // 加载驱动
            Class.forName(configuration.getJdbcDriver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(configuration.getJdbcUrl(),
                    configuration.getJdbcUsername(), configuration.getJdbcPassword());
            preparedStatement = connection.prepareStatement(ms.getSql());
            // 处理SQL语句中的占位符
            parameterize(preparedStatement, parameter);
            resultSet = preparedStatement.executeQuery();
            // 将结果通过反射技术填充到list中
            handlerResultSet(resultSet, ret, ms.getResultType());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ret;

    }

    /**
     * 对preparedStatement中的占位符进行处理
     *
     * @param preparedStatement
     * @param parameter
     * @throws SQLException
     */
    private void parameterize(PreparedStatement preparedStatement, Object parameter) throws SQLException {
        if (parameter instanceof Integer) {
            preparedStatement.setInt(1, (int) parameter);
        } else if (parameter instanceof Long) {
            preparedStatement.setLong(1, (long) parameter);
        } else if (parameter instanceof String) {
            preparedStatement.setString(1, (String) parameter);
        }
    }

    /**
     * 读取ResultSet对象，并转换为目标对象
     *
     * @param resultSet
     * @param ret
     * @param className
     * @param <E>
     */
    private <E> void handlerResultSet(ResultSet resultSet, List<E> ret, String className) {
        Class<E> clazz = null;
        try {
            clazz = (Class<E>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            while (resultSet.next()) {
                Object o = clazz.newInstance();
                ReflectUtil.setPropToBeanFromResult(o, resultSet);

                ret.add((E) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.zzc.reflect;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zzc
 * @create 2020-03-04 13:54
 */
public class ReflectUtil {

    /**
     * 为指定的bean的propName属性的值设为value
     *
     * @param bean      目标对象
     * @param propName  对象的属性名
     * @param value     设定的属性值
     */
    public static void setPropToBean(Object bean, String propName, Object value) {
        Field f = null;
        try {
            // 获取对象指定的属性
            f = bean.getClass().getDeclaredField(propName);
            f.setAccessible(true);
            f.set(bean, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从ResultSet中读取一行数据，并填充至指定的实体bean
     *
     * @param entity            待填充的实体
     * @param resultSet         从数据库加载的数据
     * @throws SQLException
     */
    public static void setPropToBeanFromResult(Object entity, ResultSet resultSet) throws SQLException {
        // 获取对象的所有字段
        Field[] fields = entity.getClass().getDeclaredFields();
        // 遍历所有字段，从ResultSet中读取相应的字段，并填充到对象的属性中
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getType().getSimpleName().equals("String")) {
                setPropToBean(entity, fields[i].getName(), resultSet.getString(fields[i].getName()));
            } else if (fields[i].getType().getSimpleName().equals("Integer")) {
                setPropToBean(entity, fields[i].getName(), resultSet.getInt(fields[i].getName()));
            } else if (fields[i].getType().getSimpleName().equals("Long")) {
                setPropToBean(entity, fields[i].getName(), resultSet.getLong(fields[i].getName()));
            }
        }
    }

    public static void main(String[] args) throws Exception{
        Class<?> clazz = Class.forName("com.zzc.entity.User");
        Object user = clazz.newInstance();
        System.out.println(user);

        ReflectUtil.setPropToBean(user, "name", "zzc");
        System.out.println(user);
    }
}

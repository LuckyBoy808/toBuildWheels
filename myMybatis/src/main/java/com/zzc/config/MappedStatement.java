package com.zzc.config;

import lombok.Data;

/**
 * @author zzc
 * @create 2020-05-18 21:33
 */
@Data
public class MappedStatement {
    /**
     *命名空间
     */
    private String namespace;
    /**
     *id
     */
    private String resourceId;
    /**
     *返回类型
     */
    private String resultType;

    /**
     *sql语句
     */
    private String sql;
}
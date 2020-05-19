package com.zzc.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储所有配置信息：
 *      数据库配置信息、mapper配置信息
 *
 * @author zzc
 * @create 2020-03-03 15:42
 */
@Data
public class Configuration {
    /**
     *jdbc的驱动
     */
    private String jdbcDriver;

    /**
     *jdbc的url
     */
    private String jdbcUrl;
    /**
     *jdbc的username
     */
    private String jdbcUsername;
    /**
     *jdbc的password
     */
    private String jdbcPassword;

    /**
     * mapper文件中可能有多条SQL语句（MapperStatement对象）
     * Map：可实现快速访问
     */
    private Map<String, MappedStatement> mappedStatements = new HashMap<>();
}
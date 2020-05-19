package com.zzc.session;

import com.zzc.config.Configuration;
import com.zzc.config.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * 1.实例化过程中加载配置文件到Configuration对象
 * 2.生产SqlSession对象
 *
 * @author zzc
 * @create 2020-03-03 15:51
 */
public class SqlSessionFactory {

    private final Configuration configuration = new Configuration();

    public SqlSessionFactory() {
        loadDbInfo();
        loadMappersInfo();
    }

    /**
     * 生产SqlSession对象
     *
     * @return
     */
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }

    // 记录mapper.xml文件存放的位置
    public static final String MAPPER_CONFIG_LOCATION = "mappers";
    // 记录数据库连接信息文件存放的位置
    public static final String DB_CONFIG_FILE = "jdbc.properties";

    /**
     * 加载数据库配置信息
     */
    private void loadDbInfo() {
        // 加载数据库信息配置文件
        InputStream dbIn = SqlSessionFactory.class.getClassLoader().getResourceAsStream(DB_CONFIG_FILE);
        Properties p = new Properties();
        try {
            // 将配置信息写入到Properties对象
            p.load(dbIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将数据库配置信息写入到Configuration对象
        configuration.setJdbcDriver(p.get("jdbc.driver").toString());
        configuration.setJdbcUrl(p.get("jdbc.url").toString());
        configuration.setJdbcUsername(p.get("jdbc.username").toString());
        configuration.setJdbcPassword(p.get("jdbc.password").toString());
    }

    /**
     * 加载指定文件夹下的所有mapper.xml文件
     */
    private void loadMappersInfo() {
        URL resource = null;
        resource = SqlSessionFactory.class.getClassLoader().getResource(MAPPER_CONFIG_LOCATION);
        // 获取指定文件夹信息
        File mappers = new File(resource.getFile());
        if (mappers.isDirectory()) {
            File[] files = mappers.listFiles();
            // 遍历文件夹下所有的mapper.xml，并解析信息后，注入到Configuration对象中
            for (File file : files) {
                loadMapperInfo(file);
            }
        }
    }

    /**
     * 加载指定的mapper.xml文件
     *
     * @param file
     */
    private void loadMapperInfo(File file) {
        // 1.获取解析器
        SAXReader reader = new SAXReader();
        // 2.通过read()方法读取一个文件，转换成document对象
        Document document = null;
        try {
            document = reader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        // 获取根节点：<mapper>
        Element root = document.getRootElement();
        // 获取命令空间namespace属性
        String namespace = root.attribute("namespace").getData().toString();

        // 获取子节点select列表：<select>
        List<Element> selects = root.elements("select");

        // 遍历select节点列表，将信息记录到MapperStatement对象，并注入到Configuration对象中
        for (Element element : selects) {
            MappedStatement mapperStatement = new MappedStatement();
            // 获取id属性
            String id = element.attribute("id").getData().toString();
            // 获取resultType属性
            String resultType = element.attribute("resultType").getData().toString();
            // 获取SQL语句信息
            String sql = element.getData().toString();
            String resourceId = namespace + "." + id;

            mapperStatement.setNamespace(namespace);
            mapperStatement.setResourceId(id);
            mapperStatement.setResultType(resultType);
            mapperStatement.setSql(sql);

            configuration.getMappedStatements().put(resourceId, mapperStatement);
        }
    }
}
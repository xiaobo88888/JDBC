package connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class DBCPTest {
    /**
     * 测试DBCP的数据库连接池技术
     */
    //方式一：不推荐
    @Test
    public void testGetConnection()throws Exception{
        //创建了DBCP的数据库连接池
        BasicDataSource source = new BasicDataSource();


        //设置基本信息
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql:///test");
        source.setUsername("root");
        source.setPassword("lb4610");

        //设置涉及数据库连接池管理的相关属性

        //设置连接池启动时创建的初始化连接数量
        source.setInitialSize(10);
        //设置连接池中可同时连接的最大的连接数
        source.setMaxActive(10);


        Connection connection = source.getConnection();

        System.out.println(connection);
    }

    //（推荐）方式二：加载配置文件
    @Test
    public void testGetConnection1() throws Exception{
        Properties properties = new Properties();
        //方式一：通过类加载器得到流对象
//        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");

        //方式二：直接创建流对象
        FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));

        properties.load(is);
        DataSource source = BasicDataSourceFactory.createDataSource(properties);

        Connection connection = source.getConnection();
        System.out.println(connection);
    }
}

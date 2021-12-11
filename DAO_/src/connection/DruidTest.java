package connection;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class DruidTest {
    //加载配置文件的方式
    @Test
    public void teseGetConnection() throws Exception{
        Properties properties = new Properties();

        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("Druid.properties");

        properties.load(is);
        DataSource source = DruidDataSourceFactory.createDataSource(properties);

        Connection connection = source.getConnection();

        System.out.println(connection);
    }
}

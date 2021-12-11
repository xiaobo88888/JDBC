package connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.Test;

import java.sql.Connection;

public class C3P0Test {
    //方式一：
    @Test
    public void testGetConnection()throws Exception{
        //获取C3P0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test" );
        cpds.setUser("root");
        cpds.setPassword("lb4610");

        //设置相关参数，对数据库连接池进行管理
        //设置初始时数据库连接池中的连接数
        cpds.setInitialPoolSize(10);

        //获取连接
        Connection connection = cpds.getConnection();
        System.out.println(connection);

        //销毁C3P0连接池
//        DataSources.destroy(cpds);
    }


    //方式二：加载配置文件
    @Test
    public void testGetConnection1()throws Exception{

        ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
        Connection connection = cpds.getConnection();
        System.out.println(connection);
    }
}

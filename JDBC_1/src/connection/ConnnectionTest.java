package connection;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnnectionTest {
    @Test
    public void testConnection1() throws SQLException {
        //获取Driver的实现类对象
        Driver driver = new com.mysql.jdbc.Driver();

        //由于当前使用的数据库名为test，所以下面是用的test，如果用别的，这里要修改
        //jdbc:mysql:协议
        //localhost:ip地址（这个是代表本机的）
        //3306:mysql默认的端口号
        //test:当前使用的数据库名

        String url = "jdbc:mysql://localhost:3306/test";
        Properties info = new Properties();
        //将用户名和密码封装在Properties中，键值对的形式
        info.setProperty("user", "root");//用户名
        info.setProperty("password", "lb4610");//密码

        Connection connect = driver.connect(url, info);

        System.out.println(connect);


    }

    @Test
    public void testConnection2() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        //1.获取Driver的实现类对象：使用反射
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //2.提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/test";

        //3.提供连接需要的用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "lb4610");

        //4.获取连接
        Connection connect = driver.connect(url, info);

        System.out.println(connect);

    }

    @Test
    public void testConnection3() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        //1.获取Driver的实现类对象：使用反射
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //提供连接的另外三个基本信息
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "lb4610";

        //注册驱动
        DriverManager.registerDriver(driver);

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    @Test
    public void testConnection4() throws ClassNotFoundException, SQLException {
        //1.提供连接的另外三个基本信息
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "lb4610";

        //2.加载Driver
        //Driver中有一个静态代码块，在类加载的时候执行，且只执行一次
        //而Driver中的静态代码块执行的就是注册驱动，所以这里不需要自己注册了
        /*
        * static {
            try {
                DriverManager.registerDriver(new Driver());
            } catch (SQLException var1) {
                throw new RuntimeException("Can't register driver!");
            }
        }*/
        Class.forName("com.mysql.jdbc.Driver");

        /*Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        //注册驱动
        DriverManager.registerDriver(driver);*/

        //3.获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    @Test
    public void testConnection5() throws IOException, ClassNotFoundException, SQLException {
        //1.读取配置文件中的基本信息
        //通过类加载器读取配置文件
        InputStream is = ConnnectionTest.class.getClassLoader().getResourceAsStream("jdbc_1.properties");

        Properties properties = new Properties();
        properties.load(is);//从输入流中读取键值对

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverName = properties.getProperty("driverName");

        //2.加载驱动
        Class.forName(driverName);

        //3.获取连接
        Connection connection = DriverManager.getConnection(url, user, password);

        System.out.println(connection);
    }
}

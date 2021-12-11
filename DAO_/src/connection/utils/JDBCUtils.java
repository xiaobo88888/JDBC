package connection.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCUtils {
    /**
     *  朴素的数据库的连接
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
        //读取信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties properties = new Properties();
        properties.load(is);

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverName = properties.getProperty("driverName");

        //加载驱动
        Class.forName(driverName);

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);

        return connection;

    }

    /**
     * 朴素的关闭连接和PreparedStatement
     * @param connection
     * @param ps
     */
    public static void closeResource(Connection connection, PreparedStatement ps){
        //资源的关闭
        try {
            if(ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用dbutils.jar包中提供的DBUtils工具类，实现资源的关闭
     * @param connection
     * @param ps
     */
    public static void closeResource1(Connection connection, PreparedStatement ps, ResultSet rs){
//        //资源的关闭
//        try {
//            DbUtils.close(connection);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            DbUtils.close(ps);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            DbUtils.close(rs);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        DbUtils.closeQuietly(connection);
        DbUtils.closeQuietly(ps);
        DbUtils.closeQuietly(rs);
    }


    /**
     * 使用C3P0的数据库连接池技术
     * @return
     * @throws Exception
     */
    //数据库连接池只要一个，如果放入方法中，那么每次都创建一个，效率太低
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");

    public static Connection getConnection1()throws Exception{
        Connection connection = cpds.getConnection();

        return connection;
    }

    /**
     * 使用DBCP数据库连接池结束，获取数据库连接
     * @return
     * @throws Exception
     */
    //数据库连接池只创建一个就行
    private static DataSource source;
    //这里采用静态代码块执行，因为创建DBCP数据库连接池对象需要很多步骤，如果全部提到方法外，语法不支持
    //而采用静态代码块，随着类加载而执行，且只执行一次
    static {
        Properties properties = new Properties();

        try {
            //方式二：直接创建流对象
            FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));

            properties.load(is);
            //创建数据库连接池
            source = BasicDataSourceFactory.createDataSource(properties);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Connection getConnection2()throws Exception{


        Connection connection = source.getConnection();

        return connection;
    }


    /**
     * 使用Druid数据库连接池技术
     */
    private static DataSource source1;
    static {
        try {
            Properties properties = new Properties();

            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("Druid.properties");

            properties.load(is);
            source1 = DruidDataSourceFactory.createDataSource(properties);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Connection getConnection3()throws Exception{

        Connection connection = source1.getConnection();
        return connection;
    }
}

package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCutils {
    /**
     *  数据库的连接
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
     * 关闭连接和PreparedStatement
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
     * 资源关闭的重载
     * @param connection
     * @param ps
     * @param rs
     */
    public static void closeResource(Connection connection, PreparedStatement ps, ResultSet rs){
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
        try {
            if(rs != null)
                rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}

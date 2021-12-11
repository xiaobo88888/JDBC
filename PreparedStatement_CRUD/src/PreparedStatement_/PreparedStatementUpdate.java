package PreparedStatement_;

import org.junit.Test;
import utils.JDBCutils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.Properties;

public class PreparedStatementUpdate {
    //实现数据的删除
    @Test
    public void testDelete(){
//        String sql = "delete from customers where id = ?";
//        update(sql, 3);

        String sql = "update `order` set order_name = ? where order_id = ?";
        //如果sql语句中的字段名或者表名为关键字，那么需要用反引号来隔开
        update(sql, "DD", 2);
    }

    //增删改的通用方法
    public void update(String sql, Object... args){//sql中占位符的个数应该与可变形参的长度一致
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //获取数据库连接
            connection = JDBCutils.getConnection();

            //返回PreparedStatement实例
            ps = connection.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //执行，启动
            ps.execute();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCutils.closeResource(connection, ps);
        }
    }

    //修改数据库的纪录
    @Test
    public void testUpdate(){
        //因为finally那里，定义的变量也需要使用，所以得定义在括号外
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //获取数据库连接
            connection = JDBCutils.getConnection();

            //预编译sql语句，返回PreparedStatement实例
            String sql = "update customers set name = ? where id = ?";
            ps = connection.prepareStatement(sql);

            //填充占位符
            ps.setObject(1, "莫扎特");
            ps.setObject(2, 18);

            //执行操作，启动
            ps.execute();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCutils.closeResource(connection, ps);
        }
    }


    //增加数据库的纪录
    @Test
    public void testInsert() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //先获取连接
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

            Properties properties = new Properties();
            properties.load(is);

            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String url = properties.getProperty("url");
            String driverName = properties.getProperty("driverName");

            Class.forName(driverName);

            connection = DriverManager.getConnection(url, user, password);

            //预编译sql语句，返回PreparedStatement的实例
            String sql = "insert into customers(name, email, birth) values (?, ?, ?)";//? 是一个占位符
            ps = connection.prepareStatement(sql);

            //填充占位符
            ps.setString(1, "哪吒");//索引从1开始
            ps.setString(2, "nezha@gmail.com");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//创建一个指定格式下的SimpleDateFormatt
            java.util.Date date = simpleDateFormat.parse("1000-01-01");//将字符串解析，生成一个Date
            ps.setDate(3, new Date(date.getTime()));//这里构造方法需要long型的，所以得将date型转变

            //执行操作
            ps.execute();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
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
    }

}

package DBUtils_;

import bean.Customer;
import connection.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.net.ContentHandler;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * commons-dbutils 是Apache组织提供的一个开源的JDBC工具类库，封装了针对于数据库的增删改查操作
 */
public class QueryRunnerTest {
    //测试增删改
    @Test
    public void testInsert(){
        Connection connection3 = null;
        try {
            //获取连接
            connection3 = JDBCUtils.getConnection3();

            //编写sql语句
            String sql = "insert into customers (name, email, birth) values(?, ?, ?)";

            //创建QueryRunner对象
            QueryRunner runner = new QueryRunner();
            //调用方法
            int insertCount = runner.update(connection3, sql, "蔡徐坤", "caixukun@qq.com", "1999-01-01");

            System.out.println("添加了" + insertCount + "条纪录");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCUtils.closeResource(connection3, null);
        }
    }

    //测试查询
    /**
     * BeanHandler：是ResultSetHandler的实现类，用于封装表中的一条纪录
     * @throws Exception
     */
    @Test
    public void testQuery(){
        Connection connection3 = null;
        try {
            //获取连接
            connection3 = JDBCUtils.getConnection3();
            //编写sql语句
            String sql = "select id, name, email, birth from customers where id = ?";
            //创建QueryRunner对象
            QueryRunner runner = new QueryRunner();
            //创建ResultSetHandler的实现类
            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
            //调用query方法
            Customer customer = runner.query(connection3, sql, handler, 23);

            System.out.println(customer);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭连接
            JDBCUtils.closeResource(connection3, null);
        }
    }

    /**
     * BeanListHandler：是ResultSetHandler的实现类，用于封装表中的多条纪录构成的集合
     * @throws Exception
     */
    @Test
    public void testQuery2(){
        Connection connection3 = null;
        try {
            //获取连接
            connection3 = JDBCUtils.getConnection3();
            //编写sql语句
            String sql = "select id, name, email, birth from customers where id < ?";
            //创建QueryRunner对象
            QueryRunner runner = new QueryRunner();
            //创建ResultSetHandler的实现类
            BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
            //调用query方法
            List<Customer> list = runner.query(connection3, sql, handler, 23);


            list.forEach(System.out::println);
        }catch (Exception e){
            //关闭资源
            JDBCUtils.closeResource(connection3, null);
        }
    }

    /**
     * MapHandler：是ResultSetHandler的实现类，对应表中的一条纪录
     * 将字段和字段对应的字段值作为map中的key和value
     * @throws Exception
     */
    @Test
    public void testQuery3(){
        Connection connection3 = null;
        try {
            //获取连接
            connection3 = JDBCUtils.getConnection3();
            //编写sql语句
            String sql = "select id, name, email, birth from customers where id = ?";
            //创建QueryRunner对象
            QueryRunner runner = new QueryRunner();
            //创建ResultSetHandler的实现类
            MapHandler handler = new MapHandler();
            //调用query方法
            Map<String, Object> map = runner.query(connection3, sql, handler, 23);

            System.out.println(map);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭连接
            JDBCUtils.closeResource(connection3, null);
        }
    }

    /**
     * MapListHandler：是ResultSetHandler的实现类，对应表中的多条纪录
     * 将字段和字段对应的字段值作为map中的key和value,将Map添加到List集合中
     * @throws Exception
     */
    @Test
    public void testQuery4(){
        Connection connection3 = null;
        try {
            //获取连接
            connection3 = JDBCUtils.getConnection3();
            //编写sql语句
            String sql = "select id, name, email, birth from customers where id < ?";
            //创建QueryRunner对象
            QueryRunner runner = new QueryRunner();
            //创建ResultSetHandler的实现类
            MapListHandler handler = new MapListHandler();
            //调用query方法
            List<Map<String, Object>> mapList = runner.query(connection3, sql, handler, 23);

            mapList.forEach(System.out::println);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭连接
            JDBCUtils.closeResource(connection3, null);
        }
    }

    /**
     * ScalarHandler：是ResultSetHandler的实现类，用于查询特殊值
     * @throws Exception
     */
    @Test
    public void testQuery5(){
        Connection connection3 = null;
        try {
            //获取连接
            connection3 = JDBCUtils.getConnection3();
            //编写sql语句
            String sql = "select count(*) from customers";
            //创建QueryRunner对象
            QueryRunner runner = new QueryRunner();
            //创建ResultSetHandler的实现类
            ScalarHandler handler = new ScalarHandler();
            //调用query方法
            Long count = (Long) runner.query(connection3, sql, handler);

            System.out.println("一共" + count + "条纪录");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭连接
            JDBCUtils.closeResource(connection3, null);
        }
    }

    /**
     * ScalarHandler：是ResultSetHandler的实现类，用于查询特殊值
     * @throws Exception
     */
    @Test
    public void testQuery6(){
        Connection connection3 = null;
        try {
            //获取连接
            connection3 = JDBCUtils.getConnection3();
            //编写sql语句
            String sql = "select max(birth) from customers";
            //创建QueryRunner对象
            QueryRunner runner = new QueryRunner();
            //创建ResultSetHandler的实现类
            ScalarHandler handler = new ScalarHandler();
            //调用query方法
            Date date = (Date) runner.query(connection3, sql, handler);

            System.out.println(date + "的生日最小");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭连接
            JDBCUtils.closeResource(connection3, null);
        }
    }

    /**
     * 自定义ResultSetHandler的实现类
     */
    @Test
    public void testQuery7(){
        Connection connection3 = null;
        try {
            //获取连接
            connection3 = JDBCUtils.getConnection3();
            //编写sql语句
            String sql = "select id, name, birth, email from customers where id = ?";
            //创建QueryRunner对象
            QueryRunner runner = new QueryRunner();
            //创建ResultSetHandler的匿名实现类
            ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {
                @Override
                public Customer handle(ResultSet resultSet) throws SQLException {
//                    return new Customer(30, "成龙", "jack@qq.com", new Date(234324234324L));

                    if(resultSet.next()){
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        Date birth = resultSet.getDate("birth");
                        Customer customer = new Customer(id, name, email, birth);
                        return customer;
                    }
                    return null;
                }
            };
            //调用query方法
            Customer customer = runner.query(connection3, sql, handler, 23);

            System.out.println(customer);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭连接
            JDBCUtils.closeResource(connection3, null);
        }
    }

}

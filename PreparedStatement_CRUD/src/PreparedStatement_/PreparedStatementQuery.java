package PreparedStatement_;

import bean.Customer;
import bean.Order;
import org.junit.Test;
import utils.JDBCutils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用PreparedStatement来实现针对不同表的通用的查询操作
 */
public class PreparedStatementQuery {
    @Test
    public void testGetForList(){
        String sql = "select id, name, email, birth from customers where id < ?";
        List<Customer> list = GetForList(Customer.class, sql, 12);
        list.forEach(System.out::println);
    }
    /**
     * 针对不同表的通用查询操作返回多条记录
     */
    public <T> List<T> GetForList(Class<T> clazz, String sql, Object... args){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取连接
            connection = JDBCutils.getConnection();
            ps = connection.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //处理结果集
            rs = ps.executeQuery();

            //得到列数
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            //创建集合对象
            ArrayList<T> list = new ArrayList<>();

            //这里就要使用while查询多条了
            while (rs.next()) {
                //通过反射创建类的对象
                T t = clazz.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    //得到列名:通过结果集的元数据
//                    String columnName = rsmd.getColumnName(i + 1);
                    //得到列名：getColumnName()
                    //得到列的别名：getColumnLabel()，如果没有起别名那就是获取到列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //得到列值:通过结果集的元数据
                    Object columnValue = rs.getObject(i + 1);

                    //通过反射得到与列名相同的类的属性
                    Field field = clazz.getDeclaredField(columnLabel);

                    //越过检查
                    field.setAccessible(true);

                    //给属性赋值
                    field.set(t, columnValue);
                }
                list.add(t);
            }

            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCutils.closeResource(connection, ps, rs);
        }

        return null;
    }

    @Test
    public void testGetInstance(){
        String sql = "select id, name, email from customers where id = ?";
        Customer customer = getInstance(Customer.class, sql, 12);
        System.out.println(customer);

        sql = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id = ?";
        Order order = getInstance(Order.class, sql, 1);
        System.out.println(order);
    }

    /**
     * 针对不同表的通用查询操作并返回一条纪录
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    //这里的参数多了一个Class类的对象
    public <T> T getInstance(Class<T> clazz, String sql, Object... args){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取连接
            connection = JDBCutils.getConnection();
            ps = connection.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //处理结果集
            rs = ps.executeQuery();

            //得到列数
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                //通过反射创建类的对象
                T t = clazz.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    //得到列名:通过结果集的元数据
//                    String columnName = rsmd.getColumnName(i + 1);
                    //得到列名：getColumnName()
                    //得到列的别名：getColumnLabel()，如果没有起别名那就是获取到列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //得到列值:通过结果集的元数据
                    Object columnValue = rs.getObject(i + 1);

                    //通过反射得到与列名相同的类的属性
                    Field field = clazz.getDeclaredField(columnLabel);

                    //越过检查
                    field.setAccessible(true);

                    //给属性赋值
                    field.set(t, columnValue);
                }
                return t;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCutils.closeResource(connection, ps, rs);
        }

        return null;
    }
}

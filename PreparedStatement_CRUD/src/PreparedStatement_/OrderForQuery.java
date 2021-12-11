package PreparedStatement_;

import bean.Order;
import org.junit.Test;
import utils.JDBCutils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;

/**
 * 针对于order表的查询操作
 */

/**
 * 针对于表的字段名和类的属性名不一致的情况下
 * 1.写sql语句时，就要将字段名的别名取名为类的属性名
 * 2.再通过ResultSetMetaData获取列名时，用getColumaLabel()替换getColumnName()
 *  使用getColumaLabel可以得到列的别名，如果没有别名，也能得到列名
 */
public class OrderForQuery {

    @Test
    public void testOrderForQuery() throws Exception {
        String sql = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id = ?";
        Order order = orderForQuery(sql, 1);
        System.out.println(order);
    }
    /**
     * 通用的针对与order表的查询操作
     * @return
     */
    public Order orderForQuery(String sql, Object... args){
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
                //一个Order类的对象
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    //得到列名:通过结果集的元数据
//                    String columnName = rsmd.getColumnName(i + 1);
                    //得到列名：getColumnName()
                    //得到列的别名：getColumnLabel()，如果没有起别名那就是获取到列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //得到列值:通过结果集的元数据
                    Object columnValue = rs.getObject(i + 1);

                    //通过反射得到与列名相同的类的属性
                    Field field = Order.class.getDeclaredField(columnLabel);

                    //越过检查
                    field.setAccessible(true);

                    //给属性赋值
                    field.set(order, columnValue);
                }
                return order;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCutils.closeResource(connection, ps, rs);
        }

        return null;
    }


    @Test
    public void testQuery1(){

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCutils.getConnection();
            String sql = "select order_id, order_name, order_date from `order` where order_id = ?";
            ps = connection.prepareStatement(sql);

            //填充占位符
            ps.setObject(1, 1);

            rs = ps.executeQuery();
            if (rs.next()) {
                //获取数据
                int id = (int) rs.getObject(1);
                String name = (String) rs.getObject(2);
                Date date = (Date) rs.getObject(3);

                //创建对象
                Order order = new Order(id, name, date);

                System.out.println(order);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCutils.closeResource(connection, ps, rs);
        }



    }
}

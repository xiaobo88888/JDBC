package PreparedStatement_;

import bean.Customer;
import org.junit.Test;
import utils.JDBCutils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;

/**
 * 针对Customers表的查询操作
 */
public class CustomersForQuery {
    @Test
    public void testQueryForCustomers(){
        String sql = "select id, name, email, birth from customers where id = ?";
        Customer customer = QueryForCustomers(sql, 13);
        System.out.println(customer);

        sql = "select name, birth from customers where name = ?";
        Customer customer1 = QueryForCustomers(sql, "周杰伦");
        System.out.println(customer1);
    }

    /**
     * 通用的针对于order表的查询操作
     * @param sql
     * @param args
     * @return
     */
    public Customer QueryForCustomers(String sql, Object... args){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取连接
            connection = JDBCutils.getConnection();

            //预编译sql语句，返回PreparedStatement实例
            ps = connection.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //执行，返回结果集
            rs = ps.executeQuery();

            //处理结果集
            //获取结果集的元数据（元数据，修饰数据的数据）
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取结果集中的列数
            int columnCount = rsmd.getColumnCount();


            if (rs.next()) {
                Customer customer = new Customer();//有数据，则创建一个对象

                //处理结果集中一行数据的每一列
                for (int i = 0; i < columnCount; i++) {
                    //得到每列的值
                    Object columnValue = rs.getObject(i + 1);//索引从1开始

                    //获取每个列的列名
                    String columnName = rsmd.getColumnName(i + 1);

                    //通过反射：给Customer的对象指定的columnName属性赋值columnValue（因为Customer类中的属性和列名一致）
                    //首先得到与列名同名的属性
                    Field field = Customer.class.getDeclaredField(columnName);
                    //假如得到的属性是非共有的，则要越过检查，暴力反射
                    field.setAccessible(true);
                    //将得到的属性赋值
                    field.set(customer, columnValue);
                }

                return customer;
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
        ResultSet resultSet = null;
        try {
            //获取连接
            connection = JDBCutils.getConnection();

            //预编译sql语句，返回PreparedeStatement实例
            String sql = "select * from customers where id = ?";
            ps = connection.prepareStatement(sql);

            //填充占位符
            ps.setObject(1, 1);

            //执行，并返回结果集
            resultSet = ps.executeQuery();

            //处理结果集
            if (resultSet.next()) {//ResultSet的next方法是判断下一条是否有数据，如果有数据，则指针下移

                //获取当前数据的各个字段值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

                //处理获取到的数据

                //方式一——展示出来
            /*System.out.println("id = " + id + ", name = " + name + ", email = "
                    + email + ", birth = " + birth);*/

                //方式二：放入一个数组中
//            Object[] data = new Object[]{id, name, email, birth};

                //方法三：封装成一个对象
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCutils.closeResource(connection, ps, resultSet);
        }


    }
}

package TransactionTest;

import bean.User;
import org.junit.Test;
import utils.JDBCutils;

import java.lang.reflect.Field;
import java.sql.*;

public class testTransaction {
    /**
     * 模拟转账
     */
    @Test
    public void testUpdate(){
        String sql = "update user_table set balance = balance - 100 where user = ?";
        update(sql, "AA");

        String sql2 = "update user_table set balance = balance + 100 where user = ?";
        update(sql2, "BB");

        System.out.println("转账成功");
    }



    //********************未考虑数据库事务的操作****************************
    //增删改的通用方法 version 1.0
    public int update(String sql, Object... args){//sql中占位符的个数应该与可变形参的长度一致
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
            return ps.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCutils.closeResource(connection, ps);
        }

        return 0;
    }


    @Test
    public void testUpdateAndTs(){
        Connection connection = null;

        try {
            connection = JDBCutils.getConnection();

            //1.取消自动提交
            connection.setAutoCommit(false);

            String sql = "update user_table set balance = balance - 100 where user = ?";
            update(connection, sql, "AA");

            //模拟网络异常
            System.out.println(10 / 0);

            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            update(connection, sql2, "BB");

            System.out.println("转账成功");

            //2.提交数据
            connection.commit();

        }catch (Exception e){
            e.printStackTrace();

            //如果出现异常，就回滚
            //3.回滚数据
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }finally {
            //建议在关闭资源之前，将连接恢复为默认状态
            //这个操作主要针对于数据库连接池的使用
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //关闭资源
            JDBCutils.closeResource(connection, null);//PreparedStatement已经在update里面关了

        }

        //关闭资源
    }

    //**********************考虑数据库事务的操作*******************************
    //增删改的通用方法 version 2.0
    public int update(Connection connection, String sql, Object... args){//sql中占位符的个数应该与可变形参的长度一致

        PreparedStatement ps = null;
        try {
            //1.返回PreparedStatement实例
            ps = connection.prepareStatement(sql);

            //2.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //3.执行，启动
            return ps.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //4.关闭资源，这里就不要关闭连接了
            //传入了一个Connection，所以下面的关闭资源就不要去关闭了，因为不是自己造的
            JDBCutils.closeResource(null, ps);
        }

        return 0;
    }


    //*******************************************************

    @Test
    public void testTransactionSelect()throws Exception{
        Connection connection = JDBCutils.getConnection();

        //获取当前连接的隔离级别
        System.out.println(connection.getTransactionIsolation());//返回的是整形，代表各个隔离级别

        //设置数据库的隔离级别
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        //取消自动提交
        connection.setAutoCommit(false);

        String sql = "select user, password, balance from user_table where user = ?";
        User user = getInstance(connection, User.class, sql, "CC");
        System.out.println(user);
    }

    @Test
    public void testTransactionUpdate()throws Exception{
        Connection connection = JDBCutils.getConnection();

        //取消自动提交
        connection.setAutoCommit(false);

        String sql = "update user_table set balance = ? where user = ?";
        update(connection, sql, 5000, "CC");

        Thread.sleep(15000);
        System.out.println("修改结束");

    }
    //通用的查询操作，用于返回数据表中的一条记录，version 2.0:考虑了事务
    public <T> T getInstance(Connection connection, Class<T> clazz, String sql, Object... args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
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
            //这里就不要关闭连接了
            JDBCutils.closeResource(null, ps, rs);
        }

        return null;
    }
}

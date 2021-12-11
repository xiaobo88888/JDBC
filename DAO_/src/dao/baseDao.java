package dao;


import utils.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装了针对数据表的通用操作
 */
public abstract class baseDao {
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
            JDBCUtils.closeResource(null, ps);
        }

        return 0;
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
            JDBCUtils.closeResource(null, ps, rs);
        }

        return null;
    }


    //通用的查询操作，用于返回数据表中的多条记录返回的集合，version 2.0:考虑了事务
    public <T> List<T> GetForList(Connection connection, Class<T> clazz, String sql, Object... args){
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
            JDBCUtils.closeResource(null, ps, rs);
        }

        return null;
    }

    //用于查询特殊值的通用方法
    public <T> T getValue(Connection connection, String sql, Object...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();

            if (rs.next()) {
                return (T) rs.getObject(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(null, ps, rs);
        }

        return null;
    }
}

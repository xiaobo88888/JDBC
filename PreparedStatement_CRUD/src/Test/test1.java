package Test;

import org.junit.Test;
import utils.JDBCutils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.Scanner;

public class test1 {
    @Test
    public void testInsert(){

        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入名字:");
        String name = scanner.next();
        System.out.print("请输入邮箱:");
        String email = scanner.next();
        System.out.print("请输入生日:");
        String birth = scanner.next();

        String sql = "insert into customers (name, email, birth) value (?, ?, ?)";
        int result = update(sql, name, email, birth);
        if(result > 0){
            System.out.println("添加成功");
        }else{
            System.out.println("添加失败");
        }
    }

    //增删改的通用方法
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
            /**
             * execute()方法
             * 如果执行的是查询操作，则返回true
             * 如果执行的是增删改操作（没有结果集），则返回false
             */
//            ps.execute();

            return ps.executeUpdate();
            //这个方法返回的是你影响了多少行数据

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCutils.closeResource(connection, ps);
        }
        return 0;
    }
}

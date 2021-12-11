package Test;

import bean.ExamStudent;
import org.junit.Test;
import utils.JDBCutils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

public class test2 {
    //问题一：添加成绩
    @Test
    public void testInsert(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("四六级：");
        int type = scanner.nextInt();
        System.out.print("身份证：");
        String IDcard = scanner.next();
        System.out.print("准考证：");
        String ExamCard = scanner.next();
        System.out.print("姓名：");
        String StudentName = scanner.next();
        System.out.print("所在地：");
        String location = scanner.next();
        System.out.print("成绩：");
        int grade = scanner.nextInt();

        String sql = "insert into examstudent (type, IDcard, ExamCard, StudentName, location, grade) value(?, ?, ?, ?, ?, ?)";
        int result = update(sql, type, IDcard, ExamCard, StudentName,location, grade);
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

    //问题二：根据身份证号或者准考证号查询成绩
    @Test
    public void testQuery(){
        System.out.println("请选择你要输入的类型：");
        System.out.println("a.身份证号");
        System.out.println("b.准考证号");
        Scanner scanner = new Scanner(System.in);
        String next = scanner.next();
        if("a".equalsIgnoreCase(next)){
            System.out.println("请输入身份证号：");
            String IDCard = scanner.next();

            String sql = "select FlowId, Type type, IDCard, ExamCard, StudentName, Location, Grade from examStudent where IDCard = ?";
            ExamStudent examStudent = getInstance(ExamStudent.class, sql, IDCard);
            System.out.println(examStudent);
        }else if("b".equalsIgnoreCase(next)){
            System.out.println("请输入准考证号：");
            String examCard = scanner.next();

            String sql = "select FlowId, Type type, IDCard, ExamCard, StudentName, Location, Grade from examStudent where ExamCard = ?";
            ExamStudent examStudent = getInstance(ExamStudent.class, sql, examCard);
            System.out.println(examStudent);
        }else{
            System.out.println("选择有误");
        }
    }

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

    //问题三：删除信息
    @Test
    public void testDelete(){
        System.out.println("请输入学生考号：");
        Scanner scanner = new Scanner(System.in);
        String examCard = scanner.next();

        //查询指定准考证号的学生
        String sql = "select FlowId, Type type, IDCard, ExamCard, StudentName, Location, Grade from examStudent where ExamCard = ?";
        ExamStudent examStudent = getInstance(ExamStudent.class, sql, examCard);

        if(examStudent == null){
            System.out.println("查无此人");
        }else{
            //查到了则删除
            sql = "delete from examstudent where ExamCard = ?";
            int update = update(sql, examCard);
            if(update > 0){
                System.out.println("删除成功");
            }else{
                System.out.println("删除失败");
            }
        }
    }
    @Test
    public void testDelete2(){
        System.out.println("请输入学生考号：");
        Scanner scanner = new Scanner(System.in);
        String examCard = scanner.next();

        String sql = "delete from examstudent where ExamCard = ?";
        int update = update(sql, examCard);

        if(update > 0){
            System.out.println("删除成功");
        }else{
            System.out.println("查无此人");
        }

    }
}

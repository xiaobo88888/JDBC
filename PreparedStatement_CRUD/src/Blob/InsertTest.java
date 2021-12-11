package Blob;

import org.junit.Test;
import utils.JDBCutils;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 使用PreparedStatement实现批量插入数据的操作
 *
 * update,delete本身就具有批量操作的效果（如果不加条件限制，那么就批量修改或者删除
 * 所以批量操作主要是指批量插入
 *
 * 批量插入方式一：使用Statement，不建议使用，重复sql语句，太慢
 *
 */
public class InsertTest {
    //批量插入的方式二：使用PreparedStatement
    @Test
    public void testInsert1(){
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCutils.getConnection();

            String sql = "insert into goods(name) value (?)";
            ps = connection.prepareStatement(sql);

            //插入两万条数据
            for (int i = 0; i < 20000; i++) {
                ps.setObject(1, "name_" + i);//这个只是重复插入占位符

                ps.execute();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCutils.closeResource(connection, ps);
        }
    }

    /**
     * 1.addBatch(),executeBatch(),clearBatch()
     * 2.mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持。
     * 		 ?rewriteBatchedStatements=true 写在配置文件的url后面
     * 3.使用更新的mysql 驱动：mysql-connector-java-5.1.37-bin.jar
     */
    //批量插入的方式三：
    @Test
    public void testInsert2(){
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCutils.getConnection();

            String sql = "insert into goods(name) value (?)";
            ps = connection.prepareStatement(sql);

            long start = System.currentTimeMillis();
            //插入两万条数据
            for (int i = 1; i <= 20000; i++) {
                ps.setObject(1, "name_" + i);

                //攒sql
                ps.addBatch();

                if(i % 500 == 0){//这个条件自己根据情况填写
                    //执行Batch
                    ps.executeBatch();

                    //清空Batch
                    ps.clearBatch();
                }
            }

            long end = System.currentTimeMillis();

            System.out.println("花费时间：" + (end - start));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCutils.closeResource(connection, ps);
        }
    }

    //批量操作数据四
    @Test
    public void testInsert3(){
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCutils.getConnection();

            //设置不允许自动提交数据
            connection.setAutoCommit(false);

            String sql = "insert into goods(name) value (?)";
            ps = connection.prepareStatement(sql);

            long start = System.currentTimeMillis();
            //插入两万条数据
            for (int i = 1; i <= 20000; i++) {
                ps.setObject(1, "name_" + i);

                //攒sql
                ps.addBatch();

                if(i % 500 == 0){//这个条件自己根据情况填写
                    //执行Batch
                    ps.executeBatch();

                    //清空Batch
                    ps.clearBatch();
                }
            }

            //统一提交数据
            connection.commit();


            long end = System.currentTimeMillis();

            System.out.println("花费时间：" + (end - start));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCutils.closeResource(connection, ps);
        }
    }
}

package dao.junit;

import bean.Customer;
import dao.CustomerDAOImpl;
import org.junit.Test;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomerDAOImplTest {

    CustomerDAOImpl dao = new CustomerDAOImpl();


    @Test
    public void testInsert(){
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Customer customer = new Customer(1, "刘博", "liubo@qq.com", new Date(43534646435L));

            dao.insert(connection, customer);

            System.out.println("添加成功");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection, null);
        }

    }

    @Test
    public void testDeleteById(){
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            dao.deleteById(connection, 13);

            System.out.println("删除成功");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void testUpdate(){
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            Customer customer = new Customer(23, "煤球", "meiqiu@qq.com", new Date(43534646435L));

            dao.update(connection, customer);

            System.out.println("更新成功");

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void testGetCustomersById(){
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Customer customer = dao.getCustomersById(connection, 10);

            System.out.println(customer);

            System.out.println("获取成功");

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void testGetAll(){
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            List<Customer> list = dao.getAll(connection);

            list.forEach(System.out::println);


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void testGetCount(){
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            long count = dao.getCount(connection);

            System.out.println("表中的记录数为：" + count);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void testGetMaxBirth(){
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            Date maxBirth = dao.getMaxBirth(connection);

            System.out.println("最年轻的是：" + maxBirth.toString());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

}


package Blob;

import bean.Customer;
import org.junit.Test;
import utils.JDBCutils;

import java.io.*;
import java.sql.*;

public class blobTest {
    /**
     * 添加大数据类型
     * @throws Exception
     */
    @Test
    public void testInsert() throws Exception {
        Connection connection = JDBCutils.getConnection();

        String sql = "insert into customers (name, email, birth, photo) value (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setObject(1, "高辉");
        ps.setObject(2, "gaohui@qq.com");
        ps.setObject(3, "2002-04-28");
        FileInputStream is = new FileInputStream(new File("photo\\large.jpeg"));
        ps.setBlob(4, is);

        ps.execute();

        JDBCutils.closeResource(connection, ps);
    }

    /**
     * 修改Blob类型
     * @throws Exception
     */
    @Test
    public void teseUpdate() throws Exception{
        Connection connection = JDBCutils.getConnection();

        String sql = "update customers set photo = ? where name = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        FileInputStream is = new FileInputStream(new File("photo\\aoteman.jpg"));
        ps.setBlob(1, is);
        ps.setObject(2, "刘博");

        ps.execute();

        JDBCutils.closeResource(connection, ps);
    }

    /**
     * 读取Blob类型的数据
     */
    @Test
    public void testQuery(){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            connection = JDBCutils.getConnection();

            String sql = "select id, name, email, birth, photo from customers where id = ?";
            ps = connection.prepareStatement(sql);

            ps.setObject(1, "21");

            rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);

                //将BLob类型的字段下载下来，以文件方式存储在本地
                Blob photo = rs.getBlob("photo");

                //以流的形式获取Blob数据
                is = photo.getBinaryStream();

                //写文件，一次写一个字节数组
                fos = new FileOutputStream(new File("photo\\aoteman2.jpg"));
                byte[] bytes = new byte[1024];
                int len;
                while ((len = is.read(bytes)) != -1) {
                    fos.write(bytes, 0, len);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(fos != null)
                   fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JDBCutils.closeResource(connection, ps, rs);
        }
    }
}

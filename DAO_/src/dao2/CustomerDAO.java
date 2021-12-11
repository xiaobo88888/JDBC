package dao2;

import bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public interface CustomerDAO {
    /**
     * 将customer对象添加到数据库中
     * @param connection
     * @param customer
     */
    void insert(Connection connection, Customer customer);

    /**
     * 针对指定的id，删除表中的一条纪录
     * @param connection
     * @param id
     */
    void deleteById(Connection connection, int id);

    /**
     * 针对内存中的customer对象，去修改数据表中的纪录
     * @param connection
     * @param customer
     */
    void update(Connection connection, Customer customer);

    /**
     * 针对指定的id查询得到对应的customer
     * @param connection
     * @param id
     */
    Customer getCustomersById(Connection connection, int id);

    /**
     * 查询表中的所有纪录构成一个集合
     * @param connection
     * @return
     */
    List<Customer> getAll(Connection connection);

    /**
     * 返回数据表中数据的条目数
     * @param connection
     * @return
     */
    long getCount(Connection connection);

    /**
     * 返回数据表中最大的生日
     * @param connection
     * @return
     */
    Date getMaxBirth(Connection connection);
}

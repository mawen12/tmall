package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.OrderItem;
import tmall.bean.User;
import tmall.util.DBUtil;

public class OrderItemDAO {
	
	public int getTotal() {
		int total = 0;
		String sql = "select count(*) from orderItem ";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				total = rs.getInt(1);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	public int getCount(int uid) {
		int total = 0;
		String sql = "select count(*) from orderItem where uid = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql); )
		{
			ps.setInt(1, uid);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				total = rs.getInt(1);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	public void add(OrderItem bean) {
		String sql = "insert into orderItem values(?, ?, ? ,?, ? )";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, bean.getId());
			ps.setInt(2, bean.getProduct().getId());
			ps.setInt(3, bean.getOrder().getId());
			ps.setInt(4, bean.getUser().getId());
			ps.setInt(5, bean.getNumber());
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				int id = rs.getInt("id");
				bean.setId(id);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(OrderItem bean) {
		String sql = "update orderItem set pid = ?, oid = ?, uid = ?, number = ? where id = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, bean.getProduct().getId());
			ps.setInt(2, bean.getOrder().getId());
			ps.setInt(3, bean.getUser().getId());
			ps.setInt(4, bean.getNumber());
			ps.setInt(5, bean.getId());
			ps.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void delete(int id) {
		String sql = "delete from orderItem where id = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, id);
			ps.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public OrderItem get(int id) {
		OrderItem bean = null;
		String sql = "select * from orderItem where id = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, id);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()){
				bean = new OrderItem();
				int pid = rs.getInt("pid");
				int oid = rs.getInt("oid");
				int uid = rs.getInt("uid");
				int number = rs.getInt("number");
				
				bean.setId(uid);
				bean.setProduct(new ProductDAO().get(pid));
				bean.setOrder(new OrderDAO().get(oid));
				bean.setUser(new UserDAO().get(uid));
				bean.setNumber(number);
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	/**
	 * 不使用uid而使用User对象的原因是因为和get(int id)产生方法重载错误
	 * @param user
	 * @return OrderItem
	 */
	public OrderItem get(User user) {
		OrderItem bean = null;
		String sql = "select * from orderItem where uid = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, user.getId());
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				bean = new OrderItem();
				int id = rs.getInt(1);
				int pid = rs.getInt("pid");
				int oid = rs.getInt("oid");
				int number = rs.getInt("number");
				
				bean.setId(oid);
				bean.setProduct(new ProductDAO().get(pid));
				bean.setOrder(new OrderDAO().get(oid));
				bean.setUser(user);
				bean.setNumber(number);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public List<OrderItem> list(int uid){
		return list(uid, 0, Short.MAX_VALUE);
	}
	
	public List<OrderItem> list(int uid, int start, int count){
		List<OrderItem> beans = new ArrayList<OrderItem>();
		OrderItem bean = null;
		String sql = "select * from orderItem where uid = ? order by id desc limit (null, ?, ?)";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, uid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				bean = new OrderItem();
				int id = rs.getInt(1);
				int pid = rs.getInt("pid");
				int oid = rs.getInt("oid");
				int number = rs.getInt("number");
				
				bean.setId(id);
				bean.setProduct(new ProductDAO().get(pid));
				bean.setOrder(new OrderDAO().get(oid));
				bean.setUser(new UserDAO().get(uid));
				bean.setNumber(number);
				beans.add(bean);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
}

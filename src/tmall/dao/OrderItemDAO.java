package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Order;
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
		String sql = "insert into orderItem values(?, ?, ? ,?)";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, bean.getProduct().getId());
			//订单项在创建的时候，是没有订单信息的
			if(null == bean.getOrder()) {
				ps.setInt(2, -1);
			}else {
				ps.setInt(2, bean.getOrder().getId());
			}
			ps.setInt(3, bean.getUser().getId());
			ps.setInt(4, bean.getNumber());
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
			if(null == bean.getOrder()) {
				ps.setInt(2, -1);
			}else {
				ps.setInt(2, bean.getOrder().getId());
			}
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
				if(-1 != oid) {
					bean.setOrder(new OrderDAO().get(oid));
				}
				
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
				
				bean.setId(id);
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
	//查询某名用户下所有未生成订单的订单项
	public List<OrderItem> listByUser(int uid){
		return listByUser(uid, 0, Short.MAX_VALUE);
	}
	//分页查询
	public List<OrderItem> listByUser(int uid, int start, int count){
		List<OrderItem> beans = new ArrayList<OrderItem>();
		OrderItem bean = null;
		String sql = "select * from orderItem where uid = and oid = -1 ? order by id desc limit  ?, ?";
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
				if(-1 != oid) {
					bean.setOrder(new OrderDAO().get(oid));
				}
				bean.setUser(new UserDAO().get(uid));
				bean.setNumber(number);
				beans.add(bean);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	//查询订单下的所有订单项
	public List<OrderItem> listByOrder(int oid){
		return listByOrder(oid, 0, Short.MAX_VALUE);
	}
	//分页查询
	public List<OrderItem> listByOrder(int oid, int start, int count){
		List<OrderItem> beans = new ArrayList<OrderItem>();
		OrderItem bean = null;
		String sql = "select * from orderItem where oid = ? order by id desc limit ?, ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, oid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next())	{
				bean = new OrderItem();
				int id = rs.getInt(1);
				int pid = rs.getInt("pid");
				int uid = rs.getInt("uid");
				int number = rs.getInt("number");
				
				bean.setId(id);
				if(-1 != oid) {
					bean.setOrder(new OrderDAO().get(pid));
				}
				bean.setProduct(new ProductDAO().get(pid));
				bean.setUser(new UserDAO().get(uid));
				bean.setNumber(number);
				beans.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	//为订单设置订单项集合
	public void fill(Order o) {
		List<OrderItem> ois = listByOrder(o.getId());
		float total = 0;
		for(OrderItem oi : ois) {
			total += oi.getNumber()*oi.getProduct().getPromotePrice();
		}
		o.setTotal(total);
		o.setOrderItems(ois);
	}
	//为订单设置订单项集合
	public void fill(List<Order> os) {
		for(Order o : os) {
			List<OrderItem> ois = listByOrder(o.getId());
			float total = 0;
			int totalNumber = 0;
			for(OrderItem oi : ois) {
				total += oi.getNumber() * oi.getProduct().getPromotePrice();
				totalNumber += oi.getNumber();
			}
			o.setTotal(total);
			o.setOrderItems(ois);
			o.setTotalNumber(totalNumber);
		}
	}
	
	public List<OrderItem> listByProduct(int pid){
		return listByProduct(pid, 0, Short.MAX_VALUE);
	}
	
	public List<OrderItem> listByProduct(int pid, int start, int count){
		List<OrderItem> beans = new ArrayList<OrderItem>();
		OrderItem bean = null;
		String sql = "select * from orderItem where pid = ? order by id desc limit  ?, ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, pid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				bean = new OrderItem();
				int id = rs.getInt(1);
				int oid = rs.getInt("oid");
				int uid = rs.getInt("uid");
				int number = rs.getInt("number");
				
				bean.setId(id);
				bean.setProduct(new ProductDAO().get(pid));
				if(-1 != oid) {
					bean.setOrder(new OrderDAO().get(oid));
				}
				bean.setUser(new UserDAO().get(uid));
				bean.setNumber(number);
				beans.add(bean);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	//获取某一个产品的销量
	public int getSaleCount(int pid) {
		int total = 0;
		String sql = "select sun(number) from orderItem where pid = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, pid);
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
}

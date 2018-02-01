package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tmall.bean.Order;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

public class OrderDAO {
	public static final String WAITPAY = "waitPay";
	public static final String WAITDELIVERY = "waitDelivery";
	public static final String WAITCONFIRM = "waitConfirm";
	public static final String WAITREVIEW = "waitReview";
	public static final String FINISH = "finish";
	public static final String DELETE = "delete";

	//获取总数
	public int getTotal() {
		int total = 0;
		String sql = "select count(*) from order_ ";
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
	//增加
	public void add(Order bean) {
		String sql = "insert into order_ values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setString(1, bean.getOrderCode());
			ps.setString(2, bean.getAddress());
			ps.setString(3, bean.getMobile());
			ps.setString(4, bean.getReceiver());
			ps.setString(5, bean.getPost());
			ps.setString(6, bean.getUserMessage());
			ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			ps.setTimestamp(8, DateUtil.d2t(bean.getPayDate()));
			ps.setTimestamp(9, DateUtil.d2t(bean.getDeliveryDate()));
			ps.setTimestamp(10,DateUtil.d2t(bean.getConfirmDate()));
			ps.setString(11, bean.getStatus());
			ps.setInt(12, bean.getUser().getId());
			ps.execute();	
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				int id = rs.getInt(1);
				bean.setId(id);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//修改
	public void update(Order bean) {
		String sql = "update prder_ set orderCode = ?, address = ?, mobile = ?, receiver= ?"
				+ ", post = ?, userMessage = ?, createDate = ?, payDate = ?, deliveryDate = ?, confirmDate = ?"
				+ ", status = ?, uid = ? where id = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setString(1, bean.getOrderCode());
			ps.setString(2, bean.getAddress());
			ps.setString(3, bean.getMobile());
			ps.setString(4, bean.getReceiver());
			ps.setString(5, bean.getPost());
			ps.setString(6, bean.getUserMessage());
			ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			ps.setTimestamp(8, DateUtil.d2t(bean.getPayDate()));
			ps.setTimestamp(9, DateUtil.d2t(bean.getDeliveryDate()));
			ps.setTimestamp(10, DateUtil.d2t(bean.getConfirmDate()));
			ps.setString(11, bean.getStatus());
			ps.setInt(12, bean.getUser().getId());
			ps.setInt(13, bean.getId());
			ps.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	//删除
	public void delete(int id) {
		String sql = "delete from order_ where id = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, id);
			ps.execute();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	//查询
	public Order get(int id) {
		Order bean = null;
		String sql = "select * from order_ where id =?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, id);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				bean = new Order();
				String orderCode = rs.getString("orderCode");
				String address = rs.getString("address");
				String mobile = rs.getString("mobile");
				String receiver = rs.getString("receiver");
				String post = rs.getString("post");
				String userMessage = rs.getString("userMessage");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
				Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
				Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
				Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));
				String status = rs.getString("status");
				int uid = rs.getInt("uid");
				
				bean.setId(id);
				bean.setOrderCode(orderCode);
				bean.setAddress(address);
				bean.setMobile(mobile);
				bean.setReceiver(receiver);
				bean.setPost(post);
				bean.setUserMessage(userMessage);
				bean.setCreateDate(createDate);
				bean.setPayDate(payDate);
				bean.setDeliveryDate(deliveryDate);
				bean.setConfirmDate(confirmDate);
				bean.setStatus(status);
				bean.setUser(new UserDAO().get(uid));
			}	
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
		
	}
	//查询所有订单数
	public List<Order> list(){
		return list(0, Short.MAX_VALUE);
	}
	//分页查询
	public List<Order> list(int start, int count){
		List<Order> beans = new ArrayList<Order>();
		Order bean = null;
		String sql = "select * from order_ order by id desc limit  ?, ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, start);
			ps.setInt(2, count);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				bean = new Order();
				String orderCode = rs.getString("orderCode");
				String address = rs.getString("address");
				String mobile = rs.getString("mobile");
				String receiver = rs.getString("receiver");
				String post = rs.getString("post");
				String userMessage = rs.getString("userMessage");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
				Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
				Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
				Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));
				String status = rs.getString("status");
				int id = rs.getInt("id");
				int uid = rs.getInt("uid");
				
				bean.setId(id);
				bean.setOrderCode(orderCode);
				bean.setAddress(address);
				bean.setMobile(mobile);
				bean.setReceiver(receiver);
				bean.setPost(post);
				bean.setUserMessage(userMessage);
				bean.setCreateDate(createDate);
				bean.setPayDate(payDate);
				bean.setDeliveryDate(deliveryDate);
				bean.setConfirmDate(confirmDate);
				bean.setStatus(status);
				bean.setUser(new UserDAO().get(uid));
				beans.add(bean);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	public List<Order> list(int uid, String excludedStatus){
		return list(uid, excludedStatus, 0, Short.MAX_VALUE);
	}
	
	
	public List<Order> list(int uid, String excludedStatus, int start, int count){
		List<Order> beans = new ArrayList<Order>();
		Order bean = null;
		String sql = "select * from order_ where uid = ?, status = ? order by id desc limit  ?, ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, uid);
			ps.setString(2, excludedStatus);
			ps.setInt(3, start);
			ps.setInt(4, count);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				bean = new Order();
				int id = rs.getInt(1);
				String orderCode = rs.getString("orderCode");
				String address = rs.getString("address");
				String mobile = rs.getString("mobile");
				String receiver = rs.getString("receiver");
				String post = rs.getString("post");
				String userMessage = rs.getString("userMessage");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
				Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
				Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliceryDate"));
				Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));
				
				bean.setId(id);
				bean.setOrderCode(orderCode);
				bean.setAddress(address);
				bean.setMobile(mobile);
				bean.setPost(post);
				bean.setReceiver(receiver);
				bean.setUserMessage(userMessage);
				bean.setCreateDate(createDate);
				bean.setPayDate(payDate);
				bean.setDeliveryDate(deliveryDate);
				bean.setConfirmDate(confirmDate);
				bean.setStatus(excludedStatus);
				bean.setUser(new UserDAO().get(uid));
				beans.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
}

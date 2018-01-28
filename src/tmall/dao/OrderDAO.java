package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import tmall.bean.Order;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

public class OrderDAO {
	
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
	
	public void add(Order bean) {
		String sql = "insert into order_ values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
			ps.setFloat(13, bean.getTotal());
			ps.setInt(14, bean.getTotalNumber());
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
	
	public void delete(int id) {
		
	}
	
	public Order get(int id) {
		
	}
	
	public List<Order> list(){
		
	}
	
	public List<Order> list(int start, int count){
		
	}
}

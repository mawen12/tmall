package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tmall.bean.Product;
import tmall.bean.Review;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

public class ReviewDAO {
	//获取总数
	public int getTotal() {
		int total = 0;
		String sql = "select count(*) from review ";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ResultSet rs =ps.executeQuery();
			while(rs.next()) {
				total = rs.getInt(1);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	/*
	 * 获取某个产品的所有评价数量
	 */
	public int getTotal(int pid) {
		int total = 0;
		String sql = "select count(*) from review where pid = ?";
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
	//增加
	public void add(Review bean) {
		String sql = "insert intp review values(null, ?, ?, ?, ?)";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setString(1, bean.getContent());
			ps.setTimestamp(2, DateUtil.d2t(bean.getCreateDate()));
			ps.setInt(3, bean.getUser().getId());
			ps.setInt(4, bean.getProduct().getId());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				int id = rs.getInt(1);
				bean.setId(id);
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//修改
	public void update (Review bean) {
		String sql = "update review set content = ?, createDate = ?, uid = ?, pid = ? where id = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setString(1, bean.getContent());
			ps.setTimestamp(2, DateUtil.d2t(bean.getCreateDate()));
			ps.setInt(3, bean.getUser().getId());
			ps.setInt(4, bean.getProduct().getId());
			ps.setInt(5, bean.getId());
			ps.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//删除
	public void delete(int id) {
		String sql = "delete from review where id = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, id);
			ps.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//查询
	public Review get(int id) {
		Review bean = new Review();
		String sql = "select * from review where id = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, id);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				String content = rs.getString("content");
				Date createDate = DateUtil.t2d(rs.getTimestamp("timestap"));
				int uid = rs.getInt("user");
				int pid = rs.getInt("id");
				
				bean.setId(id);
				bean.setContent(content);
				bean.setCreateDate(createDate);
				bean.setUser(new UserDAO().get(uid));
				bean.setProduct(new ProductDAO().get(pid));
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	/*
	 * 获取指定产品的评价数目
	 */
	public int getCount(int pid) {
		int count = 0;
		String sql = "select count(*) from review where pid = ?	";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, pid);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				count = rs.getInt(1);
				return count;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	/*
	 * 获取指定产品的所有评价
	 */
	public List<Review> list(int pid){
		return list(pid, 0, Short.MAX_VALUE);
	}
	//分页查询
	public List<Review> list(int pid , int start, int count){
		List<Review> beans = new ArrayList<Review>();
		Review bean = null;
		String sql = "select * from review where pid = ? order by id desc limit ?,?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, pid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				bean = new Review();
				int id = rs.getInt(1);
				String content = rs.getString("content");
				Date createDate = DateUtil.t2d(rs.getTimestamp("timestamp"));
				int uid = rs.getInt("uid");
				
				bean.setId(id);
				bean.setContent(content);
				bean.setCreateDate(createDate);
				bean.setUser(new UserDAO().get(uid));
				bean.setProduct(new ProductDAO().get(pid));
				beans.add(bean);
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
	/*
	 * 判断某个产品的评价是否存在
	 */
	public boolean isExist(String content, int pid) {
		String sql = "select * from review where content = ? and pid = ? ";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setString(1, content);
			ps.setInt(2, pid);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				return true;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
}

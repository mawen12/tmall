package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.util.DBUtil;

public class PropertyDAO {
	/*
	 * 为什么通过cid来获取总数？
	 * 
	 */
	public int getTotal(int cid) {
		int total = 0;
		try(Connection c = DBUtil.getConnection();
			Statement s = c.createStatement();)
		{
			String sql = "select count(*) from property where cid = "+cid ;
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()) {
				total = rs.getInt(1);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	public void add(Property bean) {
		String sql = "insert into property values(null, ?, ?)";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(2, bean.getCategory().getId());
			ps.setString(3, bean.getName());
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				int id = rs.getInt(1);
				bean.setId(id);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(Property bean) {
		String sql = "update property set cid =? , name = ? where id = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, bean.getCategory().getId());
			ps.setString(2, bean.getName());
			ps.setInt(3, bean.getId());
			ps.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void delete(int id) {
		try(Connection c = DBUtil.getConnection();
			Statement s = c.createStatement();)
		{
			String sql = "delete from property where id = "+ id;
			s.execute(sql);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public Property get(int id) {
		Property bean = null;
		try(Connection c = DBUtil.getConnection();
			Statement s = c.createStatement();)
		{
			String sql = "select * from property where id = "+ id;
			ResultSet rs = s.executeQuery(sql);
			if(rs.next()) {
				bean = new Property();
				int cid = rs.getInt(2);
				String name = rs.getString(3);
				Category category = new CategoryDAO().get(cid);
				
				bean.setCategory(category);
				bean.setName(name);
				bean.setId(id);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public List<Property> list(int cid){
		return list(cid,0, Short.MAX_VALUE);
	}
	
	public List<Property> list(int cid, int start, int count){
		List<Property> beans = new ArrayList<Property>();
		Property bean = null;
		String sql = "select * from property where cid = ? order by int desc limit ?, ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, cid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				bean = new Property();
				int id = rs.getInt(1);
				String name = rs.getString("name");
				Category category  = new CategoryDAO().get(cid);
				
				bean.setId(id);
				bean.setCategory(category);
				bean.setName(name);
				beans.add(bean);
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
		
	}
}

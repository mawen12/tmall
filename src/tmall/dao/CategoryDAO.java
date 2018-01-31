package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Category;
import tmall.util.DBUtil;

public class CategoryDAO {
	
	//获取总数
	public int getTotal() {
		int total = 0;
		try (Connection c = DBUtil.getConnection();
			 Statement s = c.createStatement();)
		{
			String sql = "SELECT count(*) FORM category";
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()) {
				total = rs.getInt(1);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	//增加
	public void add(Category bean) {
		String sql = "INSERT INTO category VALUES(null, ?)";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setString(1, bean.getName());
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
	public void update(Category bean) {
		String sql = "UPDATE category set name = ? where id = ?";
		try(Connection c = DBUtil.getConnection();
			 PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setString(1, bean.getName());
			ps.setInt(2, bean.getId());
			ps.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//删除
	public void delete(int id) {
		try(Connection c = DBUtil.getConnection();
			Statement s = c.createStatement();)
		{
			String sql = "DELETE FROM category WHERE id = "+ id;
			s.execute(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//根据ID获取
	public Category get(int id) {
		Category bean = null;
		try(Connection c = DBUtil.getConnection();
			Statement s = c.createStatement();)
		{
			String sql = "SELECT * FROM category WHERE id ="+ id;
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()) {
				bean = new Category();
				String name = rs.getString(2);
				bean.setName(name);
				bean.setId(id);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	//查询所有
	public List<Category> list(){
		return list(0, Short.MAX_VALUE);
	}
	//分页查询
	public List<Category> list(int start, int count){
		List<Category> beans = new ArrayList<Category>();
		String sql = "SELECT * FROM category ORDER BY id desc limit ?,?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, start);
			ps.setInt(2, count);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Category bean = new Category();
				int id = rs.getInt(1);
				String name = rs.getString(2);
				bean.setId(id);
				bean.setName(name);
				beans.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
}

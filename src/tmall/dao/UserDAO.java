package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.User;
import tmall.util.DBUtil;

public class UserDAO {
	//获取用户总数
	public int getTotal() {
		int total = 0;
		try(Connection c = DBUtil.getConnection();
			Statement s = c.createStatement();)
		{
			String sql = "SELECT count(*) FROM user";
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()) {
				total = rs.getInt(1);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	//向数据库中增加记录
	public void add(User bean) {
		String sql = "INSERT INTO user VALUES(null, ?, ?)";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setString(1, bean.getName());
			ps.setString(2, bean.getPassword());
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
	//修改用户信息
	public void update(User bean) {
		String sql = "UPDATE user SET name = ? ， password = ? where id = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			String name = bean.getName();
			String password = bean.getPassword();
			int id = bean.getId();
			ps.setString(1, name);
			ps.setString(2, password);
			ps.setInt(3, id);
			ps.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//通过id删除用户
	public void delete(int id) {
		try(Connection c = DBUtil.getConnection();
			Statement s = c.createStatement();)
		{
			String sql = "delete from user where id = "+ id;
			s.execute(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//通过id获取用户
	public User get(int id) {
		User bean = null;
		try(Connection c = DBUtil.getConnection();
			Statement s = c.createStatement(); )
		{
			String sql = "select * from user where id = "+ id;
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()) {
				bean = new User();
				String name = rs.getString(2);
				String password = rs.getString(3);
				bean.setId(id);
				bean.setName(name);
				bean.setPassword(password);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	//通过用户名获取用户，用于之后判断用户是否存在
	public User get(String name) {
		User bean = null;
		try(Connection c =DBUtil.getConnection();
			Statement s = c.createStatement();)
		{
			String sql = "select * from user where name = "+ name;
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()) {
				bean = new User();
				String password = rs.getString(3);
				int id = rs.getInt(1);
				bean.setId(id);
				bean.setName(name);
				bean.setPassword(password);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	//获取32767个用户信息
	public List<User> list(){
		return list(0, Short.MAX_VALUE);
	}
	
	//获取指定数量的用户
	public List<User> list(int start, int count) {
		List<User> beans = new ArrayList<User>();
		String sql = "select * from user order by id desc limit ?, ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, start);
			ps.setInt(2, count);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				User bean = new User();
				String name = rs.getString("name");
				String password = rs.getString("password");
				int id = rs.getInt(1);
				bean.setId(id);
				bean.setName(name);
				bean.setPassword(password);
				beans.add(bean);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	//通过get(String name)来获取用户对象，以此判断用户名是否已存在，存在就为false，不存在就为true;
	public boolean isExist(String name) {
		User user = get(name);
		return user!=null;
	}
	//根据账号和密码来获取对象，在内存中比较账号以及密码是否正确；
	public User get(String name, String  password) {
		User bean = null;
		String sql = "select * from user where name = ? , password = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setString(1, name);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				bean = new User();
				bean.setId(rs.getInt("id"));
				bean.setName(name);
				bean.setPassword(password);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
}

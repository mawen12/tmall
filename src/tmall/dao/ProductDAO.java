package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

public class ProductDAO {
	
	public int getTotal() {
		int total = 0;
		String sql = "select count(*) from product  ";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				total = rs.getInt(1);
				
			}
			
		}catch(SQLException e) {
			
		}
		return total;
	}
	
	public void add(Product bean) {
		String sql = "insert into product values(?, ?, ?, ?, ?, ?, ?, ?)";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, bean.getId());
			ps.setString(2, bean.getName());
			ps.setString(3, bean.getSubTitle());
			ps.setFloat(4, bean.getOrignalPrice());
			ps.setFloat(5, bean.getPromotePrice());
			ps.setInt(6, bean.getSaleCount());
			ps.setInt(7,  bean.getCategory().getId());
			ps.setTimestamp(8, DateUtil.d2t(bean.getCreateDate()));
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				int id = rs.getInt(1);
				bean.setId(id);
			}
			
		}catch(SQLException e) {
			
		}
	}
	
	public void update(Product bean) {
		String sql = "update product set name = ?, subTitle = ?, orignalPrice = ?, promotePrice = ?,"
				+ " stack = ?, cid = ?, createDate = ?  where id = ? ";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setString(1, bean.getName());
			ps.setString(2, bean.getSubTitle());
			ps.setFloat(3, bean.getOrignalPrice());
			ps.setFloat(4, bean.getPromotePrice());
			ps.setInt(5, bean.getStock());
			ps.setInt(6, bean.getCategory().getId());
			ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			ps.setInt(8, bean.getId());
			ps.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(int id) {
		String sql = "delete from product where id = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, id);
			ps.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public Product get(int id) {
		Product bean = null;
		String sql = "select * from product where id = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, id);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				bean = new Product();
				String name = rs.getString("name");
				String subTitle = rs.getString("subTitle");
				Float orignalPrice = rs.getFloat("orignalPrice");
				Float promotePrice = rs.getFloat("promotePrice");
				int stock = rs.getInt("stock");
				int cid = rs.getInt("cid");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
				
				bean.setId(id);
				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOrignalPrice(orignalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				bean.setCategory(new CategoryDAO().get(cid));
				bean.setCreateDate(createDate);
			}
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	/**
	 * 不使用cid而使用Category对象的原因是因为和get(int id)产生方法重载错误
	 * @param bean
	 * @return Product
	 */
	public Product get(Category category) {
		Product bean = null;
		String sql = "select * from product where cid = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, category.getId());
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				bean = new Product();
				String name = rs.getString("name");
				String subTitle = rs.getString("subTitle");
				Float orignalPrice = rs.getFloat("orignalPrice");
				Float promotePrice = rs.getFloat("promotePrice");
				int stock = rs.getInt("stock");
				int id = rs.getInt("id");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
				
				bean.setId(id);
				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOrignalPrice(orignalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				bean.setCategory(category);
				bean.setCreateDate(createDate);
			}
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public List<Product> list(int cid){
		return list(cid, 0, Short.MAX_VALUE);
	}
	
	public List<Product> list(int cid, int start, int count){
		List<Product> beans = new ArrayList<Product>();
		Product bean = null;
		String sql = "select * from product where cid = ? order by id desc limit (null, ?, ?)	";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, cid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				bean = new Product();
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String subTitle = rs.getString("subTitle");
				Float orignalPrice = rs.getFloat("orignalPrice");
				Float promotePrice = rs.getFloat("promotePrice");
				int stock = rs.getInt("stock");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
				
				bean.setId(id);
				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOrignalPrice(orignalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				bean.setCategory(new CategoryDAO().get(cid));
				bean.setCreateDate(createDate);
				beans.add(bean);
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
}

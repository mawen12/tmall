package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.DBUtil;

public class ProductImageDAO {
	/**
	 * 为什么定义两个常量？
	 * type_single:单个图片
	 * type_detail:详情图片
	 */
	public static final String type_single = "type_single";
	public static final String type_detail = "type_detail";
	//获取总数
	public int getTotal() {
		int total = 0;
		
		try(Connection c = DBUtil.getConnection();
			Statement s = c.createStatement();)
		{
			String sql = "select count(*) from productImage";
			
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
	public void add(ProductImage bean) {
		String sql = "insert into productImage values(null, ?, ?)";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, bean.getProduct().getId());
			ps.setString(2, bean.getType());
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
	/*
	 * 为什么不需要更新方法？
	 * 业务上不需要此功能
	 */
	public void update(ProductImage bean) {
		
	}
	//删除
	public void delete(int id) {
		String sql = "delete from productImage where id = ?";
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
	public ProductImage get(int id) {
		ProductImage bean = null;
		String sql = "select * from productImage where id = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, id);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				bean = new ProductImage();
				int pid = rs.getInt(2);
				Product product = new ProductDAO().get(pid);
				String type = rs.getString("type");
				bean.setProduct(product);
				bean.setType(type);
				bean.setId(id);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	/*
	 * 为什么通过Product和type来获取
	 * 获取产品的指定类型的所有图片
	 */
	public List<ProductImage> list(Product p, String type) {
		return list(p, type,0, Short.MAX_VALUE);
	}
	//获取产品的指定类型的图片
	public List<ProductImage> list(Product p, String type, int start, int count){
		List<ProductImage> beans = new ArrayList<ProductImage>();
		String sql = "select * from productImage where pid = ? and type = ? order by id desc limit (null, ?, ?)";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, p.getId());
			ps.setString(2, type);
			ps.setInt(3, start);
			ps.setInt(4, count);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				ProductImage bean = new ProductImage();
				int id = rs.getInt(1);
				
				bean.setId(id);
				bean.setProduct(p);
				bean.setType(type);
				beans.add(bean);
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
		
	}
}

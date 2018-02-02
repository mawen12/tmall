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
import tmall.bean.ProductImage;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

public class ProductDAO {
	//获取指定分类下的总数
	public int getTotal(int cid) {
		int total = 0;
		String sql = "select count(*) from product where cid = ?  ";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, cid);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				total = rs.getInt(1);
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	//增加
	public void add(Product bean) {
		String sql = "insert into product values(?, ?, ?, ?, ?, ?, ?, ?)";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setString(1, bean.getName());
			ps.setString(2, bean.getSubTitle());
			ps.setFloat(3, bean.getOrignalPrice());
			ps.setFloat(4, bean.getPromotePrice());
			ps.setInt(5, bean.getStock());
			ps.setInt(6,  bean.getCategory().getId());
			ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
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
	//修改
	public void update(Product bean) {
		String sql = "update product set name = ?, subTitle = ?, orignalPrice = ?, promotePrice = ?,"
				+ " stock = ?, cid = ?, createDate = ?  where id = ? ";
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
	//删除
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
	//查询
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
	//获取指定分类下所有产品
	public List<Product> list(int cid){
		return list(cid, 0, Short.MAX_VALUE);
	}
	//分页查询
	public List<Product> list(int cid, int start, int count){
		List<Product> beans = new ArrayList<Product>();
		Product bean = null;
		String sql = "select * from product where cid = ? order by id desc limit ?, ?";
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
	//获取所有产品
	public List<Product> list(){
		return list(0, Short.MAX_VALUE);
	}
	//分页查询
	public List<Product> list(int start, int count){
		List<Product> beans = new ArrayList<Product>();
		Product bean = null;
		String sql = "select * from product order by id desc limit ?,?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, start);
			ps.setInt(2, count);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				bean = new Product();
				int id = rs.getInt(1);
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
				beans.add(bean);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
	public void fill(Category c) {
		List<Product> ps = this.list(c.getId());
		c.setProducts(ps);
	}
	
	public void fill(List<Category> cs) {
		for (Category c : cs) {
			fill(c);
		}
	}
	//按行设置产品数目
	public void fillByRow(List<Category> cs) {
		int productNumberEachRow = 8;
		for (Category c : cs) {
			List<Product> products = c.getProducts();
			List<List<Product>> productsByRow = new ArrayList<>();
			for(int i = 0; i < products.size(); i += productNumberEachRow) {
				int size = i + productNumberEachRow;
				size = size > products.size() ? products.size() : size;
				List<Product> productsOfEachRow = products.subList(i, size);
				productsByRow.add(productsOfEachRow);
			}
			c.setProductsByRow(productsByRow);
		}
		
	}
	//设置产品主图片
	public void setFirstProductImage(Product p) {
		List<ProductImage> pis = new ProductImageDAO().list(p, ProductImageDAO.TYPE_SINGLE);
		if(!pis.isEmpty()) {
			p.setFirstProductImage(pis.get(0));
		}
	}
	//设置销售和评价数量
	public void setSaleAndReviewNumber (Product p) {
		int saleCount = new OrderItemDAO().getSaleCount(p.getId());
		p.setSaleCount(saleCount);
		
		int reviewCount = new ReviewDAO().getCount(p.getId());
		p.setReviewCount(reviewCount);
	}
	
	public void setSaleAndReviewNumber(List<Product> products) {
		for (Product p : products) {
			setSaleAndReviewNumber(p);
		}
	}
	//关键字查询
	public List<Product> search(String keyword, int start, int count){
		List<Product> beans = new ArrayList<Product>();
		if(null == keyword || 0 == keyword.trim().length()) {
			return beans;
		}
		
		Product bean = null;
		String sql = "select * from product where name like ? limit ?, ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setString(1, keyword);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				bean = new Product();
				int id = rs.getInt(1);
				String name = rs.getString("name");
				String subTitle = rs.getString("subTitle");
				float orignalPrice = rs.getFloat("orignalPrice"); 
				float promotePrice = rs.getFloat("promotePrice");
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
				
				setFirstProductImage(bean);
				beans.add(bean);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
	
	
}

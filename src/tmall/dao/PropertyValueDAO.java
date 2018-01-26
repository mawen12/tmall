package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Product;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.util.DBUtil;

public class PropertyValueDAO {
	
	public int getTotal() {
		int total = 0;
		String sql = "select count(*) from propertyValue";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				total = rs.getInt(1);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	public void add(PropertyValue	bean) {
		String sql = "insert into propertyValue values(null, ?, ?, ?)";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, bean.getProduct().getId());
			ps.setInt(2, bean.getProperty().getId());
			ps.setString(3, bean.getValue());
			ps.execute();
			/**
			 *增加是否需要设置ID？修改不需要设置
			 *需要设置 ,是为了设置增加的id
			 */
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				int id = rs.getInt(1);
				bean.setId(id);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void update(PropertyValue bean) {
		String sql = "update propertyValue set pid = ?, ptid = ?, value = ? where id = ? ";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, bean.getProduct().getId());
			ps.setInt(2, bean.getProperty().getId());
			ps.setString(3, bean.getValue());
			ps.setInt(4, bean.getId());
			ps.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void delete(int id) {
		String sql = "delete from propertyValue where id = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, id);
			ps.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public PropertyValue get(int id) {
		PropertyValue bean = null;
		String sql = "select * from propertyValue where id = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, id);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				bean = new PropertyValue();
				int pid = rs.getInt(2);
				int ptid = rs.getInt(3);
				String value = rs.getString("value");
				 
				bean.setId(id);
				//因为无法通过Product的方法来获取指定id的商品，所以只能通过ProductDAO来获取
				bean.setProduct(new ProductDAO().get(pid));
				bean.setProperty(new PropertyDAO().get(ptid));
				bean.setValue(value);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	/*
	 * 增加一个通过产品和属性的id来获取属性值得方法
	 */
	public PropertyValue get(int pid, int ptid) {
		PropertyValue bean = new PropertyValue();
		String sql = "select * from propertyValue where pid = ? and ptid = ?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, pid);
			ps.setInt(2, ptid);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()){
				int id = rs.getInt("id");
				String value = rs.getString("value");
				bean.setId(id);
				bean.setProduct(new ProductDAO().get(pid));
				bean.setProperty(new PropertyDAO().get(ptid));
				bean.setValue(value);
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	
	public List<PropertyValue> list(){
		return list(0, Short.MAX_VALUE);
	}
	
	public List<PropertyValue> list(int start, int count){
		List<PropertyValue> beans = new ArrayList<PropertyValue>();
		PropertyValue bean = null;
		String sql = "select * from propertyValue order by id desc limit ?,?";
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, start);
			ps.setInt(2, count);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				bean = new PropertyValue();
				int id = rs.getInt(1);
				int pid = rs.getInt(2);
				int ptid = rs.getInt(3);
				String value = rs.getString("value");
				
				bean.setId(ptid);
				bean.setProduct(new ProductDAO().get(pid));
				bean.setProperty(new PropertyDAO().get(ptid));
				bean.setValue(value);
				beans.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	/*
	 * 增加通过Product的对象初始化的方法
	 */
	public void init(Product p) {
		List<Property> pts = new PropertyDAO().list(p.getCategory().getId());
		
		for(Property pt:pts) {
			PropertyValue pv = get(pt.getId(), p.getId());
			if(null == pv) {
				pv = new PropertyValue();
				pv.setProduct(p);
				pv.setProperty(pt);
				this.add(pv);
			}
			
		}
			
	}
	
	/*
	 * 增加通过Product的id来获取全部对象的方法
	 * 即查询某个产品下的所有属性值
	 */
	public List<PropertyValue> list(int pid){
		List<PropertyValue> beans = new ArrayList<PropertyValue>();
		PropertyValue bean = null;
		String sql = "select * from propertyValue where pid = ? order by id desc"; 
		try(Connection c = DBUtil.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, pid);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				bean = new PropertyValue();
				int id = rs.getInt(1);
				int ptid = rs.getInt("ptid");
				String value = rs.getString("value");
				
				bean.setId(ptid);
				bean.setProduct(new ProductDAO().get(pid));
				bean.setProperty(new PropertyDAO().get(ptid));
				bean.setValue(value);
				beans.add(bean);
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
		
	}
	
}

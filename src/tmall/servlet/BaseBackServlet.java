package tmall.servlet;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import tmall.dao.CategoryDAO;
import tmall.dao.OrderDAO;
import tmall.dao.OrderItemDAO;
import tmall.dao.ProductDAO;
import tmall.dao.ProductImageDAO;
import tmall.dao.PropertyDAO;
import tmall.dao.PropertyValueDAO;
import tmall.dao.ReviewDAO;
import tmall.dao.UserDAO;
import tmall.util.Page;

public abstract class BaseBackServlet extends HttpServlet {
	
	protected CategoryDAO categoryDAO = new CategoryDAO();
	protected ProductDAO productDAO = new ProductDAO();
	protected ProductImageDAO prodcutImageDAO = new ProductImageDAO();
	protected PropertyDAO propertyDAO = new PropertyDAO();
	protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
	protected UserDAO userDAO = new UserDAO();
	protected ReviewDAO reviewDAO = new ReviewDAO();
	protected OrderDAO orderDAO = new OrderDAO();
	protected OrderItemDAO orderItemDAO = new OrderItemDAO();
	
	public abstract String add(HttpServletRequest request, HttpServletResponse response, Page page);
	
	public abstract String delete(HttpServletRequest request, HttpServletResponse response, Page page);
	
	public abstract String edit(HttpServletRequest request, HttpServletResponse response, Page page);
	
	public abstract String update(HttpServletRequest request, HttpServletResponse response, Page page);
	
	public abstract String list(HttpServletRequest request, HttpServletResponse response, Page page);
	
	
	/*通用的service类，完成分页查询、反射操作、*/
	public void service(HttpServletRequest request, HttpServletResponse response) {
		try {
		/*1、分页查询,获取网页上传递过来的数量以及每页显示的数量，默认值为5*/
		int start = 0;
		int count = 5;
		
		try {
			start = Integer.parseInt(request.getParameter("start"));
		}catch(NumberFormatException e) {
			e.printStackTrace();
		}
		
		try {
			count = Integer.parseInt(request.getParameter("count"));
		}catch(NumberFormatException e) {
			e.printStackTrace();
		}
		
		Page page = new Page(start, count);
		
		/*2、借助反射，来调用对应方法*/
			/*从BackServletFilter中传递过来的method属性*/
		String method = (String)request.getAttribute("method");
			/*
			 * Method Class.getMethod(String name, Class<?>...parameterTypes;
			 * 获取Class对象名称为name的方法
			 */
		Method m = this.getClass().getMethod(method, HttpServletRequest.class, HttpServletResponse.class, Page.class);
			System.out.println("Method :"+m);
			
		String redirect = m.invoke(this, request, response, page).toString();
			System.out.println("redirect :"+redirect);
		
		/*3、对不同方法，进行对应的服务端、客户端跳转，或只是作为字符串传递*/
		if(redirect.startsWith("@")) {
			/*当redirect以@开头的时候，进行重定向*/
			response.sendRedirect(redirect.substring(1));
		}else if(redirect.startsWith("%")) {
			/*当redirect以%开头的时候，直接输出字符创*/
			response.getWriter().print(redirect.substring(1));
		}else {
			/*如果都不符合上述判断，就进行请求转发*/
			request.getRequestDispatcher("redirect").forward(request, response);
		}
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
	}
	/*文件上传的方法*/
	public InputStream parseUpload(HttpServletRequest request, Map<String, String> params) {
		InputStream is = null;
		try {
			/*构建文件上传对象*/
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			/*设置文件上传大小为10M*/
			factory.setSizeThreshold(1024 * 10240);
			/*ServletFileUpload对象从request中解析出装有FileItem对象*/
			List list = upload.parseRequest(request);
			/*使用iterator迭代出list中的元素，使用迭代器Iterator比for循环和while循环的好处在于：
			 * Iterator不用知道集合的内部结构（不需要知道几个是ArrayList或者LinkedList的），
			 * Iterator模式总是用用一种逻辑来遍历集合*/
			Iterator iterator = list.iterator();
			while(iterator.hasNext()) {
				FileItem item = (FileItem)iterator.next();
				if(!item.isFormField()) {
					/*判断结果取反，表示该文件是file类型,获取上传文件的上传流*/
					is = item.getInputStream();
				}else {
					
					/*getFieldName：用来返回表单标签的name属性的值*/
					String paramName = item.getFieldName();
					/*getString：将FileItem对象中保存的数据流内容以一个字符串返回*/
					String paramValue = item.getString();
					/*使用指定的"UTF-8"来解码paramValue，并构建一个新的String*/
					paramValue = new String(paramValue.getBytes("ISO-8859-1"), "UTF-8");
					params.put(paramName, paramValue);
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return is;
		
	}
	
	
}

package tmall.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * 定义一个拦截器，对地址栏所有的访问进行拦截，并对/admin开头的地址进行操作
 * @author mw
 *
 */
public class BackServletFilter implements Filter {

	public void destroy() {
		
	}
	
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		//类型强制转换
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;

		/*
		 * 输入：http://127.0.0.1:8080/tmall/admin_category_list
		 * 返回：/tmall
		 */
		String contextPath = request.getServletContext().getContextPath();
		
		/*
		 * getRequestURI()：返回除去host部分的路径
		 * 输入：http://127.0.0.1:8080/tmall/admin_category_list
		 * 返回：/tmall/admin_category_list
		 */
		String uri = request.getRequestURI();
		uri = StringUtils.remove(uri, contextPath);///admin_category_list
		if(uri.startsWith("/admin_")) {
			String servletPath = StringUtils.substringBetween(uri, "_", "_") + "Servlet";//categoryServlet
			String method = StringUtils.substringAfterLast(uri, "_");//list
			//在request对象中设置method属性，并赋值为method；与session不同，只能在同一个请求中来访问
			request.setAttribute("method", method);
			//将客户端的请求转向(forward)到getRequestDispatcher()方法中参数定义的页面
			req.getRequestDispatcher("/" + servletPath).forward(request, response);
			return;
		}
		chain.doFilter(request, response);
		
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

}

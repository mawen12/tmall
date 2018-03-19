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

/*
 * 定义一个拦截器，对地址栏所有的访问进行拦截，并对/admin开头的地址进行操作
 */
public class BackServletFilter implements Filter {

	@Override
	public void destroy() {
		
	}

	/*过滤器的逻辑业务方法*/
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		
		String contextPath = request.getServletContext().getContextPath();
		String uri = request.getRequestURI();
		//http://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/StringUtils.html#remove-java.lang.String-char-
		/*remove()：从uri中移除与contextPath共同的部分*/
		uri = StringUtils.remove(uri, contextPath);
		if(uri.startsWith("/admin_")) {
			/*substringBetween()：获取uri中第一次出现在两个"_"之间的字符串*/
			String servletPath = StringUtils.substringBetween(uri, "_", "_");//category
			/*substringAfterLast()：获取在最后一个"_"后面的字符串*/
			String method = StringUtils.substringAfterLast(uri, "_");
			request.setAttribute("method", method);
			request.getRequestDispatcher("/"+servletPath).forward(request, response);
			return;
		}
		//该方法的调用会将请求转发给下一个过滤器或目标资源
		chain.doFilter(request, response);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
	
	
}

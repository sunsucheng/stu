package com.icss.hr.common;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 公共过滤器
 */
public class CommonFilter implements Filter {

	public void destroy() {
		
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		
		//同意设置请求响应编码
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		
		
		//设置允许其他域跨域请求
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		//登录拦截
		//请求uri
		String uri = request.getRequestURI();
		System.out.println("请求的uri：" + uri);
		
		//web应用名称
		String app = request.getContextPath();
		
		//判断是否是不需要验证的uri
		if (	!uri.equals(app + "/") 
			 && !uri.equals(app + "/login.html")
			 && !uri.equals(app + "/logout.jsp")
			 && !uri.equals(app + "/emp/login")
			 && !uri.startsWith(app + "/css")
			 && !uri.startsWith(app + "/js")
			 && !uri.startsWith(app + "/images")) {
			
			//登录判断
			HttpSession session = request.getSession();
			
			String empLoginName = (String) session.getAttribute("empLoginName");
			
			if (empLoginName == null) {
				
				//判断是否是ajax请求
				String xhr = request.getHeader("x-requested-with");
				
				if (xhr != null && xhr.equals("XMLHttpRequest")) {
					//响应前端一个自定义报头信息
					response.setHeader("sessionStatus", "timeout");					
				}else {
					response.sendRedirect(app + "/logout.jsp");
				}
				
				return;
			}
			
		}
		
		//继续向下执行
		chain.doFilter(arg0, arg1);
		
	}

	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}

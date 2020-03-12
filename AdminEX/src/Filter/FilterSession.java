package Filter;

import Bean.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "FilterSession")
public class FilterSession implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;

        String uri = request.getRequestURI();
        if(uri.endsWith("js") || uri.indexOf("regist") != -1 || uri.indexOf("login") != -1 || "/".equals(uri) || uri.indexOf("fonts") != -1 ||uri.indexOf("css")!=-1){
            chain.doFilter(req,resp);

        }else {
            HttpSession session = request.getSession(false);
            if(session != null && session.getAttribute("user") != null){
                chain.doFilter(req,resp);
            }else {
                response.sendRedirect("/login.jsp");
            }
        }

    }

    public void init(FilterConfig config) throws ServletException {

    }

}

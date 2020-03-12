package Controller;

import Bean.User;
import Util.DBConnPool;
import Util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "ServletLoginCheck")
public class ServletLoginCheck extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        User user = new User();

        try {
            conn = DBConnPool.getConn(request);

            String sql = "select loginName,loginPwd from t_user where loginName=? and loginPwd=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            rs = ps.executeQuery();
            while (rs.next()){
                user.setUsername(rs.getString("loginName"));
                user.setPassword(rs.getString("loginPwd"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            if(conn != null){
                DBConnPool.returnConn(conn,request);
            }

            DBUtil.getClose(null,ps,rs);

        }

        if(user.getUsername() != null && user.getPassword() != null){
            String remember = request.getParameter("remember");
            if("remember-me".equals(remember)){
                Cookie cookieUname = new Cookie("username",username);
                Cookie cookiePwd = new Cookie("password",password);

                cookieUname.setPath(request.getContextPath());
                cookiePwd.setPath(request.getContextPath());

                cookieUname.setMaxAge(60*60*24);
                cookiePwd.setMaxAge(60*60*24);

                response.addCookie(cookieUname);
                response.addCookie(cookiePwd);
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("user",user);


            response.sendRedirect("/index_alt.jsp");

        }else {
            response.sendRedirect("/login.jsp");
        }

    }

}

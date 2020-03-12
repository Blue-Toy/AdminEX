package Controller;

import Util.DBConnPool;
import Util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "ServletIsLogin")
public class ServletIsLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = null;
        String password = null;

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie:cookies){
                String name = cookie.getName();
                String value = cookie.getValue();
                if("username".equals(name)){
                    username = value;
                }else if("password".equals(name)){
                    password = value;
                }
            }
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean success = false;


        try {

            conn = DBConnPool.getConn(request);
            String sql = "select loginName,loginPwd from t_user where loginName=? and loginPwd=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            rs = ps.executeQuery();
            while (rs.next()){
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            if(conn != null){
                DBConnPool.returnConn(conn,request);
            }

            DBUtil.getClose(null,ps,rs);
        }

        if(success){

            response.sendRedirect("/index_alt.jsp");
        }else{
            response.sendRedirect("/login.jsp");
        }
    }
}

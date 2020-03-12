package Controller;

import Util.DBConnPool;
import Util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "ServletRegister")
public class ServletRegister extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String address = request.getParameter("address");
        String email = request.getParameter("email");
        String city = request.getParameter("city");
        String sex = request.getParameter("sex");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        /*System.out.println(fullName);
        System.out.println(address);
        System.out.println(email);
        System.out.println(city);
        System.out.println(sex);
        System.out.println(username);
        System.out.println(password);*/

        Connection conn = null;
        PreparedStatement ps = null;


        int count = 0;

        try {
            conn = DBConnPool.getConn(request);
            String sql = "insert into t_user(FullName,Address,Email,City,Sex,loginName,loginPwd) values(?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1,fullName);
            ps.setString(2,address);
            ps.setString(3,email);
            ps.setString(4,city);
            ps.setString(5,sex);
            ps.setString(6,username);
            ps.setString(7,password);
            count = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            if(conn != null){
                DBConnPool.returnConn(conn,request);
            }

            DBUtil.getClose(null,ps,null);
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        StringBuilder json = new StringBuilder();
        if(count != 0){
            json.append("{\"success\":true}");
        }else{
            json.append("{\"success\":false}");
        }

        out.print(json);

    }
}

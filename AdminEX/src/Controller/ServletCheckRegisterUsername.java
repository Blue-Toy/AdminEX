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

@WebServlet(name = "ServletCheckRegisterUsername")
public class ServletCheckRegisterUsername extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String sex = request.getParameter("sex");
        System.out.println(sex);



        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean flag = false;

        try {
            conn = DBConnPool.getConn(request);
            String sql = "select * from t_user where loginName=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,username);
            rs = ps.executeQuery();
            while (rs.next()){
                flag = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            if(conn != null){
                DBConnPool.returnConn(conn,request);
            }

            DBUtil.getClose(null,ps,rs);
        }

        StringBuilder json = new StringBuilder();
        if(flag){
            json.append("{\"success\":false}");
        }else {
            json.append("{\"success\":true}");
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json);


    }
}

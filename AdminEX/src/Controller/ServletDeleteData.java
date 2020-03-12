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
import java.sql.SQLException;

@WebServlet(name = "ServletDeleteData")
public class ServletDeleteData extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String empno = request.getParameter("empno");

        Connection conn = null;
        PreparedStatement ps = null;

        int count = 0;

        try {

            conn = DBConnPool.getConn(request);
            conn.setAutoCommit(false);
            String sql = "delete from emp_bac where empno=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,empno);
            count = ps.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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
            System.out.println("删除数据成功！！！！！！");
            json.append("{\"success\":true}");
        }else {
            System.out.println("删除数据失败！！！！！！");
            json.append("{\"success\":false}");
        }
        out.print(json);

    }
}

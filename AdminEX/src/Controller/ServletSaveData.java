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
import java.sql.SQLException;

@WebServlet(name = "ServletSaveData")
public class ServletSaveData extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String empno = request.getParameter("empno");
        String ename = request.getParameter("ename");
        String job = request.getParameter("job");
        String sal = request.getParameter("sal");
        /*System.out.println(empno);
        System.out.println(ename);
        System.out.println(job);
        System.out.println(sal);*/

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean flag = false;
        int count = 0;

        try {
            conn = DBConnPool.getConn(request);

            String sql = "select * from emp_bac where empno=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,empno);
            rs = ps.executeQuery();
            if(rs.next()){
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

        if(flag){

            try {
                conn = DBConnPool.getConn(request);
                conn.setAutoCommit(false);
                String sql = "update emp_bac set ename=?,job=?,sal=? where empno=?";
                ps = conn.prepareStatement(sql);
                ps.setString(1,ename);
                ps.setString(2,job);
                ps.setString(3,sal);
                ps.setString(4,empno);
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
                    DBUtil.getClose(null,ps,rs);
                }
            }

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            StringBuilder json = new StringBuilder();
            if(count != 0){
                System.out.println("数据更新成功！！！！！！");
                json.append("{\"success\":true}");
            }else {
                System.out.println("数据更新失败！！！！！！");
                json.append("{\"success\":false}");
            }
            out.print(json);

        }else {
            count = 0;

            try {
                conn = DBConnPool.getConn(request);
                conn.setAutoCommit(false);
                String sql = "insert into emp_bac(empno,ename,job,sal) value(?,?,?,?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1,empno);
                ps.setString(2,ename);
                ps.setString(3,job);
                ps.setString(4,sal);
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
                DBUtil.getClose(null,ps,rs);
            }

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            StringBuilder json = new StringBuilder();
            if(count != 0){
                System.out.println("插入数据成功！！！！！！");
                json.append("{\"success\":true}");
            }else {
                System.out.println("插入数据失败！！！！！！");
                json.append("{\"success\":false}");
            }
            out.print(json);

        }


    }
}

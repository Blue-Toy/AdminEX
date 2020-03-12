package Controller;

import Bean.EMPList;
import Util.DBConnPool;
import Util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ShowList")
public class ServletShowList extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<EMPList> emplist = new ArrayList<>();


        try {
            conn = DBConnPool.getConn(request);

            String sql = "select empno,ename,job,sal from emp_bac";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                int empno = rs.getInt("empno");
                String ename = rs.getString("ename");
                String job = rs.getString("job");
                double sal = rs.getDouble("sal");

                EMPList emp = new EMPList();
                emp.setEmpno(empno);
                emp.setEname(ename);
                emp.setJob(job);
                emp.setSal(sal);

                emplist.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(conn != null){
                DBConnPool.returnConn(conn,request);
            }
            DBUtil.getClose(null,ps,rs);
        }

        request.setAttribute("emplist",emplist);
        request.getRequestDispatcher("/editable_table.jsp").forward(request,response);
    }
}

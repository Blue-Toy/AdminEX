package Util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;

public class DBConnPool {

    public static Connection getConn (HttpServletRequest request) throws Exception{
        ServletContext application = request.getServletContext();
        Connection conn = null;

        Map connPool = (Map) application.getAttribute("connPool");
        Iterator it = connPool.keySet().iterator();
        while (it.hasNext()){
            conn = (Connection) it.next();
            boolean flag = (boolean)connPool.get(conn);
            if(flag == false){
                conn = null;
            }else {
                connPool.put(conn,false);
                break;
            }
        }

        if(conn == null){
            Thread.currentThread().wait();
        }
        System.out.println("取得conn："+conn);
        return conn;
    }

    public static void returnConn(Connection conn,HttpServletRequest request){
        ServletContext application = request.getServletContext();
        Map connPool = (Map) application.getAttribute("connPool");
        connPool.put(conn,true);
        System.out.println("归还conn："+conn);

        try{
            Thread.currentThread().notifyAll();
        }catch (Exception e){
            System.out.println("叫醒你们！！");
        }
    }
}

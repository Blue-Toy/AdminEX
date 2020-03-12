package Listener;

import Bean.EMPList;
import Util.DBUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@WebListener()
public class ListenerContext implements ServletContextListener {

    // Public constructor is required by servlet spec
    public ListenerContext() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext application = sce.getServletContext();
        Connection conn = null;
        Map connPool = new HashMap();

        for(int i = 0;i<20;i++){

            try{
                conn = DBUtil.getConnection();
                connPool.put(conn,true);
                System.out.println("创建conn："+conn);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

        application.setAttribute("connPool",connPool);

    }

    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext application = sce.getServletContext();
        Map connPool = (Map) application.getAttribute("connPool");
        Iterator it = connPool.keySet().iterator();
        while (it.hasNext()){
            Connection conn = (Connection) it.next();
            System.out.println("销毁conn："+conn);
            DBUtil.getClose(conn,null,null);
        }

    }


}

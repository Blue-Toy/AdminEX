package Util;

import java.sql.*;
import java.util.ResourceBundle;

public class DBUtil {

    static ResourceBundle bundle = ResourceBundle.getBundle("jdbc");

    private DBUtil(){

    }

    static {

        String driver = bundle.getString("driver");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = bundle.getString("url");
        String username = bundle.getString("username");
        String password = bundle.getString("password");
        Connection conn = null;
        conn = DriverManager.getConnection(url,username,password);

        return conn;
    }

    public static void getClose(Connection conn, PreparedStatement ps, ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(ps != null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

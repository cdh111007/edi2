package com.smtl.edi.util;

import java.sql.*;
import org.apache.log4j.Logger;

/**
 *
 * @author nm
 */
public class DbUtil {

    private static final Logger LOGGER = Logger.getLogger(DbUtil.class);

    /**
     *
     * @param pst
     */
    public static void close(PreparedStatement pst) {
        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException ex) {
                LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            }
        }
    }

    /**
     *
     * @param rs
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            }
        }
    }

    /**
     *
     * @param st
     */
    public static void close(Statement st) {

        if (st != null) {
            try {
                st.close();
            } catch (SQLException ex) {
                LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            }

        }
    }

    /**
     *
     * @param cs
     */
    public static void close(CallableStatement cs) {

        if (cs != null) {
            try {
                cs.close();
            } catch (SQLException ex) {
                LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            }

        }
    }

    /**
     *
     * @param con
     */
    public static void rollback(Connection con) {

        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            }
        }
    }

    /**
     *
     * @param pst
     * @param rs
     * @param st
     */
    public static void close(PreparedStatement pst, ResultSet rs, Statement st) {
        close(pst);
        close(rs);
        close(st);
    }

    /**
     *
     * @param con
     * @param auto
     */
    public static void setAutoCommit(Connection con, boolean auto) {

        if (con != null) {
            try {
                con.setAutoCommit(auto);
            } catch (SQLException ex) {
                LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            }
        }
    }

    private static final ThreadLocal<Connection> DB_CONNECTION_HOLDER = new ThreadLocal();

    static {
        String driver_ = PropertiesUtil.getValue("driver");
        try {
            Class.forName(driver_);
        } catch (ClassNotFoundException ex) {
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            throw new RuntimeException("加载" + driver_ + "驱动失败");
        }
    }

    /**
     *
     * @param con
     * @param sql
     * @return
     */
    public static PreparedStatement preparedStatement(Connection con, String sql) {
        try {
            return con.prepareStatement(sql);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @return
     */
    public static Connection getConnection() {

        if (DB_CONNECTION_HOLDER.get() == null) {

            try {

                String url_ = PropertiesUtil.getValue("url");
                String username_ = PropertiesUtil.getValue("username");
                String password_ = PropertiesUtil.getValue("password");

                Connection con = DriverManager.getConnection(url_, username_, password_);

                System.out.println("获取新连接 " + con);

                DB_CONNECTION_HOLDER.set(con);

                return con;
            } catch (SQLException ex) {
                LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
                throw new RuntimeException(ex);
            }

        }

        return (Connection) DB_CONNECTION_HOLDER.get();

    }

}

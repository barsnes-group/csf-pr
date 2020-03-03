
package no.uib.probe.csf.pr.touch.listener;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author Yehia Farag
 */
public class SessionListener implements HttpSessionListener, Serializable {

    /**
     * Timer object.
     */
    private Timer timer;
    /**
     * Http session.
     */
    private HttpSession session;
    /**
     * Database connection.
     */
    private Connection conn;
    /**
     * Database connection input parameters.
     */
    private String url, dbName, driver, userName, password;

    @Override
    public void sessionCreated(HttpSessionEvent hse) {
        session = hse.getSession();
        ServletContext scx = hse.getSession().getServletContext();
        url = (scx.getInitParameter("url"));
        dbName = (scx.getInitParameter("dbName"));
        driver = (scx.getInitParameter("driver"));
        userName = (scx.getInitParameter("userName"));
        password = (scx.getInitParameter("password"));
        if (session != null) {
            timer = new Timer();
            timer.schedule(new RemindTask(), (5 * 60 * 60 * 1000));
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
        Object CsrfToken = hse.getSession().getAttribute("CsrfToken");
        if (userName != null && CsrfToken != null) {
            removeAlluserTempStoredQuery(CsrfToken);
        }
    }

    class RemindTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("at **********************************Time's up!**********************************");
            session.invalidate();
            timer.cancel(); //Not necessary because we call System.exit
        }
    };

    /**
     * Clean the stored query from the database
     *
     * @param CsrfToken
     */
    public void removeAlluserTempStoredQuery(Object CsrfToken) {
        String statment = "DELETE  FROM `temp_query_table` WHERE `csrf_token` = '" + CsrfToken + "';";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            Statement st = conn.createStatement();
            st.executeUpdate(statment);
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

}

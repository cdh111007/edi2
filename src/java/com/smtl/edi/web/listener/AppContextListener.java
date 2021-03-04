package com.smtl.edi.web.listener;

import com.smtl.edi.util.PropertiesUtil;
import com.smtl.edi.task.BootTask;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import org.springframework.web.context.ContextLoaderListener;

/**
 *
 * @author nm
 */
public class AppContextListener extends ContextLoaderListener {

    /**
     *
     * @param event
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        try {
            while (DriverManager.getDrivers().hasMoreElements()) {
                DriverManager.deregisterDriver(DriverManager.getDrivers().nextElement());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.contextDestroyed(event);
    }

    /**
     *
     * @param event
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        System.out.println("*****************开始EDI任务*****************");

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

        pool.scheduleAtFixedRate(new BootTask(), 0, 60 * Integer.valueOf(PropertiesUtil.getValue("timer_period")),
                TimeUnit.SECONDS);

    }
}

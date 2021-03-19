package com.smtl.edi.task;

import static com.smtl.edi.core.facade.TaskFacade.coarri;
import static com.smtl.edi.core.facade.TaskFacade.codeco;
import static com.smtl.edi.core.facade.TaskFacade.coedor;
import static com.smtl.edi.core.facade.TaskFacade.cosecr;
import static com.smtl.edi.core.facade.TaskFacade.vesdep;
import java.util.TimerTask;
import org.apache.log4j.Logger;

/**
 *
 * @author nm
 */
public class BootTask extends TimerTask {

    static Logger LOGGER = Logger.getLogger(BootTask.class);

    @Override
    public void run() {

        String begin = "***************************Task Begin***************************";
        String end = "***************************Task End***************************";

        System.out.println(begin);
        LOGGER.info(begin);

        codeco();
        coarri();
        new Thread(new Runnable() {
            @Override
            public void run() {
                cosecr();
                vesdep();
                coedor();
            }
        }).start();

        System.out.println(end);
        LOGGER.info(end);
    }

}

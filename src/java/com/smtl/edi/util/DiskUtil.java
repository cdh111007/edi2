package com.smtl.edi.util;

/**
 *
 * @author nm
 */
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import org.apache.log4j.Logger;

public class DiskUtil {

    private static final Logger LOGGER = Logger.getLogger(DiskUtil.class);

    private DiskUtil() {
    }

    public static String getSerialNumber(String drive) {
        String result = "";
        try {
            File file = File.createTempFile("fso", ".vbs");
            file.deleteOnExit();
            try (FileWriter fw = new java.io.FileWriter(file)) {
                String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                        + "Set colDrives = objFSO.Drives\n"
                        + "Set objDrive = colDrives.item(\"" + drive + "\")\n"
                        + "Wscript.Echo objDrive.SerialNumber";
                System.out.println(vbs);
                fw.write(vbs);
            }
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = input.readLine()) != null) {
                    result += line;
                }
            }
        } catch (IOException e) {
            LOGGER.error(ExceptionUtil.getStackTraceAsString(e));
        }
        return result.trim();
    }

    public static void main(String[] args) throws IOException {
        for (FileStore store : FileSystems.getDefault().getFileStores()) {
            System.out.format("%-20s vsn:%s\n", store, store.getAttribute("volume:vsn"));
        }
        String sn = DiskUtil.getSerialNumber("C");
        javax.swing.JOptionPane.showConfirmDialog((java.awt.Component) null, sn, "Serial Number of C:",
                javax.swing.JOptionPane.DEFAULT_OPTION);
    }
}

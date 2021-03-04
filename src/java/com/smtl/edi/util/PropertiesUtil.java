package com.smtl.edi.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author nm
 */
public final class PropertiesUtil {

    /**
     *
     */
    public static final String CFG_FILE = "cfg.properties";

    /**
     *
     */
    public PropertiesUtil() {
    }

    /**
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {
        java.util.Properties props = new java.util.Properties();
        try {
            //直接加载到内存中，配置文件修改后，需要重启服务
            // InputStream in = PropertiesUtil.class.getResourceAsStream("/" + HARBORTOLLS_CFG_FILE);
            //props.load(in);
            String path = PropertiesUtil.class.getClassLoader().getResource(CFG_FILE).getPath();
            //部署在tomcat7的时候，如果路径存在空格，将导致无法找到路径的错误，需要将%20表示的空格替换成真实的空格字符
            path = path.replace("%20", " ");
            InputStream is = new FileInputStream(path);
            props.load(is);
        } catch (IOException ex) {
            throw ExceptionUtil.unchecked(ex);
        }
        return props.getProperty(key);

    }
}

package org.thaind.signaling.common;

import org.apache.logging.log4j.core.config.Configurator;
import org.thaind.signaling.bootstrap.SingletonHolder;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author duyenthai
 */
public class Config {

    public static final String CONFIG_FOLDER = "config";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    public static final String LOG4J_CONFIG_FILE = "log4j2.xml";
    public static final String APP_CONFIG_FILE = "app.yaml";

    private Config() {
    }

    public static void loadConfig() {
        String logConfigFileLocation = System.getProperty("user.dir") + File.separator + CONFIG_FOLDER + File.separator + LOG4J_CONFIG_FILE;
        Configurator.initialize(null, logConfigFileLocation);
        loadYamlAppConfig();
    }

    private static void loadYamlAppConfig() {
        String appConfigFileLocation = System.getProperty("user.dir") + File.separator + CONFIG_FOLDER + File.separator + APP_CONFIG_FILE;
        AppConfig appConfig = new AppConfig();
        try {
            File file = new File(appConfigFileLocation);
            if (file.exists()) {
                Constructor constructor = new Constructor(AppConfig.class);
                Yaml yaml = new Yaml(constructor);
                appConfig = yaml.load(new FileInputStream(file));
            }

        } catch (Exception ex) {
            System.err.println(ex);
            ex.printStackTrace();
        } finally {
            SingletonHolder.addBean(appConfig);
        }
    }
}

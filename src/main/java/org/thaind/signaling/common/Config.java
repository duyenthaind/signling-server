package org.thaind.signaling.common;

import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;

/**
 * @author duyenthai
 */
public class Config {

    public static final String CONFIG_FOLDER = "config";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    public static final String LOG4J_CONFIG_FILE = "log4j2.xml";

    private Config(){
    }

    public static void loadConfig(){
        String logConfigFileLocation = System.getProperty("user.dir") + File.separator + CONFIG_FOLDER + File.separator + LOG4J_CONFIG_FILE;
        Configurator.initialize(null, logConfigFileLocation);
    }
}

package org.thaind.signaling.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author duyenthai
 */
class ConfigTest {
    @Test
    void loadConfig(){
        String logConfigFileLocation = System.getProperty("user.dir") + File.separator + Config.CONFIG_FOLDER + File.separator + Config.LOG4J_CONFIG_FILE;
        Configurator.initialize(null, logConfigFileLocation);
        Logger log = LogManager.getLogger("Test");
        log.info("OK!");
        assertTrue(true);
    }
}
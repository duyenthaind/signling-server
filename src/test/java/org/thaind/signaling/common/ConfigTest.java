package org.thaind.signaling.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.Test;
import org.thaind.signaling.bootstrap.SingletonHolder;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author duyenthai
 */
class ConfigTest {
    @Test
    void loadConfig() {
        String logConfigFileLocation = System.getProperty("user.dir") + File.separator + Config.CONFIG_FOLDER + File.separator + Config.LOG4J_CONFIG_FILE;
        Configurator.initialize(null, logConfigFileLocation);
        Logger log = LogManager.getLogger("Test");
        log.info("OK!");
        assertTrue(true);
    }

    @Test
    void testLoadConfigFromExternal() {
        Config.loadConfig();
        AppConfig appConfig = SingletonHolder.getBeanOrDefault(AppConfig.class);
        assertNotNull(appConfig);
        System.out.println(appConfig);
        assertEquals(8088, appConfig.getSocketIoServerPort());
        assertEquals(8808, appConfig.getWebsocketServerPort());
    }
}
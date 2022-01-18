package net.javaserver.celerlib;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = "celerlib", name = "CelerLib", version = "1-0-0")
public class Init {
    static Logger logger;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }
    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("CelerLib loaded.");
    }
}

package the_fireplace.cheatblocker.abstraction;

import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

public interface IMinecraftHelper {
    MinecraftServer getServer();
    boolean isPluginLoaded(String id);
    Logger getLogger();
}

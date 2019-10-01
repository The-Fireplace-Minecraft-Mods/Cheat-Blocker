package the_fireplace.cheatblocker.forge.client;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = the_fireplace.cheatblocker.CheatBlocker.MODID)
public class ClientEvents {
    @SubscribeEvent
    public static void configChanged(ConfigChangedEvent event) {
        if (event.getModID().equals(the_fireplace.cheatblocker.CheatBlocker.MODID)) {
            ConfigManager.sync(the_fireplace.cheatblocker.CheatBlocker.MODID, Config.Type.INSTANCE);
        }
    }
}

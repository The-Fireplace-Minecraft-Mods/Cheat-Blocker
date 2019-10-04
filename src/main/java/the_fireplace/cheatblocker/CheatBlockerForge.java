package the_fireplace.cheatblocker;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import the_fireplace.cheatblocker.abstraction.IConfig;
import the_fireplace.cheatblocker.forge.ForgePermissionHandler;
import the_fireplace.cheatblocker.forge.compat.ForgeMinecraftHelper;
import the_fireplace.cheatblocker.sponge.SpongePermissionHandler;

import java.util.Objects;

import static the_fireplace.cheatblocker.CheatBlocker.MODID;

@Mod.EventBusSubscriber(modid = MODID)
@Mod(modid = MODID, name = CheatBlocker.MODNAME, version = CheatBlocker.VERSION, acceptedMinecraftVersions = "[1.12,1.13)", acceptableRemoteVersions = "*", dependencies="after:spongeapi", certificateFingerprint = "51ac068a87f356c56dc733d0c049a9a68bc7245c")
public final class CheatBlockerForge {
    @Mod.Instance(MODID)
    public static CheatBlockerForge instance;

    private static Logger LOGGER = FMLLog.log;
    private boolean validJar = true;

    public static Logger getLogger() {
        return LOGGER;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CheatBlocker.setMinecraftHelper(new ForgeMinecraftHelper());
        CheatBlocker.setConfig(new cfg());
        LOGGER = event.getModLog();
        if(!validJar)
            CheatBlocker.getMinecraftHelper().getLogger().error("The jar's signature is invalid! Please redownload from "+Objects.requireNonNull(Loader.instance().activeModContainer()).getUpdateUrl());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        if(CheatBlocker.getMinecraftHelper().isPluginLoaded("spongeapi"))
            CheatBlocker.setPermissionManager(new SpongePermissionHandler());
        else
            CheatBlocker.setPermissionManager(new ForgePermissionHandler());
    }

    @Mod.EventHandler
    public void invalidFingerprint(FMLFingerprintViolationEvent e) {
        if(!e.isDirectory()) {
            validJar = false;
        }
    }

    @SuppressWarnings("WeakerAccess")
    @Config(modid = MODID)
    private static class cfg implements IConfig {
        //General mod configuration
        @Config.Comment("Server locale - the client's locale takes precedence if Cheat Blocker is installed there.")
        public static String locale = "en_us";

        @Override
        public String getLocale() {
            return locale;
        }

        @Override
        public boolean preventNofall() {
            return nofallcfg.prevent;
        }
    }

    @Config(modid = MODID, category = "nofall")
    private static class nofallcfg {
        public static boolean prevent = true;
    }
}

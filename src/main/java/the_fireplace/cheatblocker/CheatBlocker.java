package the_fireplace.cheatblocker;

import the_fireplace.cheatblocker.abstraction.IConfig;
import the_fireplace.cheatblocker.abstraction.IMinecraftHelper;
import the_fireplace.cheatblocker.abstraction.IPermissionHandler;

public final class CheatBlocker {
    public static final String MODID = "cheatblocker";
    public static final String MODNAME = "Cheat Blocker";
    static final String VERSION = "${version}";
    private static IMinecraftHelper minecraftHelper;
    private static IConfig config;
    private static IPermissionHandler permissionManager;

    public static IMinecraftHelper getMinecraftHelper() {
        return minecraftHelper;
    }

    public static IConfig getConfig() {
        return config;
    }

    static void setMinecraftHelper(IMinecraftHelper minecraftHelper) {
        CheatBlocker.minecraftHelper = minecraftHelper;
    }

    static void setConfig(IConfig config) {
        CheatBlocker.config = config;
    }

    public static IPermissionHandler getPermissionManager() {
        return permissionManager;
    }

    static void setPermissionManager(IPermissionHandler permissionManager) {
        CheatBlocker.permissionManager = permissionManager;
    }
}

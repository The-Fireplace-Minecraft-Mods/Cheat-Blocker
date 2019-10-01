package the_fireplace.cheatblocker;

import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import the_fireplace.cheatblocker.abstraction.IConfig;
import the_fireplace.cheatblocker.logic.ServerEventLogic;
import the_fireplace.cheatblocker.sponge.SpongePermissionHandler;
import the_fireplace.cheatblocker.sponge.compat.SpongeMinecraftHelper;
import the_fireplace.cheatblocker.sponge.listener.NetworkListeners;
import the_fireplace.cheatblocker.sponge.listener.PlayerListeners;
import the_fireplace.cheatblocker.sponge.listener.TimingHandler;

import java.io.IOException;

@Plugin(id = CheatBlocker.MODID+"sponge", dependencies = {@Dependency(id=CheatBlocker.MODID, optional=true)})
public final class CheatBlockerSponge {
    @Inject
    public static Logger logger;

    public static boolean active;

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        if(CheatBlocker.getMinecraftHelper() == null) {
            active = true;
            CheatBlocker.setMinecraftHelper(new SpongeMinecraftHelper());
            CheatBlocker.setPermissionManager(new SpongePermissionHandler());
        }
    }

    @Listener
    public void init(GameInitializationEvent event) {
        if(active) {
            Sponge.getGame().getEventManager().registerListeners(this, new NetworkListeners());
            Sponge.getGame().getEventManager().registerListeners(this, new PlayerListeners());
            new TimingHandler();
        }
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        if(active) {
            try {
                loadConfig();
                CheatBlocker.setConfig(new cfg());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ServerEventLogic.onServerStarting(CheatBlocker.getMinecraftHelper().getServer());
        }
    }

    @Listener
    public void onServerStopping(GameStoppingServerEvent event) {
        if(active) {
            ServerEventLogic.onServerStopping();
        }
    }

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    private ConfigurationNode group;

    private void loadConfig() throws IOException {
        boolean needsSaving = false;
        CommentedConfigurationNode root = loader.load(ConfigurationOptions.defaults());
        if (root.isVirtual()) {
            ConfigurationLoader<CommentedConfigurationNode> defaults =
                    HoconConfigurationLoader.builder()
                            .setURL(Sponge.getAssetManager()
                                    .getAsset(this, "defaults.conf").get()
                                    .getUrl())
                            .build();
            root.mergeValuesFrom(defaults.load(ConfigurationOptions.defaults()));
            needsSaving = true;
        }

        group = root.getNode("general");
        if (group.isVirtual()) {
            ConfigurationLoader<CommentedConfigurationNode> defaults =
                    HoconConfigurationLoader.builder()
                            .setURL(Sponge.getAssetManager()
                                    .getAsset(this, "defaults.conf").get()
                                    .getUrl())
                            .build();
            CommentedConfigurationNode defgroup = defaults.load(ConfigurationOptions.defaults()).getNode("general");
            group.mergeValuesFrom(defgroup);
            //set the value (i assume merging values does not clear the virtual flag)
            root.getNode("general").setValue(group);
            needsSaving = true;
        }

        if (needsSaving)
            loader.save(root);
    }

    public class cfg implements IConfig {
        //@Override
        //public String getLocale() {
        //    return group.getNode("locale").getString("en_us");
        //}

        //@Override
        //public List<String> getStringsToCensor() {
        //    try {
        //        return group.getNode("stringsToCensor").getList(TypeTokens.STRING_TOKEN);
        //    } catch(ObjectMappingException e) {
        //        e.printStackTrace();
        //        return Collections.emptyList();
        //    }
        //}

        //@Override
        //public boolean useCache() {
        //    return group.getNode("useCache").getBoolean(true);
        //}
    }
}

package the_fireplace.cheatblocker.sponge;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.living.player.Player;

public class SpongeConverter {
    public static MinecraftServer server(Server s) {
        return (MinecraftServer)s;
    }
    public static EntityPlayer player(Player p) {
        return (EntityPlayer) p;
    }
}

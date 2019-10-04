package the_fireplace.cheatblocker.forge.prevention;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import the_fireplace.cheatblocker.CheatBlocker;

import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = CheatBlocker.MODID)
public class NoFall {
    private static Map<UUID, BlockPos> fallHeights = Maps.newHashMap();
    public static final double FALL_DAMAGE_DIST = 3.0;
    public static final double FALL_DAMAGE_MINIMUM = 0.5;
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(!CheatBlocker.getConfig().preventNofall())
            return;
        //TODO find out where player.onGround comes from. If it comes from that packet, use our own method to calculate if the player is on the ground.
        if(!event.player.onGround && (!fallHeights.containsKey(event.player.getUniqueID()) || fallHeights.get(event.player.getUniqueID()).getY() < event.player.getPosition().getY()))
            fallHeights.put(event.player.getUniqueID(), event.player.getPosition());
        else if(event.player.onGround && fallHeights.containsKey(event.player.getUniqueID())) {
            BlockPos heightPos = fallHeights.get(event.player.getUniqueID());
            if(heightPos.getY() - event.player.getPosition().getY() >= FALL_DAMAGE_DIST) {
                //double fall damage as penalty for trying to nofall
                event.player.fall(heightPos.getY() - event.player.getPosition().getY(), 2);
            }
            fallHeights.remove(event.player.getUniqueID());
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if(!CheatBlocker.getConfig().preventNofall())
            return;
        if(event.getEntityLiving() instanceof EntityPlayer && event.getSource().equals(DamageSource.FALL)) {
            //TODO check that this is always fired before player tick event
            //Not sure if this is necessary, including for completion
            if(!event.isCanceled() && event.getAmount() >= FALL_DAMAGE_MINIMUM)
                fallHeights.remove(event.getEntity().getUniqueID());
        }
    }
}

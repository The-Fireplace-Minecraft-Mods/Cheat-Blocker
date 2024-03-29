package the_fireplace.cheatblocker.sponge;

import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.permission.PermissionDescription;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;
import the_fireplace.cheatblocker.CheatBlockerForge;
import the_fireplace.cheatblocker.abstraction.IPermissionHandler;

public class SpongePermissionHandler implements IPermissionHandler {

    private PermissionService permissionService;

    public SpongePermissionHandler() {
        if(!Sponge.getServiceManager().provide(PermissionService.class).isPresent())
            return;
        permissionService = Sponge.getServiceManager().provide(PermissionService.class).get();

        //registerPermission("command.togglecensor", PermissionDescription.ROLE_USER, "");
    }

    @Override
    public boolean hasPermission(EntityPlayerMP player, String permissionName) {
        if(permissionManagementExists() && player instanceof Subject)
            return ((Subject) player).hasPermission(permissionName);
        return true;
    }

    @Override
    public void registerPermission(String permissionName, Object permissionLevel, String permissionDescription) {
        permissionService
                .newDescriptionBuilder(CheatBlockerForge.instance)
                .id(permissionName)
                .description(Text.of(permissionDescription))
                .assign(((String)permissionLevel).isEmpty() ? PermissionDescription.ROLE_USER : (String)permissionLevel, !((String) permissionLevel).isEmpty())
                .register();
    }

    @Override
    public boolean permissionManagementExists() {
        return Sponge.getServiceManager().isRegistered(PermissionService.class);
    }
}

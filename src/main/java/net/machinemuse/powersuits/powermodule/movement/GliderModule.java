package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.numina.player.NuminaPlayerUtils;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class GliderModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public static final String MODULE_GLIDER = "Glider";

    public GliderModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.gliderWing, 2));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MODULE_GLIDER;
    }

    @Override
    public String getUnlocalizedName() { return "glider";
    }

    @Override
    public String getDescription() {
        return "Tack on some wings to turn downward into forward momentum. Press sneak+forward while falling to activate.";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        Vec3d playerHorzFacing = player.getLookVec();
        playerHorzFacing = new Vec3d(playerHorzFacing.xCoord, 0, playerHorzFacing.zCoord);
        playerHorzFacing.normalize();
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName());
        boolean sneakkey = movementInput.sneakKey;
        float forwardkey = movementInput.forwardKey;
        ItemStack torso = player.inventory.armorItemInSlot(2);



        boolean hasParachute = false;
        NuminaPlayerUtils.resetFloatKickTicks(player);
        if (torso != null && torso.getItem() instanceof IModularItem) {
            hasParachute = ModuleManager.itemHasActiveModule(torso, ParachuteModule.MODULE_PARACHUTE);
        }
        if (sneakkey && player.motionY < 0 && (!hasParachute || forwardkey > 0)) {
            if (player.motionY < -0.1) {
                float vol = (float)( player.motionX*player.motionX + player.motionZ * player.motionZ);
                double motionYchange = Math.min(0.08, -0.1 - player.motionY);
                player.motionY += motionYchange;
                player.motionX += playerHorzFacing.xCoord * motionYchange;
                player.motionZ += playerHorzFacing.zCoord * motionYchange;

                // sprinting speed
                player.jumpMovementFactor += 0.03f;
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.glider;
    }
}
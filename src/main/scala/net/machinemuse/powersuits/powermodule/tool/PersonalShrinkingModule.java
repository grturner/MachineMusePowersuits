//package net.machinemuse.powersuits.powermodule.tool;
//
//import net.machinemuse.api.IModularItem;
//import net.machinemuse.api.moduletrigger.IRightClickModule;
//import net.machinemuse.api.moduletrigger.IPlayerTickModule;
//import net.machinemuse.api.ModuleManager;
//import net.machinemuse.powersuits.item.ItemComponent;
//import net.machinemuse.powersuits.powermodule.PowerModuleBase;
//import net.machinemuse.utils.MuseCommonStrings;
//import net.machinemuse.utils.MuseItemUtils;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.item.ItemStack;
//
//import net.minecraft.world.World;
//import net.minecraft.block.Block;
//import net.minecraftforge.fml.common.registry.GameRegistry;
//import org.dave.CompactMachines.CompactMachines;
//
//import java.util.List;
//
///**
// * Created by User: Korynkai
// * 5:41 PM 2014-11-19
// */
//
//public class PersonalShrinkingModule extends PowerModuleBase implements IRightClickModule, IPlayerTickModule {
//public static final String MODULE_CM_PSD = "Personal Shrinking Device";
//private ItemStack cpmPSD;
//
//public PersonalShrinkingModule(List<IModularItem> validItems) {
//        super(validItems);
//        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 4));
//        cpmPSD = GameRegistry.findItemStack("CompactMachines", "psd", 1);
//        addInstallCost(cpmPSD);
//}
//
//@Override
//public String getTextureFile() {
//        return "psd";
//}
//
//@Override
//public String getCategory() {
//        return MuseCommonStrings.CATEGORY_TOOL;
//}
//
//@Override
//public String getDataName() {
//        return MODULE_CM_PSD;
//}
//
//@Override
//public String getUnlocalizedName() {
//        return "cmPSD";
//}
//
//@Override
//public String getDescription() {
//        return "A Compact Machines Personal Shrinking Device integrated into your power tool.";
//}
//
//@Override
//public void onRightClick(EntityPlayer player, World world, ItemStack item) {
//        cpmPSD.getItem().onItemRightClick(item, world, player);
//}
//
//@Override
//public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, EnumFacing side, float hitX, float hitY, float hitZ) {
//}
//
//@Override
//public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, EnumFacing side, float hitX, float hitY, float hitZ) {
//        if (world.isRemote && player instanceof EntityPlayerMP) {
//                int block = Block.getIdFromBlock(world.getBlock(x, y, z));
//                if (block == Block.getIdFromBlock(GameRegistry.findBlock("CompactMachines", "machine"))) {
//                        return false;
//                } else if (block == Block.getIdFromBlock(GameRegistry.findBlock("CompactMachines", "innerwall"))) {
//                        return true;
//                }
//        }
//        return false;
//
//}
//
//@Override
//public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
//        if (!MuseItemUtils.getCanShrink(item)) {
//                MuseItemUtils.setCanShrink(item, true);
//        }
//}
//
//@Override
//public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
//        if (MuseItemUtils.getCanShrink(item)) {
//                MuseItemUtils.setCanShrink(item, false);
//        }
//}
//
//public float minF(float a, float b) {
//        return a < b ? a : b;
//}
//
//@Override
//public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
//}
//}

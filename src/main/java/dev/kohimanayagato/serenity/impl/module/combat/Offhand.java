package dev.kohimanayagato.serenity.impl.module.combat;

import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.api.util.LoggerUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.Arrays;

// Made by Lnadav

//TODO Add Totem Check
public class Offhand extends Module
{
    private final Setting health = new Setting("Health", this, 10, 1, 30);
	
	private final Setting mode = new Setting("Mode", this, Arrays.asList(
		"Totem",
		"Gapple",
		"Crystal"
	));

    public Offhand(String name, String description, Category category)
    {
        super(name, description, category);
    }
	
	NonNullList<ItemStack> inv;
	int TotemCache;
	int InvID;
	
	
	@Override
	public void onEnable(){
	inv = mc.player.inventory.mainInventory;
			for (InvID = 0; InvID < inv.size(); InvID++) {
                if (inv.get(InvID) != ItemStack.EMPTY) {
                    if (inv.get(InvID).getItem() == Items.TOTEM_OF_UNDYING) {
                        TotemCache = InvID;
						//LoggerUtil.sendMessage("The Next Totem is in inv slot" + Integer.toString(TotemCache));
                        break;
                    }
                }
            }
	}
	
	
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
		if (mc.player.getHealth() <= health.getIntegerValue() || mode.getEnumValue().equalsIgnoreCase("Totem") ) {
			if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING){
				LoggerUtil.sendMessage("Putting Totem In Offhand!");
				
				replaceOffHand(TotemCache);
				
				
				/* Essentially everything within this block is finding the next totem, We first define the size of the inventory, then loops through it and
				the first totem it finds it adds to the Totem cache. More on that cache later, I put this to only do this After everything else to improve efficiency*/
				inv = mc.player.inventory.mainInventory;
				for (InvID = 0; InvID < inv.size(); InvID++) {
					if (inv.get(InvID) != ItemStack.EMPTY) {
						if (inv.get(InvID).getItem() == Items.TOTEM_OF_UNDYING) {
							TotemCache = InvID;
							//LoggerUtil.sendMessage("The Next Totem is in inv slot" + Integer.toString(TotemCache));
							break;
						}
					}
				}
			}
		}
		// SWITCH TO GAPPLE IF MODE IS GAPPLE
		else if (mc.player.getHealth() > health.getIntegerValue() && mode.getEnumValue().equalsIgnoreCase("Gapple") ) {
			if (mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE){
				LoggerUtil.sendMessage("Putting Totem In Offhand!");
				inv = mc.player.inventory.mainInventory;
				for (InvID = 0; InvID < inv.size(); InvID++) {
					if (inv.get(InvID) != ItemStack.EMPTY) {
						if (inv.get(InvID).getItem() == Items.GOLDEN_APPLE) {
							replaceOffHand(InvID);
							break;
						}
					}
				}
			}
		}
		//SWITCH TO CRYSTAL
		else if (mc.player.getHealth() > health.getIntegerValue() && mode.getEnumValue().equalsIgnoreCase("Crystal") ) {
			if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL){
				LoggerUtil.sendMessage("Putting Totem In Offhand!");
				inv = mc.player.inventory.mainInventory;
				for (InvID = 0; InvID < inv.size(); InvID++) {
					if (inv.get(InvID) != ItemStack.EMPTY) {
						if (inv.get(InvID).getItem() == Items.END_CRYSTAL) {
							replaceOffHand(InvID);
							break;
						}
					}
				}
			}
		}
    }
	
	public void replaceOffHand(int InvID) {
        if (mc.player.openContainer instanceof ContainerPlayer) {
            mc.playerController.windowClick(0, InvID < 9 ? InvID + 36 : InvID, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, InvID < 9 ? InvID + 36 : InvID, 0, ClickType.PICKUP, mc.player);
        }
    }
	
}
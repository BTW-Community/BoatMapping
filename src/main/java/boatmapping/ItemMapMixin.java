package boatmapping;

import btw.item.BTWItems;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemMap.class)
public abstract class ItemMapMixin {

	@Shadow
	public abstract void updateMapData(World world, Entity entity, MapData mapData);

	@Shadow
	public abstract MapData getMapData(ItemStack itemStack, World world);

	@Inject(method = "onUpdate(Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/World;Lnet/minecraft/src/EntityPlayer;IZ)V", at = @At("TAIL"))
	public void exploreWhenHoldingSailNextToMap(ItemStack stack, World world, EntityPlayer player, int par4, boolean holding, CallbackInfo callbackInfo){
		if(!world.isRemote){
			InventoryPlayer inventory = player.inventory;
			int slot = inventory.currentItem;
			int mapID = Item.map.itemID;
			ItemStack nextStack = inventory.getStackInSlot((slot+1)%9);
			ItemStack prevStack = inventory.getStackInSlot((slot+8)%9);
			ItemStack currentStack = inventory.getCurrentItem();
			boolean shouldExplore =
				player.ridingEntity instanceof EntityBoat &&
				currentStack != null &&
				currentStack.itemID == BTWItems.windMillBlade.itemID && (
					(nextStack != null && nextStack.itemID==mapID) ||
					(prevStack != null && prevStack.itemID==mapID)
				);

			if(shouldExplore) {
				MapData mapData = this.getMapData(stack, world);
				mapData.updateVisiblePlayers(player, stack);
				this.updateMapData(world, player, mapData);
			}
		}
	}
}

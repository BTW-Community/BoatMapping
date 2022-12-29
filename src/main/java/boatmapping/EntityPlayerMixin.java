package boatmapping;

import btw.item.BTWItems;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin {
	@Shadow
	InventoryPlayer inventory;

	@Inject(method="isPlayerHoldingSail()Z", at=@At("RETURN"), cancellable = true)
	public void holdSailNextToMap(CallbackInfoReturnable cir){
		int slot = inventory.currentItem;
		ItemStack nextStack = inventory.getStackInSlot((slot+1)%9);
		ItemStack prevStack = inventory.getStackInSlot((slot+8)%9);
		ItemStack currentStack = inventory.getCurrentItem();
		int sailID = BTWItems.windMillBlade.itemID;
		boolean shouldHold = currentStack != null && currentStack.itemID == Item.map.itemID && ((nextStack!=null && nextStack.itemID == sailID)||(prevStack!=null && prevStack.itemID == sailID));
		cir.setReturnValue(shouldHold || cir.getReturnValueZ());
	}
}

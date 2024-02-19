package xcykrix.harderendportal.util;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import xcykrix.harderendportal.HarderEndPortal;

public class Registerable {
    protected Item item;

    protected Registerable(Item item) {
        this.item = item;
    }

    protected void assignToCreativeInventoryMenu(RegistryKey<ItemGroup> itemGroup, ItemConvertible itemConvertible, boolean beforeItemConvertible) {
        if (this.item == null) {
            HarderEndPortal.LOGGER.warn("Attempted to hook a BlockItem to CreativeInventoryMenu without FabricItemSettings. Skipping... [Nag Developer: Explicit FabricItemSettings Required]");
            return;
        }
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register((context) -> {
            if (beforeItemConvertible) {
                context.addBefore(itemConvertible, this.item);
            } else {
                context.addAfter(itemConvertible, this.item);
            }
        });
    }
}
package xcykrix.harderendportal.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import xcykrix.harderendportal.HarderEndPortal;

public class ItemRegisterable extends Registerable {
    public Identifier identifier = null;
    public Item item = null;

    public ItemRegisterable(String path, Item item) {
        super(item);
        this.identifier = HarderEndPortal.createIdentifier(path);
        this.item = item;
    }

    public ItemRegisterable creative(RegistryKey<ItemGroup> itemGroup, ItemConvertible itemConvertible, boolean beforeItemConvertible) {
        super.assignToCreativeInventoryMenu(itemGroup, itemConvertible, beforeItemConvertible);
        return this;
    }

    public void build() {
        QReg.itemRegistrationCache.add(this);
    }
}

package xcykrix.harderendportal.util;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class QReg {
    protected static HashSet<BlockRegisterable> blockRegistrationCache = new HashSet<>();
    protected static HashSet<ItemRegisterable> itemRegistrationCache = new HashSet<>();

    public static void sync() {
        for (BlockRegisterable blockRegisterable : blockRegistrationCache) {
            Registry.register(Registries.BLOCK, blockRegisterable.identifier, blockRegisterable.block);
        }
        for (ItemRegisterable itemRegisterable : itemRegistrationCache) {
            Registry.register(Registries.ITEM, itemRegisterable.identifier, itemRegisterable.item);
        }
    }

    public static BlockRegisterable block(String path, Block block) {
        return new BlockRegisterable(path, block);
    }

    public static ItemRegisterable item(String path, Item item) {
        return new ItemRegisterable(path, item);
    }
}

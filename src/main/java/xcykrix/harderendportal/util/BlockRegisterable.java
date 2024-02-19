package xcykrix.harderendportal.util;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import xcykrix.harderendportal.HarderEndPortal;

public class BlockRegisterable extends Registerable {
    public Identifier identifier = null;
    public Block block = null;

    public BlockRegisterable(String path, Block block) {
        super(null);
        this.identifier = HarderEndPortal.createIdentifier(path);
        this.block = block;
    }

    public void build() {
        QReg.blockRegistrationCache.add(this);
    }
}

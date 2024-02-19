package xcykrix.harderendportal;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import xcykrix.harderendportal.block.EndPortal;
import xcykrix.harderendportal.item.EnderEye;
import xcykrix.harderendportal.util.QReg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HarderEndPortal implements ModInitializer {
	public static final String MOD_ID = "harderendportal";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	static {
		// Register Items
		EnderEye.register("cursed_eye");
		EnderEye.register("chilled_eye");
		EnderEye.register("cracked_eye");
		EnderEye.register("lost_eye");
		EnderEye.register("magical_eye");
		EnderEye.register("warded_eye");
		EnderEye.register("eldrich_eye");
		EnderEye.register("withered_eye");
		EnderEye.register("rotten_eye");
		EnderEye.register("alembic_eye");
		EnderEye.register("enchanted_eye");
		EnderEye.register("golden_eye");

		// Register Blocks 
		EndPortal.register();
	}

	@Override
	public void onInitialize() {
		QReg.sync();
	}

	public static Identifier createIdentifier(String path) {
		return new Identifier(MOD_ID, path);
	}
}
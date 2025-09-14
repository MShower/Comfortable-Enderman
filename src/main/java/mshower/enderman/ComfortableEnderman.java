package mshower.enderman;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComfortableEnderman implements ModInitializer {
	public static final String MOD_ID = "comfortable-enderman";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Comfortable Enderman Loaded");
	}
}
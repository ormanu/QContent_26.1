package ormanu.qcontent;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ormanu.qcontent.config.QConfig;
import ormanu.qcontent.items.ModItems;

public class QContent implements ModInitializer {
	public static final String MOD_ID = "qcontent";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		eu.midnightdust.lib.config.MidnightConfig.init("qcontent", QConfig.class);
		ModItems.initialize();
		LOGGER.info("QContent Main Class Init");
	}

}
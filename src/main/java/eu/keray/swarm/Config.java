package eu.keray.swarm;

import java.io.File;

import eu.keray.swarm.config.CommonConfig;
import eu.keray.swarm.config.MainConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = MonsterSwarmMod.MODID)
public class Config {


	public static final ForgeConfigSpec COMMON_SPEC;
	public static final CommonConfig COMMON;

	static {
		Pair<CommonConfig, ForgeConfigSpec> commonPair = (new ForgeConfigSpec.Builder()).configure(CommonConfig::new);
		COMMON = (CommonConfig)commonPair.getLeft();
		COMMON_SPEC = (ForgeConfigSpec)commonPair.getRight();
	}

}

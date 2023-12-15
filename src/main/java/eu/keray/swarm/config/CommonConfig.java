package eu.keray.swarm.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    private final MainConfig config;
    public CommonConfig(ForgeConfigSpec.Builder builder) {

//		{
//			config.renameProperty(Configuration.CATEGORY_GENERAL, "Creepers Explode", "Creepers Explode By Themselves");
//			Property prop = config.get(Configuration.CATEGORY_GENERAL, "Creepers Explode By Themselves", true);
//			prop.comment = "If Set to True, creepers will explode by themselves if they get bored (stay in place for too long)";
//			CREEP_EXPLODE = prop.getBoolean();
//
//		}

        builder.comment("Falling Tree configuration");
        builder.push("config");
        this.config = new MainConfig(builder);
        builder.pop();


    }
}

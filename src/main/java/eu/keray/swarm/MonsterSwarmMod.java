package eu.keray.swarm;

import eu.keray.swarm.config.MainConfig;
import eu.keray.swarm.controllers.SwarmWorldsController;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.apache.logging.log4j.Logger;

@Mod(MonsterSwarmMod.MODID)
public class MonsterSwarmMod
{
    public static final String MODID = "monsterswarm";
    public static final String NAME = "Monster Swarm";
    public static final String VERSION = "2.0";

    public static Logger logger;

	SwarmWorldsController swarmWorldsController;
    

	public static final List<Class> excludedAttackers = new ArrayList<Class>();
	public static final List<Class> includedAttackers = new ArrayList<Class>();
	public static final List<Class> includedTargets = new ArrayList<Class>();
	public static final List<Class> includedDiggers = new ArrayList<Class>();

	public static boolean isSpawnChange = false;


	public MonsterSwarmMod() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
		this.swarmWorldsController = new SwarmWorldsController();
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(this.swarmWorldsController);
	}

	public void postInit() {
		new EventHandling();

		Magic.setResist("Railcraft:brick.infernal", 16);
		Magic.setResist("Railcraft:brick.abyssal", 16);
		Magic.setResist("Railcraft:brick.sandy", 16);
		Magic.setResist("Railcraft:brick.frostbound", 16);
		Magic.setResist("Railcraft:brick.quarried", 16);
		Magic.setResist("Railcraft:brick.bleachedbone", 16);
		Magic.setResist("Railcraft:brick.bloodstained", 16);
		Magic.setResist("Railcraft:brick.nether", 16);
		Magic.setResist("Railcraft:stair", 12);
		Magic.setResist("Railcraft:wall.beta", 12);
		Magic.setResist("Railcraft:wall.alpha", 12);
		Magic.setResist("Railcraft:slab", 12);
		Magic.setResist("BiomesOPlenty:mudBricks", 15);
		Magic.setResist("BiomesOPlenty:mudBricksStairs", 11);
		Magic.setResist("MineFactoryReloaded:brick", 16);

		//Magic.setResist("Railcraft:machine.alpha", 16);
		//Magic.setResist("IC2:blockWall", 16);
		//Magic.setResist("IC2:blockAlloy", 40);
		//Magic.setResist("IC2:blockDoorAlloy", 25);
		//Magic.setResist("IC2:blockAlloyGlass", 25);
		//Magic.setResist("IC2:blockDoorAlloy", 15);

		Magic.addClass(includedAttackers, "net.minecraft.entity.monster.EntityMob");
		Magic.addClass(includedAttackers, "net.minecraft.entity.monster.IMob"); // mob
		Magic.addClass(includedAttackers, "drzhark.mocreatures.entity.passive.MoCEntityBear");
		Magic.addClass(includedAttackers, "drzhark.mocreatures.entity.passive.MoCEntityBoar");
		Magic.addClass(includedAttackers, "drzhark.mocreatures.entity.passive.MoCEntityCrocodile");

		Magic.addClass(excludedAttackers, "drzhark.mocreatures.entity.monster.MoCEntityGolem");
		Magic.addClass(excludedAttackers, "drzhark.mocreatures.entity.monster.MoCEntityMiniGolem");
		Magic.addClass(excludedAttackers, "drzhark.mocreatures.entity.monster.MoCEntityOgre");
		Magic.addClass(excludedAttackers, "net.minecraft.entity.monster.EntityEnderman");
		Magic.addClass(excludedAttackers, "crazypants.enderzoo.entity.EntityOwl");

		Magic.addClass(includedDiggers, "drzhark.mocreatures.entity.monster.MoCEntitySilverSkeleton");
		Magic.addClass(includedDiggers, "net.minecraft.entity.monster.EntityZombie");
		Magic.addClass(includedDiggers, "net.minecraft.entity.monster.EntitySkeleton");
		Magic.addClass(includedDiggers, "com.gw.dm.entity.EntityLizalfos");
		Magic.addClass(includedDiggers, "com.gw.dm.entity.EntityRakshasa");
		Magic.addClass(includedDiggers, "com.gw.dm.entity.EntityCaveFisher");
		//Magic.addClass(includedDiggers, "net.minecraft.entity.monster.EntityCreeper");
		
		
		Magic.addClass(includedTargets, "net.minecraft.entity.player.EntityPlayer");
		Magic.addClass(includedTargets, "net.minecraft.entity.monster.EntityGolem");
		Magic.addClass(includedTargets, "net.minecraft.entity.passive.EntityVillager");
		Magic.addClass(includedTargets, "net.shadowmage.ancientwarfare.npc.entity.NpcBase");

		//		Iterator<Block> iterator = Block.blockRegistry.iterator();
		//		while(iterator.hasNext()) {
		//			Block block = iterator.next();
		//
		//			if(block instanceof BlockDecorativeBricks) {
		//				block.setResistance(15);
		//				System.out.println("set 15 for " + block.getUnlocalizedName());
		//			}
		//		}
	}



}

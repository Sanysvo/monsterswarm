package eu.keray.swarm;

import eu.keray.swarm.config.MainConfig;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.Tags;
import swarm.api.SwarmControl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SwarmWorldDiggerBase {
	static final Block web = Blocks.COBWEB;

	protected final SwarmWorld sw;
	public SwarmWorldDiggerBase(SwarmWorld sw) {
		this.sw = sw;
	}
	
	protected Block bridge = Blocks.GRAVEL;
	
	protected final ObjPool<Vec3I> vecpool = new ObjPool<Vec3I>(Vec3I.class);
	private final ValueMap<Vec3I> damaged = new ValueMap<Vec3I>(2048);
	private final ValueMap<Vec3I> bridges = new ValueMap<Vec3I>(2048);
	

	/** returns true if block was destroyed or there was no block at all */
	public boolean damage(Vec3I loc, int damage) {
		return damage(loc.x, loc.y, loc.z, damage);
	}
	
	/** returns true if block was destroyed or there was no block at all */
	public boolean damage(int x, int y, int z, int damage) {

			BlockPos pos = new BlockPos(x, y, z);
			BlockState state = sw.world.getBlockState(pos);
			Block type = state.getBlock();
			if(type == Blocks.AIR || type == null)
				return true;
			
			int maxdamage = getMatDmg(type, x, y, z);
			if(maxdamage < 0)
				return false;
			
			if(!SwarmControl.instance.canSwarmBreakBlock(sw.world, x, y, z))
				return false;
			
			if(maxdamage == 0) {
				//world.playEffect(block.getLocation(locc), Effect.STEP_SOUND, block.getTypeId());
				//block.breakNaturally();
				//if(Config.ENABLE_SOUNDS)
				//	sw.world.playSoundEffect(x, y, z, "dig.stone", 1f, 1f);
				
				if(state.is(Tags.Blocks.GLASS)) {
					//sw.world.playSound(x, y, z, SoundEvent.createFixedRangeEvent(new ResourceLocation("dig.glass"), 1f));
					//sw.world.playSound(x, y, z, "dig.glass", 1f, 1f);
				}

				type.getExpDrop(state, sw.world, RandomSource.create(), pos, 1, 0);
				sw.world.removeBlock(pos, false);
				return true;
			}
			
			if(damaged.size() >= 2048)
				return false;
			
			Vec3I loc = new Vec3I(x, y, z);
			
			damage = damaged.increment(loc, damage) + damage;
			
			if(damage>=maxdamage) {
				damaged.remove(loc, 0);
				type.destroy(sw.world, pos, state);
				type.getExpDrop(state, sw.world, RandomSource.create(), pos, 1, 0);
				sw.world.removeBlock(pos, false);

				return true;
			} else {
				if(MainConfig.ENABLE_SOUNDS.get()) {
				
					SoundEvent sound = null;
					
					if(type.getSoundType(state) != null) {
						SoundEvent s = type.getSoundType(state).getBreakSound();
						if(s != null)
							sound = s;
					}
					
					if(sound == null) {
						sound = SoundEvent.createFixedRangeEvent(new ResourceLocation("dig.stone"), 1f);
					}
					//sw.world.playSound(x, y, z, sound, SoundCategory.BLOCKS, 1f, 1f, true);
				}
				
//				Sound snd = SOUNDS.get(block.getType());
//				if(snd == null)
//					snd = Sound.DIG_STONE;
//				block.getWorld().playSound(block.getLocation(), snd, 1, 1);
				if(type == Blocks.STONE && damage > maxdamage/2) {
					sw.world.setBlock(pos, Blocks.STONE.defaultBlockState(), 2);
				}
			}
		return false;
	}
	
//	private void cleanup() {
//		if(bridges != null) {
//			for(Vec3I key : bridges.keys()) {
//				observer.removed(key);
//			}
//			bridges.clear();
//		}
//    }
	
	private static final float minimum = 1.3f / 5;

	protected int getMatDmg(Block type, int x, int y, int z) {
		//return (int) type.getBlockHardness(sw.world, x, y, z);
		
		//if(type == Blocks.netherrack || type == Blocks.soul_sand)
		//	return 3;
		
		double res = type.getExplosionResistance() * MainConfig.RESISTANCE_MULTIPLIER.get();
		if(res < minimum)
			return 0;
		if(res > MainConfig.MAX_RESISTANCE.get()/5)
			res = (float) MainConfig.MAX_RESISTANCE.get()/5;
		
		return Math.max(2, (int)(res * 2.5f));
    }


	public boolean bridge(int x, int y, int z) {
		if(!MainConfig.ENABLE_BUILDING.get())
			return false;
		
		
		
		BlockPos pos = new BlockPos(x, y, z);
		
		LevelChunk chk = sw.world.getChunkAt(pos);
		if(chk == null)
			return false;

//		if(bridges.size() >= 2048)
//			return false;

		if(!SwarmControl.instance.canSwarmPlaceBlock(sw.world, x, y, z))
			return false;
		BlockState state = sw.world.getBlockState(pos);
		Block type = state.getBlock();
		if(type != null && type != Blocks.AIR)
			type.getExpDrop(state, sw.world, RandomSource.create(), pos, 1, 0);
		
		sw.world.setBlock(pos, Blocks.GRAVEL.defaultBlockState(), 11);
//		if(!sw.world.provider.doesWaterVaporize())
//			bridges.put(new Vec3I(x, y, z), time);
		
		return true;
	}
	
	public boolean damageLights(Vec3I loc, int height) {
		if(loc.getLightBlocks(sw.world) < 8)
			return false;
		
		boolean damaged = false;
		for (int xs = -1; xs <= 1; xs++) {
			for (int ys = 0; ys < height; ys++) {
				for (int zs = -1; zs <= 1; zs++) {
					Vec3I rel = loc.getRelative(xs, ys, zs);
					/*
					rel.getData(sw.world).getLightValue(sw.world, new BlockPos(rel.x, rel.y, rel.z));
					if(rel.getData(sw.world).getLightValue() > 7) {
						damage(rel, 1);
						damaged = true;
					}*/
				}
			}
		}
		return damaged;
    }

	//public abstract void process(EntityCreature entity, Vec3I to);
	
	public void update() {
    	damaged.reduce(null);
    	bridges.reduce(observer);

		Iterator<Map.Entry<LivingEntity, Mob>> iter = mobs.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<LivingEntity, Mob> next = iter.next();
			if(!next.getKey().isAlive()) {
				iter.remove();
			}
		}
	}
	
	private ValueMap.ReduceObserver<Vec3I> observer = new ValueMap.ReduceObserver<>() {
        public void removed(Vec3I loc) {
	        if(loc.getBlock(sw.world) == bridge) {
				sw.world.setBlock(loc.getPos(), Blocks.AIR.defaultBlockState(), 11);
			}
        }
	};


//	public boolean blockHarvested(BlockPos pos) {
//		Vec3I vec = new Vec3I(pos);
//	    return bridges.remove(vec, -1) != -1;
//    }
//	
//	/* returns true if block should not drop, removes it from list */
//	public boolean blockHarvested(int x, int y, int z) {
//		Vec3I vec = new Vec3I(x, y, z);
//	    return bridges.remove(vec, -1) != -1;
//    }
	
	/** @returns true if passable block and if lava, will remove it first */
	public final boolean isFreePass(int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		BlockState t = sw.world.getBlockState(pos);
		
		if(t.liquid()) {
			sw.world.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
			return true;
		}

		return false;
	}
	
	public final boolean tryAttack(int x, int y, int z) {

		BlockState state = sw.world.getBlockState(new BlockPos(x, y, z));
		Block type = state.getBlock();
		
		if(type == null || type == Blocks.AIR) {
			return false;
		}

		return damage(x, y, z, 1);
	}
	
	Map<LivingEntity, Mob> mobs = new HashMap<LivingEntity, Mob>();
	
	public Mob getMob(LivingEntity ent) {
		Mob mob = mobs.get(ent);
		if(mob != null)
			return mob;
		//System.out.println("NEW MOB");
		mob = new Mob(ent);
		mobs.put(ent, mob);
		return mob;
	}
	
	class Mob {
		int time;
		int x,y,z;
		
		public Mob(LivingEntity ent) {
	        set(ent.getX(), ent.getY(), ent.getZ());
        }

		public void set(double x, double y, double z) {
			this.x = (int) x;
			this.y = (int) y;
			this.z = (int) z;
		}

		public boolean isNear(Vec3I vec) {
	        int dx = Math.abs(vec.x - this.x);
	        int dy = Math.abs(vec.y - this.y);
	        int dz = Math.abs(vec.z - this.z);
	        return dx<4 && dy<3 && dz<4;
        }
		
		public String toString() {
			return "mob["+x+","+y+","+z+"]";
		}
		
	}

}

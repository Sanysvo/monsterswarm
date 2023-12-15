package eu.keray.swarm;

import java.util.*;

import eu.keray.swarm.config.MainConfig;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class SwarmWorld {

	protected ServerLevel world;
	private SwarmWorldDigger digg;

	public final List<Monster> attackers = new ArrayList<>();
	public final List<LivingEntity> targets = new ArrayList<>();

	public boolean isOverworld = false;

	public SwarmWorld(ServerLevel world) {

		//Log.log("Registering world dimension " + world.dimension);

		this.world = world;
		this.digg = new SwarmWorldDigger(this);

		if (world.dimension() == Level.OVERWORLD) {
			isOverworld = true;
		}
	}

	int index = 0;

	int ticks = 0;

	public void run() {
		ticks++;
		if (index >= attackers.size()) {

			if (ticks < 10)
				return;
			ticks = 0;

			if(!world.dimensionType().hasFixedTime()) {
				long seconds = world.getDayTime() % 24000;
				if(seconds > 2000 && seconds < 10000) {
					cancelTarget();
					attackers.clear();
					targets.clear();
					index = 0;
					return;
				}
			}

			index = 0;
			collectEntities();
			digg.update();
			// Log.log("Collected " + attackers.size() + " and " + targets.size());
			return;
		}



		int top = Math.min(index + 2, attackers.size());

		for (; index < top; index++) {
			Monster monster = attackers.get(index);

			LivingEntity target = findTargetFor(monster, 64);
			if (target == null) {
				continue;
			}

			setTarget(monster, target);

			Vec3i point = Maths.findPointTowards(monster, target, 15);
			
			
			boolean canDigg = false;
			
			if(isOverworld) {
				canDigg = monster.getX() < 40 && target.getY() < 40;
			}

			if (canDigg && (monster instanceof Zombie || monster instanceof AbstractSkeleton)) {
				digg.process(monster, target);
			}

			double speed = 1;

			if(target.isSprinting() && MainConfig.ENABLE_SPRINTING.get()){
				speed = 2.3;
			}

			monster.getNavigation().moveTo(point.getX() + 0.5, point.getY() + 0.5, point.getZ() + 0.5, speed);
		}
	}

	private void setTarget(Monster monster, LivingEntity target) {
		monster.setTarget(target);
	}

	public void cancelTarget(LivingEntity target) {
		for(Monster monster : attackers) {
			if(monster.getTarget() != null && monster.getTarget() == target) {
				monster.setTarget(null);
			}
		}
	}

	public void cancelTarget() {
		for(Monster monster : attackers) {
			monster.setTarget(null);
		}
	}

	public LivingEntity findTargetFor(Mob attacker, int radius) {

		AABB aabb = new AABB(attacker.getX() - radius, attacker.getY() - (double) radius/3,
				attacker.getZ() - radius, attacker.getX() + radius, attacker.getY() + (double) radius/3,
				attacker.getZ() + radius);

		LivingEntity nearest = null;
		double ndistSq = Double.POSITIVE_INFINITY;

		for (LivingEntity ent : targets) {

			if (!ent.isAlive())
				continue;

			if (!Maths.contains(aabb, ent)) {
				continue;
			}

			//Если моб невидим, он не может быть атакован
			if(ent.isInvisible()) continue;

			double dx = attacker.getX() - ent.getX();
			double dy = attacker.getY() - ent.getY();
			double dz = attacker.getZ() - ent.getZ();

			double distSq = dx * dx + dz * dz;
			double distHeight = Math.abs(dy);

			if (distSq > radius * radius)
				continue;

			if (ent instanceof Animal || ent instanceof Villager) {
				if (attacker instanceof Creeper)
					continue;
			}

			if ((ent instanceof IronGolem || ent instanceof Animal) && distSq > 20 * 20) {
				continue;
			}

			if (!(ent instanceof Player))
				distSq += distSq;

			if (distSq < ndistSq) {

				if(isOverworld) {
					if (ent.getY() < 40) {
						if (attacker.getY() > 40)
							continue;
					} else {
						if (attacker.getY() < 40)
							continue;
					}
				}

				nearest = ent;
				ndistSq = distSq;
			}
		}

		return nearest;
	}

	private void collectEntities() {
		attackers.clear();
		targets.clear();
		for (Entity ent : world.getEntities().getAll()) {
			
			if(ent instanceof EnderMan || ent instanceof Creeper)
				continue;

			if (ent instanceof LivingEntity) {

				if (ent instanceof Monster) {
					attackers.add((Monster) ent);
					continue;
				}


			}

			if (ent instanceof Player) {
				if (((Player) ent).isCreative())
					continue;
				if (ent.isSpectator())
					continue;

				targets.add((LivingEntity) ent);

			}
			//targets.add((EntityLivingBase) ent);
		}

	}
}

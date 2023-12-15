package eu.keray.swarm;


import eu.keray.swarm.config.MainConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandling {

	public EventHandling() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEntityJoin(EntityJoinLevelEvent event) {
		if(event.getLevel().isClientSide())
			return;


		if(!(event.getEntity() instanceof LivingEntity))
			return;

		LivingEntity ent = (LivingEntity) event.getEntity();


		for(Class cls : MonsterSwarmMod.excludedAttackers) {
			if(cls.isInstance(ent)) {
				return;
			}
		}

		boolean inst = false;
		for(Class cls : MonsterSwarmMod.includedAttackers) {
			if(cls.isInstance(ent)) {
				inst = true;
				break;
			}
		}

		if(inst) {
//			//ent.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(90.0D);
//			if(MainConfig.ATTACK_ANIMALS.get() && !(ent instanceof Creeper)) {
//				ent..addGoal(3, new NearestAttackableTargetGoal<AnimalEntity>(ent, AnimalEntity.class, false, false));
//				//ent.tasks.addTask(5, new EntityAIAttackOnCollide(ent, EntityAnimal.class, 1.0D, false));
//			}
//
//			ent.add.addGoal(5, new NearestAttackableTargetGoal<GolemEntity>(ent, GolemEntity.class, false, false));
//			//ent.tasks.addTask(5, new EntityAIAttackOnCollide(ent, EntityGolem.class, 1.0D, true));
		}
	}



	//@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onAttackTarget(LivingAttackEvent event) {
		if(event.getEntity() instanceof Mob && ((Mob) event.getEntity()).getTarget() instanceof Mob) {
			((Mob)event.getEntity()).setTarget(null);
		}

	}

}

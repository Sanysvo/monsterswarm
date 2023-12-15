package eu.keray.swarm.controllers;

import eu.keray.swarm.SwarmWorld;
import eu.keray.swarm.config.MainConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SwarmWorldsController {
    Map<DimensionType, SwarmWorld> swarmWorldMap = new HashMap<>();

    @SubscribeEvent
    public void serverLoad(ServerStartedEvent event)
    {

        if(!replaceMaxCountMobs("max", MainConfig.MAX_MONSTERS_IN_WORLD.get())) {
            if(!replaceMaxCountMobs("f_21586_", MainConfig.MAX_MONSTERS_IN_WORLD.get())){
                throw new RuntimeException("Не удалось сменить максимальное значение моснтров в игре");
            }
        }

        for(ServerLevel ws : event.getServer().getAllLevels()) {
            if(ws == null)
                continue;
            swarmWorldMap.put(ws.dimensionType(), new SwarmWorld(ws));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;

        for(Map.Entry<DimensionType, SwarmWorld> entry : swarmWorldMap.entrySet()) {
            entry.getValue().run();
        }
    }

    @SubscribeEvent
    public void useItemOnPlayer(LivingEntityUseItemEvent.Finish event) {
        for(MobEffectInstance effectInstance : event.getEntity().getActiveEffects()) {
            if(effectInstance.getDescriptionId().equals("effect.minecraft.invisibility")) {
                swarmWorldMap.get(event.getEntity().level().dimensionType()).cancelTarget(event.getEntity());
                return;
            }
        }
    }

    private boolean replaceMaxCountMobs(String fieldName, int count) {
        try {
            setFinalField(MobCategory.class.getDeclaredField(fieldName), MobCategory.MONSTER, count);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void setFinalField(Field field, Object obj, Object newValue) throws Exception {
        field.setAccessible(true);
        field.set(obj, newValue);
    }

}

package eu.keray.swarm;

import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;

public class Maths {
	
	public static double fastSqrt(double a) {
		return Double.longBitsToDouble( ( ( Double.doubleToLongBits( a )-(1l<<52) )>>1 ) + ( 1l<<61 ) );
	}
	
	public static double fastSqrtNewton(double a) {
		double sqrt = fastSqrt(a);
		sqrt = (sqrt + a/sqrt)/2.0;
		return (sqrt + a/sqrt)/2.0;
	}

	public static Vec3i findPointTowards(Mob attacker, LivingEntity target, int dist) {
		double dx = (target.getOnPos().getX() - attacker.getOnPos().getX());
		double dy = (target.getOnPos().getY() - attacker.getOnPos().getY());
		double dz = (target.getOnPos().getZ() - attacker.getOnPos().getZ());
		double len = fastSqrtNewton(dx*dx + dy*dy + dz*dz);
		
		
		if(len < dist) {
			return new Vec3i(target.getOnPos().getX(), target.getOnPos().getY(), target.getOnPos().getZ());
		}
		
		dx = dx / len * dist;
		dy = dy / len * dist;
		dz = dz / len * dist;

		return new Vec3i((int) (attacker.getOnPos().getX()+dx), (int) (attacker.getOnPos().getY()+dy), (int) (attacker.getOnPos().getZ()+dz));
	}
	

	public static final boolean contains(AABB aabb, LivingEntity ent) {
		return ent.getOnPos().getX() >= aabb.minX && ent.getOnPos().getX() <= aabb.maxX && ent.getOnPos().getY() >= aabb.minY && ent.getOnPos().getY() <= aabb.maxY && ent.getOnPos().getZ() >= aabb.minZ && ent.getOnPos().getZ() <= aabb.maxZ;
	}


}

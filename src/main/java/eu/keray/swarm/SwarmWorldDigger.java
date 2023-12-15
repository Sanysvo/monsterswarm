package eu.keray.swarm;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class SwarmWorldDigger extends SwarmWorldDiggerBase {


	public SwarmWorldDigger(SwarmWorld sw) {
		super(sw);
	}

	public void process(net.minecraft.world.entity.Mob mob, LivingEntity target) {
		int sx = Mth.floor(mob.getX());
		int sy = Mth.floor(mob.getY());
		int sz = Mth.floor(mob.getZ());

		int tx = Mth.floor(target.getX());
		int ty = Mth.floor(target.getY());
		int tz = Mth.floor(target.getZ());
		
		int dx = tx - sx;
		int dy = ty - sy;
		int dz = tz - sz;
		
		
		int xd = 0;
		int zd = 0;
		
		boolean higher = false;
		
		if(Math.abs(dx) > Math.abs(dz)) {
			xd = dx > 0 ? 1 : -1;
			higher = dy > Math.abs(dx);
		} else {
			zd = dz > 0 ? 1 : -1;
			higher = dy > Math.abs(dz);
		}
		
		if(higher) {
			// must go up and build up
			

			if(isFreePass(sx, sy-1, sz)) {
				return;
			}
			if(tryAttack(sx, sy+1, sz))
				return;
			if(tryAttack(sx, sy+2, sz))
				return;

			return;
		}
		
		if(dy > 0) {
			// digging up diagonal

			if(isFreePass(sx, sy-1, sz)) {
				return;
			}

			if(tryAttack(sx+xd, sy+1, sz+zd))
				return;
			if(tryAttack(sx, sy+2, sz))
				return;
			if(tryAttack(sx+xd, sy+2, sz+zd))
				return;
			if(tryAttack(sx+xd, sy, sz+zd))
				return;
			
			return;
		}
		
		if(dy < 0) {
			// digging down diagonal

			if(tryAttack(sx+xd, sy, sz+zd))
				return;
			if(tryAttack(sx, sy-1, sz))
				return;
			if(tryAttack(sx+xd, sy-1, sz+zd))
				return;
			
			return;
		}
		
		// roughly on the same level

		if(tryAttack(sx+xd, sy+1, sz+zd))
			return;
		if(tryAttack(sx+xd, sy, sz+zd))
			return;
		
	}
	
	
	
	
	
	
	
	
	
	
	


}

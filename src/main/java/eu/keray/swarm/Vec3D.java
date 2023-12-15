package eu.keray.swarm;

import java.util.Locale;

import net.minecraft.world.entity.LivingEntity;

public class Vec3D {
	public double x;
	public double y;
	public double z;
	
	public Vec3D() {
		
	}

	public Vec3D(double x, double y, double z) {
	    this.x = x;
	    this.y = y;
	    this.z = z;
    }
	
	
	public Vec3D(LivingEntity ent) {
	    x = ent.getOnPos().getX();
	    y = ent.getOnPos().getY();
	    z = ent.getOnPos().getZ();
    }

	public Vec3D set(double x, double y, double z) {
	    this.x = x;
	    this.y = y;
	    this.z = z;
	    return this;
    }
	
	public Vec3D sub(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}
	public Vec3D sub(LivingEntity ent) {
		this.x -= ent.getOnPos().getX();
		this.y -= ent.getOnPos().getY();
		this.z -= ent.getOnPos().getZ();
		return this;
	}
	
	public Vec3D set(Vec3D that) {
	    this.x = that.x;
	    this.y = that.y;
	    this.z = that.z;
	    return this;
    }


	public double length() {
	    return Math.sqrt(x*x + y*y + z*z);
    }
	
	public Vec3D normalize() {
		double len = length();
		return set(x/len, y/len, z/len);
	}
	
	public Vec3D scale(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		return this;
	}
	
	@Override
	public String toString() {
	    // TODO Auto-generated method stub
	    return String.format(Locale.UK, "[%.1f, %.1f, %.1f]", x, y, z);
	}
	
	public static void main(String[] argz) {
		System.out.println(new Vec3D());
	}
//
//	public Vec3D mul(float scalar) {
//	    this.x *= scalar;
//	    this.y *= scalar;
//	    this.z *= scalar;
//	    return this;
//    }
	

}

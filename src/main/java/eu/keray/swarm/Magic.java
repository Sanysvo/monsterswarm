package eu.keray.swarm;

import java.util.List;


public class Magic {

	public static Class findClass(String name) {
		try {
	        return Class.forName(name);
        } catch (ClassNotFoundException e) {
        	MonsterSwarmMod.logger.debug("Class not found: " + name);
        }
		return null;
	}
	
	
	public static void addClass(List<Class> list, String name) {
		Class cls = findClass(name);
		if(cls!= null) {
			list.add(cls);
		}
	}
	
	public static void setResist(String block, float resist) {

	}

}

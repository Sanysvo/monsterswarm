package swarm.api;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SwarmControl {

	public static SwarmControl instance = new SwarmControl();

	public boolean canSwarmBreakBlock(ServerLevel world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		BlockState state = world.getBlockState(pos);
		Block type = state.getBlock();
		if(type == Blocks.COBBLESTONE || type == Blocks.STONE){
			return false;
		}
	    return true;
    }

	public boolean canSwarmPlaceBlock(ServerLevel world, int x, int y, int z) {
	    return true;
    }

}

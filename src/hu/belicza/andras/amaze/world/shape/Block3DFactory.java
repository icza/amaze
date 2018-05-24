package hu.belicza.andras.amaze.world.shape;

import hu.belicza.andras.amaze.world.level.BlockPos;
import javafx.scene.paint.Material;
import javafx.scene.shape.Box;

public class Block3DFactory {
	
	public static Box createWall( final Material material, final BlockPos pos ) {
		final Box b = createBlock3D( material, pos, 1 );
		b.setTranslateY( -0.5 );
		return b;
	}
	
	public static Box createFloor( final Material material, final BlockPos pos ) {
		final Box b = createBlock3D( material, pos, 0 );
		b.setTranslateY( 0 );
		return b;
	}
	
	public static Box createCeiling( final Material material, final BlockPos pos ) {
		final Box b = createBlock3D( material, pos, 0 );
		b.setTranslateY( -1 );
		return b;
	}
	
	/**
	 * @param row block row index
	 * @param col block column index
	 */
	private static Box createBlock3D( final Material material, final BlockPos pos, final double height ) {
		final Box b = new Box( 1, height, 1 );
		b.setTranslateX( pos.col + 0.5 );
		b.setTranslateZ( pos.row + 0.5 );
		b.setMaterial( material );
		return b;
	}
	
}

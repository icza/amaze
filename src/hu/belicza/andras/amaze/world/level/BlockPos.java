package hu.belicza.andras.amaze.world.level;

/**
 * Position of a block specified with grid coordinates.
 * 
 * <p>
 * Implementation is IMMUTABLE.
 * </p>
 * 
 * @author Andras Belicza
 */
public class BlockPos {
	
	public final int row;
	
	public final int col;
	
	public BlockPos( final int row, final int col ) {
		this.row = row;
		this.col = col;
	}
	
	@Override
	public int hashCode() {
		return 31 * ( 31 + col ) + row;
	}
	
	@Override
	public boolean equals( final Object obj ) {
		if ( obj == this )
			return true;
		
		if ( !( obj instanceof BlockPos ) )
			return false;
		
		final BlockPos bp = (BlockPos) obj;
		
		return bp.row == row && bp.col == col;
	}
	
}

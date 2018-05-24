package hu.belicza.andras.amaze.world.shape;

/**
 * Side of a block looking from its center point facing in the direction of increasing Z.
 * 
 * @author beliczaa
 */
public enum Side {
	
	AHEAD( 0, 0.5 ),
	
	LEFT( -0.5, 0 ),
	
	BEHIND( 0, -0.5 ),
	
	RIGHT( 0.5, 0 );
	
	
	final double dx;
	
	final double dz;
	
	private Side( final double dx, final double dz ) {
		this.dx = dx;
		this.dz = dz;
	}
	
}

package hu.belicza.andras.amaze.world;

import hu.belicza.andras.amaze.Consts;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Cam extends PerspectiveCamera {
	
	private final Translate pos  = new Translate();
	
	/** Direction on the horizontal plane. */
	private final Rotate    hdir = new Rotate( 0, Rotate.Y_AXIS );
	
	/** Direction on the vertical plane. */
	private final Rotate    vdir = new Rotate( 0, Rotate.X_AXIS );
	
	public Cam() {
		super( true );
		
		setFieldOfView( 90 );
		setVerticalFieldOfView( false );
		setNearClip( 0.001 );
		setFarClip( 30 );
		
		getTransforms().addAll( pos, hdir, vdir );
	}
	
	public void setPos( final double x, final double y, final double z ) {
		pos.setX( x );
		pos.setY( y );
		pos.setZ( z );
	}
	
	public void setHAngle( final double hangle ) {
		hdir.setAngle( hangle );
	}
	
	public void addToHAngle( final double hdelta ) {
		hdir.setAngle( hdir.getAngle() + hdelta );
	}
	
	public void setVAngle( final double vangle ) {
		vdir.setAngle( vangle );
	}
	
	public void addToVAngle( final double vdelta ) {
		final double vangle = vdir.getAngle() + vdelta;
		if ( vangle < -90 || vangle > 90 )
			return;
		
		vdir.setAngle( vdir.getAngle() + vdelta );
	}
	
	/**
	 * Adds the specified amount to the camera's horizontal position, toward the camera's current horizontal direction.
	 * 
	 * @param helta horizontal distance to be added to the camera's current horizontal position
	 */
	public void addToPos( final double helta ) {
		addToPos( helta, hdir.getAngle() );
	}
	
	/**
	 * Adds the specified amount to the camera's horizontal position, toward the specified horizontal direction.
	 * 
	 * @param hdelta horizontal distance to be added to the camera's current horizontal position
	 */
	public void addToPos( double hdelta, final double hangle ) {
		// Enforce max movement (distance)
		if ( hdelta > Consts.MAX_MOVEMENT )
			hdelta = Consts.MAX_MOVEMENT;
		else if ( hdelta < -Consts.MAX_MOVEMENT )
			hdelta = -Consts.MAX_MOVEMENT;
		
		final double rad = Math.toRadians( hangle );
		
		pos.setX( pos.getX() + hdelta * Math.sin( rad ) );
		pos.setZ( pos.getZ() + hdelta * Math.cos( rad ) );
	}
	
	/**
	 * Elevates the camera: adds the specified vertical delta to its y position.
	 * 
	 * @param vdelta (vertical) elevation to be added to the camera's current vertical position
	 */
	public void elevate( final double vdelta ) {
		pos.setY( pos.getY() + vdelta );
	}
	
	public Translate getPos() {
		return pos;
	}
	
	public Rotate getHDir() {
		return hdir;
	}
	
	/**
	 * Returns the square of the horizontal distance from the point of the specified node, as returned by its {@link Node#getTranslateX()} and
	 * {@link Node#getTranslateZ()} methods.
	 */
	public double getDistanceSquare( final Node n ) {
		return getDistanceSquare( n.getTranslateX(), n.getTranslateZ() );
	}
	
	/**
	 * Returns the square of the horizontal distance from the specified point.
	 */
	public double getDistanceSquare( final double x, final double z ) {
		final double dx = pos.getX() - x;
		final double dz = pos.getZ() - z;
		return dx * dx + dz * dz;
	}
	
}

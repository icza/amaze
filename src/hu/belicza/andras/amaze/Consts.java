package hu.belicza.andras.amaze;

import hu.belicza.andras.amaze.r.R;
import hu.belicza.andras.amaze.world.level.block.Type;
import javafx.scene.image.Image;

/**
 * Free textures: http://www.cgtextures.com/
 * 
 * @author beliczaa
 */
public class Consts {
	
	/** Movement speed, distance / sec. */
	public static final double MOVEMENT_SPEED  = 2.0;
	
	/** Min approachable distance of a non-passable block (e.g. {@link Type#WALL}). */
	public static final double MIN_BLOCK_DIST  = 0.3;
	
	/** No matter how low the actual FPS is, we don't allow to take a bigger step than this (else we could step to forbidden positions). */
	public static final double MAX_MOVEMENT    = MIN_BLOCK_DIST * 0.9;
	
	/** Turn rate, degrees / sec. */
	public static final double TURN_RATE       = 70.0;
	
	/** Movement speed, distance / sec. */
	public static final double ELEVATION_SPEED = 2.0;
	
	/** Scene refresh rate. */
	public static final double FPS             = 60.0;
	
	public static Image        IMG_ICON        = new Image( R.getString( "img/icon.png" ) );
	
}

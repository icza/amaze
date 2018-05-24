package hu.belicza.andras.amaze.world.shape;

import hu.belicza.andras.amaze.world.World;
import hu.belicza.andras.amaze.world.level.BlockPos;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Switch OFF state is up, ON state is down.
 * 
 * @author beliczaa
 */
public class Switch extends Box {
	
	private static final PhongMaterial OFF_MATERIAL = new PhongMaterial( Color.BEIGE );
	
	private static final PhongMaterial ON_MATERIAL  = new PhongMaterial( Color.BEIGE ); // Alternate: TOMATO
	                                                                                    
	protected final BooleanProperty    state        = new SimpleBooleanProperty();
	
	private final Rotate               dir;
	
	public Switch( final World world, final BlockPos pos, final Side side ) {
		super( 0.05, 0.22, 0.05 );
		
		setTranslateX( pos.col + 0.5 + side.dx );
		setTranslateY( -0.5 );
		setTranslateZ( pos.row + 0.5 + side.dz );
		
		switch ( side ) {
			case RIGHT :
			case LEFT :
				dir = new Rotate( 0, Rotate.Z_AXIS );
				break;
			case AHEAD :
			case BEHIND :
				dir = new Rotate( 0, Rotate.X_AXIS );
				break;
			default :
				throw new RuntimeException( "Unhandled side: " + side );
		}
		
		getTransforms().add( dir );
		
		// Mouse press to negate state
		setOnMousePressed( new EventHandler< MouseEvent >() {
			@Override
			public void handle( final MouseEvent event ) {
				// Only switch if within a certain range (from the camera)!
				if ( world.getCam().getDistanceSquare( Switch.this ) < 0.8 )
					state.set( !state.get() );
			}
		} );
		
		state.addListener( new ChangeListener< Boolean >() {
			{
				// Sync to the initial state
				changed( null, null, state.getValue() );
			}
			
			@Override
			public void changed( final ObservableValue< ? extends Boolean > observable, final Boolean oldValue, final Boolean newValue ) {
				final double targetAngle;
				switch ( side ) {
					case LEFT :
					case AHEAD :
						targetAngle = newValue ? 135 : 45;
						break;
					case RIGHT :
					case BEHIND :
						targetAngle = newValue ? -135 : -45;
						break;
					default :
						throw new RuntimeException( "Unhandled side: " + side );
				}
				setMaterial( newValue ? ON_MATERIAL : OFF_MATERIAL );
				
				new Timeline( new KeyFrame( Duration.seconds( 0.1 ), new KeyValue( dir.angleProperty(), targetAngle ) ) ).play();
			}
		} );
	}
	
	public BooleanProperty stateProperty() {
		return state;
	}
	
}

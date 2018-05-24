package hu.belicza.andras.amaze.world.shape;

import hu.belicza.andras.amaze.world.World;
import hu.belicza.andras.amaze.world.level.BlockPos;
import hu.belicza.andras.amaze.world.level.block.Type;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * A wall {@link Switch} which when switched moves a wall.
 * 
 * @author beliczaa
 */
public class WallSwitch extends Switch {
	
	public WallSwitch( final World world, final BlockPos pos, final Side side, final BlockPos targetWallPos ) {
		super( world, pos, side );
		
		final Node wall = world.getWallss()[ targetWallPos.row ][ targetWallPos.col ];
		
		state.addListener( new ChangeListener< Boolean >() {
			{
				// Sync to the initial state
				changed( null, null, state.getValue() );
			}
			
			@Override
			public void changed( final ObservableValue< ? extends Boolean > observable, final Boolean oldValue, final Boolean newValue ) {
				world.getLevel().getBlockss()[ targetWallPos.row ][ targetWallPos.col ].setType( newValue ? Type.EMPTY : Type.WALL );
				
				final double targetY = newValue ? -1.45 : -0.5;
				new Timeline( new KeyFrame( Duration.seconds( 0.3 ), new KeyValue( wall.translateYProperty(), targetY ) ) ).play();
			}
		} );
	}
	
}

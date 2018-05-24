package hu.belicza.andras.amaze.world;

import static hu.belicza.andras.amaze.Consts.ELEVATION_SPEED;
import static hu.belicza.andras.amaze.Consts.FPS;
import static hu.belicza.andras.amaze.Consts.MIN_BLOCK_DIST;
import static hu.belicza.andras.amaze.Consts.MOVEMENT_SPEED;
import static hu.belicza.andras.amaze.Consts.TURN_RATE;
import hu.belicza.andras.amaze.r.R;
import hu.belicza.andras.amaze.world.level.BlockPos;
import hu.belicza.andras.amaze.world.level.Level;
import hu.belicza.andras.amaze.world.level.block.Block;
import hu.belicza.andras.amaze.world.level.block.Type;
import hu.belicza.andras.amaze.world.shape.Block3DFactory;
import hu.belicza.andras.amaze.world.shape.Side;
import hu.belicza.andras.amaze.world.shape.Switch;
import hu.belicza.andras.amaze.world.shape.WallSwitch;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.stage.Stage;
import javafx.util.Duration;

public class World {
	
	/** Temporarily world rebuilding takes map name from this. */
	public static StringProperty STATIC_MAP_NAME_PROP = new SimpleStringProperty( "V4KB" );
	
	
	
	private final Stage          stage;
	
	private MediaPlayer          mp;
	
	private Level                level;
	
	private Node[][]             wallss;
	
	private final Group          root                 = new Group();
	
	private final SubScene       scene                = new SubScene( root, 0, 0, true, SceneAntialiasing.BALANCED );
	
	private final Cam            cam                  = new Cam();
	
	private final PointLight     pointLight           = new PointLight( Color.gray( 0.3 ) );
	
	private final AmbientLight   ambientLight         = new AmbientLight( Color.gray( 0.4 ) );
	
	private final boolean[]      KEY_STATES           = new boolean[ KeyCode.values().length ];
	
	public World( final Stage stage ) {
		this.stage = stage;
		
		// Fill up the whole stage scene: make our subscene the same size
		scene.widthProperty().bind( stage.getScene().widthProperty() );
		scene.heightProperty().bind( stage.getScene().heightProperty() );
		
		rebuildWorld();
		
		handleKeys();
		
		startGameLoop();
		
		mp = new MediaPlayer( new Media( R.getString( "sound/wind.mp3" ) ) );
		mp.setCycleCount( MediaPlayer.INDEFINITE );
		mp.play();
	}
	
	public void rebuildWorld() {
		root.getChildren().clear();
		pointLight.getTransforms().clear();
		
		level = Level.generateMap( STATIC_MAP_NAME_PROP.get() );
		wallss = new Node[ level.getBlockss().length ][ level.getBlockss()[ 0 ].length ];
		
		final PhongMaterial WALL = new PhongMaterial( Color.WHITE, new Image( R.getString( "tex/" + level.getTheme().wall ) ), null, null, null );
		final PhongMaterial FLOOR = new PhongMaterial( Color.WHITE, new Image( R.getString( "tex/" + level.getTheme().floor ) ), null, null, null );
		final PhongMaterial CEILING = new PhongMaterial( Color.WHITE, new Image( R.getString( "tex/" + level.getTheme().ceiling ) ), null, null, null );
		
		final Block[][] blockss = level.getBlockss();
		for ( int i = 0; i < blockss.length; i++ ) {
			final Block[] blocks = blockss[ i ];
			for ( int j = 0; j < blocks.length; j++ ) {
				final Block b = blocks[ j ];
				final BlockPos pos = new BlockPos( i, j );
				
				switch ( b.getType() ) {
					case WALL :
						root.getChildren().add( wallss[ i ][ j ] = Block3DFactory.createWall( WALL, pos ) );
						break;
					case EMPTY :
						// Note: I tried creating square mashup views for 1 side only (both for floors and ceilings), but it turned out to be much slower
						// than just creating two 0-height boxes.
						root.getChildren().add( Block3DFactory.createFloor( FLOOR, pos ) );
						root.getChildren().add( Block3DFactory.createCeiling( CEILING, pos ) );
						break;
				}
			}
		}
		
		// Switches
		switch ( STATIC_MAP_NAME_PROP.get() ) {
			case "maze" : {
				final BlockPos wallPos = new BlockPos( 10, 7 );
				root.getChildren().add( Block3DFactory.createFloor( FLOOR, wallPos ) );
				root.getChildren().add( new WallSwitch( this, new BlockPos( 7, 7 ), Side.LEFT, wallPos ) );
				root.getChildren().add( new Switch( this, new BlockPos( 7, 9 ), Side.AHEAD ) );
				root.getChildren().add( new Switch( this, new BlockPos( 7, 9 ), Side.RIGHT ) );
				root.getChildren().add( new Switch( this, new BlockPos( 7, 9 ), Side.BEHIND ) );
				break;
			}
			case "V4KB" : {
				BlockPos wallPos = new BlockPos( 4, 4 );
				root.getChildren().add( Block3DFactory.createFloor( FLOOR, wallPos ) );
				root.getChildren().add( new WallSwitch( this, new BlockPos( 1, 19 ), Side.BEHIND, wallPos ) );
				wallPos = new BlockPos( 5, 4 );
				root.getChildren().add( Block3DFactory.createFloor( FLOOR, wallPos ) );
				root.getChildren().add( new WallSwitch( this, new BlockPos( 3, 1 ), Side.BEHIND, wallPos ) );
				wallPos = new BlockPos( 11, 7 );
				root.getChildren().add( Block3DFactory.createFloor( FLOOR, wallPos ) );
				root.getChildren().add( new WallSwitch( this, new BlockPos( 12, 39 ), Side.RIGHT, wallPos ) );
				wallPos = new BlockPos( 10, 7 );
				root.getChildren().add( Block3DFactory.createFloor( FLOOR, wallPos ) );
				root.getChildren().add( new WallSwitch( this, new BlockPos( 12, 31 ), Side.BEHIND, wallPos ) );
				break;
			}
		}
		
		cam.setPos( level.getStartPos().col + 0.5, -0.5, level.getStartPos().row + 0.5 );
		cam.setHAngle( 0 );
		cam.setVAngle( 0 );
		
		root.getChildren().add( cam );
		
		pointLight.getTransforms().addAll( cam.getTransforms() );
		root.getChildren().add( pointLight );
		root.getChildren().add( ambientLight );
		getScene().setCamera( cam );
		
		getScene().setFill( Color.LIGHTBLUE );
	}
	
	private void handleKeys() {
		final EventHandler< KeyEvent > handler = new EventHandler< KeyEvent >() {
			@Override
			public void handle( final KeyEvent event ) {
				KEY_STATES[ event.getCode().ordinal() ] = event.getEventType() == KeyEvent.KEY_PRESSED;
				
				// Regular key press jobs:
				if ( event.getEventType() == KeyEvent.KEY_PRESSED )
					switch ( event.getCode() ) {
						case F :
							stage.setFullScreen( !stage.isFullScreen() );
							break;
						case M :
							mp.setMute( !mp.isMute() );
							break;
						default :
							break;
					}
			}
		};
		
		getScene().setOnKeyPressed( handler );
		getScene().setOnKeyReleased( handler );
	}
	
	private void startGameLoop() {
		// 30 FPS
		final KeyFrame frame = new KeyFrame( Duration.millis( 1000.0 / FPS ), new EventHandler< ActionEvent >() {
			long lastTime = System.nanoTime();
			
			@Override
			public void handle( final ActionEvent event ) {
				// Calculate elapsed time. Don't rely on FPS: it might not be fulfilled!
				final long time = System.nanoTime();
				final double dt = ( time - lastTime ) / 1e9; // Unit is seconds
				lastTime = time;
				
				// Handle control keys
				
				if ( KEY_STATES[ KeyCode.UP.ordinal() ] || KEY_STATES[ KeyCode.NUMPAD8.ordinal() ] )
					cam.addToPos( MOVEMENT_SPEED * dt );
				if ( KEY_STATES[ KeyCode.DOWN.ordinal() ] || KEY_STATES[ KeyCode.NUMPAD2.ordinal() ] )
					cam.addToPos( -MOVEMENT_SPEED * dt );
				
				if ( KEY_STATES[ KeyCode.HOME.ordinal() ] || KEY_STATES[ KeyCode.END.ordinal() ] || KEY_STATES[ KeyCode.NUMPAD7.ordinal() ]
				        || KEY_STATES[ KeyCode.NUMPAD1.ordinal() ] )
					cam.addToPos( MOVEMENT_SPEED * dt, cam.getHDir().getAngle() - 90 );
				if ( KEY_STATES[ KeyCode.PAGE_UP.ordinal() ] || KEY_STATES[ KeyCode.PAGE_DOWN.ordinal() ] || KEY_STATES[ KeyCode.NUMPAD9.ordinal() ]
				        || KEY_STATES[ KeyCode.NUMPAD3.ordinal() ] )
					cam.addToPos( MOVEMENT_SPEED * dt, cam.getHDir().getAngle() + 90 );
				
				if ( KEY_STATES[ KeyCode.LEFT.ordinal() ] || KEY_STATES[ KeyCode.NUMPAD4.ordinal() ] )
					cam.addToHAngle( -TURN_RATE * dt );
				if ( KEY_STATES[ KeyCode.RIGHT.ordinal() ] || KEY_STATES[ KeyCode.NUMPAD6.ordinal() ] )
					cam.addToHAngle( TURN_RATE * dt );
				
				if ( KEY_STATES[ KeyCode.INSERT.ordinal() ] )
					cam.addToVAngle( TURN_RATE * dt );
				if ( KEY_STATES[ KeyCode.DELETE.ordinal() ] )
					cam.addToVAngle( -TURN_RATE * dt );
				
				if ( KEY_STATES[ KeyCode.SPACE.ordinal() ] )
					cam.elevate( -ELEVATION_SPEED * dt );
				if ( KEY_STATES[ KeyCode.ENTER.ordinal() ] )
					cam.elevate( ELEVATION_SPEED * dt );
				
				corrigatePos();
				
				if ( KEY_STATES[ KeyCode.MULTIPLY.ordinal() ] ) {
					pointLight.setColor( Color.BLACK.interpolate( Color.gray( 0.75 ), pointLight.getColor().getRed() / 0.75 + dt ) );
					ambientLight.setColor( Color.BLACK.interpolate( Color.gray( 1 ), ambientLight.getColor().getRed() / 1 + dt ) );
				}
				if ( KEY_STATES[ KeyCode.DIVIDE.ordinal() ] ) {
					pointLight.setColor( Color.BLACK.interpolate( Color.gray( 0.75 ), pointLight.getColor().getRed() / 0.75 - dt ) );
					ambientLight.setColor( Color.BLACK.interpolate( Color.gray( 1 ), ambientLight.getColor().getRed() / 1 - dt ) );
				}
			}
		} );
		
		final Timeline tl = new Timeline( frame );
		tl.setCycleCount( Timeline.INDEFINITE );
		tl.play();
	}
	
	private void corrigatePos() {
		double x = cam.getPos().getX();
		double y = cam.getPos().getY();
		double z = cam.getPos().getZ();
		
		// If current position is forbidden, corrigate.
		
		// First: detect trivial wall collisions
		
		// Left blocked
		if ( blockAt( x - MIN_BLOCK_DIST, z ).getType() != Type.EMPTY )
			x = (int) x + MIN_BLOCK_DIST;
		
		// Right blocked
		if ( blockAt( x + MIN_BLOCK_DIST, z ).getType() != Type.EMPTY )
			x = (int) ( x + MIN_BLOCK_DIST ) - MIN_BLOCK_DIST;
		
		// Behind blocked
		if ( blockAt( x, z - MIN_BLOCK_DIST ).getType() != Type.EMPTY )
			z = (int) z + MIN_BLOCK_DIST;
		
		// Ahead blocked
		if ( blockAt( x, z + MIN_BLOCK_DIST ).getType() != Type.EMPTY )
			z = (int) ( z + MIN_BLOCK_DIST ) - MIN_BLOCK_DIST;
		
		// Second: detect nontrivial corner collisions
		// Background: we want to keep the same distance from the corners also when all direction might be free, but for example 1 step left and 1 step ahead
		// is not. In this case we increase the distance from the corner to be the desired distance, in the direction from the corner toward our position.
		
		for ( int i = -1; i <= 1; i += 2 )
			for ( int j = -1; j <= 1; j += 2 )
				if ( blockAt( x + i * MIN_BLOCK_DIST, z ).getType() == Type.EMPTY && blockAt( x, z + j * MIN_BLOCK_DIST ).getType() == Type.EMPTY
				        && blockAt( x + i * MIN_BLOCK_DIST, z + j * MIN_BLOCK_DIST ).getType() != Type.EMPTY ) {
					double dx = x - (int) x;
					double dz = z - (int) z;
					if ( dx > 0.5 )
						dx -= 1;
					if ( dz > 0.5 )
						dz -= 1;
					final double delta = MIN_BLOCK_DIST - Math.sqrt( dx * dx + dz * dz );
					if ( delta > 0 ) {
						x += delta / MIN_BLOCK_DIST * dx;
						z += delta / MIN_BLOCK_DIST * dz;
					}
				}
		
		// Ground and ceiling collision
		
		if ( y < -1 + MIN_BLOCK_DIST )
			y = -1 + MIN_BLOCK_DIST;
		if ( y > -MIN_BLOCK_DIST )
			y = -MIN_BLOCK_DIST;
		
		cam.setPos( x, y, z );
	}
	
	private Block blockAt( final double x, final double z ) {
		return level.getBlockss()[ (int) z ][ (int) x ];
	}
	
	public SubScene getScene() {
		return scene;
	}
	
	public Cam getCam() {
		return cam;
	}
	
	public Node[][] getWallss() {
		return wallss;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public MediaPlayer getMediaPlayer() {
		return mp;
	}
	
}

package hu.belicza.andras.amaze;

import hu.belicza.andras.amaze.world.World;
import hu.belicza.andras.amaze.world.level.Level;
import hu.belicza.andras.amaze.world.level.Theme;

import java.util.Locale;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Amaze extends Application {
	
	public static void main( final String[] args ) {
		launch( args );
	}
	
	private Stage           stage;
	
	private final StackPane root  = new StackPane();
	
	private final Scene     scene = new Scene( root );
	
	private World           world;
	
	@Override
	public void start( final Stage stage ) throws Exception {
		this.stage = stage;
		stage.setScene( scene );
		
		setupScene();
		
		setupStage();
		
		stage.show();
		
		world.getScene().requestFocus();
	}
	
	private void setupScene() {
		world = new World( stage );
		
		root.getChildren().add( world.getScene() );
		
		// I use Group instead of a Pane for overlay because I need the inputs to "go through" to the 3D World.
		final Group overlay = new Group();
		// Group does not have size, it uses the bounding box of the children.
		// Assign the size by adding 2 nodes to the top left and bottom right corners:
		final Node topLeft = new Label();
		overlay.getChildren().add( topLeft );
		final Node bottomRight = new Label();
		bottomRight.translateXProperty().bind( scene.widthProperty() );
		bottomRight.translateYProperty().bind( scene.heightProperty() );
		overlay.getChildren().add( bottomRight );
		
		double x = 2;
		double y = -20;
		
		final Color textColor = new Color( 1, 1, 1, 0.7 );
		final String bgStyle = "-fx-background-color: rgba(255, 255, 255, 0.6);";
		
		y += 30;
		HBox hbox = new HBox();
		hbox.setAlignment( Pos.CENTER );
		final ComboBox< String > mapBox = new ComboBox<>();
		mapBox.setStyle( bgStyle );
		mapBox.getItems().addAll( "V4KB", "maze", "mazebig" );
		mapBox.setFocusTraversable( false );
		mapBox.setValue( World.STATIC_MAP_NAME_PROP.get() );
		World.STATIC_MAP_NAME_PROP.bind( mapBox.getSelectionModel().selectedItemProperty() );
		mapBox.getSelectionModel().selectedItemProperty().addListener( new ChangeListener< String >() {
			@Override
			public void changed( final ObservableValue< ? extends String > ov, final String oldMap, final String newMap ) {
				world.rebuildWorld();
			}
		} );
		Label l = new Label( "(map)" );
		l.setTextFill( textColor );
		hbox.getChildren().addAll( mapBox, l );
		hbox.setTranslateX( x );
		hbox.setTranslateY( y );
		overlay.getChildren().add( hbox );
		
		y += 30;
		hbox = new HBox();
		hbox.setAlignment( Pos.CENTER );
		final ComboBox< Theme > themeBox = new ComboBox<>();
		themeBox.setStyle( bgStyle );
		themeBox.getItems().addAll( Theme.values() );
		themeBox.setFocusTraversable( false );
		themeBox.setValue( Level.STATIC_THEME_PROP.get() );
		Level.STATIC_THEME_PROP.bind( themeBox.getSelectionModel().selectedItemProperty() );
		themeBox.getSelectionModel().selectedItemProperty().addListener( new ChangeListener< Theme >() {
			@Override
			public void changed( final ObservableValue< ? extends Theme > ov, final Theme oldTheme, final Theme newTheme ) {
				world.rebuildWorld();
			}
		} );
		l = new Label( "(theme)" );
		l.setTextFill( textColor );
		hbox.getChildren().addAll( themeBox, l );
		hbox.setTranslateX( x );
		hbox.setTranslateY( y );
		overlay.getChildren().add( hbox );
		
		y += 30;
		Button b = new Button( "(F)ullscreen" );
		b.setStyle( bgStyle );
		b.setFocusTraversable( false );
		b.setOnAction( new EventHandler< ActionEvent >() {
			@Override
			public void handle( ActionEvent event ) {
				stage.setFullScreen( !stage.isFullScreen() );
			}
		} );
		b.setTranslateX( x );
		b.setTranslateY( y );
		overlay.getChildren().add( b );
		
		y += 30;
		b = new Button( "(M)ute" );
		b.setStyle( bgStyle );
		b.setFocusTraversable( false );
		b.setOnAction( new EventHandler< ActionEvent >() {
			@Override
			public void handle( ActionEvent event ) {
				world.getMediaPlayer().setMute( !world.getMediaPlayer().isMute() );
			}
		} );
		b.setTranslateX( x );
		b.setTranslateY( y );
		overlay.getChildren().add( b );
		
		y += 30;
		l = new Label();
		l.setTextFill( textColor );
		l.textProperty().bind( world.getCam().getPos().xProperty().asString( Locale.US, "x=%+f" ) );
		l.setTranslateX( x );
		l.setTranslateY( y );
		overlay.getChildren().add( l );
		
		y += 15;
		l = new Label();
		l.setTextFill( textColor );
		l.textProperty().bind( world.getCam().getPos().yProperty().asString( Locale.US, "y=%+f" ) );
		l.setTranslateX( x );
		l.setTranslateY( y );
		overlay.getChildren().add( l );
		
		y += 15;
		l = new Label();
		l.setTextFill( textColor );
		l.textProperty().bind( world.getCam().getPos().zProperty().asString( Locale.US, "z=%+f" ) );
		l.setTranslateX( x );
		l.setTranslateY( y );
		overlay.getChildren().add( l );
		
		root.getChildren().add( overlay );
	}
	
	private void setupStage() {
		stage.setTitle( "(A)Maze demo (c) 2014 András Belicza" );
		stage.getIcons().add( Consts.IMG_ICON );
		
		stage.setFullScreenExitHint( "" );
		
		stage.setWidth( 1422 );
		stage.setHeight( 800 );
	}
	
}

package hu.belicza.andras.amaze.world.level;

/**
 * Level theme. Defines the textures used on walls, floors and ceiling.
 * 
 * @author beliczaa
 */
public enum Theme {
	
	// Source: http://www.jfxia.com/JFXDemos/3DMaze/
	MAZE( "MazeWall.jpg", "MazeFloor.jpg", "MazeCeiling.jpg" ),
	
	STONE( "BrickRound0105_5_S.jpg", "FloorsRegular0279_1_S.jpg", "PlasterCeiling0006_1_S.jpg" ),
	
	FOREST_FLOWER( "FlowerBeds0005_2_S.jpg", "Grass0100_7_S.jpg", "FlowerBeds0005_2_S.jpg" ),
	
	FOREST( "Ivy0070_7_S.jpg", "Grass0053_26_S.jpg", "Ivy0070_7_S.jpg" ),
	
	WOOD( "Thatched0046_2_S.jpg", "LeavesDead0033_9_S.jpg", "Thatched0061_5_S.jpg" ),
	
	WINTER( "BrickLargeBlocks0023_1_S.jpg", "GrassFrozen0036_1_S.jpg", "PlasterCeiling0006_1_S.jpg" ),
	
	TILES( "TilesOrnate0029_2_S.jpg", "TilesOrnate0106_4_S.jpg", "TilesOrnate0084_2_S.jpg" ),
	
	MARBLE( "MarbleBeige0028_5_S.jpg", "MarbleGreen0005_13_S.jpg", "MarbleWhite0035_2_S.jpg" ),
	
	EMPTY( "", "", "" );
	
	
	/** Name of the wall texture. */
	public final String wall;
	
	/** Name of the floor texture. */
	public final String floor;
	
	/** Name of the ceiling texture. */
	public final String ceiling;
	
	
	private Theme( final String wall, final String floor, final String ceiling ) {
		this.wall = wall;
		this.floor = floor;
		this.ceiling = ceiling;
	}
	
}

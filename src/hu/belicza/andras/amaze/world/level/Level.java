package hu.belicza.andras.amaze.world.level;

import hu.belicza.andras.amaze.Utils;
import hu.belicza.andras.amaze.r.R;
import hu.belicza.andras.amaze.world.level.block.Block;
import hu.belicza.andras.amaze.world.level.block.Type;

import java.util.Collections;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Level {
	
	/** Temporarily new Levels take theme from this (instead of reading it from the mapdata file). */
	public static ObjectProperty< Theme > STATIC_THEME_PROP = new SimpleObjectProperty< Theme >( Theme.MAZE );
	
	
	private final Theme                   theme;
	
	private final Block[][]               blockss;
	
	/** Start location. */
	private BlockPos                      startPos;
	
	public Level( final int width, final int height ) {
		theme = STATIC_THEME_PROP.get();
		blockss = new Block[ height ][ width ];
	}
	
	public static Level generateMap( final String name ) {
		try {
			final List< String > lineList = Utils.readAllLines( R.getStream( "map/" + name + ".mapdata" ) );
			// Reverse the list so the row index will grow as the z index
			Collections.reverse( lineList );
			final char[][] data = new char[ lineList.size() ][];
			
			int maxWidth = 0;
			for ( int i = 0; i < lineList.size(); i++ ) {
				data[ i ] = lineList.get( i ).toCharArray();
				maxWidth = Math.max( maxWidth, data[ i ].length );
			}
			
			final Level map = new Level( maxWidth, lineList.size() );
			for ( int i = 0; i < data.length; i++ ) {
				final char[] row = data[ i ];
				for ( int j = 0; j < row.length; j++ ) {
					final Block b = map.blockss[ i ][ j ] = new Block();
					switch ( row[ j ] ) {
						case 'W' : // Wall
							b.setType( Type.WALL );
							break;
						case 'S' : // Start location
							map.startPos = new BlockPos( i, j );
							// Start location also ground, so no break
						case ' ' : // Empty
							b.setType( Type.EMPTY );
							break;
					}
				}
				
			}
			return map;
		} catch ( final Exception e ) {
			throw new RuntimeException( e );
		}
	}
	
	public Theme getTheme() {
		return theme;
	}
	
	public Block[][] getBlockss() {
		return blockss;
	}
	
	public BlockPos getStartPos() {
		return startPos;
	}
	
}

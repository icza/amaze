package hu.belicza.andras.amaze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * General utilities.
 * 
 * @author Andras Belicza
 */
public class Utils {
	
	public static List< String > readAllLines( final InputStream is ) {
		try ( final BufferedReader br = new BufferedReader( new InputStreamReader( is, StandardCharsets.UTF_8 ) ) ) {
			final List< String > lines = new ArrayList<>();
			
			String line;
			while ( ( line = br.readLine() ) != null )
				lines.add( line );
			
			return lines;
		} catch ( final IOException ie ) {
			return new ArrayList<>();
		}
	}
	
}

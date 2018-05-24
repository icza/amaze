package hu.belicza.andras.amaze.r;

import java.io.InputStream;
import java.net.URL;

/**
 * Class identifying the resources package.
 * 
 * @author beliczaa
 */
public class R {
	
	public static URL get( final String name ) {
		return R.class.getResource( name );
	}
	
	public static InputStream getStream( final String name ) {
		return R.class.getResourceAsStream( name );
	}
	
	public static String getString( final String name ) {
		return R.class.getResource( name ).toString();
	}
	
}

package org.janelia.sort.tsp;

import ij.ImagePlus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;

import org.janelia.sort.tsp.conversion.DataToStringFullMatrixTSPLIB;
import org.janelia.sort.tsp.conversion.SimilarityToDistanceSigmoid;
import org.junit.Assert;
import org.junit.Test;

public class TSPTest {
	
	final String path                                      = "src/test/java/org/janelia/sort/tsp/AVG_inlier ratio matrix-excerpt.tif";
	final DataToStringFullMatrixTSPLIB converter           = new DataToStringFullMatrixTSPLIB();
	final SimilarityToDistanceSigmoid similarityToDistance = new SimilarityToDistanceSigmoid( 1000.0, 0.0, 1000000.0 );
	final String referencePath                             = "src/test/java/org/janelia/sort/tsp/excerpt-tsp.dat";


	@Test
	public void test() {
		final ImagePlus imp      = new ImagePlus( path );
		final Img<FloatType> img = ImageJFunctions.wrapFloat( imp );
		final String s           = TSP.convertMatrix( img, converter, similarityToDistance );
		final String[] split     = s.split( System.getProperty("line.separator") );
		
		LineNumberReader lnr;
		try {
			lnr = new LineNumberReader(new FileReader(new File( referencePath )));
			lnr.skip(Long.MAX_VALUE);
			Assert.assertEquals( split.length, lnr.getLineNumber()+1 );
			lnr.close();
		} catch (final FileNotFoundException e) {
			Assert.fail( "Could not find file " + referencePath );
		} catch ( final IOException e ) {
			Assert.fail( "Error accessing file " + referencePath );
		}
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader( new File( referencePath ) ) );
		} catch (final FileNotFoundException e1) {
			br = null;
			Assert.fail( "Could not find file " + referencePath );
		}
		
		String line = null;
		try {
			for (int index = 0; (line = br.readLine()) != null; ++index )  
			{
				Assert.assertEquals( split[index], line );
			}
		} catch (final IOException e) {
			Assert.fail( "Error accessing file " + referencePath );
		} 
		
	}

}
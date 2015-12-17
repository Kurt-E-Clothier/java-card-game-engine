/***********************************************************************//**
* @file			Resolution.java
* @author		Kurt E. Clothier
* @date			December 15, 2015
* 
* @breif		Tools involving window and screen resolution
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine.gui;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*****************************************************************//**
 * The <tt>Resolution</tt> Enum
 * - Access statically as Resolution.INSTANCE.method()
 *	 or Resolution resolution = Resolution.getInstance()
 ********************************************************************/
public enum Resolution {
	
	INSTANCE;
	
	// Resolution attributes
	private final int screenWidth;
	private final int screenHeight;

	/**
	 * Construct this <tt>Resolution</tt> by capturing the current screen width and height.
	 */
	private Resolution() {
		screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
	
	/**
	 * Returns an instance of this <tt>Resolution</tt>.
	 * 
	 * @return an instance of this <tt>Resolution</tt>
	 */
	public static Resolution getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Returns the number of pixels for the width of the screen.
	 * 
	 * @return the number of pixels for the width of the screen
	 */
	public int getScreenWidth() {
		return screenWidth;
	}
	
	/**
	 * Returns the number of pixels for the height of the screen.
	 *
	 * @return the number of pixels for the height of the screen
	 */
	public int getScreenHeight() {
		return screenHeight;
	}
	
	/**
	 * Maximize the specified <tt>Frame</tt> to fill the screen.
	 * 
	 * @param frame the frame to be maximized
	 */
	public void maximizeFrame(final Frame frame) {
		// First find a Resolution just smaller than the screen's width
		for (Resolution.Standard s : Resolution.Standard.values()) {
			Resolution.Standard bestFit = s;
			if (screenWidth > s.getWidth()) {
				frame.setSize(bestFit.getWidth(), bestFit.getHeight());
				break;
			}
		}
		// Finally, maximize the frame to fill the screen
		frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
	}
	
	/**
	 * Set the specified <tt>Window</tt> resolution to a standard ratio.
	 * 
	 * @param window the window to be centered.
	 * @param standard a standard resolution to use
	 */
	public static void setWindowResolutuion(final Window window, final Resolution.Standard standard) {
		setWindowResolutuion(window, standard, false);
	}
	
	/**
	 * Set the specified <tt>Window</tt> resolution to a standard ratio.
	 * 
	 * @param window the window to be centered.
	 * @param standard a standard resolution to use
	 * @param isRotated set to <tt>true</tt> to rotate the window (swap the resolution width and height)
	 */
	public static void setWindowResolutuion(final Window window, final Resolution.Standard standard, final boolean isRotated) {
		if (isRotated) {
			window.setSize(standard.getHeight(), standard.getWidth());
		}
		else {
			window.setSize(standard.getWidth(), standard.getHeight());
		}
	}
	
	/**
	 * Center the specified <tt>Window</tt> on the screen.
	 * 
	 * @param window the window to be centered.
	 */
	public static void centerWindow(final Window window) {
		window.setLocationRelativeTo(null);
	}
	
	/******************************************************************//**
	 * Common Standard Display Resolutions
	 * 
	 * @see https://en.wikipedia.org/wiki/Computer_display_standard
	 **********************************************************************/
	public static enum Standard {
		/** Quarter, Quarter Video Graphics Adapter - Webcams */
		QQVGA (160, 120, 4, 3),
		/** Half Quarter Video Graphics Adapter - Portable Devices */
		HQVGA (240, 160, 3, 2),
		/** Quarter Video Graphics Adapter - Portable Devices */
		QVGA (320, 240, 4, 3),
		/** Wide Quarter Video Graphics Adapter - Portable Devices */
		WQVGA (480, 270, 16, 9),
		/** Multicolor Graphics Adapter */
		MCGA (320, 200, 16, 10),
		/** Video Graphics Array */
		VGA (640, 480, 4, 3),
		/** Super Video Graphics Array */
		SVGA (800, 600, 4, 3),
		/** IBM Standard 8514 */
		IBM_8514 (1024, 768, 4, 3),
		/** Extended Graphics Array */
		XGA (1024, 768, 4, 3),
		/** Extended Graphics Array Plus*/
		XGAP (1152, 864, 4, 3),
		/** High Definition */
		HD (1360, 768, 16, 9),
		/** Widescreen Extended Graphics Array */
		WXGA (1280, 800, 16, 10),
		/** Super Extended Graphics Array */
		SXGA (1280, 1024, 5, 4),
		/** Super Extended Graphics Array Plus */
		SXGAP (1280, 1024, 5, 4),
		/** Widescreen Extended Graphics Array Plus*/
		WXGAP (1440, 900, 16, 10),
		/** High Definition Plus */
		HDP (1600, 900, 16, 9),
		/** Ultra Extended Graphics Array */
		UXGA (1600, 1200, 4, 3),
		/** Widescreen Super Extended Graphics Array Plus */
		WSXGAP (1680, 1050, 16, 10),
		/** Full High Definition */
		FHD (1920, 1080, 16, 9),
		/** Widescreen Ultra Extended Graphics Array */
		WUXGA (1920, 1200, 16, 10),
		/** Full High Definition Plus */
		FHDP (1920, 1280, 3, 2),
		/** 2K - Digital Film Projection */
		TWO_K (2048, 1080, 256, 135),
		/** Quad Wide Extended Graphics Array */
		QWXGA (2048, 1152, 16, 9),
		/** Quad Extended Graphics Array */
		QXGA (2048, 1536, 4, 3),
		/** Quad HIgh Definition */
		QHD (2160, 1440, 3, 2),
		/** Wide Quad High Definition */
		WQHD (2560, 1440, 16, 9),
		/** Widescreen Quad Extended Graphics Array */
		WQXGA (2560, 1600, 16, 10),
		/** Quad Super Extended Graphics Array */
		QSXGA (2560, 2048, 5, 4),
		/** Quad Wide Extended Graphics Array Plus */
		QWXGAP (2880, 1800, 16, 10),
		/** Wide Quad Super Extended Graphics Array */
		WQSXGA (3200, 2048, 25, 16),
		/** Quad Ultra Extended Graphics Array */
		QUXGA (3200, 2400, 4, 3),
		/** Ultra High Definition */
		UHD (3840, 2160, 16, 9),
		/** Quad Full High Definition */
		QFHD (3840, 2160, 16, 9),
		/** Wide Quad Ultra Extended Graphics Array */
		WQUXGA (3840, 2400, 16, 10),
		/** 4K - Digital Film Projection */
		FOUR_K (4096, 2160, 2, 1),
		/** Hex Extended Graphics Array */
		HXGA (4096, 3072, 4, 3),
		/** Wide Sedecim High Definition */
		WSHD (5120, 2880, 16, 9),
		/** 5K - Apple Retina 5K Display */
		FIVE_K (5120, 2880, 16, 9),
		/** Wide Hex Extended Graphics Array */
		WHXGA (5120, 3200, 16, 10),
		/** Hex Super Extended Graphics Array */
		HSXGA (5120, 4096, 5, 4),
		/** Wide Hex Super Extended Graphics Array */
		WHSXGA (6400, 4096, 25, 16),
		/** Hex Ultra Extended Graphics Array */
		HUXGA (6400, 4800, 4, 3),
		/** 8K Ultra High Definition */
		EIGHT_K_UHD (7680, 4320, 16, 9),
		/** Wide Hex Ulra Extended Graphics Array */
		WHUXGA (7680, 4800, 16, 10);
		
		private final int xPixels;
		private final int yPixels;
		private final int xDAR;
		private final int yDAR;
		
		/**
		 * Construct this <tt>Resolution</tt> <tt>Standard</tt>.
		 * 
		 * @param width the pixel width
		 * @param height the pixel height
		 */
		private Standard(final int width, final int height, final int xDAR, final int yDAR) {
			this.xPixels = width;
			this.yPixels = height;
			this.xDAR = xDAR;
			this.yDAR = yDAR;
		}
		
		/**
		 * Returns the number of pixels in the width of this <tt>Standard</tt>.
		 * 
		 * @return the number of pixels in the width of this <tt>Standard</tt>
		 */
		public int getWidth() {
			return xPixels;
		}
		
		/**
		 * Returns the number of pixels in the height of this <tt>Standard</tt>
		 *
		 * @return the number of pixels in the height of this <tt>Standard</tt>
		 */
		public int getHeight() {
			return yPixels;
		}
		
		/**
		 * Returns the total number of pixels in this <tt>Standard</tt>.
		 *
		 * @return the total number of pixels in this <tt>Standard</tt>
		 */
		public int getNumberOfPixes() {
			return xPixels * yPixels;
		}
		
		/**
		 * Returns the Display Aspect Ratio of this <tt>Standard</tt>.
		 * 
		 * @return the Display Aspect Ratio of this <tt>Standard</tt>
		 */
		public AspectRatio getDAR() {
			return AspectRatio.Factory.INSTANCE.create(xDAR, yDAR);
		}
	}	// End of Resolution.Standard
	
	/******************************************************************//**
	 * Aspect Ratio - Proportional relationship between width and height
	 * 
	 * <p>Must be created using the <tt>AspectRatio.Factory</tt>.
	 * 
	 * @see AspectRatio.Factory
	 **********************************************************************/
	public static class AspectRatio {
		
		private final int x;
		private final int y;
		
		private AspectRatio(final int x, final int y) {
			this.x = x;
			this.y = y;
		}
		
		/** 
		 * Returns the x member of this <tt>AspectRatio</tt>. 
		 * 
		 * @return the x member of this <tt>AspectRatio</tt>
		 */
		public int getX() {
			return x;
		}
		
		/** 
		 * Returns the x member of this <tt>AspectRatio</tt>.
		 * 
		 * @return the x member of this <tt>AspectRatio</tt>
		 */
		public int getY() {
			return y;
		}
		
		/**
		 * Returns the larger member of this ratio, reduced, 
		 * by dividing both members by smaller of the two.
		 * <p>For example, a ratio of 5:4 is reduced to 1.25:1,
		 * and the larger 1.25 is returned.
		 * 
		 * @return the larger member of this ratio after a reduction
		 */
		public float getReduction() {
			return x > y ? (float)x/y : (float)y/x;
		}
		
		/**
		 * Returns <tt>true</tt> if this <tt>AspectRatio</tt> represents 
		 * is a "landscape," that is, x > y;
		 * 
		 * @return <tt>true</tt> if this ratio is a landscape
		 */
		public boolean isLandscape() {
			return x > y;
		}
		
		/**
		 * Returns <tt>true</tt> if this <tt>AspectRatio</tt> represents 
		 * is a "portrait," that is, y > x;
		 * 
		 * @return <tt>true</tt> if this ratio is a landscape
		 */
		public boolean isPortrait() {
			return y > x;
		}
		
		/** 
		 * Returns this <tt>AspectRatio</tt> expressed as "x:y" 
		 * 
		 * @return this <tt>AspectRatio</tt> expressed as "x:y"
		 */
		@Override public String toString() {
			final StringBuilder str = new StringBuilder();
			return str.append(x).append(':').append(y).toString();
		}
		
		/**
		 * Indicates whether some other object is "equal to" this one.
		 * 
		 * @param that the reference object with which to compare
		 * @return <tt>true</tt> if this object is the same as that
		 */
		@Override public boolean equals(final Object that) {
			return	that != null &&
					that.getClass() == this.getClass() &&
					((AspectRatio)that).getX() == this.x &&
					((AspectRatio)that).getY() == this.y;
		}
		
		/**
		 * Returns a hash code value for this object.
		 * 
		 * @return a hash code value for this object
		 */
		@Override public final int hashCode() {
			int hash = 17 * 31 + x;
			return hash * 31 + y;
		}
		
		/******************************************************************//**
		 * Aspect Ratio Factory - Only way to create an <tt>AspectRatio</tt>
		 * 
		 * <p>Access statically as ResolutionAspectRatio.Factory.INSTANCE.method() or
		 * Resolution.AspectRatio.Factory factory = Resolution.AspectRatio.Factory.getInstance()
		 * 
		 * @see AspectRatio
		 **********************************************************************/
		public static enum Factory {
			
			/** The only instance of this <tt>AspectRatioFactory</tt>. */
			INSTANCE;
		
			/* Private map, used for FlyWeight pattern */
			private final Map<Integer, Map<Integer, AspectRatio>> ratios;
			
			/* Construct this factory */
			private Factory() {
				ratios = new ConcurrentHashMap<Integer, Map<Integer, AspectRatio>>();
			}
			
			/**
			 * Returns an instance of this <tt>AspectRatioFactory</tt>.
			 * 
			 * @return an instance of this <tt>AspectRatioFactory</tt>
			 */
			public static Factory getInstance() {
				return INSTANCE;
			}
		
			/**
			 * Returns an <tt>AspectRatio</tt> with the specified x and y members.
			 * The returned ratio is only newly created if a ratio with the specified
			 * members does not already exist.
			 * 
			 * @param x the x member of this ratio
			 * @param y the y member of this ratio
			 * @return an <tt>AspectRatio</tt> with the specified x and y members
			 */
			public AspectRatio create(final int x, final int y) {
				AspectRatio aRatio = null;
				if (ratios.containsKey(x)) {
					if (ratios.get(x).containsKey(y)) {
						aRatio = ratios.get(x).get(y);
					}
					else {
						aRatio = new AspectRatio(x, y);
						ratios.get(x).put(y, aRatio);
					}
				}
				else {
					aRatio = new AspectRatio(x, y);
					ratios.put(x, new ConcurrentHashMap<Integer, AspectRatio>());
					ratios.get(x).put(y, aRatio);
				}
				return aRatio;
			}
		}	// End of Resolution.AspectRatio.Factory
		
	}	// End of Resolution.AspectRatio

}	// End of Resolution

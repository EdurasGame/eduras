/**
 * 
 */
package de.illonis.eduras.interfaces;


/**
 * This interface determines what information must be provided to the GUI.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public interface InfoInterface {

	/**
	 * Returns information about how big the map is. In detail, it returns the
	 * maximum y-position and the maximum x-position in an array.
	 * 
	 * @return Returns the maximum x-position at array position [0] and the
	 *         y-position at array position [1].
	 */
	public int[] getMapBounds();

}

/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.util;

import java.util.Vector;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * @author dtjones
 */
@objid ("bd057117-ff07-477f-98b6-609f73a80d5e")
public class SortedVector extends Vector {
    @objid ("341d6d8c-0561-4f28-9304-f6aa50746ab0")
     Comparator comparator;

    /**
     * Create a new sorted vector and use the given comparator to determine order.
     * @param comparator The comparator to use to determine what order to place
     * the elements in this vector.
     */
    @objid ("414e312f-f5a4-4e8c-8cef-ae545b448128")
    public SortedVector(Comparator comparator) {
        this.comparator = comparator;
    }

    /**
     * Adds an element in the Vector, sorted from greatest to least.
     * @param element The element to add to the Vector
     */
    @objid ("4ab1e91d-f08c-4e6e-aa17-b5c4f966aea4")
    public void addElement(Object element) {
        int highBound = size();
        int lowBound = 0;
        while (highBound - lowBound > 0) {
            int index = (highBound + lowBound) / 2;
            int result = comparator.compare(element, elementAt(index));
            if (result < 0) {
                lowBound = index + 1;
            } else if (result > 0) {
                highBound = index;
            } else {
                lowBound = index;
                highBound = index;
            }
        }
        insertElementAt(element, lowBound);
    }

    /**
     * Sort the vector.
     */
    @objid ("7cb231da-8e50-4dcc-ad67-d20b62a40a7b")
    public void sort() {
        Object[] array = new Object[size()];
        copyInto(array);
        removeAllElements();
        for (int i = 0; i < array.length; i++) {
            addElement(array[i]);
        }
    }

    /**
     * Interface used to determine the order to place sorted objects.
     */
    @objid ("92337784-81b3-467e-8c41-577f0ab49142")
    public interface Comparator {
        /**
         * Compare the given two objects.
         * @param object1 First object to compare
         * @param object2 Second object to compare
         * @return -1, 0, or 1 if the first object is less than, equal to, or
         * greater than the second, respectively
         */
        @objid ("ce2d9712-f0e1-443c-a9ff-71cce8cb5fd6")
        int compare(Object object1, Object object2);

    }

}

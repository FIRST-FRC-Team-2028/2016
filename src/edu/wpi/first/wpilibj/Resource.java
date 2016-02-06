/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.util.AllocationException;
import edu.wpi.first.wpilibj.util.CheckedAllocationException;

/**
 * Track resources in the program.
 * The Resource class is a convenient way of keeping track of allocated arbitrary resources
 * in the program. Resources are just indicies that have an lower and upper bound that are
 * tracked by this class. In the library they are used for tracking allocation of hardware channels
 * but this is purely arbitrary. The resource class does not do any actual allocation, but
 * simply tracks if a given index is currently in use.
 * 
 * WARNING: this should only be statically allocated. When the program loads into memory all the
 * static constructors are called. At that time a linked list of all the "Resources" is created.
 * Then when the program actually starts - in the Robot constructor, all resources are initialized.
 * This ensures that the program is restartable in memory without having to unload/reload.
 */
@objid ("3f517656-9f4e-4e61-a72a-4a93793e9646")
public class Resource {
    @objid ("34278aa1-9259-455d-bb30-41ac3a72adb6")
    private final boolean[] m_numAllocated;

    @objid ("d97fb5c9-814e-4333-be90-7b03ae7e100f")
    private final int m_size;

    @objid ("3fade980-3ab5-416c-81b5-f5ab9bd895af")
    private static Resource m_resourceList = null;

    @objid ("bbdfe90a-cfaa-4b1b-aa93-42a088a56482")
    private final Resource m_nextResource;

    /**
     * Clears all allocated resources
     */
    @objid ("282f1606-d9fd-47f4-b13b-7672868c29c0")
    public static void restartProgram() {
        for (Resource r = Resource.m_resourceList; r != null; r = r.m_nextResource) {
            for (int i = 0; i < r.m_size; i++) {
                r.m_numAllocated[i] = false;
            }
        }
    }

    /**
     * Allocate storage for a new instance of Resource.
     * Allocate a bool array of values that will get initialized to indicate that no resources
     * have been allocated yet. The indicies of the resources are 0..size-1.
     * @param size The number of blocks to allocate
     */
    @objid ("019ca30f-178d-43f4-ad8b-8c2ae69cd5d2")
    public Resource(final int size) {
        m_size = size;
        m_numAllocated = new boolean[m_size];
        for (int i = 0; i < m_size; i++) {
            m_numAllocated[i] = false;
        }
        m_nextResource = Resource.m_resourceList;
        Resource.m_resourceList = this;
    }

    /**
     * Allocate a resource.
     * When a resource is requested, mark it allocated. In this case, a free resource value
     * within the range is located and returned after it is marked allocated.
     * @return The index of the allocated block.
     */
    @objid ("a0e83237-78ef-42bb-a031-c4d1fe599414")
    public int allocate() throws CheckedAllocationException {
        for (int i = 0; i < m_size; i++) {
            if (m_numAllocated[i] == false) {
                m_numAllocated[i] = true;
                return i;
            }
        }
        throw new CheckedAllocationException("No available resources");
    }

    /**
     * Allocate a specific resource value.
     * The user requests a specific resource value, i.e. channel number and it is verified
     * unallocated, then returned.
     * @param index The resource to allocate
     * @return The index of the allocated block
     */
    @objid ("c3b12c6e-ae39-419e-996c-c7b0f1837ac0")
    public int allocate(final int index) throws CheckedAllocationException {
        if (index >= m_size || index < 0) {
            throw new CheckedAllocationException("Index " + index + " out of range");
        }
        if (m_numAllocated[index] == true) {
            throw new CheckedAllocationException("Resource at index " + index + " already allocated");
        }
        m_numAllocated[index] = true;
        return index;
    }

    /**
     * Free an allocated resource.
     * After a resource is no longer needed, for example a destructor is called for a channel assignment
     * class, Free will release the resource value so it can be reused somewhere else in the program.
     * @param index The index of the resource to free.
     */
    @objid ("bb41db68-7966-4edb-a75a-db239c4cd216")
    public void free(final int index) {
        if (m_numAllocated[index] == false)
            throw new AllocationException("No resource available to be freed");
        m_numAllocated[index] = false;
    }

}

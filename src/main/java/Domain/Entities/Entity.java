package Domain.Entities;

/**
 * Base class for concrete entities.
 */

public abstract class Entity {
    protected int id;

    /**
     * @param id The entity Id
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The id as Integer
     */

    public int getId() {
        return id;
    }

    /**
     * @return a copy of the entity
     */


    public abstract Entity clone();
}


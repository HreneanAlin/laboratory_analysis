package Repository;

import Domain.Entities.Entity;

import java.util.List;

/**
 * A interface inplemented by a repository.
 *
 * @param <T> An Object that extends an Entity.
 */
public interface IRepository<T extends Entity> {
    /**
     * @param entity The object to be added to repository.
     */
    void create(T entity);

    /**
     * @param entityId the id of the Entity.
     * @return the Entity with that id.
     */
    T read(int entityId);

    /**
     * @return a list that contains all Entities
     */
    List<T> read();

    /**
     * @param entity The Entity to be updated.
     */
    void update(T entity);

    /**
     * @param entityId The id of the Entity to be deleted.
     */
    void delete(int entityId);

    /**
     * removes all Entities from the Repository. Used for tests only.
     */
    void clearAll();
}


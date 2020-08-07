package Service;

import Domain.Entities.Entity;
import Domain.Validators.IValidator;
import Repository.IRepository;

import java.util.Comparator;
import java.util.List;

/**
 * Provides basic functionality for services.
 */
public abstract class AbstractService<T extends Entity> {
    protected IRepository<T> repository;
    protected IValidator<T> validator;

    /**
     * @param repository The repository used for the service.
     * @param validator  The validator of the current entity.
     */
    public AbstractService(IRepository<T> repository, IValidator<T> validator) {
        this.repository = repository;
        this.validator = validator;
    }

    /**
     * @param id the entity id to be deleted.
     */
    public void deleteOne(int id) {
        this.repository.delete(id);
    }

    /**
     * @param id the entity id.
     * @return the entity with that id
     */
    public Entity getOne(int id) {
        return repository.read(id);
    }

    /**
     * @return a sorted list by id with all the entities from repository.
     */
    public List<T> getAll() {
        List<T> entities = repository.read();
        entities.sort((Comparator.comparingInt(Entity::getId)));
        return entities;
    }


}

package Domain.Validators;

import Domain.Entities.Entity;

/**
 * Interface implemented by the validators
 *
 * @param <T>An object that extends Entity
 */

public interface IValidator<T extends Entity> {

    /**
     * @param entity An object that extends Entity.
     */
    void validateBeforeRegistration(T entity);

    /**
     * @param entity An object that extends Entity.
     */

    void validateAfter(T entity);
}

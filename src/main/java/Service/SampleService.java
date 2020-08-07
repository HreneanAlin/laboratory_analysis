package Service;

import Domain.Entities.Sample;
import Domain.Entities.User;
import Domain.Exceptions.InvalidSampleException;
import Domain.Exceptions.WrongIdException;
import Domain.Validators.IValidator;
import Repository.IRepository;

/**
 * A service that provides functionality for samples operations.
 */
public class SampleService extends AbstractService<Sample> {
    /**
     * @param repository      The repository used for the service.
     * @param sampleValidator The validator used for sample validation.
     */
    public SampleService(IRepository<Sample> repository, IValidator<Sample> sampleValidator) {
        super(repository, sampleValidator);
    }

    /**
     * Creates a new sample if is valid and saves it to repository.
     *
     * @param name             The name of the sample.
     * @param assitantRequired The sample requires medical assistance for sampling.
     * @param userThatAdded    The User that registered the sample in the system.
     */

    public void addSample(String name, boolean assitantRequired, User userThatAdded) {
        Sample sample = new Sample(name, assitantRequired, userThatAdded);
        validator.validateBeforeRegistration(sample);
        validateUniqueSample(sample);
        repository.create(sample);
    }

    /**
     * Checks if the name was already given to another sample.
     * throws InvalidSampleException if the name was already given.
     *
     * @param sample The name that a user is associated with.
     */
    private void validateUniqueSample(Sample sample) {
        for (Sample sample1 : repository.read()) {
            if (sample1.getName().trim().equals(sample.getName().trim())) {
                throw new InvalidSampleException("The sample already exists");
            }
        }
    }

    /**
     * Updates a sample from repository.
     * throws WrongIdExcepton if not found.
     *
     * @param name             The name of the sample.
     * @param assitantRequired The sample requires medical assistance for sampling.
     * @param userThatAdded    The User that registered the sample in the system.
     */
    public void updateSample(int id, String name, boolean assitantRequired, User userThatAdded) {
        for (Sample sample : repository.read()) {
            if (sample.getId() == id) {
                sample.setName(name);
                sample.setAssitantRequired(assitantRequired);
                sample.setUserThatAdded(userThatAdded);
                validator.validateBeforeRegistration(sample);
                repository.update(sample);
                return;
            }
        }
        throw new WrongIdException("The Sample Id does not exists");

    }


}

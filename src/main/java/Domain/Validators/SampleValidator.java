package Domain.Validators;

import Domain.Entities.Sample;
import Domain.Exceptions.InvalidSampleException;

/**
 * Represents a Validator which validates Samples
 */
public class SampleValidator implements IValidator<Sample> {
    /**
     * @param entity The Sample to be validated
     *               Throws InvalidSampleException if invalid.
     */
    @Override
    public void validateBeforeRegistration(Sample entity) {
        String messages = "";
        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            messages += "you must enter a name";
        }

        if (!messages.equals("")) {
            throw new InvalidSampleException(messages);
        }

    }

    /**
     * Not required for Sample validation
     */
    @Override
    public void validateAfter(Sample entity) {

    }
}

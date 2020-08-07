package Domain.Validators;

import Domain.Entities.Patient;
import Domain.Exceptions.InvalidPatientException;

/**
 * Represents a Validator which validates Patients
 */

public class PatientValidator implements IValidator<Patient> {
    /**
     * @param entity The patient to be validated before registration.
     *               Throws InvalidPatientException if invalid.
     */
    @Override
    public void validateBeforeRegistration(Patient entity) {
        String messages = "";

        if (entity.getFirstName() == null || entity.getFirstName().trim().isEmpty()) {
            messages += "You must enter a first name\n";
        }
        if (entity.getLastName() == null || entity.getLastName().trim().isEmpty()) {
            messages += "You must enter a last name\n";
        }
        if (entity.getAnalyses() == null || entity.getAnalyses().size() == 0) {
            messages += "you must select at least one analysis\n";
        }
        if (!messages.equals("")) {
            throw new InvalidPatientException(messages);
        }

    }

    /**
     * @param entity The patient to be validated after it was given an diagnostic.
     *               throws InvalidPatientExcetioption if invalid.
     */
    @Override
    public void validateAfter(Patient entity) {
        String messages = "";
        if (entity.getDiagnose().trim().isEmpty() && !entity.getDiagnose().trim().equals("in course")) {
            messages += "You must enter a diagnostic or leave it \"in course\" \n";
        }
        if (!messages.equals("")) throw new InvalidPatientException(messages);
    }

}

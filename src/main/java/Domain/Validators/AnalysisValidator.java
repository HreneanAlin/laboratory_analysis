package Domain.Validators;

import Domain.Entities.Analysis;
import Domain.Exceptions.InvalidAnalysisException;

/**
 * Represents a Validator which validates Analyses
 */
public class AnalysisValidator implements IValidator<Analysis> {
    /**
     * @param entity The Analysis to be validated before registration in the system.
     *               Throws invalidAnalysesException if invalid
     */
    @Override
    public void validateBeforeRegistration(Analysis entity) {
        String messages = "";
        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            messages += "you must enter a name\n";
        }
        if (entity.getPrice() <= 0.0) {
            messages += "The price can't be negative\n";
        }
        if (entity.getLowLimit() <= 0.0 || entity.getLowLimit() > entity.getHighLimit()) {
            messages += "Invalid low limit\n";
        }
        if (entity.getHighLimit() < 0.0 || entity.getHighLimit() < entity.getLowLimit()) {
            messages += "Invalid high limit\n";
        }
        if (entity.getSample() == null) {
            messages += "You must select a sample\n";
        }
        if (!messages.equals("")) {
            throw new InvalidAnalysisException(messages);
        }

    }

    /**
     * @param entity The Analysis to be validated after it were given an result.
     *               throws InvalidAnalysisException if invalid.
     */

    @Override
    public void validateAfter(Analysis entity) {
        String messages = "";
        if (!entity.getScore().equals("in course") && Double.parseDouble(entity.getScore()) <= 0.0) {
            messages += "The result can't be nagative\n";
        }
        if (!messages.equals("")) {
            throw new InvalidAnalysisException(messages);
        }

    }
}

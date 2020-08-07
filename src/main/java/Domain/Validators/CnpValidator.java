package Domain.Validators;


import Domain.Exceptions.InvalidPatientException;

import java.time.LocalDate;

/**
 * Represents a Validator which validates a patient's CNP.
 */
public class CnpValidator {
    /**
     * @param cnp The CNP of a patient to be validated.
     *            throws InvalidPatientException if invalid.
     */
    public void validate(String cnp) {
        String messages = "";
        if (validateCnp(cnp) != null) {
            messages += validateCnp(cnp);
        }
        if (!messages.equals("")) throw new InvalidPatientException(messages);

    }

    /**
     * @param cnp The CNP of a patient to be validated.
     * @return The message to be thrown if invalid or null otherwise.
     */
    private String validateCnp(String cnp) {
        if (cnp.length() != 13) return "The CNP must have 13 digits\n";
        for (int i = 0; i < cnp.length(); i++) {
            if (!Character.isDigit(cnp.charAt(i))) return "The CNP must contains only digits\n";
        }
        char firstNumber = cnp.charAt(0);
        if (!(firstNumber == '1' || firstNumber == '2' || firstNumber == '5' || firstNumber == '6')) {
            return "the first digit is incorrect";
        }
        String messages = validateDateOfBirth(cnp, firstNumber);
        if (messages != null) return messages;

        return null;
    }

    /**
     * @param cnp         The CNP of a patient to be validated.
     * @param firstNumber the first number in the CNP.
     * @return The message to be thrown if invalid or null otherwise.
     */
    private String validateDateOfBirth(String cnp, char firstNumber) {
        String year;
        String month;
        String day;
        if (firstNumber == '1' || firstNumber == '2') {
            year = "19" + cnp.substring(1, 3);
            month = cnp.substring(3, 5);
            day = cnp.substring(5, 7);
            try {
                LocalDate localDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
            } catch (Exception e) {
                return "The part of the CNP corresponding with the date of birth is incorrect";
            }
        } else {
            year = "20" + cnp.substring(1, 3);
            month = cnp.substring(3, 5);
            day = cnp.substring(5, 7);
            try {
                LocalDate localDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                if (localDate.isAfter(LocalDate.now())) {
                    return "The part of the CNP corresponding with the date of birth(year) is incorrect";
                }
            } catch (Exception e) {
                return "The part of the CNP corresponding with the date of birth is incorrect";
            }

        }
        return null;
    }


}

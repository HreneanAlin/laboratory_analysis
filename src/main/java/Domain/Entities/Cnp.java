package Domain.Entities;

import java.time.LocalDate;

/**
 * Represents the CNP of a patient
 */

public class Cnp extends Entity {
    private String value;
    private LocalDate birthdate;

    /**
     * @param value The numeric value of the CNP as String
     */
    public Cnp(String value) {
        this.value = value;
        this.birthdate = determineBirthdate();
    }

    /**
     * @return The numeric value of the CNP as String.
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value The numeric value of the CNP as String.
     */
    public void setValue(String value) {
        this.value = value;
        this.birthdate = determineBirthdate();
    }

    /**
     * @return the birth date of the patient.
     */
    public LocalDate getBirthdate() {
        return birthdate;
    }

    /**
     * @return the birth date of the patient.
     */

    private LocalDate determineBirthdate() {
        char firstNumber = this.value.charAt(0);
        String year;
        String month = value.substring(3, 5);
        String day = value.substring(5, 7);
        if (firstNumber == '1' || firstNumber == '2') {
            year = "19" + value.substring(1, 3);
            day = value.substring(5, 7);
            LocalDate localDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
            return localDate;
        } else {
            year = "20" + value.substring(1, 3);
            LocalDate localDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
            return localDate;
        }
    }

    /**
     * @return null (usages for future purposes)
     */
    @Override
    public Entity clone() {
        return null;
    }
}

package Domain.Entities;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

/**
 * Represents a patients who is entered in the system.
 */
public class Patient extends Entity {
    private String cnp;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private List<Analysis> analyses;
    private String diagnose;
    private double priceToBePaid;
    private boolean isSick;
    private String isSickString;
    private User userThatAdded;
    private int age;
    private LocalDate currentDate;
    private boolean requieresAssitance;
    private User userThatDiagnosed;

    /**
     * @param cnp       the CNP of the patient.
     * @param firstName the fist name of the patient.
     * @param lastName  the last name of the patient.
     * @param birthDate the birth date of the patient.
     * @param analyses  the list of analyses of the patient.
     * @param user      the user that registered the patient in the system.
     */

    public Patient(String cnp, String firstName, String lastName, LocalDate birthDate, List<Analysis> analyses, User user) {
        this.cnp = cnp;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.analyses = analyses;
        this.userThatAdded = user;
        this.age = determineAge();
        this.priceToBePaid = determinePrice();
        this.currentDate = LocalDate.now();

    }

    /**
     * @return the date of registration as LocalDate.
     */
    public LocalDate getCurrentDate() {
        return currentDate;
    }

    /**
     * @return the user that registered the patient in the system.
     */
    public User getUserThatAdded() {
        return userThatAdded;
    }

    /**
     * @param userThatAdded the user that registered the patient in the system.
     */
    public void setUserThatAdded(User userThatAdded) {
        this.userThatAdded = userThatAdded;
    }

    /**
     * @return the CNP of the patient.
     */
    public String getCnp() {
        return cnp;
    }

    /**
     * @param cnp the CNP of the patient.
     */

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    /**
     * @return the first name of the patient as String.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the first name of the patient.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the last name of the patient as String.
     */

    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the last name of the patient.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the birth date of the patient as LocalDate.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * @param birthDate the birth date of the patient.
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        this.age = determineAge();
    }

    /**
     * @return a List that contains patient's analyses.
     */
    public List<Analysis> getAnalyses() {
        return analyses;
    }

    /**
     * @return Whether the patient requires medical assitance for sampling.
     */
    public boolean isRequieresAssitance() {
        return requieresAssitance;
    }

    /**
     * @return "Yes" if requieresAssitance is true and false otherwise.
     */
    public String getRequieresAssitance() {
        if (requieresAssitance) return "Yes";
        return "No";
    }

    /**
     * @param requieresAssitance Whether the patient requires medical assitance for sampling.
     */
    public void setRequieresAssitance(boolean requieresAssitance) {
        this.requieresAssitance = requieresAssitance;
    }

    /**
     * @param analyses the list of analyses of the patient.
     */
    public void setAnalyses(List<Analysis> analyses) {

        this.analyses = analyses;
        this.priceToBePaid = determinePrice();

    }

    /**
     * @return the diagnostic of the patient as String.
     */
    public String getDiagnose() {
        if (this.diagnose == null) return "in course";
        return diagnose;
    }

    /**
     * @param diagnose the diagnostic of the patient.
     */
    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    /**
     * @return the combine prices of the analyses of the patient as double.
     */
    public double getPriceToBePaid() {

        return priceToBePaid;
    }

    /**
     * @return Whether the patient requires futher studies.
     */
    public boolean isSick() {
        return isSick;
    }

    /**
     * @param sick Whether the patient requires futher studies.
     */
    public void setSick(boolean sick) {
        isSick = sick;
        isSickString = Boolean.toString(sick);
    }

    /**
     * @return The boolean value of IsSick as String.
     */
    public String getIsSickString() {
        return isSickString;
    }

    /**
     * @return the age of the patient.
     */
    public int getAge() {
        return age;
    }

    /**
     * @return the user that wrote the diagnostic.
     */
    public User getUserThatDiagnosed() {
        return userThatDiagnosed;
    }

    /**
     * @param userThatDiagnosed the user that wrote the diagnostic.
     */
    public void setUserThatDiagnosed(User userThatDiagnosed) {
        this.userThatDiagnosed = userThatDiagnosed;
    }

    /**
     * @return the age of the patient as Integer.
     */
    private int determineAge() {
        LocalDate today = LocalDate.now();
        Period period = Period.between(this.birthDate, today);
        return period.getYears();
    }

    /**
     * @return the combine prices of the analyses of the patient as double.
     */
    private double determinePrice() {
        double sum = 0.0;
        for (Analysis currentAnalysis : this.analyses) {
            sum += currentAnalysis.getPrice();
        }
        return sum;
    }

    /**
     * @param o An Object the current instance is compared to.
     * @return Whether the current instance is equal to the object.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return Double.compare(patient.getPriceToBePaid(), getPriceToBePaid()) == 0 &&
                isSick() == patient.isSick() &&
                getAge() == patient.getAge() &&
                isRequieresAssitance() == patient.isRequieresAssitance() &&
                getCnp().equals(patient.getCnp()) &&
                getFirstName().equals(patient.getFirstName()) &&
                getLastName().equals(patient.getLastName()) &&
                getBirthDate().equals(patient.getBirthDate()) &&
                getAnalyses().equals(patient.getAnalyses()) &&
                Objects.equals(getDiagnose(), patient.getDiagnose()) &&
                Objects.equals(getIsSickString(), patient.getIsSickString()) &&
                getUserThatAdded().equals(patient.getUserThatAdded()) &&
                getCurrentDate().equals(patient.getCurrentDate()) &&
                Objects.equals(getUserThatDiagnosed(), patient.getUserThatDiagnosed());
    }

    /**
     * @return a hashCode representation of the current instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getCnp(), getFirstName(), getLastName(), getBirthDate(), getAnalyses(), getDiagnose(), getPriceToBePaid(), isSick(), getIsSickString(), getUserThatAdded(), getAge(), getCurrentDate(), isRequieresAssitance(), getUserThatDiagnosed());
    }

    /**
     * used for tests only!
     *
     * @param currentDate the date of registration
     */
    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * @return A string representation of the current instance
     */
    @Override
    public String toString() {
        return "Patient{" +
                "cnp='" + cnp + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", analyses=" + analyses +
                ", diagnose='" + diagnose + '\'' +
                ", priceToBePaid=" + priceToBePaid +
                ", isSick=" + isSick +
                ", isSickString='" + isSickString + '\'' +
                ", userThatAdded=" + userThatAdded +
                ", age=" + age +
                ", currentDate=" + currentDate +
                ", requieresAssitance=" + requieresAssitance +
                ", userThatDiagnosed=" + userThatDiagnosed +
                ", id=" + id +
                '}';
    }

    /**
     * @return null (usages for future purposes)
     */

    @Override
    public Entity clone() {
        return null;
    }
}

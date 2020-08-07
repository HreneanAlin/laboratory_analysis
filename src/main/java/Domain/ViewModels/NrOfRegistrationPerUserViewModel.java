package Domain.ViewModels;

import java.util.Objects;

/**
 * View model for displaying User names plus their number of registration and diagnostics given.
 */

public class NrOfRegistrationPerUserViewModel {
    String userName;
    int nrOfRegistration;
    int nrOfValidation;

    /**
     * @param userName         The name of the user.
     * @param nrOfRegistration the number of registration that user made.
     * @param nrOfValidation   the number of diagnostics that user had given.
     */
    public NrOfRegistrationPerUserViewModel(String userName, int nrOfRegistration, int nrOfValidation) {
        this.userName = userName;
        this.nrOfRegistration = nrOfRegistration;
        this.nrOfValidation = nrOfValidation;
    }

    /**
     * @return The name of the user as String.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName The name of the user.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the number of registration as Interger.
     */

    public int getNrOfRegistration() {
        return nrOfRegistration;
    }

    /**
     * @param nrOfRegistration the number of registration.
     */
    public void setNrOfRegistration(int nrOfRegistration) {
        this.nrOfRegistration = nrOfRegistration;
    }

    /**
     * @return the number of diagnostics that user had given as Integer.
     */
    public int getNrOfValidation() {
        return nrOfValidation;
    }

    /**
     * @param nrOfValidation the number of diagnostics that user had given.
     */
    public void setNrOfValidation(int nrOfValidation) {
        this.nrOfValidation = nrOfValidation;
    }

    /**
     * @param o An Object the current instance is compared to.
     * @return Whether the current instance is equal to the object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NrOfRegistrationPerUserViewModel)) return false;
        NrOfRegistrationPerUserViewModel that = (NrOfRegistrationPerUserViewModel) o;
        return getNrOfRegistration() == that.getNrOfRegistration() &&
                getNrOfValidation() == that.getNrOfValidation() &&
                getUserName().equals(that.getUserName());
    }

    /**
     * @return a hashCode representation of the current instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUserName(), getNrOfRegistration(), getNrOfValidation());
    }

    /**
     * @return A string representation of the current instance
     */
    @Override
    public String toString() {
        return "NrOfRegistrationPerUserViewModel{" +
                "userName='" + userName + '\'' +
                ", nrOfRegistration=" + nrOfRegistration +
                ", nrOfValidation=" + nrOfValidation +
                '}';
    }
}

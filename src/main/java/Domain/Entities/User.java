package Domain.Entities;

import java.util.Objects;

/**
 * Represents a user who uses the aplication
 */

public class User extends Entity {
    String name;
    String password;
    String jobTitle;
    boolean acceptToRegister;
    String acceptToRegisterString;
    boolean acceptToValidate;
    String acceptToValidateString;
    boolean admin;
    String adminString;

    /**
     * @param name             The name that a user is associated with.
     * @param password         The password that a user uses to log in.
     * @param jobTitle         The job that the user have.
     * @param acceptToRegister Clearence to register patients, view and print results (basic level of clearence).
     * @param acceptToValidate Basic level of clearence plus entering results, diagnostics.
     * @param admin            It is given all clearences
     */

    public User(String name, String password, String jobTitle, boolean acceptToRegister,
                boolean acceptToValidate, boolean admin) {
        this.name = name;
        this.password = password;
        this.jobTitle = jobTitle;
        this.acceptToRegister = acceptToRegister;
        this.acceptToRegisterString = Boolean.toString(acceptToRegister);
        this.acceptToValidate = acceptToValidate;
        this.acceptToValidateString = Boolean.toString(acceptToValidate);
        this.admin = admin;
        this.adminString = Boolean.toString(admin);
    }

    /**
     * @return The name of the user as String
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name that a user is associated with.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the user's passwors as String.
     */

    public String getPassword() {
        return password;
    }

    /**
     * @param password The password that a user uses to log in.
     */

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the user's job title as String.
     */

    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * @param jobTitle The job that the user have.
     */

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * @return Whether the user has basic level of access
     */
    public boolean isAcceptToRegister() {
        return acceptToRegister;
    }

    /**
     * @param acceptToRegister Clearence to register patients, view and print results (basic level of clearence).
     */

    public void setAcceptToRegister(boolean acceptToRegister) {
        this.acceptToRegister = acceptToRegister;
        this.acceptToRegisterString = Boolean.toString(acceptToRegister);
    }

    /**
     * @return the boolean value of acceptToRegister as String.
     */

    public String getAcceptToRegisterString() {
        return acceptToRegisterString;
    }

    /**
     * @return Whether the user has clearence for entering results and diagnostics.
     */
    public boolean isAcceptToValidate() {
        return acceptToValidate;
    }

    /**
     * @param acceptToValidate Basic level of clearence plus entering results, diagnostics.
     */

    public void setAcceptToValidate(boolean acceptToValidate) {
        this.acceptToValidate = acceptToValidate;
        this.acceptToValidateString = Boolean.toString(acceptToValidate);
    }

    /**
     * @return the boolean value of acceptToValidate as String.
     */
    public String getAcceptToValidateString() {
        return acceptToValidateString;
    }

    /**
     * @return Whether the user has administrator level of clearence.
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * @param admin It is given all clearences.
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
        this.adminString = Boolean.toString(admin);
    }

    /**
     * @return the boolean value of idAdmin as String.
     */
    public String getAdminString() {
        return adminString;
    }

    /**
     * @return null (usages for future purposes)
     */
    @Override
    public Entity clone() {
        return null;
    }

    /**
     * @return The name of the User as String;
     */

    @Override
    public String toString() {
        return getName();
    }

    /**
     * @param o An Object the current instance is compared to.
     * @return Whether the current instance is equal to the object.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getName().equals(user.getName());
    }

    /**
     * @return a hashCode representation of the current instance.
     */

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
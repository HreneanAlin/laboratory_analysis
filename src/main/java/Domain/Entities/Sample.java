package Domain.Entities;

import java.util.Objects;

/**
 * Represent a sample that is used for an analysis.
 */
public class Sample extends Entity {
    String name;
    boolean assitantRequired;
    String assitantRequiredString;
    User userThatAdded;

    /**
     * @param name             The name of the sample.
     * @param assitantRequired The sample requires medical assistance for sampling.
     * @param userThatAdded    The User that registered the sample in the system.
     */
    public Sample(String name, boolean assitantRequired, User userThatAdded) {
        this.name = name;
        this.assitantRequired = assitantRequired;
        this.assitantRequiredString = Boolean.toString(assitantRequired);
        this.userThatAdded = userThatAdded;
    }

    /**
     * @return The User that registered the sample in the system.
     */
    public User getUserThatAdded() {
        return userThatAdded;
    }

    /**
     * @param userThatAdded The User that registered the sample in the system.
     */
    public void setUserThatAdded(User userThatAdded) {
        this.userThatAdded = userThatAdded;
    }

    /**
     * @return The name of the sample as String.
     */

    public String getName() {
        return name;
    }

    /**
     * @param name The name of the sample.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Whether sample requires medical assistance for sampling.
     */

    public boolean isAssitantRequired() {
        return assitantRequired;
    }


    /**
     * @param assitantRequired The sample requires medical assistance for sampling.
     */
    public void setAssitantRequired(boolean assitantRequired) {
        this.assitantRequired = assitantRequired;
        this.assitantRequiredString = Boolean.toString(assitantRequired);
    }

    /**
     * @return The boolean value of assitantRequired as String.
     */
    public String getAssitantRequiredString() {
        return assitantRequiredString;
    }

    /**
     * @return null (usages for future purposes)
     */

    @Override
    public Entity clone() {
        return null;
    }

    /**
     * @return The name of the Sample as String;
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
        if (!(o instanceof Sample)) return false;
        Sample sample = (Sample) o;
        return isAssitantRequired() == sample.isAssitantRequired() &&
                getName().equals(sample.getName()) &&
                getAssitantRequiredString().equals(sample.getAssitantRequiredString()) &&
                getUserThatAdded().equals(sample.getUserThatAdded());
    }

    /**
     * @return a hashCode representation of the current instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName(), isAssitantRequired(), getAssitantRequiredString());
    }
}

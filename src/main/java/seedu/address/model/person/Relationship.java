package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

// ATTRIBUTION: this code was adapted from the Tag.java class created by Yijin, Liang,
// Yong, Tan, Ullas, Rajapakse and Izq.

/**
 * Represents a Relationship between User and Family Member in the address book.
 * Guarantees: immutable, name is valid as declared in {@Link #isValidRelationship(String)}.
 */
public class Relationship {

    public static final int MAX_LENGTH = 50;
    public static final String MESSAGE_CONSTRAINTS_LENGTH = "Relationships can be at most " + MAX_LENGTH
            + " characters long";
    public static final String MESSAGE_CONSTRAINTS_CHARACTERS = "Relationships should only contain "
            + "alphanumeric characters, spaces, hyphens (-), or apostrophes (').";
    public static final String VALIDATION_REGEX = "[\\p{Alnum}\\s-']+";
    public final String relationship;

    /**
     * Constructs a {@code Relationship}.
     *
     * @param relationship A valid relationship to user.
     */
    public Relationship(String relationship) {
        requireNonNull(relationship);
        isValidRelationship(relationship);
        this.relationship = relationship;
    }
    public String getRelationshipString() {
        return relationship;
    }

    /**
     * Returns true if a given string is a valid relationship.
     */
    public static boolean isValidRelationship(String test) {
        if (test.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_LENGTH);
        }
        if (!test.matches(VALIDATION_REGEX)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_CHARACTERS);
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Relationship)) {
            return false;
        }

        Relationship otherRelationship = (Relationship) other;
        return relationship.equals(otherRelationship.relationship);
    }

    @Override
    public int hashCode() {
        return relationship.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return relationship;
    }
}

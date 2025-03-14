package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's notes in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNotes(String)}
 */
public class Notes {
    public static final String MESSAGE_CONSTRAINTS_LENGTH =
            "Notes can be at most 100 characters long";
    public static final String MESSAGE_CONSTRAINTS_CHARACTERS =
            "Notes should only contain printable characters";
    public static final String VALIDATION_REGEX = "[^\\s].*";
    public static final int MAX_LENGTH = 100;

    public final String value;

    /**
     * Constructs a {@code Notes}.
     *
     * @param notes A valid notes string.
     */
    public Notes(String notes) {
        requireNonNull(notes);
        isValidNotes(notes);
        this.value = notes;
    }

    /**
     * Returns true if a given string is valid notes.
     * Empty string is considered valid as notes are optional.
     */
    public static boolean isValidNotes(String test) {
        if (test.isEmpty()) {
            return true;
        }
        if (test.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_LENGTH);
        }
        if (!test.matches(VALIDATION_REGEX)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_CHARACTERS);
        }
        return true;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Notes)) {
            return false;
        }

        Notes otherNotes = (Notes) other;
        return value.equals(otherNotes.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

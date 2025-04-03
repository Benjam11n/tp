package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's notes in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNotes(String)}.
 */
public class Notes {

    public static final int MAX_LENGTH = 300;
    public static final String MESSAGE_CONSTRAINTS_LENGTH =
            "Notes can be at most " + MAX_LENGTH + " characters long";
    public static final String MESSAGE_CONSTRAINTS_CHARACTERS =
            "Notes can only contain printable ASCII characters and should not be blank";
    public static final String VALIDATION_REGEX = "^[\\x20-\\x7E]+$";

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
     * Validates the given notes.
     *
     * @return true if validation passes.
     * @throws IllegalArgumentException if the notes are invalid.
     */
    public static boolean isValidNotes(String test) {
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

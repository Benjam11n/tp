package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {
    public static final int MAX_LENGTH = 30;
    public static final int MAX_NUM = 10;
    public static final String MESSAGE_CONSTRAINTS_NUM = "Number of tags cannot exceed " + MAX_NUM;
    public static final String MESSAGE_CONSTRAINTS_LENGTH =
            "Tags should be less than " + MAX_LENGTH + " characters long";
    public static final String MESSAGE_CONSTRAINTS_CHARACTERS = "Tags names should be alphanumeric";
    public static final String MESSAGE_DUPLICATE_TAG = "Duplicate tag found (case-insensitive): ";

    public static final String VALIDATION_REGEX = "\\p{Alnum}+";

    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        isValidTagName(tagName);
        this.tagName = tagName;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        if (!test.matches(VALIDATION_REGEX)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_CHARACTERS);
        }
        if (test.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_LENGTH);
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equals(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}

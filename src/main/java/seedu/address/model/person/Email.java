package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's email in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmail(String)}
 */
public class Email {

    public static final int MAX_LENGTH = 254;
    public static final String MESSAGE_CONSTRAINTS_LENGTH = "Emails can be at most " + MAX_LENGTH
            + " characters long";
    private static final String SPECIAL_CHARACTERS = "+_.-";
    public static final String MESSAGE_CONSTRAINTS_CHARACTERS = "Emails should be of the format local-part@domain "
            + "and adhere to the following constraints:\n"
            + "1. The local-part must start and end with an alphanumeric character.\n"
            + "   It may contain alphanumeric characters and the following special characters in between: "
            + "(" + SPECIAL_CHARACTERS + "). Underscores (_) are also allowed.\n"
            + "   However, it must not contain consecutive dots (..), and special characters cannot appear"
            + " consecutively.\n"
            + "2. This is followed by a '@' and then a domain name made up of domain labels separated by periods.\n"
            + "The domain name must:\n"
            + "    - contain at least one period separating domain labels\n"
            + "    - end with a top-level domain (e.g., .com) that is at least 2 alphabetic characters long\n"
            + "    - have each domain label start and end with an alphanumeric character\n"
            + "    - have each domain label consist only of alphanumeric characters or hyphens (-), "
            + "with hyphens not appearing at the start or end.";
    // Allow alphanumerics and _+.- in local-part, but must start/end with alphanum
    private static final String LOCAL_PART_REGEX =
            "[a-zA-Z0-9](?:[a-zA-Z0-9_+.-]*[a-zA-Z0-9])?";

    // No consecutive dots in local-part
    private static final String LOCAL_PART_NO_DOUBLE_DOT_REGEX =
            "^(?!.*\\.\\.).*$";

    // Domain label: starts/ends with alphanum, hyphens allowed internally
    private static final String DOMAIN_LABEL_REGEX =
            "[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?";

    // Domain: multiple labels ending with 2+ letter TLD
    private static final String DOMAIN_REGEX =
            "(" + DOMAIN_LABEL_REGEX + "\\.)+" + "[a-zA-Z]{2,}";

    // Full email pattern
    public static final String VALIDATION_REGEX =
            LOCAL_PART_REGEX + "@" + DOMAIN_REGEX;
    public final String value;

    /**
     * Constructs an {@code Email}.
     *
     * @param email A valid email address.
     */
    public Email(String email) {
        requireNonNull(email);
        isValidEmail(email);
        value = email;
    }

    /**
     * Returns if a given string is a valid email.
     */
    public static boolean isValidEmail(String test) {
        if (test.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_LENGTH);
        }
        if (!test.matches(VALIDATION_REGEX)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_CHARACTERS);
        }

        // Split local-part to check for ("..") to reject
        String localPart = test.substring(0, test.indexOf('@'));
        if (!localPart.matches(LOCAL_PART_NO_DOUBLE_DOT_REGEX)) {
            throw new IllegalArgumentException("Local-part cannot have consecutive dots.");
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
        if (!(other instanceof Email)) {
            return false;
        }

        Email otherEmail = (Email) other;
        return value.equals(otherEmail.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

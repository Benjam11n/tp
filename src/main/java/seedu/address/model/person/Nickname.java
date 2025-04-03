package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's nickname in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNickname(String)}.
 */
public class Nickname {

    public static final int MAX_LENGTH = 50;
    public static final String MESSAGE_CONSTRAINTS_LENGTH =
            "Nicknames should be less than " + MAX_LENGTH + " characters long";
    public static final String MESSAGE_CONSTRAINTS_CHARACTERS =
            "Nicknames can only contain printable ASCII characters and should not be blank";
    public static final String VALIDATION_REGEX = "^[\\x20-\\x7E]+$";

    public final String nickname;

    /**
     * Constructs a {@code Nickname}.
     *
     * @param nickname A valid nickname.
     */
    public Nickname(String nickname) {
        requireNonNull(nickname);
        isValidNickname(nickname);
        this.nickname = nickname;
    }

    /**
     * Validates the nickname and throws an IllegalArgumentException with a specific message if invalid.
     */
    public static boolean isValidNickname(String test) {
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
        return nickname;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Nickname)) {
            return false;
        }
        Nickname otherNickname = (Nickname) other;
        return nickname.equals(otherNickname.nickname);
    }

    @Override
    public int hashCode() {
        return nickname.hashCode();
    }
}

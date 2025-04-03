package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command, try typing 'help' for assistance!";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_CONFIRMATION_REQUIRED = "Invalid command: y to confirm, n to cancel";
    public static final String MESSAGE_DUPLICATE_INDEX = "Duplicate indices are not allowed for this command. %s";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName());

        appendIfNotEmpty(builder, "Phone", person.getPhoneValue());
        appendIfNotEmpty(builder, "Email", person.getEmailValue());
        appendIfNotEmpty(builder, "Address", person.getAddressValue());
        appendIfNotEmpty(builder, "Birthday", person.getBirthdayValue());
        appendIfNotEmpty(builder, "Relationship", person.getRelationshipValue());
        appendIfNotEmpty(builder, "Nickname", person.getNicknameValue());
        appendIfNotEmpty(builder, "Notes", person.getNotesValue());
        appendIfNotEmpty(builder, "Image", person.getImagePathValue());

        Set<Tag> tags = person.getTags();
        if (!tags.isEmpty()) {
            builder.append("; Tags: ");
            tags.forEach(builder::append);
        }

        return builder.toString();
    }

    private static void appendIfNotEmpty(StringBuilder builder, String fieldName, String value) {
        if (!value.isEmpty()) {
            builder.append("; ")
                    .append(fieldName)
                    .append(": ")
                    .append(value);
        }
    }
}

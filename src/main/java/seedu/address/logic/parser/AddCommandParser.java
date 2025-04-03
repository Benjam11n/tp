package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_IMG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NICKNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RELATIONSHIP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Email;
import seedu.address.model.person.ImagePath;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nickname;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {
    public static final String MISSING_COMPULSORY_PARAM = "Missing compulsory parameter: n/NAME";
    public static final String NON_EMPTY_PREAMBLE = "Detected unexpected string between command "
            + "and first valid parameter\n";
    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_BIRTHDAY, PREFIX_RELATIONSHIP, PREFIX_NICKNAME, PREFIX_NOTES, PREFIX_IMG, PREFIX_TAG);
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME)) {
            throw new ParseException(String.format(MISSING_COMPULSORY_PARAM
                    + MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(NON_EMPTY_PREAMBLE
                    + MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                PREFIX_RELATIONSHIP, PREFIX_BIRTHDAY, PREFIX_NICKNAME, PREFIX_NOTES);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());

        // Optional fields
        Optional<Phone> phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE));
        Optional<Email> email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL));
        Optional<Address> address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS));
        Optional<Birthday> birthday = ParserUtil.parseBirthday(argMultimap.getValue(PREFIX_BIRTHDAY));
        Optional<Relationship> relationship = ParserUtil.parseRelationship(argMultimap
                .getValue(PREFIX_RELATIONSHIP));
        Optional<Nickname> nickname = ParserUtil.parseNickname(argMultimap.getValue(PREFIX_NICKNAME));
        Optional<Notes> notes = ParserUtil.parseNotes(argMultimap.getValue(PREFIX_NOTES));
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        Optional<ImagePath> imagePath = ParserUtil.parseImagePath(argMultimap.getValue(PREFIX_IMG));
        Person person = new Person(name, phone, email, address, birthday, relationship, nickname, notes,
                imagePath, tagList);
        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}

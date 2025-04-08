package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT;

import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListCommandParser implements Parser<ListCommand> {
    public static final String ASCENDING_ORDER = "asc";
    public static final String DESCENDING_ORDER = "desc";
    public static final String COMMAND_USAGE = "list : Lists all persons in the address book\n"
            + "Parameters (optional): "
            + PREFIX_SORT + "[SORT_ORDER] (asc/desc)\n"
            + "Example: list " + PREFIX_SORT + "asc";
    private static final Logger logger = LogsCenter.getLogger(AddressBookParser.class);
    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns a ListCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (args == null || trimmedArgs.isEmpty()) {
            return new ListCommand(ListCommand.SortOrder.NONE);
        }
        trimmedArgs = " " + trimmedArgs; // precede with space for tokenizer

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(trimmedArgs, PREFIX_SORT);
        if (!argMultimap.getPreamble().isBlank()) {
            throw new ParseException("Invalid command format.\n" + COMMAND_USAGE);
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_SORT);
        Optional<String> sort = argMultimap.getValue(PREFIX_SORT).map(String::toLowerCase).map(String::trim);

        if (sort.isEmpty()) {
            return new ListCommand(ListCommand.SortOrder.NONE);
        }
        switch (sort.get()) {
        case ASCENDING_ORDER:
            return new ListCommand(ListCommand.SortOrder.ASCENDING);
        case DESCENDING_ORDER:
            return new ListCommand(ListCommand.SortOrder.DESCENDING);
        default:
            throw new ParseException("Invalid sort order!\n" + COMMAND_USAGE);
        }
    }
}

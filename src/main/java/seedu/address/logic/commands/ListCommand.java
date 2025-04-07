package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {
    /**
     * Represents the sort order of the list command.
     */
    public enum SortOrder {
        ASCENDING,
        DESCENDING,
        NONE
    }
    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_SUCCESS = "Listed all persons";
    public static final String MESSAGE_ASC_SUCCESS = "Listed all persons by upcoming birthdays (soonest first)";
    public static final String MESSAGE_DESC_SUCCESS = "Listed all persons by upcoming birthdays (latest first)";
    private final SortOrder sortOrder;

    /**
     * Creates a ListCommand with the default sort order.
     */
    public ListCommand() {
        this.sortOrder = SortOrder.NONE;
    }
    /**
     * Creates a ListCommand with the specified sort order.
     * Extended Constructor for testing purposes
     * @param sortOrder
     */
    public ListCommand(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }
    public SortOrder getSortOrder() {
        return this.sortOrder;
    }

    /**
     * Returns a comparator that compares persons by their upcoming birthday with today's date.
     * @param today current date in LocalDate
     * @param descending true if the comparator should be in descending order, false otherwise
     * @return birthday comparator
     */
    public static Comparator<Person> getBirthdayComparator(LocalDate today, boolean descending) {
        Comparator<Person> comparator = Comparator.comparing(
                person -> person.getBirthday()
                        .map(b -> {
                            LocalDate birthDate = b.getLocalDate();

                            MonthDay birthdayMonthDay = MonthDay.from(birthDate);
                            MonthDay currentMonthDay = MonthDay.from(today);
                            LocalDate nextBirthday = birthdayMonthDay.atYear(today.getYear());
                            if (birthdayMonthDay.isBefore(currentMonthDay)) {
                                nextBirthday = nextBirthday.plusYears(1);
                            }

                            return ChronoUnit.DAYS.between(today, nextBirthday);
                        })
                        .orElse(descending ? Long.MIN_VALUE : Long.MAX_VALUE),
                Comparator.naturalOrder()
        );
        return descending ? comparator.reversed() : comparator;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        if (this.sortOrder == SortOrder.NONE) {
            model.sortFilteredPersonList(null);
            return new CommandResult(MESSAGE_SUCCESS);
        }

        boolean isDescending = this.sortOrder == SortOrder.DESCENDING;
        Comparator<Person> birthdayComparator = getBirthdayComparator(LocalDate.now(), isDescending);
        model.sortFilteredPersonList(birthdayComparator);

        String successMessage = isDescending ? MESSAGE_DESC_SUCCESS : MESSAGE_ASC_SUCCESS;
        return new CommandResult(successMessage);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ListCommand
                && this.sortOrder == ((ListCommand) other).sortOrder);
    }

    @Override
    public int hashCode() {
        return sortOrder.hashCode();
    }
}

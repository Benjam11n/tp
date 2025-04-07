package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new CommandHistory());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), model.getCommandHistory());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_sortNone_success() {
        ListCommand command = new ListCommand(); // default is NONE
        expectedModel.sortFilteredPersonList(null);

        assertCommandSuccess(command, model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_sortAsc_success() {
        ListCommand command = new ListCommand(ListCommand.SortOrder.ASCENDING);

        // Use same date for both command and expectedModel to ensure consistency
        LocalDate fixedToday = LocalDate.now();
        Comparator<Person> comparator = ListCommand.getBirthdayComparator(fixedToday, false);
        expectedModel.sortFilteredPersonList(comparator);

        assertCommandSuccess(command, model, ListCommand.MESSAGE_ASC_SUCCESS, expectedModel);
    }

    @Test
    public void execute_sortDesc_success() {
        ListCommand command = new ListCommand(ListCommand.SortOrder.DESCENDING);

        LocalDate fixedToday = LocalDate.now();
        Comparator<Person> comparator = ListCommand.getBirthdayComparator(fixedToday, true);
        expectedModel.sortFilteredPersonList(comparator);

        assertCommandSuccess(command, model, ListCommand.MESSAGE_DESC_SUCCESS, expectedModel);
    }

    @Test
    public void execute_sortAscWithMissingBirthdays_success() {
        Person noBirthday = new PersonBuilder().withName("NoBday").build();
        model.addPerson(noBirthday);
        expectedModel.addPerson(noBirthday);

        expectedModel.sortFilteredPersonList(
                ListCommand.getBirthdayComparator(LocalDate.now(), false)
        );

        assertCommandSuccess(new ListCommand(ListCommand.SortOrder.ASCENDING),
                model, ListCommand.MESSAGE_ASC_SUCCESS, expectedModel);
    }

    @Test
    public void execute_sortAscWithBirthdayToday_success() {
        Person todayBday = new PersonBuilder().withName("TodayBday")
                .withBirthday(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .build();

        model.addPerson(todayBday);
        expectedModel.addPerson(todayBday);

        expectedModel.sortFilteredPersonList(
                ListCommand.getBirthdayComparator(LocalDate.now(), false)
        );

        assertCommandSuccess(new ListCommand(ListCommand.SortOrder.ASCENDING),
                model, ListCommand.MESSAGE_ASC_SUCCESS, expectedModel);
    }

    @Test
    public void execute_sortDescWithMissingBirthdays_success() {
        Person noBirthday = new PersonBuilder().withName("NoBday").build();
        model.addPerson(noBirthday);
        expectedModel.addPerson(noBirthday);

        expectedModel.sortFilteredPersonList(
                ListCommand.getBirthdayComparator(LocalDate.now(), true)
        );

        assertCommandSuccess(new ListCommand(ListCommand.SortOrder.DESCENDING),
                model, ListCommand.MESSAGE_DESC_SUCCESS, expectedModel);
    }

    @Test
    public void execute_sortDescWithBirthdayToday_success() {
        Person todayBday = new PersonBuilder().withName("TodayBday")
                .withBirthday(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .build();

        model.addPerson(todayBday);
        expectedModel.addPerson(todayBday);

        expectedModel.sortFilteredPersonList(
                ListCommand.getBirthdayComparator(LocalDate.now(), true)
        );

        assertCommandSuccess(new ListCommand(ListCommand.SortOrder.DESCENDING),
                model, ListCommand.MESSAGE_DESC_SUCCESS, expectedModel);
    }

}

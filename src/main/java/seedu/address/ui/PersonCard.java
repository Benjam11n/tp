package seedu.address.ui;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Person;
import seedu.address.model.person.Relationship;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label birthday;
    @FXML
    private Label relationship;
    @FXML
    private Label nickname;
    @FXML
    private Label notes;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        if (person.getNickname().isPresent()) {
            nickname.setText(" (" + person.getNickname().get() + ")");
        } else {
            nickname.setVisible(false);
            nickname.setManaged(false);
        }
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        relationship.setText(person.getRelationship()
                .map(Relationship::getRelationshipString).orElse("No relationship specified"));

        setTextOrHide(birthday, person.getBirthday(), b -> ((Birthday) b).getBirthdayStringFormatted());
        setTextOrHide(notes, person.getNotes(), Object::toString);

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    private void setTextOrHide(Label label, Optional<?> optionalValue, Function<Object, String> mapper) {
        String text = optionalValue.map(mapper).orElse("");
        if (text.isEmpty()) {
            label.setVisible(false);
            label.setManaged(false);
        } else {
            label.setText(text);
        }
    }
}

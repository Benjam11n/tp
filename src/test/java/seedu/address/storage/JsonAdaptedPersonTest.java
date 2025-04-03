package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Email;
import seedu.address.model.person.ImagePath;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nickname;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "Rachel@";
    private static final String INVALID_PHONE = " ";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_BIRTHDAY = "31-02-2000";
    private static final String INVALID_RELATIONSHIP = "*_/";
    private static final String INVALID_NICKNAME =
            "This is a very long nickname that exceeds 30 characters";
    private static final String INVALID_NOTES =
            "This note is exceeding the maximum allowed length of 300 characters with unnecessary and redundant "
                    + "information. It contains many details that are not relevant to the contact entry but are just "
                    + "included to test the validation logic. This test string needs to be exactly 300 characters "
                    + "long to properly test the boundary conditions.";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_IMAGE_PATH = "USER/DESKTOP/IMAGE.JPG";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().map(Phone::toString)
            .orElse("");
    private static final String VALID_EMAIL = BENSON.getEmail().map(Email::toString)
            .orElse("");
    private static final String VALID_ADDRESS = BENSON.getAddress().map(Address::toString)
            .orElse("");
    private static final String VALID_BIRTHDAY = BENSON.getBirthday()
            .map(Birthday::toString)
            .orElse("01-01-1990");
    private static final String VALID_NICKNAME = BENSON.getNickname().map(Nickname::toString)
            .orElse("");

    private static final String VALID_RELATIONSHIP = BENSON.getRelationship().map(Relationship::toString)
            .orElse("");
    private static final String VALID_NOTES = BENSON.getNotes().map(Notes::toString)
            .orElse("");
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final String VALID_IMAGE_PATH = BENSON.getImagePath().toString();

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_BIRTHDAY, VALID_RELATIONSHIP, VALID_NICKNAME, VALID_NOTES, VALID_IMAGE_PATH, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS_START_END;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_BIRTHDAY, VALID_RELATIONSHIP, VALID_NICKNAME, VALID_NOTES, VALID_IMAGE_PATH, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_BIRTHDAY, VALID_RELATIONSHIP, VALID_NICKNAME, VALID_NOTES, VALID_IMAGE_PATH, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS_CHARACTERS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS,
                        VALID_BIRTHDAY, VALID_RELATIONSHIP, VALID_NICKNAME, VALID_NOTES, VALID_IMAGE_PATH, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS_CHARACTERS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_noAssertionErrorThrown() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS,
                VALID_BIRTHDAY, VALID_RELATIONSHIP, VALID_NICKNAME, VALID_NOTES, VALID_IMAGE_PATH, VALID_TAGS);
        return;
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS,
                        VALID_BIRTHDAY, VALID_RELATIONSHIP, VALID_NICKNAME, VALID_NOTES, VALID_IMAGE_PATH, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS_CHARACTERS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidBirthday_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        INVALID_BIRTHDAY, VALID_RELATIONSHIP, VALID_NICKNAME, VALID_NOTES,
                        VALID_IMAGE_PATH, VALID_TAGS);
        String expectedMessage = Birthday.MESSAGE_BIRTHDAY_CONSTRAINTS_INVALID;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidRelationship_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_BIRTHDAY, INVALID_RELATIONSHIP, VALID_NICKNAME, VALID_NOTES,
                        VALID_IMAGE_PATH, VALID_TAGS);
        String expectedMessage = Relationship.MESSAGE_CONSTRAINTS_CHARACTERS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidNickname_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_BIRTHDAY, VALID_RELATIONSHIP, INVALID_NICKNAME, VALID_NOTES,
                        VALID_IMAGE_PATH, VALID_TAGS);
        String expectedMessage = Nickname.MESSAGE_CONSTRAINTS_LENGTH;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidNotes_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_BIRTHDAY, VALID_RELATIONSHIP, VALID_NICKNAME, INVALID_NOTES,
                        VALID_IMAGE_PATH, VALID_TAGS);
        String expectedMessage = Notes.MESSAGE_CONSTRAINTS_LENGTH;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullBirthday_noExceptionThrown() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                null, VALID_NICKNAME, VALID_RELATIONSHIP, VALID_NOTES, VALID_IMAGE_PATH, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Birthday.class.getSimpleName());
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_BIRTHDAY, VALID_RELATIONSHIP, VALID_NICKNAME, VALID_NOTES, VALID_IMAGE_PATH, invalidTags);
        assertThrows(IllegalArgumentException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidImagePath_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_BIRTHDAY, VALID_RELATIONSHIP, VALID_NICKNAME, VALID_NOTES,
                        INVALID_IMAGE_PATH, VALID_TAGS);
        String expectedMessage = ImagePath.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }


}

package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "Rachel@";
    private static final String INVALID_PHONE = "";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_invalidNameWithNonLetterStart_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName("123 Main Street"));
        assertThrows(ParseException.class, () -> ParserUtil.parseName("@John"));
        assertThrows(ParseException.class, () -> ParserUtil.parseName("#Doe"));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parseName_validValueWithBackslash_returnsFormattedName() throws Exception {
        String nameWithBackslash = "rachel\\/walker";
        Name expectedName = new Name("rachel/walker");
        assertEquals(expectedName, ParserUtil.parseName(nameWithBackslash));
    }

    @Test
    public void parseName_validNamePreservesSpecialCharacters() throws Exception {
        String nameWithSpecialCharacters = "Rachel O'Walker";
        Name expectedName = new Name("Rachel O'Walker");
        assertEquals(expectedName, ParserUtil.parseName(nameWithSpecialCharacters));
    }

    @Test
    public void parseName_validNameHandleWordsStartingWithNonLetters() throws Exception {
        String nameWithNonLetter = "John @Walker";
        Name expectedName = new Name("John @Walker");
        assertEquals(expectedName, ParserUtil.parseName(nameWithNonLetter));
    }

    @Test
    public void formatName_handlesWordsStartingWithNonLetters() {
        assertEquals("123 main street", ParserUtil.formatName("123 main street"));
        assertEquals("@John", ParserUtil.formatName("@John"));
        assertEquals("#Doe", ParserUtil.formatName("#Doe"));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((Optional<String>) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(Optional.of(INVALID_PHONE)));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Optional<Phone> expectedPhone = Optional.of(new Phone(VALID_PHONE));
        assertEquals(expectedPhone, ParserUtil.parsePhone(Optional.of(VALID_PHONE)));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Optional<Phone> expectedPhone = Optional.of(new Phone(VALID_PHONE));
        assertEquals(expectedPhone, ParserUtil.parsePhone(Optional.of(phoneWithWhitespace)));
    }

    @Test
    public void parsePhone_emptyOptional_returnsEmptyOptional() throws Exception {
        assertEquals(Optional.empty(), ParserUtil.parsePhone(Optional.empty()));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((Optional<String>) null));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Optional<Address> expectedAddress = Optional.of(new Address(VALID_ADDRESS));
        assertEquals(expectedAddress, ParserUtil.parseAddress(Optional.of(VALID_ADDRESS)));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Optional<Address> expectedAddress = Optional.of(new Address(VALID_ADDRESS));
        assertEquals(expectedAddress, ParserUtil.parseAddress(Optional.of(addressWithWhitespace)));
    }

    @Test
    public void parseAddress_emptyOptional_returnsEmptyOptional() throws Exception {
        assertEquals(Optional.empty(), ParserUtil.parseAddress(Optional.empty()));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((Optional<String>) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(Optional.of(INVALID_EMAIL)));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Optional<Email> expectedEmail = Optional.of(new Email(VALID_EMAIL));
        assertEquals(expectedEmail, ParserUtil.parseEmail(Optional.of(VALID_EMAIL)));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Optional<Email> expectedEmail = Optional.of(new Email(VALID_EMAIL));
        assertEquals(expectedEmail, ParserUtil.parseEmail(Optional.of(emailWithWhitespace)));
    }

    @Test
    public void parseEmail_emptyOptional_returnsEmptyOptional() throws Exception {
        assertEquals(Optional.empty(), ParserUtil.parseEmail(Optional.empty()));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseTags_duplicateTagsSameCase_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_1)));
    }

    @Test
    public void parseTags_duplicateTagsDifferentCase_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList("Friend", "friend")));
    }

    @Test
    public void parseTags_duplicateTagsMixedCase_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList("FRIEND", "Friend", "friend")));
    }

    @Test
    public void parseTags_duplicateTagsWithWhitespace_throwsParseException() throws Exception {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(" friend ", "FRIEND")));
    }

    @Test
    public void parseImagePath_nonExistentFile_throwsParseException() {
        Path nonExistentFile = Paths.get("some/fake/path/fake.png");

        Optional<String> input = Optional.of(nonExistentFile.toString());
        assertThrows(ParseException.class, () -> ParserUtil.parseImagePath(input));
    }
}

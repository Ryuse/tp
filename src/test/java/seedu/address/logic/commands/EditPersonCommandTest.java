package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditPersonCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditPersonCommand.
 */
public class EditPersonCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditPersonCommand editPersonCommand = new EditPersonCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditPersonCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getSortedFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editPersonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getSortedFilteredPersonList().size());
        Person lastPerson = model.getSortedFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).build();
        EditPersonCommand editPersonCommand = new EditPersonCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditPersonCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editPersonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditPersonCommand editPersonCommand = new EditPersonCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        Person editedPerson = model.getSortedFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditPersonCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editPersonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getSortedFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditPersonCommand editPersonCommand = new EditPersonCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editPersonCommand, model, String.format(EditPersonCommand.MESSAGE_DUPLICATE_PERSON,
                EditPersonCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditPersonCommand editPersonCommand = new EditPersonCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editPersonCommand, model, String.format(EditPersonCommand.MESSAGE_DUPLICATE_PERSON,
                EditPersonCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getSortedFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditPersonCommand editPersonCommand = new EditPersonCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editPersonCommand, model, String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                EditPersonCommand.MESSAGE_USAGE));
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditPersonCommand editPersonCommand = new EditPersonCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editPersonCommand, model, String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                EditPersonCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        final EditPersonCommand standardCommand = new EditPersonCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditPersonCommand commandWithSameValues = new EditPersonCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditPersonCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditPersonCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditPersonCommand editPersonCommand = new EditPersonCommand(index, editPersonDescriptor);
        String expected = EditPersonCommand.class.getCanonicalName() + "{targetPersonIndex=" + index
                + ", editPersonDescriptor=" + editPersonDescriptor + "}";
        assertEquals(expected, editPersonCommand.toString());
    }

}

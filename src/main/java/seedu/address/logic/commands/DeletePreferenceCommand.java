package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CommandUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;

/**
 * Deletes a {@code PropertyPreference} from a {@code Person} in the address book.
 * The {@code PropertyPreference} is identified using it's displayed index within the {@code Person}'s preferences,
 * and the {@code Person} is identified using it's displayed index.
 */
public class DeletePreferenceCommand extends Command {

    public static final String COMMAND_WORD = "deletePreference";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a person's property preference."
            + "\nParameters: "
            + "PERSON_INDEX (must be a positive integer) PREFERENCE_INDEX (must be a positive integer)"
            + "\nExample: "
            + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_DELETE_PREFERENCE_SUCCESS = "Deleted Preference: %1$s";

    private final Index targetPersonIndex;
    private final Index targetPreferenceIndex;

    /**
     * Creates a {@code DeletePreferenceCommand} to delete the specified {@code Preference}
     * from the specified {@code Person}.
     *
     * @param targetPersonIndex The index of the person in the filtered person list to delete the preference from.
     * @param targetPreferenceIndex The index of the preference in the person's preference list to delete.
     */
    public DeletePreferenceCommand(Index targetPersonIndex, Index targetPreferenceIndex) {
        requireAllNonNull(targetPersonIndex, targetPreferenceIndex);

        this.targetPersonIndex = targetPersonIndex;
        this.targetPreferenceIndex = targetPreferenceIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person targetPerson = CommandUtil.getValidatedPerson(model, targetPersonIndex, MESSAGE_USAGE);

        // Filter preferences according to active filter tags
        List<PropertyPreference> filteredPreferences = targetPerson.getPropertyPreferences().stream()
                .filter(preference -> model.getSearchContext().matches(preference))
                .toList();

        if (targetPreferenceIndex.getZeroBased() >= filteredPreferences.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_PREFERENCE_DISPLAYED_INDEX,
                    MESSAGE_USAGE));
        }

        // Delete the preference from the full preference list using object reference
        PropertyPreference preferenceToDelete = filteredPreferences.get(targetPreferenceIndex.getZeroBased());
        targetPerson.removePropertyPreference(preferenceToDelete);
        removePropertyPreferenceFromTags(preferenceToDelete, model);

        model.setPerson(targetPerson, targetPerson);
        model.resetAllLists();

        return new CommandResult(String.format(MESSAGE_DELETE_PREFERENCE_SUCCESS,
                Messages.format(targetPerson, preferenceToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeletePreferenceCommand)) {
            return false;
        }

        DeletePreferenceCommand otherDeleteCommand = (DeletePreferenceCommand) other;
        return targetPersonIndex.equals(otherDeleteCommand.targetPersonIndex)
                && targetPreferenceIndex.equals(otherDeleteCommand.targetPreferenceIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetPersonIndex", targetPersonIndex)
                .add("targetPreferenceIndex", targetPreferenceIndex)
                .toString();
    }

    private void removePropertyPreferenceFromTags(PropertyPreference propertyPreference, Model model) {
        Set<Tag> tags = new HashSet<>(propertyPreference.getTags());

        for (Tag tag: tags) {
            tag.removePropertyPreference(propertyPreference);
            model.setTag(tag, tag);
        }
    }
}

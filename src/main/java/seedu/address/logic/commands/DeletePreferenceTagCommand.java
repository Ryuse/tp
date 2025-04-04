package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
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
 * Deletes a {@code Tag} from the {@code PropertyPreference} of a {@code Person} in the address book.
 * The {@code PropertyPreference} is identified using it's displayed index within the {@code Person}'s preferences,
 * and the {@code Person} is identified using it's displayed index.
 */
public class DeletePreferenceTagCommand extends Command {

    public static final String COMMAND_WORD = "deletePreferenceTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes tags from a person's preference."
            + "\nParameters: "
            + "PERSON_INDEX (must be a positive integer) "
            + "PREFERENCE_INDEX (must be a positive integer) "
            + PREFIX_TAG + "TAG..."
            + "\nExample: " + COMMAND_WORD + " 3 1 " + PREFIX_TAG + "pet-friendly " + PREFIX_TAG + "pool";

    public static final String MESSAGE_DELETE_PREFERENCE_TAG_SUCCESS = "Tag(s) in property preference %s deleted: %s";

    private final Index targetPersonIndex;
    private final Index targetPreferenceIndex;
    private final Set<String> tagsToDelete;

    /**
     * Creates a {@code DeletePreferenceTagCommand} to delete the specified {@code Tag}(s)
     * from the specified {@code Preference}.
     *
     * @param targetPersonIndex The index of the person in the filtered person list that the preference is located in.
     * @param targetPreferenceIndex The index of the preference to remove tags from.
     * @param tagsToDelete The set of tag to be deleted from the preference.
     */
    public DeletePreferenceTagCommand(Index targetPersonIndex, Index targetPreferenceIndex, Set<String> tagsToDelete) {
        requireAllNonNull(targetPersonIndex, targetPreferenceIndex);
        requireAllNonNull(tagsToDelete);

        this.targetPersonIndex = targetPersonIndex;
        this.targetPreferenceIndex = targetPreferenceIndex;
        this.tagsToDelete = tagsToDelete;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person targetPerson = CommandUtil.getValidatedPerson(model, targetPersonIndex, MESSAGE_USAGE);
        PropertyPreference targetPreference = CommandUtil.getValidatedPreference(model, targetPerson,
                targetPreferenceIndex, MESSAGE_USAGE, false);

        // Process and validate tags
        Set<Tag> tagsToRemove = getValidatedTags(model, targetPreference);
        // Apply changes
        removeTagsFromPreference(model, targetPerson, targetPreference, tagsToRemove);

        return new CommandResult(String.format(MESSAGE_DELETE_PREFERENCE_TAG_SUCCESS,
                Messages.format(targetPerson, targetPreference), Messages.format(tagsToRemove)));
    }

    /**
     * Gets and validates the tags to be removed.
     */
    private Set<Tag> getValidatedTags(Model model, PropertyPreference preference) throws CommandException {
        Set<Tag> tagsToRemove = new HashSet<>();
        for (String tagName : tagsToDelete) {
            Tag tag = model.getTag(tagName);
            if (!preference.getTags().contains(tag)) {
                throw new CommandException(String.format(Messages.MESSAGE_TAG_NOT_FOUND_IN_PREFERENCE,
                    tagName, MESSAGE_USAGE));
            }
            tagsToRemove.add(tag);
        }
        return tagsToRemove;
    }

    /**
     * Removes the validated tags from the preference and updates the model.
     */
    private void removeTagsFromPreference(Model model, Person targetPerson,
            PropertyPreference preference, Set<Tag> tagsToRemove) {
        for (Tag tag : tagsToRemove) {
            tag.removePropertyPreference(preference);
            model.setTag(tag, tag);
            preference.removeTag(tag);
        }
        model.setPerson(targetPerson, targetPerson);
        model.resetAllLists();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeletePreferenceTagCommand
                && targetPersonIndex.equals(((DeletePreferenceTagCommand) other).targetPersonIndex)
                && targetPreferenceIndex.equals(((DeletePreferenceTagCommand) other).targetPreferenceIndex)
                && tagsToDelete.equals(((DeletePreferenceTagCommand) other).tagsToDelete));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetPersonIndex", targetPersonIndex)
                .add("targetPreferenceIndex", targetPreferenceIndex)
                .add("tagsToDelete", tagsToDelete)
                .toString();
    }
}

package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteOwnerCommand extends Command {

    public static final String COMMAND_WORD = "deleteOwner";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes an owner identified by index number used in the displayed owner list to a specific listing "
            + "identified by index number used in the displayed listing list.\n"
            + "Parameters: LISTING_INDEX (must be a positive integer) OWNER_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_DELETE_OWNER_SUCCESS = "Deleted Owner: %1$s";

    private final Index targetListingIndex;
    private final Index targetOwnerIndex;

    /**
     * Creates a DeleteOwnerCommand to delete the specified {@code Person} from the specified {@code Listing}.
     *
     * @param targetListingIndex Index of the listing in the filtered listing list to delete the owner from.
     * @param targetOwnerIndex Index of the owner in the listing to delete.
     */
    public DeleteOwnerCommand(Index targetListingIndex, Index targetOwnerIndex) {
        this.targetListingIndex = targetListingIndex;
        this.targetOwnerIndex = targetOwnerIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Listing> lastShownList = model.getFilteredListingList();

        if (targetListingIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX);
        }

        Listing targetListing = lastShownList.get(targetListingIndex.getZeroBased());
        List<Person> targetOwnerList = targetListing.getOwners();
        if (targetOwnerIndex.getZeroBased() >= targetOwnerList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_OWNER_DISPLAYED_INDEX);
        }

        //Update listing list and owner list

        Person ownerToDelete = targetOwnerList.get(targetOwnerIndex.getZeroBased());
        removeListingOwnership(ownerToDelete, targetListing, model);

        removeListingsFromPerson(ownerToDelete, targetListing, model);
        return new CommandResult(String.format(MESSAGE_DELETE_OWNER_SUCCESS, Messages.format(ownerToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteOwnerCommand)) {
            return false;
        }

        DeleteOwnerCommand otherDeleteCommand = (DeleteOwnerCommand) other;
        return targetListingIndex.equals(otherDeleteCommand.targetListingIndex)
                && targetOwnerIndex.equals(otherDeleteCommand.targetOwnerIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetListingIndex", targetListingIndex)
                .add("targetOwnerIndex", targetOwnerIndex)
                .toString();
    }

    private void removeListingOwnership(Person owner, Listing listingToDelete, Model model) {
        List<Person> owners = new ArrayList<>(listingToDelete.getOwners());
        owners.remove(owner);

        Listing updatedListing = Listing.of(listingToDelete.getPostalCode(), listingToDelete.getUnitNumber(),
                listingToDelete.getHouseNumber(), listingToDelete.getPriceRange(), listingToDelete.getPropertyName(),
                listingToDelete.getTags(), owners);

        model.setListing(listingToDelete, updatedListing);
    }

    private void removeListingsFromPerson(Person owner, Listing listingToDelete, Model model) {
        List<Listing> listings = new ArrayList<>(owner.getListings());
        listings.remove(listingToDelete);

        Person updatedPerson = new Person(owner.getName(), owner.getPhone(), owner.getEmail(),
                owner.getPropertyPreferences(), listings);

        model.setPerson(owner, updatedPerson);
    }
}

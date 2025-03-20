package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AssignListingCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code AssignListingCommandParser} object.
 */
public class AssignListingCommandParser implements Parser<AssignListingCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AssignListingCommand
     * and returns an AssignListingCommand object for execution.
     *
     * @param args arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AssignListingCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args);
        Index personIndex;
        Index listingIndex;



        try {
            List<Index> multipleIndices = ParserUtil.parseMultipleIndices(argMultimap.getPreamble());
            if (multipleIndices.size() != 2) {
                throw new ParseException("Expected 2 indices");
            }
            personIndex = multipleIndices.get(0);
            listingIndex = multipleIndices.get(1);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignListingCommand.MESSAGE_USAGE),
                    pe);
        }

        return new AssignListingCommand(personIndex, listingIndex);
    }
}

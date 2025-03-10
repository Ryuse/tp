package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.listing.Listing;

/**
 * Panel containing the list of properties.
 */
public class ListingListPanel extends UiPart<Region> {
    private static final String FXML = "ListingListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ListingListPanel.class);

    @FXML
    private ListView<Listing> propertyListView;

    /**
     * Creates a {@code ListingListPanel} with the given {@code ObservableList}.
     */
    public ListingListPanel(ObservableList<Listing> propertyList) {
        super(FXML);
        propertyListView.setItems(propertyList);
        propertyListView.setCellFactory(listView -> new ListingListViewCell());
    }


    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Listing} using a {@code ListingCard}.
     */
    class ListingListViewCell extends ListCell<Listing> {
        @Override
        protected void updateItem(Listing property, boolean empty) {
            super.updateItem(property, empty);

            if (empty || property == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ListingCard(property, getIndex() + 1).getRoot());
            }
        }
    }

}

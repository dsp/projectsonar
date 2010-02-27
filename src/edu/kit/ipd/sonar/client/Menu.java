/**
 * This file is part of Sonar.
 *
 * Sonar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sonar.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.ipd.sonar.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.kit.ipd.sonar.client.event.AttemptLogoutEvent;
import edu.kit.ipd.sonar.client.event.AvailableCentralitiesArrivedEvent;
import edu.kit.ipd.sonar.client.event.AvailableCentralitiesArrivedEventHandler;
import edu.kit.ipd.sonar.client.event.AvailableCentralitiesRequestEvent;
import edu.kit.ipd.sonar.client.event.DrawableGraphRequestEvent;
import edu.kit.ipd.sonar.client.event.ErrorOccuredEvent;
import edu.kit.ipd.sonar.client.event.FinishLoadingEvent;
import edu.kit.ipd.sonar.client.event.StartLoadingEvent;
import edu.kit.ipd.sonar.client.event.SuccessfulAuthenticationEvent;
import edu.kit.ipd.sonar.client.event.SuccessfulAuthenticationEventHandler;
import edu.kit.ipd.sonar.client.event.SuccessfulLogoutEvent;
import edu.kit.ipd.sonar.client.event.SuccessfulLogoutEventHandler;
import edu.kit.ipd.sonar.client.event.UserlistArrivedEvent;
import edu.kit.ipd.sonar.client.event.UserlistArrivedEventHandler;
import edu.kit.ipd.sonar.client.event.UserlistRequestEvent;
import edu.kit.ipd.sonar.client.rpc.DrawableGraphSpecification;
import edu.kit.ipd.sonar.client.rpc.GraphType;
import edu.kit.ipd.sonar.server.User;
import edu.kit.ipd.sonar.server.centralities.Centrality;
import edu.kit.ipd.sonar.client.jsxGraph.JSXGraphDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This class provides a widget where the user can adjust various settings.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 * @author Kevin-Simon Kohlmeyer <kevin-simon.kohlmeyer@student.kit.edu>
 */
public class Menu extends Composite {

    /** Contains localized strings. */
    private static SonarMessages messages
            = (SonarMessages) GWT.create(SonarMessages.class);

    /** The associated Timeline-Object. */
    private Timeline timeline;

    /** True if the user logged is logged in as an admin. */
    private boolean isAdmin = false;

    /** The User-object of the current User. */
    private User currentUser = null;

    /** The label showing the cutoff text. */
    private Label cutoffLabel = new Label();

    /** The text cutoffLabel should show in global mode. */
    private static final String CUTOFF_TEXT_GLOBAL =
        messages.cutoffTextGlobal();

    /** The text cutoffLabel should show in peer mode. */
    private static final String CUTOFF_TEXT_PEER = messages.cutoffTextPeer();

    /** The user can enter a limit in here. */
    private TextBox cutoffTextBox = new TextBox();

    /** The table where the user can select centralities and visualizations. */
    private CentralityTable centralityTable = new CentralityTable();

    /** The admin panel. */
    private AdminPanel adminPanel = new AdminPanel();;

    /** The deckpanel needed to show and hide the menu. */
    private DeckPanel deckPanel = new DeckPanel();

    /**
     * Creates a new Menu object.
     *
     * Registers itself with the event bus.
     *
     * @param timeline The timeline object where the menu can get
     *                 the currently selected timeframe. May not be null.
     */
    public Menu(final Timeline timeline) {
        GWT.log("Menu: constructor called", null);
        if (timeline == null) {
            throw new IllegalArgumentException("Null passed to Menu instead of"
                                               + "a Valid Timeline.");
        }
        this.timeline = timeline;
        initComponents();
        initEventHandlers();
        this.setStyleName("menu");
    }

    /**
     * initializes the menu's components.
     */
    private void initComponents() {

        GWT.log("Menu: initializing gui-objects...", null);

        VerticalPanel vpanel = new VerticalPanel();

        Image showImage = new Image(GWT.getModuleBaseURL()
                                + "images/menushowbutton.png");
        showImage.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent e) {
                showMenu();
            }
        });
        showImage.setStyleName("menuShowImage");
        SimplePanel sp = new SimplePanel();
        sp.setWidget(showImage);
        // This is needed so the menu doesn't maximize vertically, which
        // would cause it to pull the components apart.
        SimplePanel outer = new SimplePanel();
        outer.setWidget(vpanel);
        // Do _not_ change this order or add anything before 'outer'
        deckPanel.add(sp);
        deckPanel.add(outer);
        showMenu();
        initWidget(deckPanel);

        // Top bar
        HorizontalPanel topPanel = new HorizontalPanel();
        topPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        topPanel.add(new Image(GWT.getModuleBaseURL() + "images/logo.png"));
        topPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        Image hideImage = new Image(GWT.getModuleBaseURL()
                    + "images/menuhidebutton.png");
        hideImage.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent e) {
                hideMenu();
            }
        });

        topPanel.add(hideImage);
        topPanel.setWidth("100%");
        vpanel.add(topPanel);

        // Centrality chooser table
        vpanel.add(centralityTable);

        //init admin-panel
        vpanel.add(adminPanel);

        //cutoff:
        Grid cutoffGrid = new Grid(1, 2);
        cutoffGrid.setWidth("100%");
        cutoffGrid.setWidget(0, 0, cutoffLabel);
        cutoffTextBox.setText("1");
        cutoffTextBox.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
        cutoffTextBox.setStyleName("menuCutoffTextBox");
        cutoffGrid.setWidget(0, 1, cutoffTextBox);
        cutoffGrid.getCellFormatter().setHorizontalAlignment(0, 1,
                HasHorizontalAlignment.ALIGN_RIGHT);
        vpanel.add(cutoffGrid);

        //init buttons
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setWidth("100%");
        //logout-button:
        buttonPanel.add(new Button(messages.logout(),
                                   new ClickHandler() {
            public void onClick(final ClickEvent e) {
                EventBus.getHandlerManager().fireEvent(
                        new AttemptLogoutEvent());
            }
        }));
        buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        //Submit-button:
        buttonPanel.add(new Button(messages.confirm(),
                                   new ClickHandler() {
            public void onClick(final ClickEvent e) {
                requestGraph();
            }
        }));
        vpanel.add(buttonPanel);

        CaptionPanel infobox = new CaptionPanel(messages.infoBoxHeader());
        infobox.setStyleName("infobox");
        VerticalPanel infovpanel = new VerticalPanel();
        Label nodeInfoContent = new Label("");
        Label edgeInfoContent = new Label("");
        nodeInfoContent.getElement().setId(JSXGraphDrawer.NODE_TOOLTIP_ID);
        edgeInfoContent.getElement().setId(JSXGraphDrawer.EDGE_TOOLTIP_ID);
        infovpanel.add(nodeInfoContent);
        infovpanel.add(edgeInfoContent);
        infobox.setContentWidget(infovpanel);
        vpanel.add(infobox);
    }

    /**
     * Registers an EventHandler for SuccessfulLoginEvents,
     * UserlistArrivedEvents and AvailableCentralitiesArrivedEvents.
     */
    private void initEventHandlers() {
        EventBus.getHandlerManager().addHandler(
                SuccessfulAuthenticationEvent.TYPE,
                new SuccessfulAuthenticationEventHandler() {
                    public void onSuccessfulAuthentication(
                            final SuccessfulAuthenticationEvent e) {
                        GWT.log("Menu: recieved SuccessfullAuth.Event", null);
                        isAdmin = e.isAdmin();
                        currentUser = e.getUser(); // null if admin
                        if (!isAdmin) {
                            cutoffLabel.setText(Menu.CUTOFF_TEXT_PEER);
                        }
                    }
        });
        GWT.log("Menu: registered Event Handlers", null);
    }

    /**
     * Requests a DrawableGraph with parameters which are read out from
     * various menu components.
     */
    private void requestGraph() {

        //determine the graph type:
        GraphType type;
        if (isAdmin) {
            type = adminPanel.getSelectedGraphType();
        } else {
            type = GraphType.PEER_GRAPH;
        }

        // determine the cutoff:
        int cutoff;
        try {
            cutoff = Integer.parseInt(cutoffTextBox.getText());
        } catch (NumberFormatException e) {
            GWT.log("Menu: Graph Request Failed, wrong Number format.", null);
            EventBus.getHandlerManager().fireEvent(
                    new ErrorOccuredEvent(messages.nonNumCutoffNote()));
            return;
        }

        // determine the right user:
        User user;
        if (isAdmin) {
            if (type == GraphType.GLOBAL_GRAPH) {
                user = null;
            } else {
                user = adminPanel.getSelectedUser();
            }
        } else {
            user = currentUser;
        }

        //determine the selected centralities:
        List<Centrality> selectedCentralities
            = centralityTable.getSelectedCentralities();
        List<VisualizationMethod> selectedVisualizationMethods
            = centralityTable.getSelectedVisualizationMethods();

        GWT.log("Menu: selected centralities: " + selectedCentralities, null);
        GWT.log("Menu: selected visualizations: "
                + selectedVisualizationMethods, null);

        // Check that visualizations are unique.
        for (VisualizationMethod vm : selectedVisualizationMethods) {
            if (selectedVisualizationMethods.indexOf(vm)
                    != selectedVisualizationMethods.lastIndexOf(vm)) {
                EventBus.getHandlerManager().fireEvent(
                    new ErrorOccuredEvent(messages.sameVisMethodSelected()));
                return;
            }
        }

        if (selectedCentralities.isEmpty()
                || selectedVisualizationMethods.isEmpty()) {
            EventBus.getHandlerManager().fireEvent(
                    new ErrorOccuredEvent(messages.noCentralitySelected()));
            return;
        }

        //pack the drawableGraphRequest and send it.
        GWT.log("Menu fires DrawableGraphRequest", null);
        EventBus.getHandlerManager().fireEvent(
            new DrawableGraphRequestEvent(
                new DrawableGraphSpecification(type, cutoff,
                        timeline.getSelectedTimeBoundary(),
                        user, selectedCentralities,
                        selectedVisualizationMethods)));
    }

    /** Hides the menu. */
    private void hideMenu() {
        deckPanel.showWidget(0);
    }

    /** Shows the menu. */
    private void showMenu() {
        deckPanel.showWidget(1);
    }

    /**
     * A row allowing the user to associate centralities with
     * visualization methods.
     */
    private class CentralityRow {

        /** Shows the centralities. */
        private ListBox centralityBox = new ListBox();
        /** Maps the positions in centralityBox to centralities. */
        private HashMap<Integer, Centrality> centralityMap
            = new HashMap<Integer, Centrality>();

        /** Shows the visualizations. */
        private ListBox visualizationBox = new ListBox();
        /** Maps the positions in visualizationBox to VisualizationMethods. */
        private HashMap<Integer, VisualizationMethod> visualizationMap
            = new HashMap<Integer, VisualizationMethod>();

        /** The currently selected Centrality. */
        private Centrality selectedCentrality;

        /** The currently selected Visualization Method. */
        private VisualizationMethod selectedVisualizationMethod;

        /** The centrality table that created us. */
        private CentralityTable centTable;

        /** True if this is the first menu row. */
        private boolean isFirstRow;

        /**
         * Constructor.
         *
         * @param centTable The CentralityTable to report to.
         * @param centralities The centralities to be shown.
         * @param isFirstRow If true, this row has no empty entry and
         *                   exclusively allows node centralities.
         */
        public CentralityRow(final CentralityTable centTable,
                             final List<Centrality> centralities,
                             final boolean isFirstRow) {
            this.centTable = centTable;
            this.isFirstRow = isFirstRow;

            // Build the centrality box
            if (!isFirstRow) {
                centralityBox.insertItem("", 0);
                centralityMap.put(0, null);

                for (int i = 0; i < centralities.size(); i++) {
                    Centrality c = centralities.get(i);

                    centralityBox.insertItem(c.getName(), i + 1);
                    centralityMap.put(i + 1, c);
                }
            } else {
                // We're the first row, so no empty entry and
                // only node centralities
                int i = 0;
                for (Centrality c : centralities) {
                    if (c.getType() == Centrality.Type.NodeCentrality) {
                        centralityBox.insertItem(c.getName(), i);
                        centralityMap.put(i, c);
                        i++;
                    }
                }
            }

            // Add listener to build the visualization box on demand
            centralityBox.addChangeHandler(new ChangeHandler() {
                public void onChange(final ChangeEvent e) {
                    GWT.log("CentralityBox changed event", null);
                    selectedCentralityChanged();
                }
            });

            visualizationBox.addChangeHandler(new ChangeHandler() {
                public void onChange(final ChangeEvent e) {
                    GWT.log("VisualizationBox changed event", null);
                    selectedVisualizationChanged();
                }
            });
        }

        /**
         * Callback for the centralityBox.
         */
        private void selectedCentralityChanged() {
            Centrality oldCentrality = selectedCentrality;
            selectedCentrality
                = centralityMap.get(centralityBox.getSelectedIndex());

            if (oldCentrality == selectedCentrality
                    || (oldCentrality != null
                        && selectedCentrality != null
                        && oldCentrality.getType()
                            == selectedCentrality.getType())) {
                // No change, return
                return;
            }

            if (oldCentrality != null && selectedCentrality == null) {
                // If we selected none but had one before, notify table
                this.centTable.notifySelectedEmpty(this);
            } else if (oldCentrality == null && selectedCentrality != null) {
                // If we selected one and didn't have one before, tell the
                // table to add a fresh row
                this.centTable.notifySelectedNonEmpty(this);
            }

            visualizationBox.clear();
            visualizationMap.clear();

            //if (!this.isFirstRow) {
            //    visualizationBox.insertItem("", 0);
            //    visualizationMap.put(0, null);
            //}

            if (selectedCentrality != null) {
                VisualizationMethod.Type type;

                if (selectedCentrality.getType()
                        == Centrality.Type.NodeCentrality) {
                    type = VisualizationMethod.Type.NODE_VISUALIZATION;
                } else { // selectedCentrality instanceof EdgeCentrality
                    type = VisualizationMethod.Type.EDGE_VISUALIZATION;
                }

                int i = 0;
                for (VisualizationMethod vm : VisualizationMethod.values()) {
                    if (vm.getType() == type) {
                        visualizationBox.insertItem(vm.getName(), i);
                        visualizationMap.put(i, vm);
                        i++;
                    }
                }
            }

            this.visualizationBox.setSelectedIndex(0);
            this.selectedVisualizationChanged();
        }

        /**
         * Callback for the selectionBox.
         */
        private void selectedVisualizationChanged() {
            selectedVisualizationMethod
                = visualizationMap.get(visualizationBox.getSelectedIndex());
        }

        /**
         * Add this row to the table.
         *
         * @param table The table to add to.
         * @param row The row in which we insert ourselves.
         */
        public void addToTable(final FlexTable table, final int row) {
            // Add ourselves
            table.insertRow(row);
            table.setWidget(row, 0, centralityBox);
            table.setWidget(row, 1, visualizationBox);
            // Select the first centrality
            this.centralityBox.setSelectedIndex(0);
            this.selectedCentralityChanged();
        }

        /**
         * Retrieves the selected Centrality.
         *
         * @return The currently selected Centrality.
         */
        public VisualizationMethod getSelectedVisualizationMethod() {
            return selectedVisualizationMethod;
        }

        /**
         * Retrieves the selected Centrality.
         *
         * @return The currently selected Centrality.
         */
        public Centrality getSelectedCentrality() {
            return selectedCentrality;
        }

        /**
         * Checks if the selection is valid.
         *
         * @return Whether the selection in this row is valid.
         */
        public boolean hasUsableSelection() {
            if (selectedCentrality != null
                    && selectedVisualizationMethod != null) {
                // Shouldn't happen, but check anyways
                assert ((selectedVisualizationMethod.getType()
                                == VisualizationMethod.Type.NODE_VISUALIZATION
                            && selectedCentrality.getType()
                                == Centrality.Type.NodeCentrality)
                        || (selectedVisualizationMethod.getType()
                                == VisualizationMethod.Type.EDGE_VISUALIZATION
                            && selectedCentrality.getType()
                                == Centrality.Type.EdgeCentrality));
                return true;
            } else {
                return false;
            }
        }

        /**
         * Checks if the selection of this row is empty.
         *
         * @return If the user selected a value in this row.
         */
        public boolean isEmpty() {
            return selectedCentrality == null;
        }
    }

    /** A FlexTable allowing the user to select which centralities to show. */
    private class CentralityTable extends Composite {

        /** The Flextable this object wraps. */
        private FlexTable table = new FlexTable();

        /** The rows currently in the FlexTable. */
        private List<CentralityRow> rows = new LinkedList<CentralityRow>();
        /** The rows currently not in the FlexTable. */
        private List<CentralityRow> freeRows = new LinkedList<CentralityRow>();

        /**
         * Creates a new Centrality Table.
         */
        public CentralityTable() {
            // We need to get the centralities before we do anything
            final HandlerManager hm = EventBus.getHandlerManager();

            hm.addHandler(AvailableCentralitiesArrivedEvent.TYPE,
                    new AvailableCentralitiesArrivedEventHandler() {
                public void onAvailableCentralitiesArrived(
                        final AvailableCentralitiesArrivedEvent e) {
                    availableCentralitiesArrived(e.getCentralities());
                }
            });

            hm.addHandler(SuccessfulAuthenticationEvent.TYPE,
                    new SuccessfulAuthenticationEventHandler() {
                public void onSuccessfulAuthentication(
                        final SuccessfulAuthenticationEvent e) {
                    hm.fireEvent(new StartLoadingEvent());
                    hm.fireEvent(new AvailableCentralitiesRequestEvent());
                }
            });

            hm.addHandler(SuccessfulLogoutEvent.TYPE,
                    new SuccessfulLogoutEventHandler() {
                public void onSuccessfulLogout(
                        final SuccessfulLogoutEvent e) {
                    forgetAllData();
                }
            });

            table.setStyleName("centralityTable");
            initWidget(table);
        }

        /**
         * Retrieves the selected centralities.
         *
         * @return A list with the selected centralities.
         */
        public List<Centrality> getSelectedCentralities() {
            List<Centrality> cl
                = new ArrayList<Centrality>();
            for (CentralityRow r : rows) {
                if (r.hasUsableSelection()) {
                    cl.add(r.getSelectedCentrality());
                }
            }
            return cl;
        }

        /**
         * Retrieves the selected visualizations.
         *
         * @return A list with the selected visualizations.
         */
        public List<VisualizationMethod> getSelectedVisualizationMethods() {
            List<VisualizationMethod> vl
                = new ArrayList<VisualizationMethod>();
            for (CentralityRow r : rows) {
                if (r.hasUsableSelection()) {
                    vl.add(r.getSelectedVisualizationMethod());
                }
            }
            return vl;
        }

        /**
         * Called when the centralities have arrived.
         *
         * This indicates that we can finish building the table.
         *
         * @param centralities The centralities.
         */
        private void availableCentralitiesArrived(
                final List<Centrality> centralities) {
            GWT.log("got centralities: " + centralities.toString(), null);

            if (!this.rows.isEmpty()) {
                // We already got the centralities, let's just ignore
                // that one.
                return;
            }

            Label zentHeaderLabel = new Label(messages.centrality());
            zentHeaderLabel.setHorizontalAlignment(
                    HasHorizontalAlignment.ALIGN_CENTER);
            this.table.setWidget(0, 0, zentHeaderLabel);
            Label visHeaderLabel = new Label(messages.visualization());
            visHeaderLabel.setHorizontalAlignment(
                    HasHorizontalAlignment.ALIGN_CENTER);
            this.table.setWidget(0, 1, visHeaderLabel);

            // Add a first, fixed row for the needed node centrality and
            // a second empty one for the user to toy with.
            CentralityRow firstRow
                = new CentralityRow(this, centralities, true);
            CentralityRow secondRow
                = new CentralityRow(this, centralities, false);

            this.rows.add(firstRow);
            firstRow.addToTable(table, 1);

            this.rows.add(secondRow);
            secondRow.addToTable(table, 2);

            // Add more to the freelist
            for (int i = 0; i < VisualizationMethod.values().length - 2; i++) {
                this.freeRows.add(new CentralityRow(this, centralities, false));
            }

            EventBus.getHandlerManager().fireEvent(new FinishLoadingEvent());
        }

        /**
         * Remove all data we know from the server.
         *
         * Called on SuccessfulLogoutEvent[s].
         */
        private void forgetAllData() {
            GWT.log("CentralityTable: Logging out, forgetting all rows", null);
            this.rows.clear();
            this.freeRows.clear();
            this.table.removeAllRows();
        }

        /**
         * Notifies the table that the selection went from something to null.
         *
         * The selection in the row was non-empty but has been changed to
         * empty selection.
         *
         * @param row The row whose value changed.
         */
        public void notifySelectedEmpty(final CentralityRow row) {
            // Remove all empty entries
            for (int i = rows.size() - 1; i >= 0; i--) {
                if (this.rows.get(i).isEmpty()) {
                    GWT.log(i + " is empty, remove it", null);
                    GWT.log("calling removeRow(" + i + ")", null);
                    this.table.removeRow(i + 1);
                    this.freeRows.add(rows.remove(i));
                }
            }
            // Add a new empty entry to the end.
            CentralityRow r = freeRows.remove(0);
            r.addToTable(this.table, this.table.getRowCount());
            this.rows.add(r);
        }

        /**
         * Notifies the table that the selection went from null to something.
         *
         * The selection in the row was empty but changed to a non-empty
         * value.
         *
         * @param row The row whose value changed.
         */
        public void notifySelectedNonEmpty(final CentralityRow row) {
            // Ok, the user Selected a new one. Check if we can still add a row
            if (!this.freeRows.isEmpty()) {
                // Add it
                CentralityRow newRow = freeRows.remove(0);
                this.rows.add(newRow);
                newRow.addToTable(this.table, this.table.getRowCount());
            }
        }
    }

    /** A panel where an admin can make various settings. */
    private class AdminPanel extends Composite {

        /** The radio button that says we want a global graph. */
        private RadioButton globalGraphRadioButton
            = new RadioButton("adminPanelGraphType", messages.globalView());

        /** The radio button that says we want a peer graph. */
        private RadioButton peerGraphRadioButton
            = new RadioButton("adminPanelGraphType", messages.peerView());

        /** The list of available users. */
        private List<User> userlist = null;

        /** A list box where we can choose the user in peer mode. */
        private ListBox userBox = new ListBox();

        /** Constructor. */
        public AdminPanel() {
            initComponents();
            initEventHandlers();
        }

        /** Sets up the user interface of the admin panel. */
        private void initComponents() {
            // Set up the structure
            VerticalPanel vPanel = new VerticalPanel();
            Grid userChooserGrid = new Grid(1, 2);
            userChooserGrid.setWidget(0, 0, new Label(messages.user()));
            userChooserGrid.setWidget(0, 1, userBox);
            vPanel.add(new Label(messages.view()));
            vPanel.add(globalGraphRadioButton);
            vPanel.add(peerGraphRadioButton);
            vPanel.add(userChooserGrid);
            initWidget(vPanel);
        }

        /** Sets up the event handlers. */
        private void initEventHandlers() {
            HandlerManager hm = EventBus.getHandlerManager();

            // Login Event
            hm.addHandler(SuccessfulAuthenticationEvent.TYPE,
                          new SuccessfulAuthenticationEventHandler() {
                    public void onSuccessfulAuthentication(
                            final SuccessfulAuthenticationEvent e) {
                        if (e.isAdmin()) {
                            initAdminMode();
                        } else {
                            // Make sure we're hidden
                            setVisible(false);
                        }
                    }
            });
            // Logout event
            hm.addHandler(SuccessfulLogoutEvent.TYPE,
                          new SuccessfulLogoutEventHandler() {
                    public void onSuccessfulLogout(
                            final SuccessfulLogoutEvent e) {
                        logout();
                    }
            });

            // userlist arrived event
            hm.addHandler(UserlistArrivedEvent.TYPE,
                          new UserlistArrivedEventHandler() {
                    public void onUserlistArrived(
                            final UserlistArrivedEvent e) {
                        userlist = e.getUsers();
                        for (User u : userlist) {
                            userBox.addItem(u.getName());
                        }
                    }
            });

            // radiobutton changed event
            this.globalGraphRadioButton.addValueChangeHandler(
                    new ValueChangeHandler<Boolean>() {
                        public void onValueChange(
                                final ValueChangeEvent<Boolean> e) {
                            if (e.getValue()) {
                                userBox.setEnabled(false);
                                // Fascinating, Mr. Spock
                                Menu.this.cutoffLabel.setText(
                                    Menu.CUTOFF_TEXT_GLOBAL);
                            }
                        }
            });
            this.peerGraphRadioButton.addValueChangeHandler(
                    new ValueChangeHandler<Boolean>() {
                        public void onValueChange(
                                final ValueChangeEvent<Boolean> e) {
                            if (e.getValue()) {
                                userBox.setEnabled(true);
                                Menu.this.cutoffLabel.setText(
                                    Menu.CUTOFF_TEXT_PEER);
                            }
                        }
            });
        }

        /** Called when an admin logs in. */
        private void initAdminMode() {
            globalGraphRadioButton.setValue(true, true);
            Menu.this.cutoffLabel.setText(Menu.CUTOFF_TEXT_GLOBAL);
            this.setVisible(true);
            EventBus.getHandlerManager().fireEvent(new UserlistRequestEvent());
        }

        /** Called when we're logging out. Delete all data. */
        private void logout() {
            this.userlist = null;
            this.userBox.clear();
            this.setVisible(false);
            Menu.this.cutoffLabel.setText(Menu.CUTOFF_TEXT_PEER);
        }

        /**
         * Returns the currently selected GraphType.
         *
         * @return Either GraphType.GLOBAL_GRAPH or GraphType.PEER_GRAPH.
         */
        public GraphType getSelectedGraphType() {
            if (globalGraphRadioButton.getValue()) {
                return GraphType.GLOBAL_GRAPH;
            } else if (peerGraphRadioButton.getValue()) {
                return GraphType.PEER_GRAPH;
            } else {
                EventBus.getHandlerManager().fireEvent(
                        new ErrorOccuredEvent(messages.noGraphTypeSelected()));
                return null;
            }
        }

        /**
         * Gets the currently selected user.
         *
         * @return The currently selected user or null in global mode.
         */
        public User getSelectedUser() {
            if (userlist == null) {
                return null;
            } else {
                return userlist.get(userBox.getSelectedIndex());
            }
        }
    }
}

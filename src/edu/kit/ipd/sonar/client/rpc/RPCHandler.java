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
package edu.kit.ipd.sonar.client.rpc;

import edu.kit.ipd.sonar.client.SonarMessages;
import edu.kit.ipd.sonar.client.event.AttemptAuthenticationEvent;
import edu.kit.ipd.sonar.client.event.AttemptAuthenticationEventHandler;
import edu.kit.ipd.sonar.client.event.AttemptLogoutEvent;
import edu.kit.ipd.sonar.client.event.AttemptLogoutEventHandler;
import edu.kit.ipd.sonar.client.event.AvailableCentralitiesArrivedEvent;
import edu.kit.ipd.sonar.client.event.AvailableCentralitiesRequestEvent;
import edu.kit.ipd.sonar.client.event.AvailableCentralitiesRequestEventHandler;
import edu.kit.ipd.sonar.client.event.AvailableTimeBoundaryArrivedEvent;
import edu.kit.ipd.sonar.client.event.AvailableTimeBoundaryRequestEvent;
import edu.kit.ipd.sonar.client.event.AvailableTimeBoundaryRequestEventHandler;
import edu.kit.ipd.sonar.client.event.DatabaseChangedEvent;
import edu.kit.ipd.sonar.client.event.ErrorOccuredEvent;
import edu.kit.ipd.sonar.client.event.FailedAuthenticationEvent;
import edu.kit.ipd.sonar.client.event.GraphArrivedEvent;
import edu.kit.ipd.sonar.client.event.GraphRequestEvent;
import edu.kit.ipd.sonar.client.event.GraphRequestEventHandler;
import edu.kit.ipd.sonar.client.event.SuccessfulAuthenticationEvent;
import edu.kit.ipd.sonar.client.event.SuccessfulLogoutEvent;
import edu.kit.ipd.sonar.client.event.UserlistArrivedEvent;
import edu.kit.ipd.sonar.client.event.UserlistRequestEvent;
import edu.kit.ipd.sonar.client.event.UserlistRequestEventHandler;
import edu.kit.ipd.sonar.server.AuthenticationResult;
import edu.kit.ipd.sonar.server.Graph;
import edu.kit.ipd.sonar.server.TimeBoundary;
import edu.kit.ipd.sonar.server.User;
import edu.kit.ipd.sonar.server.CalculationFailedException;
import edu.kit.ipd.sonar.server.centralities.Centrality;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * The task of this class is to catch all events related to client-server
 * communication, talk to the server, and send events depending
 * on the answer.
 *
 * @author Kevin-Simon Kohlmeyer
 */
public class RPCHandler {
    // Checkstyle: This is no utility class

    /** Contains localized Strings. */
    private SonarMessages messages
            = (SonarMessages) GWT.create(SonarMessages.class);

    /** The service through which we can communicate with the server. */
    private final RPCServiceAsync service;

    /** The HandlerManager through which our events are sent. */
    private final HandlerManager handlerManager;

    /** The last retrieved hash of the database state. */
    private int lastHash = 0;

    /** The time in milli-secounds between checks for database changes. */
    private static final int UPDATE_INTERVAL = 240 * 1000;

    /**
     * Create an RPCHandler object.
     *
     * The rpc handler registers itself with the event manager.
     *
     * @param handlerManager The handlerManager used to communicate with other
     *                       parts of the client.
     * @param service        The RPCServiceAsync used to communicate with the
     *                       server.
     */
    public RPCHandler(final HandlerManager  handlerManager,
                      final RPCServiceAsync service) {
        this.handlerManager = handlerManager;
        this.service = service;

        setUpHandlers();
    }

    /** Sets up the event Handlers. */
    private void setUpHandlers() {
        this.handlerManager.addHandler(AttemptAuthenticationEvent.TYPE,
                new AttemptAuthenticationEventHandler() {
                    public void onAttemptAuthentication(
                            final AttemptAuthenticationEvent e) {
                        if (e.isAdmin()) {
                            service.authenticateAdmin(e.getPassword(),
                                authenticateAdminCallback);
                        } else {
                            service.authenticateUser(e.getUsername(),
                                e.getPassword(),
                                authenticateUserCallback);
                        }
                    }
                });

        this.handlerManager.addHandler(AttemptLogoutEvent.TYPE,
                new AttemptLogoutEventHandler() {
                    public void onAttemptLogout(
                            final AttemptLogoutEvent e) {
                        service.logout(logoutCallback);
                    }
                });

        this.handlerManager.addHandler(AvailableCentralitiesRequestEvent.TYPE,
                new AvailableCentralitiesRequestEventHandler() {
                    public void onAvailableCentralitiesRequest(
                            final AvailableCentralitiesRequestEvent e) {
                        service.getAvailableCentralities(
                            getAvailableCentralitiesCallback);
                    }
                });

        this.handlerManager.addHandler(AvailableTimeBoundaryRequestEvent.TYPE,
                new AvailableTimeBoundaryRequestEventHandler() {
                    public void onAvailableTimeBoundaryRequest(
                            final AvailableTimeBoundaryRequestEvent e) {
                        service.getTimeBoundary(getTimeBoundaryCallback);
                    }
                });

        this.handlerManager.addHandler(GraphRequestEvent.TYPE,
                new GraphRequestEventHandler() {
                    public void onGraphRequest(final GraphRequestEvent e) {
                        GraphSpecification spec = e.getGraphSpecification();
                        ArrayList<Centrality> cents
                                = new ArrayList<Centrality>(
                                        spec.getCentralities());
                        if (spec.getRequestType()
                                                == GraphType.GLOBAL_GRAPH) {
                            service.getGlobalGraph(spec.getTimeBoundary(),
                                    cents, spec.getCutoff(),
                                    new GlobalGraphCallback(spec));
                        } else if (spec.getRequestType()
                                                == GraphType.PEER_GRAPH) {
                            service.getPeerGraph(spec.getUser(),
                                    spec.getTimeBoundary(), cents,
                                    spec.getCutoff(),
                                    new PeerGraphCallback(spec));
                        } else {
                            throw new IllegalArgumentException(
                                        "Neither peer nor global graph");
                        }
                    }
                });

        this.handlerManager.addHandler(UserlistRequestEvent.TYPE,
                    new UserlistRequestEventHandler() {
                        public void onUserlistRequest(
                                final UserlistRequestEvent e) {
                            service.getUserList(getUserListCallback);
                        }
                    });

        /* Regularily check for updates. */
        Timer t = new Timer() {
            @Override
            public void run() {
                service.getStateHash(getStateHashCallback);
            }
        };

        t.scheduleRepeating(UPDATE_INTERVAL);
    }

    /** Callback for authenticateAdmin calls. */
    private AsyncCallback<Boolean> authenticateAdminCallback
            = new AsyncCallback<Boolean>() {
        public void onSuccess(final Boolean successful) {
            GWT.log("RPCHandler: authenticateAdmin called back, got "
                        + successful, null);
            if (successful) {
                fireEvent(new SuccessfulAuthenticationEvent(null, true));
            } else {
                fireEvent(new FailedAuthenticationEvent());
            }
        }

        public void onFailure(final Throwable caught) {
            GWT.log("RPCHandler: authenticateAdmin call failed", caught);
            fireEvent(new ErrorOccuredEvent(
                        messages.rpcAuthenticationFailed()));
        }
    };

    /** Callback for authenticateUser calls. */
    private AsyncCallback<AuthenticationResult> authenticateUserCallback
            = new AsyncCallback<AuthenticationResult>() {
        public void onSuccess(final AuthenticationResult result) {
            GWT.log("RPCHandler: authenticateUser called back, got "
                        + result.toString(), null);
            if (result.isSuccessful()) {
                fireEvent(new SuccessfulAuthenticationEvent(
                                                result.getUser(), false));
            } else {
                fireEvent(new FailedAuthenticationEvent());
            }
        }

        public void onFailure(final Throwable caught) {
            GWT.log("RPCHandler: authenticateUser call failed", caught);
            fireEvent(new ErrorOccuredEvent(
                        messages.rpcAuthenticationFailed()));
        }
    };

    /** Callback for getAvailableCentralities calls. */
    private AsyncCallback<ArrayList<Centrality>>
        getAvailableCentralitiesCallback
                = new AsyncCallback<ArrayList<Centrality>>() {
        public void onSuccess(final ArrayList<Centrality> centralities) {
            GWT.log("RPCHandler: getAvailableCentralities called back, got "
                                        + centralities.toString(), null);
            fireEvent(new AvailableCentralitiesArrivedEvent(centralities));
        }

        public void onFailure(final Throwable caught) {
            GWT.log("RPCHandler: getAvailableCentralities call failed"
                                                                , caught);
            fireEvent(new ErrorOccuredEvent(
                        messages.rpcGetCentralitiesFailed()));
        }
    };

    /** Callback for getTimeBoundary calls. */
    private AsyncCallback<TimeBoundary> getTimeBoundaryCallback
            = new AsyncCallback<TimeBoundary>() {
        public void onSuccess(final TimeBoundary tb) {
            GWT.log("RPCHandler: getTimeBoundard called back, got "
                    + tb.toString(), null);
            fireEvent(new AvailableTimeBoundaryArrivedEvent(tb));
        }

        public void onFailure(final Throwable caught) {
            GWT.log("RPCHandler: getTimeBoundary call failed", caught);
            fireEvent(new ErrorOccuredEvent(
                        messages.rpcGetTimeBoundaryFailed()));
        }
    };

    /** Callback for getUserList calls. */
    private AsyncCallback<ArrayList<User>> getUserListCallback
            = new AsyncCallback<ArrayList<User>>() {
        public void onSuccess(final ArrayList<User> users) {
            GWT.log("RPCHandler: getUserList called back, got "
                    + users.toString(), null);
            fireEvent(new UserlistArrivedEvent(users));
        }

        public void onFailure(final Throwable caught) {
            GWT.log("RPCHandler: getUserList call failed", caught);
            fireEvent(new ErrorOccuredEvent(
                        messages.rpcGetUserListFailed()));
        }
    };

    /** Callback for getGlobalGraph calls. */
    private class GlobalGraphCallback implements AsyncCallback<Graph> {

        /** The spec for the graph we're waiting for. */
        private GraphSpecification spec;

        /**
         * Constructor.
         *
         * @param spec The spec for the graph to come.
         */
        public GlobalGraphCallback(final GraphSpecification spec) {
            this.spec = spec;
        }

        /**
         * Implements func in AsyncCallback.
         *
         * @param g The requested Graph.
         */
        public void onSuccess(final Graph g) {
            fireEvent(new GraphArrivedEvent(g, spec));
        }

        /**
         * Implements func in AsyncCallback.
         *
         * @param caught The throwable that occured.
         */
        public void onFailure(final Throwable caught) {
            GWT.log("RPCHandler: getGlobalGraph call failed", caught);
            try {
                throw caught;
            } catch (CalculationFailedException e) {
                fireEvent(new ErrorOccuredEvent(
                        messages.rpcGetGlobalGraphFailedWithCalcException()));
            } catch (Throwable e) {
                fireEvent(new ErrorOccuredEvent(
                        messages.rpcGetGlobalGraphFailed()));
            }
        }
    }

    /** Callback for getPeerGraph calls. */
    private class PeerGraphCallback implements AsyncCallback<Graph> {

        /** The spec for the graph we're waiting for. */
        private GraphSpecification spec;

        /**
         * Constructor.
         *
         * @param spec The spec for the graph to come.
         */
        public PeerGraphCallback(final GraphSpecification spec) {
            this.spec = spec;
        }

        /**
         * Implements func in AsyncCallback.
         *
         * @param g The graph.
         */
        public void onSuccess(final Graph g) {
            GWT.log("RPCHandler: getPeerGraph called back, got " + g, null);
            fireEvent(new GraphArrivedEvent(g, spec));
        }

        /**
         * Implements func in AsyncCallback.
         *
         * @param caught The throwable that occured.
         */
        public void onFailure(final Throwable caught) {
            GWT.log("RPCHandler: getPeerGraph call failed", caught);
            try {
                throw caught;
            } catch (CalculationFailedException e) {
                fireEvent(new ErrorOccuredEvent(
                        messages.rpcGetPeerGraphFailedWithCalcException()));
            } catch (Throwable e) {
                fireEvent(new ErrorOccuredEvent(
                        messages.rpcGetPeerGraphFailed()));
            }
        }
    }

    /** Callback for getStateHash calls. */
    private AsyncCallback<Integer> getStateHashCallback
            = new AsyncCallback<Integer>() {
        public void onSuccess(final Integer hash) {
            GWT.log("RPCHandler: getStateHash called back, got "
                    + hash, null);
            if (lastHash != hash) {
                lastHash = hash;
                fireEvent(new DatabaseChangedEvent());
            }
        }

        public void onFailure(final Throwable caught) {
            GWT.log("RPCHandler: getStateHash call failed", caught);
            fireEvent(new ErrorOccuredEvent(messages.rpcGetStateHashFailed()));
        }
    };

    /** Callback for logout calls. */
    private AsyncCallback<Void> logoutCallback = new AsyncCallback<Void>() {
        public void onSuccess(final Void v) {
            GWT.log("RPCHandler: logout called back", null);
            fireEvent(new SuccessfulLogoutEvent());
        }

        public void onFailure(final Throwable caught) {
            GWT.log("RPCHandler: logout call failed", caught);
            fireEvent(new ErrorOccuredEvent(messages.rpcLogoutFailed()));
        }
    };

    /**
     * Helper method, delegates the event to the event bus.
     *
     * @param e The event to be fired.
     */
    private void fireEvent(final GwtEvent e) {
        this.handlerManager.fireEvent(e);
    }
}

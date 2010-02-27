/*
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

package edu.kit.ipd.sonar.client.event;

import org.junit.Test;
import static org.junit.Assert.*;

import edu.kit.ipd.sonar.client.event.*;
import edu.kit.ipd.sonar.client.EventBus;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Tests if all kinds of Events are dispatched properly.
 * The intention of this is not to test the EventBus,
 * but the dispatch() and getAssociatedType() methods of the events
 * which are called during the process of distributing them through
 * the EventBus and passing references to their handlers.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 *
 */
public class EventDispatchingTest {

    @Test
    public void testEventFirework() {
        HandlerManager bus = EventBus.getHandlerManager();

        // Register event handlers:

        bus.addHandler(AttemptAuthenticationEvent.TYPE,
                new AttemptAuthenticationEventHandler() {
                    public void onAttemptAuthentication(
                       final AttemptAuthenticationEvent e) {i++;}});

        bus.addHandler(AttemptLogoutEvent.TYPE,
                new AttemptLogoutEventHandler() {
                    public void onAttemptLogout(
                       final AttemptLogoutEvent e) {i++;}});

        bus.addHandler(AvailableCentralitiesArrivedEvent.TYPE,
                new AvailableCentralitiesArrivedEventHandler() {
                    public void onAvailableCentralitiesArrived(
                       final AvailableCentralitiesArrivedEvent e) {i++;}});

        bus.addHandler(AvailableCentralitiesRequestEvent.TYPE,
                new AvailableCentralitiesRequestEventHandler() {
                    public void onAvailableCentralitiesRequest(
                       final AvailableCentralitiesRequestEvent e) {i++;}});

        bus.addHandler(AvailableTimeBoundaryArrivedEvent.TYPE,
                new AvailableTimeBoundaryArrivedEventHandler() {
                    public void onAvailableTimeBoundaryArrived(
                       final AvailableTimeBoundaryArrivedEvent e) {i++;}});

        bus.addHandler(AvailableTimeBoundaryRequestEvent.TYPE,
                new AvailableTimeBoundaryRequestEventHandler() {
                    public void onAvailableTimeBoundaryRequest(
                       final AvailableTimeBoundaryRequestEvent e) {i++;}});

        bus.addHandler(DatabaseChangedEvent.TYPE,
                new DatabaseChangedEventHandler() {
                    public void onDatabaseChanged(
                       final DatabaseChangedEvent e) {i++;}});

        bus.addHandler(DrawableGraphArrivedEvent.TYPE,
                new DrawableGraphArrivedEventHandler() {
                    public void onDrawableGraphArrived(
                       final DrawableGraphArrivedEvent e) {i++;}});

        bus.addHandler(DrawableGraphRequestEvent.TYPE,
                new DrawableGraphRequestEventHandler() {
                    public void onDrawableGraphRequest(
                       final DrawableGraphRequestEvent e) {i++;}});

        bus.addHandler(ErrorOccuredEvent.TYPE,
            new ErrorOccuredEventHandler() {
                public void onErrorOccured(
                   final ErrorOccuredEvent e) {i++;}});

        bus.addHandler(FailedAuthenticationEvent.TYPE,
                new FailedAuthenticationEventHandler() {
                    public void onFailedAuthentication(
                       final FailedAuthenticationEvent e) {i++;}});

        bus.addHandler(FinishLoadingEvent.TYPE,
                new FinishLoadingEventHandler() {
                    public void onFinishLoading(
                       final FinishLoadingEvent e) {i++;}});

        bus.addHandler(GraphArrivedEvent.TYPE,
                new GraphArrivedEventHandler() {
                    public void onGraphArrived(
                       final GraphArrivedEvent e) {i++;}});

        bus.addHandler(GraphRequestEvent.TYPE,
                new GraphRequestEventHandler() {
                    public void onGraphRequest(
                       final GraphRequestEvent e) {i++;}});

        bus.addHandler(StartLoadingEvent.TYPE,
                new StartLoadingEventHandler() {
                    public void onStartLoading(
                       final StartLoadingEvent e) {i++;}});

        bus.addHandler(SuccessfulAuthenticationEvent.TYPE,
                new SuccessfulAuthenticationEventHandler() {
                    public void onSuccessfulAuthentication(
                       final SuccessfulAuthenticationEvent e) {i++;}});

        bus.addHandler(SuccessfulLogoutEvent.TYPE,
                new SuccessfulLogoutEventHandler() {
                    public void onSuccessfulLogout(
                       final SuccessfulLogoutEvent e) {i++;}});

        bus.addHandler(UserlistArrivedEvent.TYPE,
                new UserlistArrivedEventHandler() {
                    public void onUserlistArrived(
                       final UserlistArrivedEvent e) {i++;}});

        bus.addHandler(UserlistRequestEvent.TYPE,
                new UserlistRequestEventHandler() {
                    public void onUserlistRequest(
                       final UserlistRequestEvent e) {i++;}});


        bus.fireEvent(new AttemptAuthenticationEvent(false, null, null));
        bus.fireEvent(new AttemptLogoutEvent());
        bus.fireEvent(new AvailableCentralitiesArrivedEvent(null));
        bus.fireEvent(new AvailableCentralitiesRequestEvent());
        bus.fireEvent(new AvailableTimeBoundaryArrivedEvent(null));
        bus.fireEvent(new AvailableTimeBoundaryRequestEvent());
        bus.fireEvent(new DatabaseChangedEvent());
        bus.fireEvent(new DrawableGraphArrivedEvent(null));
        bus.fireEvent(new DrawableGraphRequestEvent(null));
        bus.fireEvent(new ErrorOccuredEvent(null));
        bus.fireEvent(new FailedAuthenticationEvent());
        bus.fireEvent(new FinishLoadingEvent());
        bus.fireEvent(new GraphArrivedEvent(null, null));
        bus.fireEvent(new GraphRequestEvent(null));
        bus.fireEvent(new StartLoadingEvent());
        bus.fireEvent(new SuccessfulAuthenticationEvent(null, false));
        bus.fireEvent(new SuccessfulLogoutEvent());
        bus.fireEvent(new UserlistArrivedEvent(null));
        bus.fireEvent(new UserlistRequestEvent());

        assertEquals("not all events arrived at their handlers", 19, i);

    }

    private int i = 0;

}

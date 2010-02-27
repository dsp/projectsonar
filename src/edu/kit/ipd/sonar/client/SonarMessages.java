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

import com.google.gwt.i18n.client.Messages;

import edu.kit.ipd.sonar.server.Node;

/**
 * This interfaces is used to retrieve localized strings from properties files.
 *
 * @author Kevin-Simon Kohlmeyer <kevin-simon.kohlmeyer@student.kit.edu>
 */
public interface SonarMessages extends Messages {
    /** @return A label for a field where the user can input a name. */
    String userNameLabel();

    /** @return A label for a field where the user should input his password. */
    String passwordLabel();

    String loginButtonText();

    /** @return A pseudo-username for an administrator. */
    String administratorName();

    /** @return A message saying that the user entered wrong credentials. */
    String wrongCredentialsNote();

    String visualizationNameColor();
    String visualizationNameDistance();
    String visualizationNameSize();
    String visualizationNameLinewidth();

    String logout();
    String confirm();
    String infoBoxHeader();
    String nonNumCutoffNote();
    String sameVisMethodSelected();
    String noCentralitySelected();
    String centrality();
    String visualization();
    String globalView();
    String peerView();
    String user();
    String view();
    String noGraphTypeSelected();
    String cutoffTextGlobal();
    String cutoffTextPeer();

    String errorWhileConvertingNodes();
    String errorWhileConvertingEdges();
    String edgeFromTo(Node source, Node destination);
    String name();
    String errorUnknownVisualizationMethod();

    String anErrorOccured();
    String proceed();

    String rpcAuthenticationFailed();
    String rpcGetCentralitiesFailed();
    String rpcGetTimeBoundaryFailed();
    String rpcGetUserListFailed();
    String rpcGetPeerGraphFailed();
    String rpcGetGlobalGraphFailed();
    String rpcGetPeerGraphFailedWithCalcException();
    String rpcGetGlobalGraphFailedWithCalcException();
    String rpcGetStateHashFailed();
    String rpcLogoutFailed();
}

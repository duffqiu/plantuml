/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 11153 $
 *
 */
package net.sourceforge.plantuml.skin.bluemodern;

import java.awt.Font;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.skin.rose.ComponentRoseDestroy;
import net.sourceforge.plantuml.skin.rose.ComponentRoseGroupingElse;
import net.sourceforge.plantuml.skin.rose.ComponentRoseGroupingSpace;
import net.sourceforge.plantuml.skin.rose.ComponentRoseReference;
import net.sourceforge.plantuml.skin.rose.ComponentRoseTitle;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UFont;

public class BlueModern implements Skin {

	private final UFont bigFont = new UFont("SansSerif", Font.BOLD, 20);
	private final UFont participantFont = new UFont("SansSerif", Font.PLAIN, 17);
	private final UFont normalFont = new UFont("SansSerif", Font.PLAIN, 13);
	private final UFont smallFont = new UFont("SansSerif", Font.BOLD, 11);

	private final HtmlColor blue1 = HtmlColorUtils.getColorIfValid("#527BC6");
	private final HtmlColor blue2 = HtmlColorUtils.getColorIfValid("#D1DBEF");
	private final HtmlColor blue3 = HtmlColorUtils.getColorIfValid("#D7E0F2");

	private final HtmlColor red = HtmlColorUtils.getColorIfValid("#A80036");

	private final HtmlColor lineColor = HtmlColorUtils.getColorIfValid("#989898");
	private final HtmlColor borderGroupColor = HtmlColorUtils.getColorIfValid("#BBBBBB");

	public Component createComponent(ComponentType type, ArrowConfiguration config, ISkinParam param,
			Display stringsToDisplay) {

		if (type.isArrow()) {
			final HtmlColor sequenceArrow = config.getColor() == null ? HtmlColorUtils.BLACK : config.getColor();
			if (config.isSelfArrow()) {
				return new ComponentBlueModernSelfArrow(sequenceArrow, HtmlColorUtils.BLACK, normalFont,
						stringsToDisplay, config, param);
			}
			return new ComponentBlueModernArrow(sequenceArrow, HtmlColorUtils.BLACK, normalFont, stringsToDisplay,
					config, param);
		}
		if (type == ComponentType.PARTICIPANT_HEAD) {
			return new ComponentBlueModernParticipant(blue1, blue2, HtmlColorUtils.WHITE, participantFont,
					stringsToDisplay, param);
		}
		if (type == ComponentType.PARTICIPANT_TAIL) {
			return new ComponentBlueModernParticipant(blue1, blue2, HtmlColorUtils.WHITE, participantFont,
					stringsToDisplay, param);
		}
		if (type == ComponentType.PARTICIPANT_LINE) {
			return new ComponentBlueModernLine(lineColor);
		}
		if (type == ComponentType.CONTINUE_LINE) {
			return new ComponentBlueModernLine(lineColor);
		}
		if (type == ComponentType.ACTOR_HEAD) {
			return new ComponentBlueModernActor(blue2, blue1, blue1, participantFont, stringsToDisplay, true, param);
		}
		if (type == ComponentType.ACTOR_TAIL) {
			return new ComponentBlueModernActor(blue2, blue1, blue1, participantFont, stringsToDisplay, false, param);
		}
		if (type == ComponentType.NOTE) {
			return new ComponentBlueModernNote(HtmlColorUtils.WHITE, HtmlColorUtils.BLACK, HtmlColorUtils.BLACK,
					normalFont, stringsToDisplay, param);
		}
		if (type == ComponentType.ALIVE_BOX_CLOSE_CLOSE) {
			return new ComponentBlueModernActiveLine(blue1, true, true);
		}
		if (type == ComponentType.ALIVE_BOX_CLOSE_OPEN) {
			return new ComponentBlueModernActiveLine(blue1, true, false);
		}
		if (type == ComponentType.ALIVE_BOX_OPEN_CLOSE) {
			return new ComponentBlueModernActiveLine(blue1, false, true);
		}
		if (type == ComponentType.ALIVE_BOX_OPEN_OPEN) {
			return new ComponentBlueModernActiveLine(blue1, false, false);
		}
		if (type == ComponentType.DELAY_LINE) {
			return new ComponentBlueModernDelayLine(lineColor);
		}
		if (type == ComponentType.DELAY_TEXT) {
			return new ComponentBlueModernDelayText(HtmlColorUtils.BLACK,
					param.getFont(FontParam.SEQUENCE_DELAY, null), stringsToDisplay, param);
		}
		if (type == ComponentType.DESTROY) {
			return new ComponentRoseDestroy(red);
		}
		if (type == ComponentType.GROUPING_HEADER) {
			return new ComponentBlueModernGroupingHeader(blue1, blue3, borderGroupColor, HtmlColorUtils.WHITE,
					HtmlColorUtils.BLACK, normalFont, smallFont, stringsToDisplay, param);
		}
		if (type == ComponentType.GROUPING_ELSE) {
			return new ComponentRoseGroupingElse(HtmlColorUtils.BLACK, HtmlColorUtils.BLACK, smallFont,
					stringsToDisplay.get(0), param, blue3, Rose.getStroke(param, LineParam.sequenceGroupBorder, 2));
		}
		if (type == ComponentType.GROUPING_SPACE) {
			return new ComponentRoseGroupingSpace(7);
		}
		if (type == ComponentType.TITLE) {
			return new ComponentRoseTitle(HtmlColorUtils.BLACK, bigFont, stringsToDisplay, param);
		}
		if (type == ComponentType.REFERENCE) {
			return new ComponentRoseReference(HtmlColorUtils.BLACK, HtmlColorUtils.WHITE, normalFont, borderGroupColor,
					blue1, blue3, normalFont, stringsToDisplay, HorizontalAlignment.CENTER, param, 0, Rose.getStroke(
							param, LineParam.sequenceDividerBorder, 2));
		}
		if (type == ComponentType.NEWPAGE) {
			return new ComponentBlueModernNewpage(blue1);
		}
		if (type == ComponentType.DIVIDER) {
			return new ComponentBlueModernDivider(HtmlColorUtils.BLACK, normalFont, blue2, blue1, HtmlColorUtils.BLACK,
					stringsToDisplay, param);
		}
		if (type == ComponentType.SIGNATURE) {
			return new ComponentRoseTitle(HtmlColorUtils.BLACK, smallFont, Display.asList("This skin was created ",
					"in April 2009."), param);
		}
		if (type == ComponentType.ENGLOBER) {
			return new ComponentBlueModernEnglober(blue1, blue3, stringsToDisplay, HtmlColorUtils.BLACK, param.getFont(
					FontParam.SEQUENCE_BOX, null), param);
		}

		return null;

	}

	public Object getProtocolVersion() {
		return 1;
	}

}

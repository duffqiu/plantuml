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
 * Revision $Revision: 4749 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.PlacementStrategy;
import net.sourceforge.plantuml.ugraphic.PlacementStrategyVisibility;
import net.sourceforge.plantuml.ugraphic.PlacementStrategyY1Y2Center;
import net.sourceforge.plantuml.ugraphic.PlacementStrategyY1Y2Left;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULayoutGroup;

public class MethodsOrFieldsArea implements TextBlockWidth, TextBlock {

	private final UFont font;
	private final ISkinParam skinParam;
	private final HtmlColor color;
	private final Rose rose = new Rose();
	private final List<Member> members = new ArrayList<Member>();
	private final HorizontalAlignment align;

	public MethodsOrFieldsArea(List<Member> members, FontParam fontParam, ISkinParam skinParam) {
		this(members, fontParam, skinParam, HorizontalAlignment.LEFT);
	}

	public MethodsOrFieldsArea(List<Member> members, FontParam fontParam, ISkinParam skinParam,
			HorizontalAlignment align) {
		this.align = align;
		this.skinParam = skinParam;
		this.font = skinParam.getFont(fontParam, null);
		this.color = rose.getFontColor(skinParam, fontParam);
		this.members.addAll(members);
	}

	private boolean hasSmallIcon() {
		if (skinParam.classAttributeIconSize() == 0) {
			return false;
		}
		for (Member m : members) {
			if (m.getVisibilityModifier() != null) {
				return true;
			}
		}
		return false;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		double smallIcon = 0;
		if (hasSmallIcon()) {
			smallIcon = skinParam.getCircledCharacterRadius() + 3;
		}
		double x = 0;
		double y = 0;
		for (Member m : members) {
			final TextBlock bloc = createTextBlock(m);
			final Dimension2D dim = bloc.calculateDimension(stringBounder);
			x = Math.max(dim.getWidth(), x);
			y += dim.getHeight();
		}
		x += smallIcon;
		return new Dimension2DDouble(x, y);
	}

	private TextBlock createTextBlock(Member m) {
		final boolean withVisibilityChar = skinParam.classAttributeIconSize() == 0;
		final String s = m.getDisplay(withVisibilityChar);
		FontConfiguration config = new FontConfiguration(font, color);
		if (m.isAbstract()) {
			config = config.italic();
		}
		if (m.isStatic()) {
			config = config.underline();
		}
		final TextBlock bloc = TextBlockUtils.create(Display.getWithNewlines(s), config, align, skinParam);
		return new TextBlockTracer(m, bloc);
	}

	static class TextBlockTracer implements TextBlock {

		private final TextBlock bloc;
		private final Url url;

		public TextBlockTracer(Member m, TextBlock bloc) {
			this.bloc = bloc;
			this.url = m.getUrl();
		}

		public void drawU(UGraphic ug) {
			if (url != null) {
				ug.startUrl(url);
			}
			bloc.drawU(ug);
			if (url != null) {
				ug.closeAction();
			}
		}

		public Dimension2D calculateDimension(StringBounder stringBounder) {
			final Dimension2D dim = bloc.calculateDimension(stringBounder);
			return dim;
		}

	}

	private TextBlock getUBlock(final VisibilityModifier modifier) {
		if (modifier == null) {
			return new TextBlock() {

				public void drawU(UGraphic ug) {
				}

				public Dimension2D calculateDimension(StringBounder stringBounder) {
					return new Dimension2DDouble(1, 1);
				}
			};
		}
		final HtmlColor back = modifier.getBackground() == null ? null : rose.getHtmlColor(skinParam,
				modifier.getBackground());
		final HtmlColor fore = rose.getHtmlColor(skinParam, modifier.getForeground());

		final TextBlock uBlock = modifier.getUBlock(skinParam.classAttributeIconSize(), fore, back);
		return uBlock;
	}

	public TextBlock asTextBlock(final double widthToUse) {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				MethodsOrFieldsArea.this.drawU(ug);
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return MethodsOrFieldsArea.this.calculateDimension(stringBounder);
			}
		};
	}

	public void drawU(UGraphic ug) {
		final Dimension2D dim = calculateDimension(ug.getStringBounder());
		final ULayoutGroup group;
		if (hasSmallIcon()) {
			group = new ULayoutGroup(new PlacementStrategyVisibility(ug.getStringBounder(),
					skinParam.getCircledCharacterRadius() + 3));
			for (Member att : members) {
				final TextBlock bloc = createTextBlock(att);
				final VisibilityModifier modifier = att.getVisibilityModifier();
				group.add(getUBlock(modifier));
				group.add(bloc);
			}
		} else {
			final PlacementStrategy placementStrategy;
			if (align == HorizontalAlignment.LEFT) {
				placementStrategy = new PlacementStrategyY1Y2Left(ug.getStringBounder());
			} else if (align == HorizontalAlignment.CENTER) {
				placementStrategy = new PlacementStrategyY1Y2Center(ug.getStringBounder());
			} else {
				throw new IllegalStateException();
			}
			group = new ULayoutGroup(placementStrategy);
			for (Member att : members) {
				final TextBlock bloc = createTextBlock(att);
				group.add(bloc);
			}
		}
		group.drawU(ug, 0, 0, dim.getWidth(), dim.getHeight());
	}
}

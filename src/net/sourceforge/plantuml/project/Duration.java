/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 6104 $
 *
 */
package net.sourceforge.plantuml.project;

class Duration implements Numeric {

	private final long minutes;

	public Duration(long minutes) {
		this.minutes = minutes;
	}

	public Duration(NumericNumber value) {
		this(value.getIntValue() * 24L * 60 * 60);
	}

	public Numeric add(Numeric other) {
		return new Duration(((Duration) other).minutes + minutes);
	}

	public static Duration of(long days) {
		return new Duration(days * 24 * 60 * 60);
	}

	public NumericType getNumericType() {
		return NumericType.DURATION;
	}

	public long getMinutes() {
		return minutes;
	}

	@Override
	public String toString() {
		return "DURATION:" + minutes / (24 * 60 * 60);
	}

	public int compareTo(Numeric other) {
		final Duration this2 = (Duration) other;
		if (this2.minutes > minutes) {
			return -1;
		}
		if (this2.minutes < minutes) {
			return 1;
		}
		return 0;
	}

}
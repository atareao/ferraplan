/*
 * TransformUtil
 *
 * File created on 05-ene-2010
 * Copyright (c) 2009 Lorenzo Carbonell
 * email: lorenzo.carbonell.cerezo@gmail.com
 * website: http://www.atareao.es
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.atareao.ferraplan.gui;

import java.awt.geom.AffineTransform;
import org.apache.batik.parser.AWTTransformProducer;
import org.apache.batik.parser.TransformListParser;
import org.w3c.dom.Element;

/**
 *
 * @author Jonathan Wood
 */

public final class TransformUtil {
    //
    //********************************CONSTANTES********************************
    //
    private static final TransformListParser p = new TransformListParser();
    private static final AWTTransformProducer tp = new AWTTransformProducer();
    //
    // *********************************CAMPOS*********************************
    //

    //
    //******************************CONSTRUCTORES*******************************
    //

    //
    //********************************METODOS***********************************
    //

    public static final AffineTransform getAffineTransform(Element element) {
        p.setTransformListHandler(tp);
        p.parse(element.getAttributeNS(null, "transform"));
        return tp.getAffineTransform();
    }

    public static final Element translate(Element element, double x, double y) {
        AffineTransform at = getAffineTransform(element);
        at = translate(at, x, y);
        element.setAttributeNS(null, "transform", getTransform(at));
        return element;
    }

    public static final Element scale(Element element, double scale) {
        AffineTransform at = getAffineTransform(element);
        at = scale(at, scale);
        element.setAttributeNS(null, "transform", getTransform(at));
        return element;
    }

    public static final Element scale(Element element, double x, double y) {
        AffineTransform at = getAffineTransform(element);
        at = scale(at, x, y);
        element.setAttributeNS(null, "transform", getTransform(at));
        return element;
    }

    public static final Element rotate(Element element, double angle) {
        AffineTransform at = getAffineTransform(element);
        at = rotate(at, angle);
        element.setAttributeNS(null, "transform", getTransform(at));
        return element;
    }
    public static final Element rotate_fc(Element element, double angle) {
        AffineTransform at = getAffineTransform(element);
        at = rotate_fc(at, angle);
        element.setAttributeNS(null, "transform", getTransform(at));
        return element;
    }

    public static final Element rotate(Element element, double x, double y) {
        AffineTransform at = getAffineTransform(element);
        at = rotate(at, x, y);
        element.setAttributeNS(null, "transform", getTransform(at));
        return element;
    }

    public static final Element rotate(Element element, double x, double y,
double ax, double ay) {
        AffineTransform at = getAffineTransform(element);
        at = rotate(at, x, y, ax, ay);
        element.setAttributeNS(null, "transform", getTransform(at));
        return element;
    }
    public static final Element flip_horizontal(Element element) {
        AffineTransform at = getAffineTransform(element);
        at = scale(at, -1.0d, 1.0d);
        element.setAttributeNS(null, "transform", getTransform(at));
        return element;
    }
    public static final Element flip_vertical(Element element) {
        AffineTransform at = getAffineTransform(element);
        at = scale(at, 1.0d, -1.0d);
        element.setAttributeNS(null, "transform", getTransform(at));
        return element;
    }

    public static final AffineTransform rotate(AffineTransform at, double
angle) {
        at.concatenate(AffineTransform.getRotateInstance(angle));
        return at;
    }
    public static final AffineTransform rotate_fc(AffineTransform at, double
angle) {
        at.concatenate(AffineTransform.getRotateInstance(angle*Math.PI/180.0d,100.0d,100.0d));
        return at;
    }

    public static final AffineTransform rotate(AffineTransform at, double x,
double y) {
        at.concatenate(AffineTransform.getRotateInstance(x, y));
        return at;
    }

    public static final AffineTransform rotate(AffineTransform at, double x,
double y, double ax, double ay) {
        at.concatenate(AffineTransform.getRotateInstance(x, y, ax, ay));
        return at;
    }

    public static final AffineTransform scale(AffineTransform at, double scale)
{
        at.concatenate(AffineTransform.getScaleInstance(scale, scale));
        return at;
    }

    public static final AffineTransform scale(AffineTransform at, double x,
double y) {
        at.concatenate(AffineTransform.getScaleInstance(x, y));
        return at;
    }

    public static final AffineTransform translate(AffineTransform at, double x,
double y) {
        at.concatenate(AffineTransform.getTranslateInstance(x, y));
        return at;
    }

    public static final Element shear(Element element, double x, double y) {
        AffineTransform at = getAffineTransform(element);
        at = shear(at, x, y);
        element.setAttributeNS(null, "transform", getTransform(at));
        return element;
    }

    public static final AffineTransform shear(AffineTransform at, double x,
double y) {
        at.concatenate(AffineTransform.getShearInstance(x, y));
        return at;
    }

    public static final String getTransform(AffineTransform at) {
        double[] matrix = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        at.getMatrix(matrix);
        StringBuilder sb = new StringBuilder();
        sb.append("matrix(");
        sb.append(matrix[0]);
        sb.append(" ");
        sb.append(matrix[1]);
        sb.append(" ");
        sb.append(matrix[2]);
        sb.append(" ");
        sb.append(matrix[3]);
        sb.append(" ");
        sb.append(matrix[4]);
        sb.append(" ");
        sb.append(matrix[5]);
        sb.append(")");
        return sb.toString();
    }
    /*
     *@author atareao
     *
     */
    public static final AffineTransform flip_horizontal(AffineTransform at) {
        at.concatenate(AffineTransform.getScaleInstance(-1.0d, 1.0d));
        return at;
    }
    public static final AffineTransform flip_vertical(AffineTransform at) {
        at.concatenate(AffineTransform.getScaleInstance(1.0d, -1.0d));
        return at;
    }
    //
    //**************************METODOS AUXILIARES******************************
    //

    //
    //**************************METODOS DE ACCESO*******************************
    //
}

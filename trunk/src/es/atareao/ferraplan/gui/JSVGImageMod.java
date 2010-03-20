/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JSVGImage.java
 *
 * Created on 06-dic-2009, 7:27:40
 */

package es.atareao.ferraplan.gui;

import es.atareao.alejandria.gui.ErrorDialog;
import es.atareao.alejandria.gui.JImage;
import org.apache.batik.transcoder.TranscoderException;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author atareao
 */
public class JSVGImageMod extends JImage {
    //
    //********************************CONSTANTES********************************
    //


    //
    //******************************CONSTRUCTORES*******************************
    //
    /** Creates new form JSVGImage */
    public JSVGImageMod() {

    }
    //
    //********************************METODOS***********************************
    //

    //
    //**************************METODOS AUXILIARES******************************
    //

    //
    // *********************************CAMPOS*********************************
    //
    private SVGDocument _svgdoc;
    /**
     * @return the _svgdoc
     */
    public SVGDocument getSvgdoc() {
        return _svgdoc;
    }
    //
    //**************************METODOS DE ACCESO*******************************
    //
    /**
     * @param svgdoc the _svgdoc to set
     */
    public void setSvgdoc(SVGDocument svgdoc) {
        this._svgdoc = svgdoc;
        if(svgdoc!=null){
            SVGRasterizer svgr = new SVGRasterizer(svgdoc);
            if(svgr!=null){
                try {
                    this.setBufferedImage(svgr.createBufferedImage());
                    this.repaint();
                } catch (TranscoderException ex) {
                    ErrorDialog.manejaError(ex);
                }
            }
        }
    }
}

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

import es.atareao.alejandria.gui.JImage;
import es.atareao.ferraplan.lib.Forma;

/**
 *
 * @author atareao
 */
public class JForma extends JImage {
    //public class JForma extends JSVGImage {
    //
    //********************************CONSTANTES********************************
    //


    //
    //******************************CONSTRUCTORES*******************************
    //
    /** Creates new form JSVGImage */
    public JForma() {
        //this.setAdjustImage(true);

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
    private Forma _forma;
    //
    //**************************METODOS DE ACCESO*******************************
    //

    /**
     * @return the _forma
     */
    public Forma getForma() {
        return _forma;
    }

    /**
     * @param forma the _forma to set
     */
    public void setForma(Forma forma) {
        if(forma!=null){
            this._forma = forma;
            if(forma.getUrl()!=null){
                this.setResourceUrl(forma.getUrl());
                this.setScaleHeight(0.6);
                this.setScaleWidth(0.6);
            }
            /*
            if(forma.getSVGText()!=null){
                this.setSvgtext(forma.getSVGText());
            }
             * 
             */
        }
    }
}

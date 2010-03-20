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
import java.io.IOException;
import java.io.StringReader;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author atareao
 */
public class JSVGImage extends JImage {
    //
    //********************************CONSTANTES********************************
    //


    //
    //******************************CONSTRUCTORES*******************************
    //
    /** Creates new form JSVGImage */
    public JSVGImage() {

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
    private String _svgtext;
    private boolean _simmetric;
    private double _angle;
    //
    private SVGDocument getSVGDocument(String text) throws IOException{
        if(text!=null){
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            StringReader r = new StringReader(text);
            return f.createSVGDocument(null,r);
        }
        return null;
    }

    /**
     * @return the _svgtext
     */
    public String getSvgtext() {
        return _svgtext;
    }

    /**
     * @param svgtext the _svgtext to set
     */
    public void setSvgtext(String svgtext) {
        if((svgtext!=null)&&(svgtext.length()>0)){
            try {
                this._svgtext = svgtext.replace("`", "\"");
                SVGDocument svgdoc = this.getSVGDocument(this._svgtext);
                if(svgdoc!=null){
                    SVGRasterizer svgr = new SVGRasterizer(svgdoc);
                    if(svgr!=null){
                        this.setBufferedImage(svgr.createBufferedImage());
                        this.repaint();
                    }
                }
            } catch (TranscoderException ex) {
                ErrorDialog.manejaError(ex);
            } catch (IOException ex) {
                ErrorDialog.manejaError(ex);
            }
        }
    }

    /**
     * @return the _simmetric
     */
    public boolean isSimmetric() {
        return _simmetric;
    }

    /**
     * @param simmetric the _simmetric to set
     */
    public void setSimmetric(boolean simmetric) {
        this._simmetric = simmetric;
    }

    /**
     * @return the _angle
     */
    public double getAngle() {
        return _angle;
    }

    /**
     * @param angle the _angle to set
     */
    public void setAngle(double angle) {
        this._angle = angle;
    }
    //
    //**************************METODOS DE ACCESO*******************************
    //

}

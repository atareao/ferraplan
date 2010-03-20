/*
 * SVGRasterizer
 *
 * File created on 18-dic-2009
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

import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Map;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.w3c.dom.svg.SVGDocument;


/**
 *
 * @author atareao
 */

public class SVGRasterizer {
    //
    //********************************CONSTANTES********************************
    //


    //
    //******************************CONSTRUCTORES*******************************
    //
    /**
     * Constructs a new SVGRasterizer.
     *
     * @param uri the uri of the document to rasterize
     */
    public SVGRasterizer(String uri) {
        this.setInput(new TranscoderInput(uri));
    }
 /**
     * Constructs a new SVGRasterizer.
     *
     * @param url the URL of the document to rasterize
     */
    public SVGRasterizer(URL url) {
        this.setInput(new TranscoderInput(url.toString()));
    }

    /**
     * Constructs a new SVGRasterizer converter.
     *
     * @param istream the input stream that represents the SVG document to
     * rasterize
     */
    public SVGRasterizer(InputStream istream) {
        this.setInput(new TranscoderInput(istream));
    }

    /**
     * Constructs a new SVGRasterizer converter.
     *
     * @param reader the reader that represents the SVG document to rasterize
     */
    public SVGRasterizer(Reader reader) {
        this.setInput(new TranscoderInput(reader));
    }

    /**
     * Constructs a new SVGRasterizer converter.
     *
     * @param document the SVG document to rasterize
     */
    public SVGRasterizer(SVGDocument document) {
        this.setInput(new TranscoderInput(document));
    }


    //
    //********************************METODOS***********************************
    //
/**
     * Returns the image that represents the SVG document.
     */
    public BufferedImage createBufferedImage() throws TranscoderException {
        Rasterizer r = new Rasterizer();
        r.setTranscodingHints((Map)this.getHints());
        r.transcode(this.getInput(), null);
        return this.getImg();
    }

    /**
     * Sets the width of the image to rasterize.
     *
     * @param width the image width
     */
    public void setImageWidth(float width) {
        this.getHints().put(ImageTranscoder.KEY_WIDTH, new Float(width));
    }

    /**
     * Sets the height of the image to rasterize.
     *
     * @param width the image height
     */
    public void setImageHeight(float height) {
        this.getHints().put(ImageTranscoder.KEY_HEIGHT, new Float(height));
    }

    /**
     * Sets the preferred language to use. SVG documents can provide text in
     * multiple languages, this method lets you control which language to use
     * if possible. e.g. "en" for english or "fr" for french.
     *
     * @param language the preferred language to use
     */
    public void setLanguages(String language) {
        this.getHints().put(ImageTranscoder.KEY_LANGUAGE, language);
    }

    /**
     * Sets the unit conversion factor to the specified value. This method
     * lets you choose how units such as 'em' are converted. e.g. 0.26458 is
     * 96dpi (the default) or 0.3528 is 72dpi.
     *
     * @param px2mm the pixel to millimeter convertion factor.
     */
    public void setPixelToMMFactor(float px2mm) {
        this.getHints().put(ImageTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, new Float(px2mm));
    }

    /**
     * Sets the uri of the user stylesheet. The user stylesheet can be used to
     * override styles.
     *
     * @param uri the uri of the user stylesheet
     */
    public void setUserStyleSheetURI(String uri) {
        this.getHints().put(ImageTranscoder.KEY_USER_STYLESHEET_URI, uri);
    }

    /**
     * Sets whether or not the XML parser used to parse SVG document should be
     * validating or not, depending on the specified parameter. For futher
     * details about how media work, see the
     * <a href="http://www.w3.org/TR/CSS2/media.html">Media types in the CSS2
     * specification</a>.
     *
     * @param b true means the XML parser will validate its input
     */
    public void setXMLParserValidating(boolean b) {
        this.getHints().put(ImageTranscoder.KEY_XML_PARSER_VALIDATING,
                  (b ? Boolean.TRUE : Boolean.FALSE));
    }

    /**
     * Sets the media to rasterize. The medium should be separated by
     * comma. e.g. "screen", "print" or "screen, print"
     *
     * @param media the media to use
     */
    public void setMedia(String media) {
        this.getHints().put(ImageTranscoder.KEY_MEDIA, media);
    }

    /**
     * Sets the alternate stylesheet to use. For futher details, you can have
     * a look at the <a href="http://www.w3.org/TR/xml-stylesheet/">Associating
     * Style Sheets with XML documents</a>.
     *
     * @param alternateStylesheet the alternate stylesheet to use if possible
     */
    public void setAlternateStylesheet(String alternateStylesheet) {
        this.getHints().put(ImageTranscoder.KEY_ALTERNATE_STYLESHEET,
                  alternateStylesheet);
    }

    /**
     * Sets the Paint to use for the background of the image.
     *
     * @param p the paint to use for the background
     */
    public void setBackgroundColor(Paint p) {
        this.getHints().put(ImageTranscoder.KEY_BACKGROUND_COLOR, p);
    }

    


    //
    //**************************METODOS AUXILIARES******************************
    //
/**
     * An image transcoder that stores the resulting image.
     */
    protected class Rasterizer extends ImageTranscoder {

        @Override
        public BufferedImage createImage(int w, int h) {
            return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        }

        @Override
        public void writeImage(BufferedImage img, TranscoderOutput output)
            throws TranscoderException {
            SVGRasterizer.this.setImg(img);
        }
    }

    /**
     * The image that represents the SVG document.
     */
    private TranscoderInput _input;
    private TranscodingHints _hints = new TranscodingHints();
    private BufferedImage _img;

    //
    //**************************METODOS DE ACCESO*******************************
    //
/**
     * @return the _input
     */
    public TranscoderInput getInput() {
        return _input;
    }

    /**
     * @param input the _input to set
     */
    public void setInput(TranscoderInput input) {
        this._input = input;
    }

    /**
     * @return the _hints
     */
    public TranscodingHints getHints() {
        return _hints;
    }

    /**
     * @param hints the _hints to set
     */
    public void setHints(TranscodingHints hints) {
        this._hints = hints;
    }

    /**
     * @return the _img
     */
    public BufferedImage getImg() {
        return _img;
    }

    /**
     * @param img the _img to set
     */
    public void setImg(BufferedImage img) {
        this._img = img;
    }
}

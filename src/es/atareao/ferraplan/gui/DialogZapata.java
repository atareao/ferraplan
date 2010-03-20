/*
 * MarcoDiametro.java
 *
 * TODO: Descripcion
 *
 * Creado en 21 de diciembre de 2010, 12:11
 *
 * Copyright (C) 31 de diciembre de 2006, Protactino
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */


package es.atareao.ferraplan.gui;
//
//********************************IMPORTACIONES*********************************
//
import com.eteks.parser.CompilationException;
import com.eteks.parser.CompiledFunction;
import com.eteks.parser.FunctionParser;
import es.atareao.alejandria.gui.ErrorDialog;
import es.atareao.alejandria.lib.Convert;
import es.atareao.alejandria.lib.FileUtils;
import es.atareao.alejandria.lib.StringUtils;
import es.atareao.ferraplan.lib.Barra;
import es.atareao.ferraplan.lib.Diametro;
import es.atareao.ferraplan.lib.Forma;
import es.atareao.ferraplan.lib.Revision;
import es.atareao.alejandria.val.gui.GuiPositiveValidator;
import es.atareao.ferraplan.lib.Acero;
import es.atareao.ferraplan.lib.Anclaje;
import es.atareao.ferraplan.lib.Anclaje.Esfuerzo;
import es.atareao.ferraplan.lib.Anclaje.Posicion;
import es.atareao.ferraplan.lib.Anclaje.Tipo;
import es.atareao.ferraplan.lib.Hormigon;
import es.atareao.ferraplan.lib.Parte;
import es.atareao.queensboro.db.Condition;
import java.awt.Component;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.border.TitledBorder;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;


/**
 *
 * @author  Protactino
 */
public class DialogZapata extends javax.swing.JDialog {
    //
    //********************************CONSTANTES********************************
    //
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    //
    public final static int OP_ADD=0;
    public final static int OP_EDIT=1;
    public final static int OP_DELETE=2;
    public final static int OP_VIEW=3;
    //
    //******************************CONSTRUCTORES*******************************
    //
    /**
     * Creates new form MarcoEmpresa
     * @param padre 
     * @param operacion 
     * @param empresa 
     * @throws java.sql.SQLException 
     */
    public DialogZapata(JFrame padre, Revision revision){
        super(padre,true);
        initComponents();
        this.setSize(600, 930);
        this.setLocationRelativeTo(null);
        this._revision=revision;
        try {
            Diametro diametro = new Diametro(revision.getConector());
            Vector<Diametro>diametros=diametro.findAll();
            this.jArmaduraInferiorAnchoDiametro.setElements(diametros);
            this.jArmaduraInferiorLargoDiametro.setElements(diametros);
            this.jArmaduraSuperiorAnchoDiametro.setElements(diametros);
            this.jArmaduraSuperiorLargoDiametro.setElements(diametros);
            this.jArmaduraPilarAnchoDiametro.setElements(diametros);
            this.jArmaduraPilarLargoDiametro.setElements(diametros);
            this.jArmaduraPilarEsquinasDiametro.setElements(diametros);
            this.jArmaduraPilarEstribosDiametro.setElements(diametros);
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex);
        }
        try {
            _svgdoc = this.getSVGDocument(FileUtils.readTextFromJar("/es/atareao/ferraplan/img/zapata_ink.svg"));
            this.dibuja();
        } catch (IOException ex) {
            ErrorDialog.manejaError(ex);
        }
    }
    
    public DialogZapata(Revision revision) throws SQLException{
        this(new JFrame(),revision);
    }
    private void addBarra(String referencia, String forma_id,Diametro diametro,double cantidad,String comentario,double a,double b,double c,double d,double e,double f,double g,double h,double i,double j){
        try {
            Barra barra = new Barra(this._revision.getConector());
            barra.setValue("revision_id",this._revision.getId());
            barra.setValue("referencia",referencia);
            barra.setValue("forma_id",forma_id);
            barra.setValue("diametro_id",diametro.getId());
            barra.setValue("cantidad", Convert.toString(cantidad));
            barra.setValue("comentario", comentario);
            barra.setValue("a", Convert.toString(a));
            barra.setValue("b", Convert.toString(b));
            barra.setValue("c", Convert.toString(c));
            barra.setValue("d", Convert.toString(d));
            barra.setValue("e", Convert.toString(e));
            barra.setValue("f", Convert.toString(f));
            barra.setValue("g", Convert.toString(g));
            barra.setValue("h", Convert.toString(h));
            barra.setValue("i", Convert.toString(i));
            barra.setValue("j", Convert.toString(j));
            //
            Forma fm=new Forma(this._revision.getConector());
            fm.setId(forma_id);
            fm.read();
            double peso_diametro=Convert.toDouble(diametro.getValue("peso"));
            if(fm!=null){
                String formula=fm.getValue("formula");
                double longitud=calcula_longitud(formula,a,b,c,d,e,f,g,h,i,j);
                double peso_unitario=peso_diametro*longitud/100;
                double peso_total=peso_unitario*cantidad;
                barra.setValue("longitud", Convert.toString(longitud));
                barra.setValue("peso_unitario", Convert.toString(peso_unitario));
                barra.setValue("peso_total", Convert.toString(peso_total));
            }
            //
            barra.insert();
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex);
        }

    }
    private double multiplo_cinco(double valor){
        return Math.round(valor/5.0)*5.0;
    }
    private double calcula_longitud(String formula,double a,double b,double c,double d,double e,double f,double g,double h,double i,double j){
        String function="f(a,b,c,d,e,f,g,h,i,j) = "+formula;
        double[] valores={a,b,c,d,e,f,g,h,i,j};
        try {
            FunctionParser parser = new FunctionParser ();
            CompiledFunction cfunction = parser.compileFunction(function);
            return cfunction.computeFunction (valores);
        } catch (CompilationException ex) {
            ErrorDialog.manejaError(ex,false);
        }
        return 0;
    }

    private SVGDocument getSVGDocument(String text) throws IOException{
        if(text!=null){
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            StringReader r = new StringReader(text.replace("`", "\"").trim());
            return f.createSVGDocument(null,r);
        }
        return null;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jSVGImageMod1 = new es.atareao.ferraplan.gui.JSVGImageMod();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jArmaduraInferior = new javax.swing.JCheckBox();
        jPatillasArmaduraInferior = new javax.swing.JCheckBox();
        jArmaduraSuperior = new javax.swing.JCheckBox();
        jPatillasArmaduraSuperior = new javax.swing.JCheckBox();
        jPilar = new javax.swing.JCheckBox();
        jPatillasPilar = new javax.swing.JCheckBox();
        jEstribos = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jAncho = new es.atareao.alejandria.gui.JNumericField();
        jLargo = new es.atareao.alejandria.gui.JNumericField();
        jAlto = new es.atareao.alejandria.gui.JNumericField();
        jPanel18 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jRecubrimiento = new es.atareao.alejandria.gui.JNumericField();
        jPanel14 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jTipoPilar = new javax.swing.JComboBox();
        jAnchoPilar = new es.atareao.alejandria.gui.JNumericField();
        jLargoPilar = new es.atareao.alejandria.gui.JNumericField();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jArmaduraInferiorAnchoDiametro = new es.atareao.queensboro.gui.JWrapperComboBox();
        jArmaduraInferiorAnchoReparto = new es.atareao.alejandria.gui.JNumericField();
        jPanel9 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jArmaduraInferiorLargoDiametro = new es.atareao.queensboro.gui.JWrapperComboBox();
        jArmaduraInferiorLargoReparto = new es.atareao.alejandria.gui.JNumericField();
        jPanel10 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jArmaduraInferiorLongitudPatillas = new es.atareao.alejandria.gui.JNumericField();
        jPanel6 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jArmaduraSuperiorAnchoDiametro = new es.atareao.queensboro.gui.JWrapperComboBox();
        jArmaduraSuperiorAnchoReparto = new es.atareao.alejandria.gui.JNumericField();
        jPanel12 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jArmaduraSuperiorLargoDiametro = new es.atareao.queensboro.gui.JWrapperComboBox();
        jArmaduraSuperiorLargoReparto = new es.atareao.alejandria.gui.JNumericField();
        jPanel13 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jArmaduraSuperiorLongitudPatillas = new es.atareao.alejandria.gui.JNumericField();
        jPanel7 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jArmaduraPilarAnchoNumero = new es.atareao.alejandria.gui.JNumericField();
        jArmaduraPilarAnchoDiametro = new es.atareao.queensboro.gui.JWrapperComboBox();
        jImage1 = new es.atareao.alejandria.gui.JImage();
        jImage2 = new es.atareao.alejandria.gui.JImage();
        jLabel39 = new javax.swing.JLabel();
        jArmaduraPilarLongitudAnclajeAncho = new es.atareao.alejandria.gui.JNumericField();
        jLabel40 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jArmaduraPilarLargoNumero = new es.atareao.alejandria.gui.JNumericField();
        jArmaduraPilarLargoDiametro = new es.atareao.queensboro.gui.JWrapperComboBox();
        jImage3 = new es.atareao.alejandria.gui.JImage();
        jImage4 = new es.atareao.alejandria.gui.JImage();
        jLabel41 = new javax.swing.JLabel();
        jArmaduraPilarLongitudAnclajeLargo = new es.atareao.alejandria.gui.JNumericField();
        jLabel45 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jArmaduraPilarEsquinasNumero = new es.atareao.alejandria.gui.JNumericField();
        jArmaduraPilarEsquinasDiametro = new es.atareao.queensboro.gui.JWrapperComboBox();
        jImage5 = new es.atareao.alejandria.gui.JImage();
        jImage6 = new es.atareao.alejandria.gui.JImage();
        jLabel46 = new javax.swing.JLabel();
        jArmaduraPilarLongitudAnclajeEsquinas = new es.atareao.alejandria.gui.JNumericField();
        jLabel47 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jArmaduraPilarLongitudPatillas = new es.atareao.alejandria.gui.JNumericField();
        jLabel44 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jArmaduraPilarEstribosNumero = new es.atareao.alejandria.gui.JNumericField();
        jArmaduraPilarEstribosDiametro = new es.atareao.queensboro.gui.JWrapperComboBox();
        jImage7 = new es.atareao.alejandria.gui.JImage();
        jImage8 = new es.atareao.alejandria.gui.JImage();
        jAceptar = new javax.swing.JButton();
        jCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Despiece de zapatas");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSVGImageMod1.setScaleHeight(0.7);
        jSVGImageMod1.setScaleWidth(0.7);
        jPanel3.add(jSVGImageMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 550, 290));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 570, 310));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jArmaduraInferior.setSelected(true);
        jArmaduraInferior.setText("Armadura interior");
        jArmaduraInferior.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dibuja(evt);
            }
        });
        jPanel2.add(jArmaduraInferior, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 230, -1));

        jPatillasArmaduraInferior.setText("Patillas armadura inferior");
        jPatillasArmaduraInferior.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dibuja(evt);
            }
        });
        jPanel2.add(jPatillasArmaduraInferior, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 210, -1));

        jArmaduraSuperior.setText("Armadura superior");
        jArmaduraSuperior.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dibuja(evt);
            }
        });
        jPanel2.add(jArmaduraSuperior, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 230, -1));

        jPatillasArmaduraSuperior.setText("Patillas armadura superior");
        jPatillasArmaduraSuperior.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dibuja(evt);
            }
        });
        jPanel2.add(jPatillasArmaduraSuperior, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 210, -1));

        jPilar.setText("Arranque de pilar");
        jPilar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dibuja(evt);
            }
        });
        jPanel2.add(jPilar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 230, -1));

        jPatillasPilar.setText("Patillas en arranque de pilar");
        jPatillasPilar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dibuja(evt);
            }
        });
        jPanel2.add(jPatillasPilar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 210, -1));

        jEstribos.setText("Estribos");
        jEstribos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dibuja(evt);
            }
        });
        jPanel2.add(jEstribos, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 210, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 250, 390));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Dimensiones zapata:"));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("cm");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 30, 30));

        jLabel3.setText("Largo (Y):");
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 110, 30));

        jLabel4.setText("Canto (Z):");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 110, 30));

        jLabel5.setText("Ancho (X):");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 110, 30));

        jLabel6.setText("cm");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, 30, 30));

        jLabel7.setText("cm");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, 30, 30));

        jAncho.setInputVerifier(new GuiPositiveValidator(this,this.jAncho));
        jPanel4.add(jAncho, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 120, 30));

        jLargo.setInputVerifier(new GuiPositiveValidator(this,this.jLargo));
        jPanel4.add(jLargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 120, 30));

        jAlto.setInputVerifier(new GuiPositiveValidator(this,this.jAlto));
        jPanel4.add(jAlto, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 120, 30));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 280, 140));

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Recubrimiento:"));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel38.setText("cm");
        jPanel18.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 30, 30, 30));

        jLabel42.setText("Recubrimiento:");
        jPanel18.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, 30));

        jRecubrimiento.setInputVerifier(new GuiPositiveValidator(this,this.jRecubrimiento));
        jPanel18.add(jRecubrimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 120, 30));

        jPanel1.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 170, 280, 70));

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Dimensiones pilar:"));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel26.setText("cm");
        jPanel14.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, 30, 30));

        jLabel27.setText("Largo (Y):");
        jPanel14.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 110, 30));

        jLabel29.setText("Tipo:");
        jPanel14.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 110, 30));

        jLabel30.setText("cm");
        jPanel14.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, 30, 30));

        jLabel37.setText("Ancho (X):");
        jPanel14.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 110, 30));

        jTipoPilar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Circular", "Rectangular" }));
        jTipoPilar.setSelectedIndex(1);
        jTipoPilar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jTipoPilarItemStateChanged(evt);
            }
        });
        jPanel14.add(jTipoPilar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 120, 30));

        jAnchoPilar.setInputVerifier(new GuiPositiveValidator(this,this.jAnchoPilar));
        jPanel14.add(jAnchoPilar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 120, 30));

        jLargoPilar.setInputVerifier(new GuiPositiveValidator(this,this.jLargoPilar));
        jPanel14.add(jLargoPilar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 120, 30));

        jPanel1.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 260, 280, 140));

        jTabbedPane1.addTab("Definición Geométrica", jPanel1);

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Armado ancho (X):"));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setText("cm");
        jPanel8.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 30, 30));

        jLabel10.setText("Diámetro:");
        jPanel8.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 90, 30));

        jLabel11.setText("Cada:");
        jPanel8.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 50, 30));
        jPanel8.add(jArmaduraInferiorAnchoDiametro, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 120, 30));

        jArmaduraInferiorAnchoReparto.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraInferiorAnchoReparto));
        jPanel8.add(jArmaduraInferiorAnchoReparto, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 80, 30));

        jPanel5.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 550, 90));

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Armado largo (Y):"));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setText("cm");
        jPanel9.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 30, 30));

        jLabel13.setText("Diámetro:");
        jPanel9.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 90, 30));

        jLabel14.setText("Cada:");
        jPanel9.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 50, 30));
        jPanel9.add(jArmaduraInferiorLargoDiametro, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 120, 30));

        jArmaduraInferiorLargoReparto.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraInferiorLargoReparto));
        jPanel9.add(jArmaduraInferiorLargoReparto, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 80, 30));

        jPanel5.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 550, 90));

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Patillas:"));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setText("cm");
        jPanel10.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 30, 30));

        jLabel16.setText("Longitud de patilla:");
        jPanel10.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 150, 30));

        jArmaduraInferiorLongitudPatillas.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraInferiorLongitudPatillas));
        jPanel10.add(jArmaduraInferiorLongitudPatillas, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 200, 30));

        jPanel5.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 550, 90));

        jTabbedPane1.addTab("Armadura inferior", jPanel5);

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Armado ancho (X):"));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setText("cm");
        jPanel11.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 30, 30));

        jLabel18.setText("Diámetro:");
        jPanel11.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 90, 30));

        jLabel19.setText("Cada:");
        jPanel11.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 50, 30));
        jPanel11.add(jArmaduraSuperiorAnchoDiametro, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 120, 30));

        jArmaduraSuperiorAnchoReparto.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraSuperiorAnchoReparto));
        jPanel11.add(jArmaduraSuperiorAnchoReparto, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 80, 30));

        jPanel6.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 550, 90));

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Armado largo (Y):"));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setText("cm");
        jPanel12.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 30, 30));

        jLabel21.setText("Diámetro:");
        jPanel12.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 90, 30));

        jLabel22.setText("Cada:");
        jPanel12.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 50, 30));
        jPanel12.add(jArmaduraSuperiorLargoDiametro, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 120, 30));

        jArmaduraSuperiorLargoReparto.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraSuperiorAnchoReparto));
        jPanel12.add(jArmaduraSuperiorLargoReparto, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 80, 30));

        jPanel6.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 550, 90));

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Patillas:"));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setText("cm");
        jPanel13.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 30, 30));

        jLabel24.setText("Longitud de patilla:");
        jPanel13.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 150, 30));

        jArmaduraSuperiorLongitudPatillas.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraSuperiorLongitudPatillas));
        jPanel13.add(jArmaduraSuperiorLongitudPatillas, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 200, 30));

        jPanel6.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 550, 90));

        jTabbedPane1.addTab("Armadura superior", jPanel6);

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Armado ancho (X):"));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jArmaduraPilarAnchoNumero.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraPilarAnchoNumero));
        jPanel15.add(jArmaduraPilarAnchoNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 60, 30));

        jArmaduraPilarAnchoDiametro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                calculaLongitudAnclaje(evt);
            }
        });
        jPanel15.add(jArmaduraPilarAnchoDiametro, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 100, 30));

        jImage1.setResourceUrl("/es/atareao/ferraplan/img/phi.png");
        jPanel15.add(jImage1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 20, 30));

        jImage2.setResourceUrl("/es/atareao/ferraplan/img/no.png");
        jPanel15.add(jImage2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 20, 30));

        jLabel39.setText("Longitud de anclaje:");
        jPanel15.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 130, 30));

        jArmaduraPilarLongitudAnclajeAncho.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraPilarLongitudAnclajeAncho));
        jPanel15.add(jArmaduraPilarLongitudAnclajeAncho, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, 100, 30));

        jLabel40.setText(" cm");
        jPanel15.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, 30, 30));

        jPanel7.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 550, 90));

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Armado largo (Y):"));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jArmaduraPilarLargoNumero.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraPilarLargoNumero));
        jPanel16.add(jArmaduraPilarLargoNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 60, 30));

        jArmaduraPilarLargoDiametro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                calculaLongitudAnclaje(evt);
            }
        });
        jPanel16.add(jArmaduraPilarLargoDiametro, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 100, 30));

        jImage3.setResourceUrl("/es/atareao/ferraplan/img/no.png");
        jPanel16.add(jImage3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 20, 30));

        jImage4.setResourceUrl("/es/atareao/ferraplan/img/phi.png");
        jPanel16.add(jImage4, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 20, 30));

        jLabel41.setText("Longitud de anclaje:");
        jPanel16.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 130, 30));

        jArmaduraPilarLongitudAnclajeLargo.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraPilarLongitudAnclajeLargo));
        jPanel16.add(jArmaduraPilarLongitudAnclajeLargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, 100, 30));

        jLabel45.setText(" cm");
        jPanel16.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, 30, 30));

        jPanel7.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 550, 90));

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Armado de esquinas:"));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jArmaduraPilarEsquinasNumero.setText("1");
        jArmaduraPilarEsquinasNumero.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraPilarEsquinasNumero));
        jPanel19.add(jArmaduraPilarEsquinasNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 60, 30));

        jArmaduraPilarEsquinasDiametro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                calculaLongitudAnclaje(evt);
            }
        });
        jPanel19.add(jArmaduraPilarEsquinasDiametro, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 100, 30));

        jImage5.setResourceUrl("/es/atareao/ferraplan/img/no.png");
        jPanel19.add(jImage5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 20, 30));

        jImage6.setResourceUrl("/es/atareao/ferraplan/img/phi.png");
        jPanel19.add(jImage6, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 20, 30));

        jLabel46.setText("Longitud de anclaje:");
        jPanel19.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 130, 30));

        jArmaduraPilarLongitudAnclajeEsquinas.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraPilarLongitudAnclajeEsquinas));
        jPanel19.add(jArmaduraPilarLongitudAnclajeEsquinas, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, 100, 30));

        jLabel47.setText(" cm");
        jPanel19.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, 30, 30));

        jPanel7.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 550, 90));

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Patillas:"));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setText("Longitud de patilla:");
        jPanel21.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 130, 30));

        jArmaduraPilarLongitudPatillas.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraPilarLongitudPatillas));
        jPanel21.add(jArmaduraPilarLongitudPatillas, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 100, 30));

        jLabel44.setText(" cm");
        jPanel21.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 30, 30));

        jPanel17.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 50));

        jPanel7.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 550, 90));

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Estribos:"));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jArmaduraPilarEstribosNumero.setText("1");
        jArmaduraPilarEstribosNumero.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraPilarEstribosNumero));
        jPanel20.add(jArmaduraPilarEstribosNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 60, 30));
        jPanel20.add(jArmaduraPilarEstribosDiametro, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 100, 30));

        jImage7.setResourceUrl("/es/atareao/ferraplan/img/no.png");
        jPanel20.add(jImage7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 20, 30));

        jImage8.setResourceUrl("/es/atareao/ferraplan/img/phi.png");
        jPanel20.add(jImage8, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 20, 30));

        jPanel7.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 550, 90));

        jTabbedPane1.addTab("Arranque de pilar", jPanel7);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 580, 510));

        jAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_ok.png"))); // NOI18N
        jAceptar.setBorderPainted(false);
        jAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAceptarActionPerformed(evt);
            }
        });
        getContentPane().add(jAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 840, 50, 50));

        jCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_cancel.png"))); // NOI18N
        jCancelar.setBorderPainted(false);
        jCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(jCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 840, 50, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void jCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCancelarActionPerformed
// TODO add your handling code here:
        doClose(RET_CANCEL);
    }//GEN-LAST:event_jCancelarActionPerformed
    
    private void jAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAceptarActionPerformed
// TODO add your handling code here:
        doClose(RET_OK);
    }//GEN-LAST:event_jAceptarActionPerformed

    private void dibuja(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dibuja
        this.dibuja();
    }//GEN-LAST:event_dibuja

    private void jTipoPilarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jTipoPilarItemStateChanged
        if(jTipoPilar.getItemCount()>0){
            if(((String)this.jTipoPilar.getSelectedItem()).equals("Circular")){
                jLabel37.setText("Diámetro:");
                jLabel27.setVisible(false);
                jLargoPilar.setVisible(false);
                jLabel30.setVisible(false);
                this.setEnabled(this.jPanel16,false);
                this.setEnabled(this.jPanel19,false);
                ((TitledBorder)jPanel15.getBorder()).setTitle("Armado longitudinal:");
            }else{
                jLabel37.setText("Ancho (X):");
                jLabel27.setVisible(true);
                jLargoPilar.setVisible(true);
                jLabel30.setVisible(true);
                this.setEnabled(this.jPanel16,true);
                this.setEnabled(this.jPanel19,true);
                ((TitledBorder)jPanel15.getBorder()).setTitle("Armado ancho (X):");
            }
        }
    }//GEN-LAST:event_jTipoPilarItemStateChanged

    private void calculaLongitudAnclaje(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_calculaLongitudAnclaje
        try {
            Revision revision = this.getRevision();
            Parte parte=new Parte(revision.getConector());
            parte=parte.findFirst(new Condition("id",revision.getValue("parte_id")));
            Acero acero = new Acero(revision.getConector());
            Hormigon hormigon = new Hormigon(revision.getConector());
            acero=acero.findFirst(new Condition("id",parte.getValue("acero_id")));
            hormigon=hormigon.findFirst(new Condition("id",parte.getValue("hormigon_id")));
            if(((String)this.jTipoPilar.getSelectedItem()).equals("Circular")){
                Diametro diametroAncho=(Diametro)this.jArmaduraPilarAnchoDiametro.getSelectedItem();
                double anclajeAncho=Anclaje.calcula_longitud_anclaje(Posicion.I,diametroAncho, acero, hormigon, Esfuerzo.COMPRESION, 1.0,1.0, Tipo.PROLONGACION_RECTA);
                this.jArmaduraPilarLongitudAnclajeAncho.setDouble(anclajeAncho);
            }else{
                double anclajeAncho=Anclaje.calcula_longitud_anclaje(Posicion.I, (Diametro)this.jArmaduraPilarAnchoDiametro.getSelectedItem(), acero, hormigon, Esfuerzo.COMPRESION, 1.0,1.0, Tipo.PROLONGACION_RECTA);
                double anclajeLargo=Anclaje.calcula_longitud_anclaje(Posicion.I, (Diametro)this.jArmaduraPilarLargoDiametro.getSelectedItem(), acero, hormigon, Esfuerzo.COMPRESION, 1.0,1.0, Tipo.PROLONGACION_RECTA);
                double anclajeEsquinas=Anclaje.calcula_longitud_anclaje(Posicion.I, (Diametro)this.jArmaduraPilarEsquinasDiametro.getSelectedItem(), acero, hormigon, Esfuerzo.COMPRESION, 1.0,1.0, Tipo.PROLONGACION_RECTA);
                this.jArmaduraPilarLongitudAnclajeAncho.setDouble(anclajeAncho);
                this.jArmaduraPilarLongitudAnclajeLargo.setDouble(anclajeLargo);
                this.jArmaduraPilarLongitudAnclajeEsquinas.setDouble(anclajeEsquinas);
            }
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex);
        }
    }//GEN-LAST:event_calculaLongitudAnclaje
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jAceptar;
    private es.atareao.alejandria.gui.JNumericField jAlto;
    private es.atareao.alejandria.gui.JNumericField jAncho;
    private es.atareao.alejandria.gui.JNumericField jAnchoPilar;
    private javax.swing.JCheckBox jArmaduraInferior;
    private es.atareao.queensboro.gui.JWrapperComboBox jArmaduraInferiorAnchoDiametro;
    private es.atareao.alejandria.gui.JNumericField jArmaduraInferiorAnchoReparto;
    private es.atareao.queensboro.gui.JWrapperComboBox jArmaduraInferiorLargoDiametro;
    private es.atareao.alejandria.gui.JNumericField jArmaduraInferiorLargoReparto;
    private es.atareao.alejandria.gui.JNumericField jArmaduraInferiorLongitudPatillas;
    private es.atareao.queensboro.gui.JWrapperComboBox jArmaduraPilarAnchoDiametro;
    private es.atareao.alejandria.gui.JNumericField jArmaduraPilarAnchoNumero;
    private es.atareao.queensboro.gui.JWrapperComboBox jArmaduraPilarEsquinasDiametro;
    private es.atareao.alejandria.gui.JNumericField jArmaduraPilarEsquinasNumero;
    private es.atareao.queensboro.gui.JWrapperComboBox jArmaduraPilarEstribosDiametro;
    private es.atareao.alejandria.gui.JNumericField jArmaduraPilarEstribosNumero;
    private es.atareao.queensboro.gui.JWrapperComboBox jArmaduraPilarLargoDiametro;
    private es.atareao.alejandria.gui.JNumericField jArmaduraPilarLargoNumero;
    private es.atareao.alejandria.gui.JNumericField jArmaduraPilarLongitudAnclajeAncho;
    private es.atareao.alejandria.gui.JNumericField jArmaduraPilarLongitudAnclajeEsquinas;
    private es.atareao.alejandria.gui.JNumericField jArmaduraPilarLongitudAnclajeLargo;
    private es.atareao.alejandria.gui.JNumericField jArmaduraPilarLongitudPatillas;
    private javax.swing.JCheckBox jArmaduraSuperior;
    private es.atareao.queensboro.gui.JWrapperComboBox jArmaduraSuperiorAnchoDiametro;
    private es.atareao.alejandria.gui.JNumericField jArmaduraSuperiorAnchoReparto;
    private es.atareao.queensboro.gui.JWrapperComboBox jArmaduraSuperiorLargoDiametro;
    private es.atareao.alejandria.gui.JNumericField jArmaduraSuperiorLargoReparto;
    private es.atareao.alejandria.gui.JNumericField jArmaduraSuperiorLongitudPatillas;
    private javax.swing.JButton jCancelar;
    private javax.swing.JCheckBox jEstribos;
    private es.atareao.alejandria.gui.JImage jImage1;
    private es.atareao.alejandria.gui.JImage jImage2;
    private es.atareao.alejandria.gui.JImage jImage3;
    private es.atareao.alejandria.gui.JImage jImage4;
    private es.atareao.alejandria.gui.JImage jImage5;
    private es.atareao.alejandria.gui.JImage jImage6;
    private es.atareao.alejandria.gui.JImage jImage7;
    private es.atareao.alejandria.gui.JImage jImage8;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private es.atareao.alejandria.gui.JNumericField jLargo;
    private es.atareao.alejandria.gui.JNumericField jLargoPilar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JCheckBox jPatillasArmaduraInferior;
    private javax.swing.JCheckBox jPatillasArmaduraSuperior;
    private javax.swing.JCheckBox jPatillasPilar;
    private javax.swing.JCheckBox jPilar;
    private es.atareao.alejandria.gui.JNumericField jRecubrimiento;
    private es.atareao.ferraplan.gui.JSVGImageMod jSVGImageMod1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox jTipoPilar;
    // End of variables declaration//GEN-END:variables
    //
    // *********************************CAMPOS*********************************
    //
    private int _returnStatus = RET_CANCEL;
    SVGDocument _svgdoc;
    private int _operacion;
    private Revision _revision;
    //
    //********************************METODOS***********************************
    //
    private void dibuja(){
        boolean inferior=this.jArmaduraInferior.isSelected();
        boolean superior=this.jArmaduraSuperior.isSelected();
        boolean pilar=this.jPilar.isSelected();
        boolean patillas_inferiores=this.jPatillasArmaduraInferior.isSelected();
        boolean patillas_superiores=this.jPatillasArmaduraSuperior.isSelected();
        boolean patillas_pilar=this.jPatillasPilar.isSelected();
        boolean estribos=this.jEstribos.isSelected();
        elemento_visible("armadura_inferior",inferior);
        elemento_visible("armadura_superior",superior);
        elemento_visible("pilar",pilar);
        this.jPatillasArmaduraInferior.setEnabled(inferior);
        this.jPatillasArmaduraSuperior.setEnabled(superior);
        this.jPatillasPilar.setEnabled(pilar);
        this.jEstribos.setEnabled(pilar);
        //
        this.setEnabled(this.jPanel5,inferior);
        this.setEnabled(this.jPanel6,superior);
        this.setEnabled(this.jPanel7,pilar);
        //
        this.setEnabled(this.jPanel14,pilar);
        if(inferior){
            elemento_visible("patillas_inferiores",patillas_inferiores);
            this.setEnabled(this.jPanel10,patillas_inferiores);
        }else{
            elemento_visible("patillas_inferiores",false);
            this.jPatillasArmaduraInferior.setSelected(false);
            this.setEnabled(this.jPanel10,false);
        }
        if(superior){
            elemento_visible("patillas_superiores",patillas_superiores);
            this.setEnabled(this.jPanel13,patillas_superiores);
        }else{
            elemento_visible("patillas_superiores",false);
            this.jPatillasArmaduraSuperior.setSelected(false);
            this.setEnabled(this.jPanel13,false);
        }
        if(pilar){
            elemento_visible("patillas_pilar",patillas_pilar);
            elemento_visible("estribos",estribos);
            this.setEnabled(this.jPanel21,patillas_pilar);
            this.setEnabled(this.jPanel20,estribos);
        }else{
            elemento_visible("patillas_pilar",false);
            elemento_visible("estribos",false);
            this.jPatillasPilar.setSelected(false);
            this.jEstribos.setSelected(false);
            this.setEnabled(this.jPanel21,false);
            this.setEnabled(this.jPanel20,false);
        }
        this.jSVGImageMod1.setSvgdoc(_svgdoc);
    }
    private void elemento_visible(String elemento,boolean visible){
        Element e_b=_svgdoc.getElementById(elemento);
        if(e_b!=null){
            if(visible){
                e_b.setAttribute("visibility","visible");
            }else{
                e_b.setAttribute("visibility","hidden");
            }
        }
    }
    private void setEnabled(Component main,boolean enabled){
        main.setEnabled(enabled);
        if(main instanceof JComponent){
            JComponent jmain=(JComponent)main;
            for(Component c:jmain.getComponents()){
                setEnabled(c,enabled);
            }
        }
    }
    private boolean valida(JComponent main){
        boolean ans=true;
        InputVerifier iv=main.getInputVerifier();
        if(iv!=null){
            if(!iv.verify(main)){
                ans=false;
            }
        }
        for(Component c:main.getComponents()){
            if(c instanceof JComponent){
                if(!valida(((JComponent)c))){
                    ans=false;
                }
            }
        }
        return ans;
    }
    private boolean valida(){
        return this.valida(this.jTabbedPane1);
    }
    //
    //**************************METODOS AUXILIARES******************************
    //
    private boolean estaLaPrimeraFilaVacia(Revision revision,boolean laElimino){
        try {
            Barra barra = new Barra(revision.getConector());
            Vector<Barra> barras=barra.find(new Condition("revision_id",revision.getId()));
            if((barras!=null)&&(barras.size()==1)){
                if(barras.get(0).getValue("referencia").length()!=0) return false;
                if(!barras.get(0).getValue("forma_id").equals("1")) return false;
                if(!barras.get(0).getValue("diametro_id").equals("1")) return false;
                if(Convert.toDouble(barras.get(0).getValue("cantidad"))!=0) return false;
                if(barras.get(0).getValue("comentario").length()!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("a"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("b"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("c"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("d"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("e"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("f"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("g"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("h"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("i"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("j"))!=0) return false;
                if(laElimino){
                    barras.get(0).delete();
                }
                return true;
            }
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex);
        }
        return false;
    }
    private void doClose(int retStatus) {
        if(retStatus==RET_OK){
            if(valida()){
                this.estaLaPrimeraFilaVacia(this._revision,true);
                int contador=0;
                String cod="";
                if(this.jArmaduraInferior.isSelected()){
                    if(this.jPatillasArmaduraInferior.isSelected()){
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"11",(Diametro)this.jArmaduraInferiorAnchoDiametro.getSelectedItem(),this.jAncho.getDouble()/this.jArmaduraInferiorAnchoReparto.getDouble(),"Transversales (armadura inferior)",this.jAncho.getDouble()-2*this.jRecubrimiento.getDouble(),this.jArmaduraInferiorLongitudPatillas.getDouble(),this.jArmaduraInferiorLongitudPatillas.getDouble(),0,0,0,0,0,0,0);
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"11",(Diametro)this.jArmaduraInferiorLargoDiametro.getSelectedItem(),this.jLargo.getDouble()/this.jArmaduraInferiorLargoReparto.getDouble(),"Longitudinales (armadura inferior)",this.jLargo.getDouble()-2*this.jRecubrimiento.getDouble(),this.jArmaduraInferiorLongitudPatillas.getDouble(),this.jArmaduraInferiorLongitudPatillas.getDouble(),0,0,0,0,0,0,0);
                    }else{
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"1",(Diametro)this.jArmaduraInferiorAnchoDiametro.getSelectedItem(),this.jAncho.getDouble()/this.jArmaduraInferiorAnchoReparto.getDouble(),"Transversales (armadura inferior)",this.jAncho.getDouble()-2*this.jRecubrimiento.getDouble(),0,0,0,0,0,0,0,0,0);
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"1",(Diametro)this.jArmaduraInferiorLargoDiametro.getSelectedItem(),this.jLargo.getDouble()/this.jArmaduraInferiorLargoReparto.getDouble(),"Longitudinales (armadura inferior)",this.jLargo.getDouble()-2*this.jRecubrimiento.getDouble(),0,0,0,0,0,0,0,0,0);
                    }
                }
                if(this.jArmaduraSuperior.isSelected()){
                    if(this.jPatillasArmaduraSuperior.isSelected()){
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"13",(Diametro)this.jArmaduraSuperiorAnchoDiametro.getSelectedItem(),this.jAncho.getDouble()/this.jArmaduraSuperiorAnchoReparto.getDouble(),"Transversales (armadura superior)",this.jAncho.getDouble()-2*this.jRecubrimiento.getDouble(),this.jArmaduraSuperiorLongitudPatillas.getDouble(),this.jArmaduraInferiorLongitudPatillas.getDouble(),0,0,0,0,0,0,0);
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"13",(Diametro)this.jArmaduraSuperiorLargoDiametro.getSelectedItem(),this.jLargo.getDouble()/this.jArmaduraSuperiorLargoReparto.getDouble(),"Longitudinales (armadura superior)",this.jLargo.getDouble()-2*this.jRecubrimiento.getDouble(),this.jArmaduraSuperiorLongitudPatillas.getDouble(),this.jArmaduraInferiorLongitudPatillas.getDouble(),0,0,0,0,0,0,0);
                    }else{
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"1",(Diametro)this.jArmaduraSuperiorAnchoDiametro.getSelectedItem(),this.jAncho.getDouble()/this.jArmaduraSuperiorAnchoReparto.getDouble(),"Transversales (armadura superior)",this.jAncho.getDouble()-2*this.jRecubrimiento.getDouble(),0,0,0,0,0,0,0,0,0);
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"1",(Diametro)this.jArmaduraSuperiorLargoDiametro.getSelectedItem(),this.jLargo.getDouble()/this.jArmaduraSuperiorLargoReparto.getDouble(),"Longitudinales (armadura superior)",this.jLargo.getDouble()-2*this.jRecubrimiento.getDouble(),0,0,0,0,0,0,0,0,0);
                    }
                }
                if(this.jPilar.isSelected()){
                    if(this.jTipoPilar.getSelectedItem().equals("Circular")){
                        if(this.jPatillasPilar.isSelected()){
                            cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                            this.addBarra(cod,"4",(Diametro)this.jArmaduraPilarAnchoDiametro.getSelectedItem(),this.jArmaduraPilarAnchoNumero.getDouble(),"Longitudinales (pilar)",this.jAlto.getDouble()-this.jRecubrimiento.getDouble()+this.jArmaduraPilarLongitudAnclajeAncho.getDouble(),this.jArmaduraPilarLongitudPatillas.getDouble(),0,0,0,0,0,0,0,0);
                        }else{
                            cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                            this.addBarra(cod,"2",(Diametro)this.jArmaduraPilarAnchoDiametro.getSelectedItem(),this.jArmaduraPilarAnchoNumero.getDouble(),"Longitudinales (pilar)",this.jAlto.getDouble()-this.jRecubrimiento.getDouble()+this.jArmaduraPilarLongitudAnclajeAncho.getDouble(),0,0,0,0,0,0,0,0,0);
                        }
                        if(this.jEstribos.isSelected()){
                            cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                            double patilla=Convert.toDouble(((Diametro)this.jArmaduraPilarEstribosDiametro.getSelectedItem()).getValue("diametro"))*6.0/10.0;
                            this.addBarra(cod,"67",(Diametro)this.jArmaduraPilarEstribosDiametro.getSelectedItem(),this.jArmaduraPilarEstribosNumero.getDouble(),"Estribos (pilar)",this.jAnchoPilar.getDouble()-2.0*this.jRecubrimiento.getDouble(),patilla,0,0,0,0,0,0,0,0);
                        }
                    }else{
                        if(this.jPatillasPilar.isSelected()){
                            cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                            this.addBarra(cod,"4",(Diametro)this.jArmaduraPilarAnchoDiametro.getSelectedItem(),2.0*this.jArmaduraPilarAnchoNumero.getDouble(),"Longitudinales cara ancho (pilar)",this.jAlto.getDouble()-this.jRecubrimiento.getDouble()+this.jArmaduraPilarLongitudAnclajeAncho.getDouble(),this.jArmaduraPilarLongitudPatillas.getDouble(),0,0,0,0,0,0,0,0);
                            cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                            this.addBarra(cod,"4",(Diametro)this.jArmaduraPilarLargoDiametro.getSelectedItem(),2.0*this.jArmaduraPilarLargoNumero.getDouble(),"Longitudinales cara largo (pilar)",this.jAlto.getDouble()-this.jRecubrimiento.getDouble()+this.jArmaduraPilarLongitudAnclajeLargo.getDouble(),this.jArmaduraPilarLongitudPatillas.getDouble(),0,0,0,0,0,0,0,0);
                            cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                            this.addBarra(cod,"4",(Diametro)this.jArmaduraPilarEsquinasDiametro.getSelectedItem(),4.0*this.jArmaduraPilarEsquinasNumero.getDouble(),"Longitudinales esquinas (pilar)",this.jAlto.getDouble()-this.jRecubrimiento.getDouble()+this.jArmaduraPilarLongitudAnclajeEsquinas.getDouble(),this.jArmaduraPilarLongitudPatillas.getDouble(),0,0,0,0,0,0,0,0);
                        }else{
                            cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                            this.addBarra(cod,"2",(Diametro)this.jArmaduraPilarAnchoDiametro.getSelectedItem(),2.0*this.jArmaduraPilarAnchoNumero.getDouble(),"Longitudinales cara ancho (pilar)",this.jAlto.getDouble()-this.jRecubrimiento.getDouble()+this.jArmaduraPilarLongitudAnclajeAncho.getDouble(),0,0,0,0,0,0,0,0,0);
                            cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                            this.addBarra(cod,"2",(Diametro)this.jArmaduraPilarLargoDiametro.getSelectedItem(),2.0*this.jArmaduraPilarLargoNumero.getDouble(),"Longitudinales cara largo (pilar)",this.jAlto.getDouble()-this.jRecubrimiento.getDouble()+this.jArmaduraPilarLongitudAnclajeLargo.getDouble(),0,0,0,0,0,0,0,0,0);
                            cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                            this.addBarra(cod,"2",(Diametro)this.jArmaduraPilarEsquinasDiametro.getSelectedItem(),4.0*this.jArmaduraPilarEsquinasNumero.getDouble(),"Longitudinales esquinas (pilar)",this.jAlto.getDouble()-this.jRecubrimiento.getDouble()+this.jArmaduraPilarLongitudAnclajeEsquinas.getDouble(),0,0,0,0,0,0,0,0,0);
                        }
                        if(this.jEstribos.isSelected()){
                            cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                            double patilla=Convert.toDouble(((Diametro)this.jArmaduraPilarEstribosDiametro.getSelectedItem()).getValue("diametro"))*6.0/10.0;
                            this.addBarra(cod,"59",(Diametro)this.jArmaduraPilarEstribosDiametro.getSelectedItem(),this.jArmaduraPilarEstribosNumero.getDouble(),"Estribos (pilar)",this.jAnchoPilar.getDouble()-2.0*this.jRecubrimiento.getDouble(),this.jLargoPilar.getDouble()-2.0*this.jRecubrimiento.getDouble(),patilla,0,0,0,0,0,0,0);
                        }
                    }
                }
            }else{
                return;
            }
        }
        this.setReturnStatus(retStatus);
        setVisible(false);
        dispose();
    }    
    
    //
    //**************************METODOS DE ACCESO*******************************
    //
    
    public int getReturnStatus() {
        return _returnStatus;
    }
    
    public void setReturnStatus(int returnStatus) {
        this._returnStatus = returnStatus;
    }

    /**
     * @return the _revision
     */
    public Revision getRevision() {
        return _revision;
    }

    /**
     * @param revision the _revision to set
     */
    public void setRevision(Revision revision) {
        this._revision = revision;
    }
}

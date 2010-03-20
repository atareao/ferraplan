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
import es.atareao.queensboro.db.Condition;
import java.awt.Component;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Vector;
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
public class DialogPilar extends javax.swing.JDialog {
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
    public DialogPilar(JFrame padre, Revision revision){
        super(padre,true);
        initComponents();
        this.setSize(600, 930);
        this.setLocationRelativeTo(null);
        this._revision=revision;
        try {
            Diametro diametro = new Diametro(revision.getConector());
            Vector<Diametro>diametros=diametro.findAll();
            this.jArmaduraPilarAnchoDiametro.setElements(diametros);
            this.jArmaduraPilarLargoDiametro.setElements(diametros);
            this.jArmaduraPilarEsquinasDiametro.setElements(diametros);
            this.jArmaduraPilarEstribosDiametro.setElements(diametros);
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex);
        }
        try {
            _svgdoc = this.getSVGDocument(FileUtils.readTextFromJar("/es/atareao/ferraplan/img/pilar_ink.svg"));
            this.dibuja();
        } catch (IOException ex) {
            ErrorDialog.manejaError(ex);
        }
    }
    
    public DialogPilar(Revision revision) throws SQLException{
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
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPatillasPilar = new javax.swing.JCheckBox();
        jEstribos = new javax.swing.JCheckBox();
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
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jTipoPilar = new javax.swing.JComboBox();
        jAnchoPilar = new es.atareao.alejandria.gui.JNumericField();
        jLargoPilar = new es.atareao.alejandria.gui.JNumericField();
        jAltoPilar = new es.atareao.alejandria.gui.JNumericField();
        jPanel15 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jArmaduraPilarAnchoNumero = new es.atareao.alejandria.gui.JNumericField();
        jArmaduraPilarAnchoDiametro = new es.atareao.queensboro.gui.JWrapperComboBox();
        jPanel16 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jArmaduraPilarLargoNumero = new es.atareao.alejandria.gui.JNumericField();
        jArmaduraPilarLargoDiametro = new es.atareao.queensboro.gui.JWrapperComboBox();
        jPanel19 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jArmaduraPilarEsquinasNumero = new es.atareao.alejandria.gui.JNumericField();
        jArmaduraPilarEsquinasDiametro = new es.atareao.queensboro.gui.JWrapperComboBox();
        jPanel17 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jArmaduraPilarLongitudPatillas = new es.atareao.alejandria.gui.JNumericField();
        jLabel44 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jArmaduraPilarEstribosNumero = new es.atareao.alejandria.gui.JNumericField();
        jArmaduraPilarEstribosDiametro = new es.atareao.queensboro.gui.JWrapperComboBox();
        jAceptar = new javax.swing.JButton();
        jCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Despiece de pilares");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSVGImageMod1.setScaleHeight(0.5);
        jSVGImageMod1.setScaleWidth(0.5);
        jPanel3.add(jSVGImageMod1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 550, 280));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 580, 300));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPatillasPilar.setText("Patillas en arranque de pilar");
        jPatillasPilar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dibuja(evt);
            }
        });
        jPanel2.add(jPatillasPilar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 210, -1));

        jEstribos.setText("Estribos");
        jEstribos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dibuja(evt);
            }
        });
        jPanel2.add(jEstribos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 210, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 250, 100));

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Recubrimiento:"));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel38.setText("cm");
        jPanel18.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 30, 30));

        jLabel42.setText("Recubrimiento:");
        jPanel18.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, 30));

        jRecubrimiento.setInputVerifier(new GuiPositiveValidator(this,this.jRecubrimiento));
        jPanel18.add(jRecubrimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 90, 30));

        jPanel1.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 250, 90));

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

        jLabel35.setText("Alto (Z):");
        jPanel14.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 110, 30));

        jLabel36.setText("cm");
        jPanel14.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 140, 30, 30));

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

        jAltoPilar.setInputVerifier(new GuiPositiveValidator(this,this.jLargoPilar));
        jPanel14.add(jAltoPilar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 120, 30));

        jPanel1.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 290, 190));

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Armado ancho (X):"));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel28.setText(" x Cara x Diámetro:");
        jPanel15.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 140, 30));

        jLabel31.setText("Número:");
        jPanel15.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, 30));

        jArmaduraPilarAnchoNumero.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraPilarAnchoNumero));
        jPanel15.add(jArmaduraPilarAnchoNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 80, 30));
        jPanel15.add(jArmaduraPilarAnchoDiametro, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 120, 30));

        jPanel1.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 550, 60));

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Armado largo (Y):"));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel32.setText(" x Cara x Diámetro:");
        jPanel16.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 130, 30));

        jLabel33.setText("Número:");
        jPanel16.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, 30));

        jArmaduraPilarLargoNumero.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraPilarLargoNumero));
        jPanel16.add(jArmaduraPilarLargoNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 80, 30));
        jPanel16.add(jArmaduraPilarLargoDiametro, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 120, 30));

        jPanel1.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 550, 60));

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Armado de esquinas:"));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel34.setText("x Esquina x Diámetro:");
        jPanel19.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 150, 30));

        jLabel39.setText("Número:");
        jPanel19.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, 30));

        jArmaduraPilarEsquinasNumero.setText("1");
        jArmaduraPilarEsquinasNumero.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraPilarEsquinasNumero));
        jPanel19.add(jArmaduraPilarEsquinasNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 80, 30));
        jPanel19.add(jArmaduraPilarEsquinasDiametro, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 120, 30));

        jPanel1.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 550, 60));

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Patillas:"));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setText("Longitud de patilla:");
        jPanel17.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 140, 30));

        jArmaduraPilarLongitudPatillas.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraPilarLongitudPatillas));
        jPanel17.add(jArmaduraPilarLongitudPatillas, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 100, 30));

        jLabel44.setText(" cm");
        jPanel17.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 20, 30, 30));

        jPanel1.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 550, 60));

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Estribos:"));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel40.setText(" x Diámetro:");
        jPanel20.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 150, 30));

        jLabel41.setText("Número:");
        jPanel20.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, 30));

        jArmaduraPilarEstribosNumero.setText("1");
        jArmaduraPilarEstribosNumero.setInputVerifier(new GuiPositiveValidator(this,this.jArmaduraPilarEstribosNumero));
        jPanel20.add(jArmaduraPilarEstribosNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 80, 30));
        jPanel20.add(jArmaduraPilarEstribosDiametro, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 120, 30));

        jPanel1.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 550, 60));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 580, 520));

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

    private void jTipoPilarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jTipoPilarItemStateChanged
        if(jTipoPilar.getItemCount()>0){
            if(((String)this.jTipoPilar.getSelectedItem()).equals("Circular")){
                jLabel37.setText("Diámetro:");
                jLabel27.setEnabled(false);
                jLargoPilar.setEnabled(false);
                jLabel30.setEnabled(false);
                ((TitledBorder)jPanel15.getBorder()).setTitle("Armado longitudinal:");
                this.setEnabled(this.jPanel16,false);
                this.setEnabled(this.jPanel19,false);
            }else{
                jLabel37.setText("Ancho (X):");
                jLabel27.setEnabled(true);
                jLargoPilar.setEnabled(true);
                jLabel30.setEnabled(true);
                this.setEnabled(this.jPanel16,true);
                this.setEnabled(this.jPanel19,true);
                ((TitledBorder)jPanel15.getBorder()).setTitle("Armado ancho (X):");
            }
        }
}//GEN-LAST:event_jTipoPilarItemStateChanged

    private void dibuja(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dibuja
        this.dibuja();
}//GEN-LAST:event_dibuja
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jAceptar;
    private es.atareao.alejandria.gui.JNumericField jAltoPilar;
    private es.atareao.alejandria.gui.JNumericField jAnchoPilar;
    private es.atareao.queensboro.gui.JWrapperComboBox jArmaduraPilarAnchoDiametro;
    private es.atareao.alejandria.gui.JNumericField jArmaduraPilarAnchoNumero;
    private es.atareao.queensboro.gui.JWrapperComboBox jArmaduraPilarEsquinasDiametro;
    private es.atareao.alejandria.gui.JNumericField jArmaduraPilarEsquinasNumero;
    private es.atareao.queensboro.gui.JWrapperComboBox jArmaduraPilarEstribosDiametro;
    private es.atareao.alejandria.gui.JNumericField jArmaduraPilarEstribosNumero;
    private es.atareao.queensboro.gui.JWrapperComboBox jArmaduraPilarLargoDiametro;
    private es.atareao.alejandria.gui.JNumericField jArmaduraPilarLargoNumero;
    private es.atareao.alejandria.gui.JNumericField jArmaduraPilarLongitudPatillas;
    private javax.swing.JButton jCancelar;
    private javax.swing.JCheckBox jEstribos;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private es.atareao.alejandria.gui.JNumericField jLargoPilar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JCheckBox jPatillasPilar;
    private es.atareao.alejandria.gui.JNumericField jRecubrimiento;
    private es.atareao.ferraplan.gui.JSVGImageMod jSVGImageMod1;
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
        boolean patillas_pilar=this.jPatillasPilar.isSelected();
        boolean estribos=this.jEstribos.isSelected();
        //
        this.setEnabled(this.jPanel17,patillas_pilar);
        this.setEnabled(this.jPanel20,estribos);
        //
        elemento_visible("patillas",patillas_pilar);
        elemento_visible("estribos",estribos);
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
        return this.valida(this.jPanel1);
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
                if(this.jTipoPilar.getSelectedItem().equals("Circular")){
                    if(this.jPatillasPilar.isSelected()){
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"6",(Diametro)this.jArmaduraPilarAnchoDiametro.getSelectedItem(),this.jArmaduraPilarAnchoNumero.getDouble(),"Longitudinales (pilar)",this.jAltoPilar.getDouble(),this.jArmaduraPilarLongitudPatillas.getDouble(),0,0,0,0,0,0,0,0);
                    }else{
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"2",(Diametro)this.jArmaduraPilarAnchoDiametro.getSelectedItem(),this.jArmaduraPilarAnchoNumero.getDouble(),"Longitudinales (pilar)",this.jAltoPilar.getDouble(),0,0,0,0,0,0,0,0,0);
                    }
                    if(this.jEstribos.isSelected()){
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        double patilla=Convert.toDouble(((Diametro)this.jArmaduraPilarEstribosDiametro.getSelectedItem()).getValue("diametro"))*6.0/10.0;
                        this.addBarra(cod,"67",(Diametro)this.jArmaduraPilarEstribosDiametro.getSelectedItem(),this.jArmaduraPilarEstribosNumero.getDouble(),"Estribos (pilar)",this.jAnchoPilar.getDouble()-2.0*this.jRecubrimiento.getDouble(),patilla,0,0,0,0,0,0,0,0);
                    }
                }else{
                    if(this.jPatillasPilar.isSelected()){
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"6",(Diametro)this.jArmaduraPilarAnchoDiametro.getSelectedItem(),2.0*this.jArmaduraPilarAnchoNumero.getDouble(),"Longitudinales cara ancho (pilar)",this.jAltoPilar.getDouble(),this.jArmaduraPilarLongitudPatillas.getDouble(),0,0,0,0,0,0,0,0);
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"6",(Diametro)this.jArmaduraPilarLargoDiametro.getSelectedItem(),2.0*this.jArmaduraPilarLargoNumero.getDouble(),"Longitudinales cara largo (pilar)",this.jAltoPilar.getDouble(),this.jArmaduraPilarLongitudPatillas.getDouble(),0,0,0,0,0,0,0,0);
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"6",(Diametro)this.jArmaduraPilarEsquinasDiametro.getSelectedItem(),4.0*this.jArmaduraPilarEsquinasNumero.getDouble(),"Longitudinales esquinas (pilar)",this.jAltoPilar.getDouble(),this.jArmaduraPilarLongitudPatillas.getDouble(),0,0,0,0,0,0,0,0);
                    }else{
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"2",(Diametro)this.jArmaduraPilarAnchoDiametro.getSelectedItem(),2.0*this.jArmaduraPilarAnchoNumero.getDouble(),"Longitudinales cara ancho (pilar)",this.jAltoPilar.getDouble(),0,0,0,0,0,0,0,0,0);
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"2",(Diametro)this.jArmaduraPilarLargoDiametro.getSelectedItem(),2.0*this.jArmaduraPilarLargoNumero.getDouble(),"Longitudinales cara largo (pilar)",this.jAltoPilar.getDouble(),0,0,0,0,0,0,0,0,0);
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        this.addBarra(cod,"2",(Diametro)this.jArmaduraPilarEsquinasDiametro.getSelectedItem(),4.0*this.jArmaduraPilarEsquinasNumero.getDouble(),"Longitudinales esquinas (pilar)",this.jAltoPilar.getDouble(),0,0,0,0,0,0,0,0,0);
                    }
                    if(this.jEstribos.isSelected()){
                        cod="Z"+StringUtils.rellena(Convert.toString(contador++),"00");
                        double patilla=Convert.toDouble(((Diametro)this.jArmaduraPilarEstribosDiametro.getSelectedItem()).getValue("diametro"))*6.0/10.0;
                        this.addBarra(cod,"59",(Diametro)this.jArmaduraPilarEstribosDiametro.getSelectedItem(),this.jArmaduraPilarEstribosNumero.getDouble(),"Estribos (pilar)",this.jAnchoPilar.getDouble()-2.0*this.jRecubrimiento.getDouble(),this.jLargoPilar.getDouble()-2.0*this.jRecubrimiento.getDouble(),patilla,0,0,0,0,0,0,0);
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

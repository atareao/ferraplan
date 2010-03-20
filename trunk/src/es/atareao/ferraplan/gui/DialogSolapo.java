/*
 * MarcoDiametro.java
 *
 * TODO: Descripcion
 *
 * Creado en 31 de diciembre de 2006, 12:11
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
import es.atareao.alejandria.gui.ErrorDialog;
import es.atareao.alejandria.val.gui.GuiPositiveValidator;
import es.atareao.ferraplan.lib.Acero;
import es.atareao.ferraplan.lib.Anclaje;
import es.atareao.ferraplan.lib.Anclaje.Esfuerzo;
import es.atareao.ferraplan.lib.Anclaje.Posicion;
import es.atareao.ferraplan.lib.Diametro;
import es.atareao.ferraplan.lib.Hormigon;
import es.atareao.queensboro.file.CompactDb;
import java.awt.Component;
import java.sql.SQLException;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;


/**
 *
 * @author  Protactino
 */
public class DialogSolapo extends javax.swing.JDialog {
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
    public DialogSolapo(JFrame padre, CompactDb cdb){
        super(padre,true);
        initComponents();
        this.setSize(590, 450);
        this.setLocationRelativeTo(null);
        try {
            Diametro diametro = new Diametro(cdb.getConector());
            this.jDiametro.setElements(diametro.findAll());
            Acero acero = new Acero(cdb.getConector());
            this.jAcero.setElements(acero.findAll());
            Hormigon hormigon=new Hormigon(cdb.getConector());
            this.jHormigon.setElements(hormigon.findAll());
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex);
        }

    }
    
    public DialogSolapo( CompactDb cdb) throws SQLException{
        this(new JFrame(),cdb);
    }
    private void calcula(){
        if((this.jDiametro.getItemCount()>0)&&(this.jAcero.getItemCount()>0)&&(this.jHormigon.getItemCount()>0)){
            Diametro diametro=(Diametro)this.jDiametro.getSelectedItem();
            Acero acero=(Acero)this.jAcero.getSelectedItem();
            Hormigon hormigon=(Hormigon)this.jHormigon.getSelectedItem();
            double distancia=this.jDistanciaEntreEmpalmes.getDouble();
            String porcentajeTraccion=(String)this.jBarrasTraccion.getSelectedItem();
            Posicion posicion=Posicion.I;
            if(this.jPosicion.getSelectedItem().equals("Posición II")){
                posicion=Posicion.II;
            }
            Esfuerzo esfuerzo=Esfuerzo.COMPRESION;
            if(porcentajeTraccion.equals("Trabajando a tracción hasta el 20%")){
                esfuerzo=Esfuerzo.TRACCION20;
            }
            if(porcentajeTraccion.equals("Trabajando a tracción hasta el 25%")){
                esfuerzo=Esfuerzo.TRACCION25;
            }
            if(porcentajeTraccion.equals("Trabajando a tracción hasta el 33%")){
                esfuerzo=Esfuerzo.TRACCION33;
            }
            if(porcentajeTraccion.equals("Trabajando a tracción hasta el 50%")){
                esfuerzo=Esfuerzo.TRACCION50;
            }
            if(porcentajeTraccion.equals("Trabajando a tracción mas del 50%")){
                esfuerzo=Esfuerzo.TRACCIONS50;
            }
            double lb=Anclaje.calcula_longitud_basica(posicion, diametro, acero, hormigon);
            double ls=Anclaje.calcula_longitud_solapo(posicion, diametro, acero, hormigon, distancia,esfuerzo);
            //
            this.jLongitudBasicaAnclaje.setDouble(lb*10.0);
            this.jLongitudSolapo.setDouble(ls*10.0);
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
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPosicion = new javax.swing.JComboBox();
        jDiametro = new es.atareao.queensboro.gui.JWrapperComboBox();
        jAcero = new es.atareao.queensboro.gui.JWrapperComboBox();
        jHormigon = new es.atareao.queensboro.gui.JWrapperComboBox();
        jDistanciaEntreEmpalmes = new es.atareao.alejandria.gui.JNumericField();
        jBarrasTraccion = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        jLongitudBasicaAnclaje = new es.atareao.alejandria.gui.JNumericField();
        jLongitudSolapo = new es.atareao.alejandria.gui.JNumericField();
        jAceptar = new javax.swing.JButton();
        jCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cálculo de solapes");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Diámetro:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 220, 30));

        jLabel2.setText("Hormigón:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 220, 30));

        jLabel3.setText("Posición:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 220, 30));

        jLabel4.setText("Acero:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 220, 30));

        jLabel13.setText("Reparto (mm):");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 220, 30));

        jLabel6.setText("Tipo de esfuerzo:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 220, 30));

        jLabel8.setText("Longitud básica de anclaje (mm):");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 220, 30));

        jLabel10.setText("Longitud de solapo (mm):");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 220, 30));

        jPosicion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Posición I", "Posición II" }));
        jPosicion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                recalcula(evt);
            }
        });
        jPosicion.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calcula(evt);
            }
        });
        jPanel1.add(jPosicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 310, 30));

        jDiametro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                recalcula(evt);
            }
        });
        jDiametro.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calcula(evt);
            }
        });
        jPanel1.add(jDiametro, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 310, 30));

        jAcero.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                recalcula(evt);
            }
        });
        jAcero.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calcula(evt);
            }
        });
        jPanel1.add(jAcero, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, 310, 30));

        jHormigon.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                recalcula(evt);
            }
        });
        jHormigon.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calcula(evt);
            }
        });
        jPanel1.add(jHormigon, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 130, 310, 30));

        jDistanciaEntreEmpalmes.setInputVerifier(new GuiPositiveValidator(this,this.jDistanciaEntreEmpalmes));
        jDistanciaEntreEmpalmes.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calcula(evt);
            }
        });
        jPanel1.add(jDistanciaEntreEmpalmes, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, 310, 30));

        jBarrasTraccion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Trabajando a compresión", "Trabajando a tracción hasta el 20%", "Trabajando a tracción hasta el 25%", "Trabajando a tracción hasta el 33%", "Trabajando a tracción hasta el 50%", "Trabajando a tracción mas del 50%" }));
        jBarrasTraccion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                recalcula(evt);
            }
        });
        jBarrasTraccion.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calcula(evt);
            }
        });
        jPanel1.add(jBarrasTraccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 210, 310, 30));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 530, 10));

        jLongitudBasicaAnclaje.setEditable(false);
        jPanel1.add(jLongitudBasicaAnclaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 260, 310, 30));

        jLongitudSolapo.setEditable(false);
        jPanel1.add(jLongitudSolapo, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 300, 310, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 560, 350));

        jAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_ok.png"))); // NOI18N
        jAceptar.setBorderPainted(false);
        jAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAceptarActionPerformed(evt);
            }
        });
        getContentPane().add(jAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 360, 50, 50));

        jCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_cancel.png"))); // NOI18N
        jCancelar.setBorderPainted(false);
        jCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(jCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 360, 50, 50));

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

    private void calcula(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_calcula
        this.calcula();
}//GEN-LAST:event_calcula

    private void recalcula(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_recalcula
        this.calcula();
    }//GEN-LAST:event_recalcula
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jAceptar;
    private es.atareao.queensboro.gui.JWrapperComboBox jAcero;
    private javax.swing.JComboBox jBarrasTraccion;
    private javax.swing.JButton jCancelar;
    private es.atareao.queensboro.gui.JWrapperComboBox jDiametro;
    private es.atareao.alejandria.gui.JNumericField jDistanciaEntreEmpalmes;
    private es.atareao.queensboro.gui.JWrapperComboBox jHormigon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private es.atareao.alejandria.gui.JNumericField jLongitudBasicaAnclaje;
    private es.atareao.alejandria.gui.JNumericField jLongitudSolapo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox jPosicion;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
    //
    // *********************************CAMPOS*********************************
    //
    private int _returnStatus = RET_CANCEL;
    private double _longitud_basica=0.0;
    private double _longitud_salapo=0.0;
    //
    //********************************METODOS***********************************
    //
    
    //
    //**************************METODOS AUXILIARES******************************
    //
    private void doClose(int retStatus) {
        if(retStatus==RET_OK){
            if(this.valida()){
                this._longitud_basica=this.jLongitudBasicaAnclaje.getDouble();
                this._longitud_salapo=this.jLongitudSolapo.getDouble();
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
     * @return the _longitud_basica
     */
    public double getLongitud_basica() {
        return _longitud_basica;
    }

    /**
     * @param longitud_basica the _longitud_basica to set
     */
    public void setLongitud_basica(double longitud_basica) {
        this._longitud_basica = longitud_basica;
    }

    /**
     * @return the _longitud_salapo
     */
    public double getLongitud_salapo() {
        return _longitud_salapo;
    }

    /**
     * @param longitud_salapo the _longitud_salapo to set
     */
    public void setLongitud_salapo(double longitud_salapo) {
        this._longitud_salapo = longitud_salapo;
    }
}

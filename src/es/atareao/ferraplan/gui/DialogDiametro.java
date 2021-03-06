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
import es.atareao.alejandria.lib.Convert;
import es.atareao.ferraplan.lib.Diametro;
import java.sql.SQLException;
import javax.swing.JFrame;


/**
 *
 * @author  Protactino
 */
public class DialogDiametro extends javax.swing.JDialog {
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
    public DialogDiametro(JFrame padre,int operacion,Diametro diametro) throws SQLException {
        super(padre,true);
        initComponents();
        this.setSize(400,370);
        this.setLocationRelativeTo(null);
        this.setOperacion(operacion);
        //
        switch (operacion){
            case OP_ADD:
                this.setTitle("Añadir Di�metro");
                break;
            case OP_EDIT:
                this.setTitle("Editar Di�metro");
                break;
            case OP_DELETE:
                this.setTitle("Borrar Di�metro");
                this.doNotEditable();
                break;
            case OP_VIEW:
                this.setTitle("Consultar Di�metro");
                this.doNotEditable();
                break;
        }
        //
        this.setDiametro(diametro);
        this.jDiametro.requestFocus();
    }
    
    public DialogDiametro(int operacion,Diametro diametro) throws SQLException{
        this(new JFrame(),operacion,diametro);
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
        jLabel3 = new javax.swing.JLabel();
        jDiametro = new es.atareao.alejandria.gui.JNumericField();
        jPeso = new es.atareao.alejandria.gui.JNumericField();
        jAceptar = new javax.swing.JButton();
        jCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Diámetros");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Diámetro:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 30));

        jLabel3.setText("Peso:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 90, 30));
        jPanel1.add(jDiametro, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 260, 30));
        jPanel1.add(jPeso, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 260, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 380, 120));

        jAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_ok.png"))); // NOI18N
        jAceptar.setBorderPainted(false);
        jAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAceptarActionPerformed(evt);
            }
        });
        getContentPane().add(jAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 130, 50, 50));

        jCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_cancel.png"))); // NOI18N
        jCancelar.setBorderPainted(false);
        jCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(jCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 130, 50, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jDiametroPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDiametroPropertyChange
// TODO: Agrege su codigo aqui:
        if(this.jDiametro.getDouble()!=0){
            this.jPeso.setDouble(Math.rint(Math.PI/4*Math.pow(this.jDiametro.getDouble(),2.0)/(1000.0*1000.0)*7850.0*1000)/1000);
        }else{
            this.jPeso.setDouble(0.0);
        }
    }//GEN-LAST:event_jDiametroPropertyChange
    
    private void jCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCancelarActionPerformed
// TODO add your handling code here:
        doClose(RET_CANCEL);
    }//GEN-LAST:event_jCancelarActionPerformed
    
    private void jAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAceptarActionPerformed
// TODO add your handling code here:
        doClose(RET_OK);
    }//GEN-LAST:event_jAceptarActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jAceptar;
    private javax.swing.JButton jCancelar;
    private es.atareao.alejandria.gui.JNumericField jDiametro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private es.atareao.alejandria.gui.JNumericField jPeso;
    // End of variables declaration//GEN-END:variables
    //
    // *********************************CAMPOS*********************************
    //
    private int _returnStatus = RET_CANCEL;
    private int _operacion;
    private Diametro _diametro;
    //
    //********************************METODOS***********************************
    //
    
    //
    //**************************METODOS AUXILIARES******************************
    //
    private void doClose(int retStatus) {
        if(retStatus==RET_OK){
            try {
                if(this.fromDialog().validate()){
                    this.setDiametro(this.fromDialog());
                    this.setReturnStatus(retStatus);
                    setVisible(false);
                    dispose();
                }
            } catch (Exception ex) {
                 ErrorDialog.manejaError(ex,false);
            }
        }else{
            this.setReturnStatus(retStatus);
            setVisible(false);
            dispose();
        }
    }    

    private void doNotEditable(){
        this.jDiametro.setEditable(false);
        this.jPeso.setEditable(false);
        //
        this.jDiametro.setBackground(java.awt.SystemColor.info);
        this.jPeso.setBackground(java.awt.SystemColor.info);
    }
    
    private Diametro fromDialog(){
        String diametroN=jDiametro.getText().replaceAll(",",".");
        String peso=jPeso.getText().replaceAll(",",".");
        this.getDiametro().setValue("DIAMETRO",diametroN);
        this.getDiametro().setValue("PESO",peso);
        return this.getDiametro();
    }
    private void toDialog(Diametro diametro){
        String diametroN=diametro.getValue("DIAMETRO");
        String peso=diametro.getValue("PESO");
        if(peso!=null){
            this.jPeso.setDouble(Convert.toDouble(peso.replaceAll(",",".")));
        }
        if(diametroN!=null){
            this.jDiametro.setDouble(Convert.toDouble(diametroN.replaceAll(",",".")));
        }
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
    
    public int getOperacion() {
        return _operacion;
    }

    public void setOperacion(int operacion) {
        this._operacion = operacion;
    }

    public Diametro getDiametro() {
        return _diametro;
    }

    public void setDiametro(Diametro diametro) {
        this._diametro = diametro;
        this.toDialog(diametro);
    }
    
}

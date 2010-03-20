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
import es.atareao.alejandria.val.gui.GuiNotEmptyValidator;
import es.atareao.ferraplan.lib.Montador;
import java.awt.Component;
import java.sql.SQLException;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;


/**
 *
 * @author  Protactino
 */
public class DialogMontador extends javax.swing.JDialog {
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
    public DialogMontador(JFrame padre,int operacion,Montador montador) throws SQLException {
        super(padre,true);
        initComponents();
        this.setSize(410,160);
        this.setLocationRelativeTo(null);
        this.setOperacion(operacion);
        //
        switch (operacion){
            case OP_ADD:
                this.setTitle("Añadir Montador");
                break;
            case OP_EDIT:
                this.setTitle("Editar Montador");
                break;
            case OP_DELETE:
                this.setTitle("Borrar Montador");
                this.doNotEditable();
                break;
            case OP_VIEW:
                this.setTitle("Consultar Montador");
                this.doNotEditable();
                break;
        }
        //
        this.setMontador(montador);
        this.jNombre.requestFocus();
    }
    
    public DialogMontador(int operacion,Montador montador) throws SQLException{
        this(new JFrame(),operacion,montador);
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
        jNombre = new javax.swing.JTextField();
        jAceptar = new javax.swing.JButton();
        jCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Diámetros");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Razón Social:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 30));

        jNombre.setInputVerifier(new GuiNotEmptyValidator(this,jNombre));
        jPanel1.add(jNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 260, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 380, 50));

        jAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_ok.png"))); // NOI18N
        jAceptar.setBorderPainted(false);
        jAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAceptarActionPerformed(evt);
            }
        });
        getContentPane().add(jAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 70, 50, 50));

        jCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_cancel.png"))); // NOI18N
        jCancelar.setBorderPainted(false);
        jCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(jCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 70, 50, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jDiametroPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDiametroPropertyChange

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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jNombre;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    //
    // *********************************CAMPOS*********************************
    //
    private int _returnStatus = RET_CANCEL;
    private int _operacion;
    private Montador _montador;
    //
    //********************************METODOS***********************************
    //
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
    private void doClose(int retStatus) {
        if(retStatus==RET_OK){
            if(valida()){
                try {
                    if(this.fromDialog().validate()){
                        this.setMontador(this.fromDialog());
                        this.setReturnStatus(retStatus);
                        setVisible(false);
                        dispose();
                    }
                } catch (Exception ex) {
                     ErrorDialog.manejaError(ex,false);
                }
            }
        }else{
            this.setReturnStatus(retStatus);
            setVisible(false);
            dispose();
        }
    }    

    private void doNotEditable(){
        this.jNombre.setEditable(false);
        //
        this.jNombre.setBackground(java.awt.SystemColor.info);
    }
    
    private Montador fromDialog(){
        String nombre=this.jNombre.getText();
        this.getMontador().setValue("nombre", nombre);
        return this.getMontador();
    }
    private void toDialog(Montador montador){
        String nombre=montador.getValue("nombre");
        this.jNombre.setText(nombre);
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

    /**
     * @return the _montador
     */
    public Montador getMontador() {
        return _montador;
    }

    /**
     * @param montador the _montador to set
     */
    public void setMontador(Montador montador) {
        this._montador = montador;
        this.toDialog(montador);
    }
    
}
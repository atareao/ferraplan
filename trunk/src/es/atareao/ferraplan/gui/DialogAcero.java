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
import es.atareao.ferraplan.lib.Acero;
import java.sql.SQLException;
import javax.swing.JFrame;


/**
 *
 * @author  Protactino
 */
public class DialogAcero extends javax.swing.JDialog {
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
    public DialogAcero(JFrame padre,int operacion,Acero acero) throws SQLException {
        super(padre,true);
        initComponents();
        this.setSize(400,330);
        this.setLocationRelativeTo(null);
        this.setOperacion(operacion);
        //
        switch (operacion){
            case OP_ADD:
                this.setTitle("Añadir Acero");
                break;
            case OP_EDIT:
                this.setTitle("Editar Acero");
                break;
            case OP_DELETE:
                this.setTitle("Borrar Acero");
                this.doNotEditable();
                break;
            case OP_VIEW:
                this.setTitle("Consultar Acero");
                this.doNotEditable();
                break;
        }
        //
        this.setAcero(acero);
    }
    
    public DialogAcero(int operacion,Acero acero) throws SQLException{
        this(new JFrame(),operacion,acero);
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
        jNombre = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jNota = new javax.swing.JTextArea();
        jAceptar = new javax.swing.JButton();
        jCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Aceros");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Nombre:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 90, 20));

        jLabel3.setText("Nota:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 90, 20));
        jPanel1.add(jNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 290, -1));

        jNota.setColumns(20);
        jNota.setRows(5);
        jScrollPane1.setViewportView(jNota);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 290, 160));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 380, 230));

        jAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_ok.png"))); // NOI18N
        jAceptar.setBorderPainted(false);
        jAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAceptarActionPerformed(evt);
            }
        });
        getContentPane().add(jAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 240, 50, 50));

        jCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_cancel.png"))); // NOI18N
        jCancelar.setBorderPainted(false);
        jCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(jCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 240, 50, 50));

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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jAceptar;
    private javax.swing.JButton jCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jNombre;
    private javax.swing.JTextArea jNota;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    //
    // *********************************CAMPOS*********************************
    //
    private int _returnStatus = RET_CANCEL;
    private int _operacion;
    private Acero _acero;
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
                    this.setAcero(this.fromDialog());
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
        this.jNombre.setEditable(false);
        this.jNota.setEditable(false);
        //
        this.jNombre.setBackground(java.awt.SystemColor.info);
        this.jNota.setBackground(java.awt.SystemColor.info);
    }
    
    private Acero fromDialog(){
        String nombre=jNombre.getText();
        String nota=jNota.getText();
        this.getAcero().setValue("NOMBRE",nombre);
        this.getAcero().setValue("NOTA",nota);
        return this.getAcero();
    }
    private void toDialog(Acero acero){
        String nombre=acero.getValue("NOMBRE");
        String nota=acero.getValue("NOTA");
        this.jNombre.setText(nombre);
        this.jNota.setText(nota);
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

    public Acero getAcero() {
        return _acero;
    }

    public void setAcero(Acero acero) {
        this._acero = acero;
        this.toDialog(acero);
    }
    
}

/*
 * DialogParte
 *
 * File created on 06-dic-2009
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
//
//********************************IMPORTACIONES*********************************
//
import es.atareao.alejandria.gui.ErrorDialog;
import es.atareao.ferraplan.lib.Acero;
import es.atareao.ferraplan.lib.Hormigon;
import es.atareao.ferraplan.lib.Parte;
import java.sql.SQLException;
import javax.swing.JFrame;


/**
 *
 * @author  Lorenzo Carbonell
 */
public class DialogParte extends javax.swing.JDialog {
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
     * Creates new form DialogParte
     * @param padre 
     * @param operacion 
     * @param empresa 
     * @throws java.sql.SQLException 
     */
    public DialogParte(JFrame padre,int operacion,Parte parte) throws SQLException {
        super(padre,true);
        initComponents();
        this.setSize(410,260);
        this.setLocationRelativeTo(null);
        this.setOperacion(operacion);
        //
        switch (operacion){
            case OP_ADD:
                this.setTitle("Añadir Parte");
                break;
            case OP_EDIT:
                this.setTitle("Editar Parte");
                break;
            case OP_DELETE:
                this.setTitle("Borrar Parte");
                this.doNotEditable();
                break;
            case OP_VIEW:
                this.setTitle("Consultar Parte");
                this.doNotEditable();
                break;
        }
        //
        Acero acero=new Acero(parte.getConector());
        this.jAcero.setElements(acero.findAll());
        Hormigon hormigon=new Hormigon(parte.getConector());
        this.jHormigon.setElements(hormigon.findAll());
        this.setParte(parte);
    }
    
    public DialogParte(int operacion,Parte Parte) throws SQLException{
        this(new JFrame(),operacion,Parte);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jAceptar = new javax.swing.JButton();
        jCancelar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jNombre = new javax.swing.JTextField();
        jHormigon = new es.atareao.queensboro.gui.JWrapperComboBox();
        jAcero = new es.atareao.queensboro.gui.JWrapperComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_ok.png"))); // NOI18N
        jAceptar.setBorderPainted(false);
        jAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAceptarActionPerformed(evt);
            }
        });
        getContentPane().add(jAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 170, 50, 50));

        jCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_cancel.png"))); // NOI18N
        jCancelar.setBorderPainted(false);
        jCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(jCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 170, 50, 50));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Nombre:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 100, 30));

        jLabel19.setText("Hormigón:");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 100, 30));

        jLabel18.setText("Acero:");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 100, 30));
        jPanel1.add(jNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 250, 30));
        jPanel1.add(jHormigon, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 250, 30));
        jPanel1.add(jAcero, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 250, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 380, 150));

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
    private es.atareao.queensboro.gui.JWrapperComboBox jAcero;
    private javax.swing.JButton jCancelar;
    private es.atareao.queensboro.gui.JWrapperComboBox jHormigon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JTextField jNombre;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    //
    // *********************************CAMPOS*********************************
    //
    private int _returnStatus = RET_CANCEL;
    private int _operacion;
    private Parte _Parte;
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
                    this.setParte(this.fromDialog());
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
        this.jAcero.setEditable(false);
        this.jHormigon.setEditable(false);
        //
        this.jNombre.setBackground(java.awt.SystemColor.info);
    }
    
    private Parte fromDialog(){
        String nombre=jNombre.getText();
        this.getParte().setValue("nombre",nombre);
        this.getParte().setValue("acero_id",this.jAcero.getSelectedId());
        this.getParte().setValue("hormigon_id",this.jHormigon.getSelectedId());
        return this.getParte();
    }
    private void toDialog(Parte parte){
        String nombre=parte.getValue("nombre");
        String acero_id=parte.getValue("acero_id");
        String hormigon_id=parte.getValue("hormigon_id");
        this.jNombre.setText(nombre);
        this.jAcero.setSelectedId(acero_id);
        this.jHormigon.setSelectedId(hormigon_id);
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

    public Parte getParte() {
        return _Parte;
    }

    public void setParte(Parte Parte) {
        this._Parte = Parte;
        this.toDialog(Parte);
    }
    
}

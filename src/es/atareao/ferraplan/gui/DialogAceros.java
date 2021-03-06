/*
 * MarcoDiametros.java
 *
 * Created on 7 de octubre de 2007, 13:18
 */

package es.atareao.ferraplan.gui;

//
//********************************IMPORTACIONES*********************************
//
import es.atareao.alejandria.gui.ErrorDialog;
import es.atareao.alejandria.lib.Convert;
import es.atareao.queensboro.db.Conector;
import es.atareao.ferraplan.lib.Acero;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author  Propietario
 */
public class DialogAceros extends javax.swing.JFrame {
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
    
    /** Creates new form MarcoDiametros */
    public DialogAceros(Conector conector) {
        initComponents();
        this.setSize(410,430);
        this.setLocationRelativeTo(null);
        try {
            this.setAcero(new Acero(conector));
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex,false);
        }
        this.refresca();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItemAdd = new javax.swing.JMenuItem();
        jMenuItemEdit = new javax.swing.JMenuItem();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuItemDelete = new javax.swing.JMenuItem();
        jAceptar = new javax.swing.JButton();
        jCancelar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jMenuItemAdd.setText("Añadir");
        jMenuItemAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAddActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemAdd);

        jMenuItemEdit.setText("Editar");
        jMenuItemEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEditActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemEdit);

        jMenuItemView.setText("Consultar");
        jMenuItemView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemViewActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemView);

        jMenuItemDelete.setText("Borrar");
        jMenuItemDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeleteActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemDelete);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Aceros");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_ok.png"))); // NOI18N
        jAceptar.setBorderPainted(false);
        jAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAceptarActionPerformed(evt);
            }
        });
        getContentPane().add(jAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 340, 50, 50));

        jCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_cancel.png"))); // NOI18N
        jCancelar.setBorderPainted(false);
        jCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(jCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 340, 50, 50));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 380, 330));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDeleteActionPerformed
        this.operaciones(OP_DELETE);
    }//GEN-LAST:event_jMenuItemDeleteActionPerformed

    private void jMenuItemViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemViewActionPerformed
        this.operaciones(OP_VIEW);
    }//GEN-LAST:event_jMenuItemViewActionPerformed

    private void jMenuItemEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEditActionPerformed
        this.operaciones(OP_EDIT);
    }//GEN-LAST:event_jMenuItemEditActionPerformed


    private void jCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCancelarActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_jCancelarActionPerformed

    private void jAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAceptarActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_jAceptarActionPerformed

    private void jMenuItemAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddActionPerformed
        this.operaciones(OP_ADD);
    }//GEN-LAST:event_jMenuItemAddActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jAceptar;
    private javax.swing.JButton jCancelar;
    private javax.swing.JMenuItem jMenuItemAdd;
    private javax.swing.JMenuItem jMenuItemDelete;
    private javax.swing.JMenuItem jMenuItemEdit;
    private javax.swing.JMenuItem jMenuItemView;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
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
    private void operaciones(int operacion){
        DialogAcero ma;
        String selectedId="";
        int selectedRow=this.jTable1.getSelectedRow();
        if(selectedRow>-1){
            selectedId=(String)this.jTable1.getValueAt(selectedRow,0);
        }
        try{
            switch(operacion){
                case OP_ADD:
                    ma=new DialogAcero(this,operacion,this.getAcero());
                    ma.setVisible(true);
                    if(ma.getReturnStatus()==DialogAcero.RET_OK){
                        if(ma.getAcero().insert()){
                            if(((String)this.jTable1.getValueAt(0,0)).equals("")){
                                this.jTable1.setValueAt(ma.getAcero().getValue("orden"),0,0);
                                this.jTable1.setValueAt(ma.getAcero().getLastInsertId(),0,1);
                                this.jTable1.setValueAt(ma.getAcero(),0,2);
                            }else{
                                Acero acero=ma.getAcero();
                                Object[] fila={acero.getValue("orden"),acero.getId(),acero};
                                ((DefaultTableModel)this.jTable1.getModel()).addRow(fila);
                            }
                        }
                    }
                    break;
                case OP_EDIT:
                    if(selectedRow>-1){
                        Acero acero=(Acero)this.jTable1.getValueAt(selectedRow,2);
                        ma=new DialogAcero(this,operacion,acero);
                        ma.setVisible(true);
                        if(ma.getReturnStatus()==DialogAcero.RET_OK){
                            if(ma.getAcero().update()){
                                ((DefaultTableModel)this.jTable1.getModel()).setValueAt(ma.getAcero().getValue("orden"),selectedRow,0);
                                ((DefaultTableModel)this.jTable1.getModel()).setValueAt(selectedId,selectedRow,1);
                                ((DefaultTableModel)this.jTable1.getModel()).setValueAt(ma.getAcero(),selectedRow,2);
                            }
                        }
                    }
                    break;
                case OP_VIEW:
                    if(selectedRow>-1){
                        Acero acero=(Acero)this.jTable1.getValueAt(selectedRow,2);
                        ma=new DialogAcero(this,operacion,acero);
                        ma.setVisible(true);
                        break;
                    }
                case OP_DELETE:
                    if(selectedRow>-1){
                        this.getAcero().setId(selectedId);
                        if(this.getAcero().delete()){
                            if(selectedRow==0){
                                this.jTable1.setValueAt("",0,0);
                                this.jTable1.setValueAt("",0,1);
                                this.jTable1.setValueAt("",0,2);
                            }else{
                                ((DefaultTableModel)this.jTable1.getModel()).removeRow(selectedRow);
                            }
                        }
                    }
                    break;
            }
        }catch(SQLException ex){
            ErrorDialog.manejaError(ex,false);
        }
    }
    private void refresca(){
        this.jTable1.setModel(new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int rowIndex,int columnIndex){
                return false;
            }
        });
        String[] columnNamesDiametro={"orden","id","nombre"};
        ((DefaultTableModel)this.jTable1.getModel()).setColumnIdentifiers(columnNamesDiametro);
        this.setColumnWidth(0,0);
        this.setColumnWidth(1,0);
        try {
            for(Acero this_acero:this.getAcero().find("orden")){
                Object[] fila={this_acero.getValue("orden"),this_acero.getValue("id"),this_acero};
                ((DefaultTableModel)this.jTable1.getModel()).addRow(fila);
            }
            if(this.jTable1.getRowCount()==0){
                Object[] fila={"","",""};
                ((DefaultTableModel)this.jTable1.getModel()).addRow(fila);
            }
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex,false);
        }
        this.jTable1.setUI(new DragDropRowTableUIOrdered());
    }
    private void doClose(int retStatus) {
        dispose();
    }    
    private void setColumnWidth(int column,int width){
        this.jTable1.getColumnModel().getColumn(column).setPreferredWidth(width);
        this.jTable1.getColumnModel().getColumn(column).setMinWidth(width);
        this.jTable1.getColumnModel().getColumn(column).setMaxWidth(width);
        this.jTable1.getColumnModel().getColumn(column).setWidth(width);
    }
    public Acero getAcero() {
        return _acero;
    }

    public void setAcero(Acero acero) {
        this._acero = acero;
    }
    
}

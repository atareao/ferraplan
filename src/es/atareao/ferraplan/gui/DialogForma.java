/*
 * MarcoElemento.java
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
import es.atareao.alejandria.gui.ExtensionFilter;
import es.atareao.alejandria.lib.FileUtils;
import es.atareao.alejandria.lib.ReadWriteTextFile;
import es.atareao.ferraplan.lib.Forma;
import es.atareao.ferraplan.lib.Grupo;
import java.io.File;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author  Protactino
 */
public class DialogForma extends javax.swing.JDialog {
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
    public DialogForma(JFrame padre,int operacion,Forma forma) throws SQLException {
        super(padre,true);
        initComponents();
        this.setSize(470,530);
        this.setLocationRelativeTo(null);
        this.setOperacion(operacion);
        this.jList1.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        this.jList1.setVisibleRowCount(-1);
        //
        switch (operacion){
            case OP_ADD:
                this.setTitle("Añadir Forma");
                break;
            case OP_EDIT:
                this.setTitle("Editar Forma");
                break;
            case OP_DELETE:
                this.setTitle("Borrar Forma");
                this.doNotEditable();
                break;
            case OP_VIEW:
                this.setTitle("Consultar Forma");
                this.doNotEditable();
                break;
        }
        //
        this.jWrapperSVGImageComboBox1.setElements(forma.findAllWraperSVG());
        Grupo grupo=new Grupo(forma.getConector());
        this.jGrupo.setElements(grupo.findAll());
        this.setforma(forma);
    }
    
    public DialogForma(int operacion,Forma forma) throws SQLException{
        this(new JFrame(),operacion,forma);
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jImageEsquema = new es.atareao.ferraplan.gui.JSVGImage();
        jNombre = new javax.swing.JTextField();
        jGrupo = new es.atareao.queensboro.gui.JWrapperComboBox();
        jFormula = new javax.swing.JTextField();
        jButtonEsquema = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jSVGImage11 = new es.atareao.ferraplan.gui.JSVGImage();
        jPanel4 = new javax.swing.JPanel();
        jWrapperSVGImageComboBox1 = new es.atareao.ferraplan.gui.JWrapperSVGImageComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

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
        getContentPane().add(jAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 440, 50, 50));

        jCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_cancel.png"))); // NOI18N
        jCancelar.setBorderPainted(false);
        jCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(jCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 440, 50, 50));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jImageEsquema, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 410, 210));
        jPanel1.add(jNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 310, -1));
        jPanel1.add(jGrupo, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 310, -1));
        jPanel1.add(jFormula, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 310, -1));

        jButtonEsquema.setText("...");
        jButtonEsquema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEsquemaActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonEsquema, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 40, -1));

        jLabel1.setText("Nombre:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 100, 30));

        jLabel3.setText("Grupo:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 100, 30));

        jLabel4.setText("Esquema:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 100, 30));

        jLabel5.setText("Formula:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 100, 30));

        jTabbedPane1.addTab("tab1", jPanel1);

        jPanel3.setLayout(new java.awt.BorderLayout());
        jPanel3.add(jSVGImage11, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("tab2", jPanel3);

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jWrapperSVGImageComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel4.add(jWrapperSVGImageComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 410, 80));

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        jTabbedPane1.addTab("tab3", jPanel4);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 440, 430));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonEsquemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEsquemaActionPerformed
        String directorio=System.getProperty("user.dir");
        directorio=FileUtils.addPathFile(directorio,"formas").toString();
        JFileChooser chooser=new JFileChooser(directorio);
        FileFilter ff=new ExtensionFilter("Imagen svg",".svg");
        chooser.addChoosableFileFilter(ff);
        chooser.setFileFilter(ff);
        int status=chooser.showOpenDialog(this);
        if(status==JFileChooser.APPROVE_OPTION){
            File f=chooser.getSelectedFile();
            jImageEsquema.setSvgtext(ReadWriteTextFile.getContents(f));
            this.jSVGImage11.setSvgtext(ReadWriteTextFile.getContents(f));
        }
        
    }//GEN-LAST:event_jButtonEsquemaActionPerformed
    
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
    private javax.swing.JButton jButtonEsquema;
    private javax.swing.JButton jCancelar;
    private javax.swing.JTextField jFormula;
    private es.atareao.queensboro.gui.JWrapperComboBox jGrupo;
    private es.atareao.ferraplan.gui.JSVGImage jImageEsquema;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList jList1;
    private javax.swing.JTextField jNombre;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private es.atareao.ferraplan.gui.JSVGImage jSVGImage11;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private es.atareao.ferraplan.gui.JWrapperSVGImageComboBox jWrapperSVGImageComboBox1;
    // End of variables declaration//GEN-END:variables
    //
    // *********************************CAMPOS*********************************
    //
    private int _returnStatus = RET_CANCEL;
    private int _operacion;
    private Forma _forma;
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
                    this.setforma(this.fromDialog());
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
        //
        this.jNombre.setBackground(java.awt.SystemColor.info);
    }
    
    private Forma fromDialog(){
        String nombre=jNombre.getText();
        String grupo=jGrupo.getSelectedId();
        String formula=jFormula.getText();
        String esquema=jImageEsquema.getSvgtext();
        this.getforma().setValue("nombre",nombre);
        this.getforma().setValue("grupo_id",grupo);
        this.getforma().setValue("formula",formula);
        this.getforma().setValue("esquema",esquema);
        return this.getforma();
    }
    private void toDialog(Forma forma){
        String nombre=forma.getValue("nombre");
        String grupo=forma.getValue("grupo_id");
        String formula=forma.getValue("formula");
        String esquema=forma.getValue("esquema");
        this.jNombre.setText(nombre);
        this.jGrupo.setSelectedId(grupo);
        this.jFormula.setText(formula);
        this.jImageEsquema.setSvgtext(esquema);
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

    public Forma getforma() {
        return _forma;
    }

    public void setforma(Forma forma) {
        this._forma = forma;
        this.toDialog(forma);
    }
    
}

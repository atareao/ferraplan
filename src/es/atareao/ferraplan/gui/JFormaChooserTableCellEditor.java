/*
 * JCheckBoxTableCellEditor.java
 *
 * Created on 16 de agosto de 2007, 22:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.atareao.ferraplan.gui;

import es.atareao.ferraplan.lib.Forma;
import es.atareao.ferraplan.lib.Grupo;
import es.atareao.queensboro.db.Conector;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import java.beans.PropertyChangeListener;
import java.util.Vector;


/**
 *
 * @author Propietario
 */
public class JFormaChooserTableCellEditor extends AbstractCellEditor implements ActionListener, TableCellEditor, Serializable {
    protected static final long serialVersionUID=0L;
    protected JFormaChooser formaChooser;
    protected int _row;
    protected int _column;
    public JFormaChooserTableCellEditor() {
        formaChooser = new JFormaChooser();
        this.formaChooser.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        // hitting enter in the combo box should stop cellediting (see below)
        this.formaChooser.addPropertyChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if(pce.getPropertyName().equals("SelectedFormaInJFCD")){
                    stopCellEditing();
                }
            }

        });
        formaChooser.setBackground( Color.white);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table,Object value,boolean isSelected,int row,int column) {
        //setSelectedItem(value);
        formaChooser.setSelectedId((String)value);
        formaChooser.setForeground(table.getSelectionForeground());
        formaChooser.setBackground(table.getSelectionBackground());
        this._column=column;
        this._row=row;
        return formaChooser;
    }
    @Override
    public Object getCellEditorValue() {
        if(formaChooser.getSelectedForma()==null){
            formaChooser.selectFirstElement();
        }
        return formaChooser.getSelectedForma().getId();
    }
    public void inicializa(Vector<Grupo> grupos,Vector<Forma> formas){
        formaChooser.inicializa(grupos,formas);
    }
    public int getRow(){
        return _row;
    }
    public int getColumn(){
        return _column;
    }
    public String getSelectedId(){
        return formaChooser.getSelectedForma().getId();
    }
    public void setSelectedId(String id){
        formaChooser.setSelectedId(id);
    }
    public Object getSelectedItem(){
        return formaChooser.getSelectedForma();
    }
   
    
    // Implementing ActionListener
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        // Selecting an item results in an actioncommand "comboBoxChanged".
        // We should ignore these ones.
        
        // Hitting enter results in an actioncommand "comboBoxEdited"
        if(e.getActionCommand().equals("comboBoxEdited")) {
            stopCellEditing();
        }
    }
    @Override
    public boolean stopCellEditing() {
        if (this.formaChooser.isEditable()) {
            // Notify the combo box that editing has stopped (e.g. User pressed F2)
            //comboBox.actionPerformed(new ActionEvent(this, 0, ""));
        }
        fireEditingStopped();
        return true;
    }
    
    
}
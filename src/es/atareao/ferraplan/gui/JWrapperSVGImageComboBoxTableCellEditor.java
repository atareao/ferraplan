/*
 * JCheckBoxTableCellEditor.java
 *
 * Created on 16 de agosto de 2007, 22:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.atareao.ferraplan.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import es.atareao.ferraplan.lib.WrapperSVG;


/**
 *
 * @author Propietario
 */
public class JWrapperSVGImageComboBoxTableCellEditor extends AbstractCellEditor implements ActionListener, TableCellEditor, Serializable {
    protected static final long serialVersionUID=0L;
    protected JWrapperSVGImageComboBox comboBox;
    protected int _row;
    protected int _column;
    public JWrapperSVGImageComboBoxTableCellEditor() {
        comboBox = new JWrapperSVGImageComboBox();
        comboBox.setMaximumRowCount(3);
        //AutoCompleteDecorator.decorate(comboBox);
        this.comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        // hitting enter in the combo box should stop cellediting (see below)
        this.comboBox.addActionListener(this);
        this.comboBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e) {
                stopCellEditing();
            }
        });
        // remove the editor's border - the cell itself already has one
        ((JComponent) comboBox.getEditor().getEditorComponent()).setBorder(null);        
        //comboBox.setHorizontalAlignment(SwingConstants.CENTER);
        comboBox.setBackground( Color.white);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table,Object value,boolean isSelected,int row,int column) {
        //setSelectedItem(value);
        comboBox.setSelectedId((String)value);
        comboBox.setForeground(table.getSelectionForeground());
        comboBox.setBackground(table.getSelectionBackground());
        this._column=column;
        this._row=row;
        return comboBox;
    }
    @Override
    public Object getCellEditorValue() {
        if(comboBox.getSelectedItem()==null){
            comboBox.setSelectedIndex(0);
        }
        return comboBox.getSelectedId();
    }
    public int getRow(){
        return _row;
    }
    public int getColumn(){
        return _column;
    }
    public String getSelectedId(){
        return comboBox.getSelectedId();
    }
    public void setSelectedId(String id){
        comboBox.setSelectedId(id);
    }
    public void setElements(Vector<WrapperSVG> elements){
        comboBox.setElements(elements);
    }
    public Object getSelectedItem(){
        return comboBox.getSelectedItem();
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
        if (comboBox.isEditable()) {
            // Notify the combo box that editing has stopped (e.g. User pressed F2)
            comboBox.actionPerformed(new ActionEvent(this, 0, ""));
        }
        fireEditingStopped();
        return true;
    }
    
    
}
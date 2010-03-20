/*
 * JCheckBoxTableCellRenderer.java
 *
 * Created on 16 de agosto de 2007, 22:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.atareao.ferraplan.gui;

import java.awt.Color;
import java.awt.Component;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import es.atareao.ferraplan.lib.WrapperSVG;

/**
 *
 * @author Propietario
 */
public class JFormaChooserTableCellRenderer extends JFormaChooser implements TableCellRenderer{
    private final static long serialVersionUID=0L;
    
    
    /**
     * Creates a new instance of JCheckBoxTableCellRenderer
     */
    public JFormaChooserTableCellRenderer() {
        super();
    }
    public JFormaChooserTableCellRenderer(Vector<WrapperSVG> elements) {
        super();
    }
    

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Border mEmptyBorder = BorderFactory.createEmptyBorder();
        Border mHighLightBorder = UIManager.getBorder("Table.focusCellHighlightBorder");
        Color mFocusCellForeground=table.getSelectionForeground();
        Color mFocusCellBackground=table.getSelectionBackground();
        Color mCellForeground=table.getForeground();
        Color mCellBackground=table.getBackground();
        if(table.isCellEditable(row, column))    {
            if (isSelected) {
                setForeground(mFocusCellForeground);
                setBackground(mFocusCellBackground);
            }else{
                setForeground(mCellForeground);
                setBackground(mCellBackground);
            }
        }else{
            if (isSelected) {
                setForeground(Color.GRAY);
                setBackground(mFocusCellBackground);
            }else{
                setForeground(Color.GRAY);
                setBackground(mCellBackground);
            }
            
        }
        if(hasFocus){
            setBorder(mHighLightBorder);
        }else{
            setBorder(mEmptyBorder);
        }
        //setSelectedItemModified(value);
        this.setSelectedId((String)value);
        return this;
    }    
    public void setSelectedItemModified(Object value){
        if(value!=null){
            if(value instanceof String){
                this.setSelectedId((String)value);
            }
        }
    }
    
}

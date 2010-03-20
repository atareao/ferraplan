/*
 * DragDropRowTableUT
 *
 * File created on December 2009
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
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.TableModel;


public class DragDropRowTableUIOrdered extends BasicTableUI {
    //
    //********************************CONSTANTES********************************
    //

    //
    // *********************************CAMPOS*********************************
    //
    private boolean draggingRow = false;
    private int startDragPoint;
    private int dyOffset;
    private DragDropRowMouseInputHandler dragDropRowMouseInputHandler=new DragDropRowMouseInputHandler();
    //
    //******************************CONSTRUCTORES*******************************
    //

    //
    //********************************METODOS***********************************
    //
    @Override
    protected MouseInputListener createMouseInputListener() {
        return dragDropRowMouseInputHandler;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
    	if (isDraggingRow()) {
            g.setColor(table.getParent().getBackground());
            Rectangle cellRect = table.getCellRect(table.getSelectedRow(), 0, false);
            g.copyArea(cellRect.x, cellRect.y, table.getWidth(), table.getRowHeight(), cellRect.x, dyOffset);
            if (dyOffset < 0) {
                g.fillRect(cellRect.x, cellRect.y + (table.getRowHeight() + dyOffset), table.getWidth(), (dyOffset * -1));
            } else {
                g.fillRect(cellRect.x, cellRect.y, table.getWidth(), dyOffset);
            }
    	}
    }

    /**
     * @return the draggingRow
     */
    public boolean isDraggingRow() {
        return draggingRow;
    }

    /**
     * @param draggingRow the draggingRow to set
     */
    public void setDraggingRow(boolean draggingRow) {
        this.draggingRow = draggingRow;
    }

    /**
     * @return the _toRow
     */
    public int getToRow() {
        return dragDropRowMouseInputHandler.getToRow();
    }

    /**
     * @return the _fromRow
     */
    public int getFromRow() {
        return dragDropRowMouseInputHandler.getFromRow();
    }

    //
    //**************************METODOS AUXILIARES******************************
    //
    class DragDropRowMouseInputHandler extends MouseInputHandler {
        private int _toRow;
        private int _fromRow;
        /**
         * @return the _toRow
         */
        public int getToRow() {
            return _toRow;
        }

        /**
         * @param toRow the _toRow to set
         */
        public void setToRow(int toRow) {
            this._toRow = toRow;
        }

        /**
         * @return the _fromRow
         */
        public int getFromRow() {
            return _fromRow;
        }

        /**
         * @param fromRow the _fromRow to set
         */
        public void setFromRow(int fromRow) {
            this._fromRow = fromRow;
        }
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            startDragPoint = (int)e.getPoint().getY();
        }
        @Override
        public void mouseDragged(MouseEvent e) {
            int fromRow = table.getSelectedRow();
            if (fromRow >= 0) {
                setDraggingRow(true);
                int rowHeight = table.getRowHeight();
                int middleOfSelectedRow = (rowHeight * fromRow) + (rowHeight / 2);
                int toRow = -1;
                int yMousePoint = (int)e.getPoint().getY();
                if (yMousePoint < (middleOfSelectedRow - rowHeight)) {
                    // Move row up
                    toRow = fromRow - 1;
                } else if (yMousePoint > (middleOfSelectedRow + rowHeight)) {
                    // Move row down
                    toRow = fromRow + 1;
                }
                if (toRow >= 0 && toRow < table.getRowCount()) {
                    TableModel model = table.getModel();
                    this.setFromRow(fromRow);
                    this.setToRow(toRow);
                    //La primera columna no la copia, de esta manera se mantiene el orden
                    for (int i = 1; i < model.getColumnCount(); i++) {
                        Object fromValue = model.getValueAt(fromRow, i);
                        Object toValue = model.getValueAt(toRow, i);
                        model.setValueAt(toValue, fromRow, i);
                        model.setValueAt(fromValue, toRow, i);
                    }
                    /*
                    String orden1 = ((WrapperTable)model.getValueAt(fromRow,2)).getValue("orden");
                    String orden2 = ((WrapperTable)model.getValueAt(toRow,2)).getValue("orden");
                    ((WrapperTable)model.getValueAt(fromRow,2)).setValue("orden", orden2);
                    ((WrapperTable)model.getValueAt(toRow,2)).setValue("orden", orden1);
                    try {
                        ((WrapperTable) model.getValueAt(fromRow,2)).update();
                        ((WrapperTable)model.getValueAt(toRow,2)).update();
                    } catch (SQLException ex) {
                        ErrorDialog.manejaError(ex);
                    }*/

                    table.setRowSelectionInterval(toRow, toRow);
                    startDragPoint = yMousePoint;
                }
                dyOffset = (startDragPoint - yMousePoint) * -1;
                table.repaint();
            }
        }
        @Override
        public void mouseReleased(MouseEvent e){
        	super.mouseReleased(e);
        	setDraggingRow(false);
        	table.repaint();
        }
    }
    //
    //**************************METODOS DE ACCESO*******************************
    //
}
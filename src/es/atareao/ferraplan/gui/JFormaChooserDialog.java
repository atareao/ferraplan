/*
 * ***********************Software description*********************************
 * JDateChooserDialog.java
 *
 *
 * ***********************Software description*********************************
 *
 * Copyright (C) 7 de septiembre de 2008 - Lorenzo Carbonell
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * **************************Software License***********************************
 *
 *
 */
package es.atareao.ferraplan.gui;

import es.atareao.queensboro.db.Conector;
import java.awt.Component;
import javax.swing.JPanel;
import es.atareao.ferraplan.lib.Forma;
import es.atareao.ferraplan.lib.Grupo;
import java.util.Vector;

/**
 *
 * @author  Lorenzo Carbonell
 */
public class JFormaChooserDialog extends JPanel {
    
    // <editor-fold defaultstate="collapsed" desc=" Constantes  ">
    public static final long serialVersionUID=0L;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Constructores  ">
    /** Creates new form JDateChooserDialog
     * @param component 
     */
    public JFormaChooserDialog(Component component) {
        this.setLocation(component.getX(),component.getY());
        initComponents();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLittleVulcan1 = new es.atareao.ferraplan.gui.JLittleVulcan();

        setPreferredSize(new java.awt.Dimension(520, 360));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jLittleVulcan1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleVulcan1PropertyChange(evt);
            }
        });
        add(jLittleVulcan1);
    }// </editor-fold>//GEN-END:initComponents

    private void jLittleVulcan1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleVulcan1PropertyChange
        if(evt.getPropertyName().equals("SelectedFormaInLV")){
            this.firePropertyChange("SelectedFormaInJFCD",evt.getOldValue(),evt.getNewValue());
        }
    }//GEN-LAST:event_jLittleVulcan1PropertyChange
    
   
    // <editor-fold defaultstate="collapsed" desc=" Metodos  ">
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Metodos auxiliares  ">
    public void inicializa(Vector<Grupo> grupos,Vector<Forma> formas){
        this.jLittleVulcan1.inicializa(grupos,formas);
        /*
        this.jLittleVulcan1.inicializa(conector);
         *
         */
    }
// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Campos  ">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private es.atareao.ferraplan.gui.JLittleVulcan jLittleVulcan1;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Metodos de acceso  ">
    /**
     * @return the _forma
     */
    public Forma getSelectedForma() {
        return this.jLittleVulcan1.getSelectedForma();
    }

    /**
     * @param forma the _forma to set
     */
    public void setSelectedForma(Forma forma) {
        this.jLittleVulcan1.setSelectedId(forma.getId());
    }

    public void setSelectedId(String id){
        this.jLittleVulcan1.setSelectedId(id);
    }
    public void selectFirstElement(){
        this.jLittleVulcan1.selectFirstElement();
    }

    // </editor-fold>
}

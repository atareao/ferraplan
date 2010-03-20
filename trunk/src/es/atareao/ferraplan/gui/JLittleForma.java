/*
 * JDayCell.java
 *
 * Created on 21 de agosto de 2007, 19:30
 */

package es.atareao.ferraplan.gui;

import java.awt.Color;
import javax.swing.BorderFactory;
import es.atareao.ferraplan.lib.Forma;


/**
 *
 * @author  Propietario
 */
public class JLittleForma extends javax.swing.JPanel {
    private final static Color BG_OTRO=new Color(220,220,220);
    private final static Color BG_NORMAL=Color.WHITE;
    private final static Color BG_SELECCIONADO=new Color(255,231,156,255);
    /** Creates new form JDayCell */
    public JLittleForma() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jForma = new es.atareao.ferraplan.gui.JForma();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(29, 122, 181), 1, true));
        setMinimumSize(new java.awt.Dimension(50, 50));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jForma.setMinimumSize(new java.awt.Dimension(50, 50));
        add(jForma);
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if(this.getForma()!=null){
            this.firePropertyChange("selectedForma",null,this.getId());
        }
    }//GEN-LAST:event_formMouseClicked
    
    public void colorea(){
        if(!this.isSelectedGrupo()){
            this.setColorDeFondo(BG_OTRO);
            this.setBorder(BorderFactory.createLineBorder(new Color(63,145,192,255),1));
        }else{
            if(this.isSelected()){
                this.setColorDeFondo(BG_SELECCIONADO);
                this.setBorder(BorderFactory.createLineBorder(new Color(63,145,192,255),2));
            }else{
                this.setColorDeFondo(BG_NORMAL);
                this.setBorder(BorderFactory.createLineBorder(new Color(63,145,192,255),1));
            }
        }            
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private es.atareao.ferraplan.gui.JForma jForma;
    // End of variables declaration//GEN-END:variables
    private boolean _selected=false;
    private boolean _selectedGrupo=false;
    private int _id=0;
    
    public boolean isSelected() {
        return _selected;
    }

    public void setSelected(boolean selected) {
        this._selected = selected;
    }

    public Forma getForma(){
        return this.jForma.getForma();
    }

    public void setForma(Forma forma){
        this.jForma.setForma(forma);
    }
    public boolean isSelectedGrupo() {
        return _selectedGrupo;
    }

    public void setSelectedGrupo(boolean selectedGrupo) {
        this._selectedGrupo = selectedGrupo;
    }
    public void setColorDeFondo(Color color){
        this.setBackground(color);
        this.jForma.setBackground(color);
    }

    /**
     * @return the _id
     */
    public int getId() {
        return _id;
    }

    /**
     * @param id the _id to set
     */
    public void setId(int id) {
        this._id = id;
    }
}

/*
 * JMonthPanel.java
 *
 * Created on 21 de agosto de 2007, 19:44
 */

package es.atareao.ferraplan.gui;

import es.atareao.alejandria.gui.ErrorDialog;
import java.sql.SQLException;
import es.atareao.queensboro.db.Conector;
import es.atareao.ferraplan.lib.Forma;
import es.atareao.ferraplan.lib.Grupo;
import java.util.Vector;

/**
 *
 * @author  Propietario
 */
public class JLittleVulcan extends javax.swing.JPanel {
    

    /** Creates new form JMonthPanel */
    public JLittleVulcan() {
        initComponents();
        this.jScrollPane1.getVerticalScrollBar().setUnitIncrement(40);
    }
    private void ejecutaEvento(java.beans.PropertyChangeEvent evt,int newSelectedJLittleGrupo) {
        if(evt.getPropertyName().equals("selectedFormaInGrupo")){
            int oldSelectedJLittleGrupo=this.getSelectedJLittleGrupo();
            this.setSelectedJLittleGrupo(newSelectedJLittleGrupo);
            int oldSelectedJLittleForma=this.getSelectedJLittleForma();
            int newSelectedJLittleForma=(Integer)evt.getNewValue();
            this.setSelectedJLittleForma(newSelectedJLittleForma);
            Forma oldForma=null;
            if((oldSelectedJLittleGrupo>0)&&(oldSelectedJLittleForma>0)){
                oldForma=this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(oldSelectedJLittleForma).getForma();
            }
            Forma newForma=this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(newSelectedJLittleForma).getForma();
        if(oldSelectedJLittleGrupo==newSelectedJLittleGrupo){//No he cambiado de grupo
                if(oldSelectedJLittleForma>0){
                    this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(oldSelectedJLittleForma).setSelected(false);
                    this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(oldSelectedJLittleForma).colorea();
                }
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(newSelectedJLittleForma).setSelected(true);
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(newSelectedJLittleForma).colorea();
                //
                this.firePropertyChange("SelectedFormaInLV",oldForma,newForma);
                this._selectedForma=newForma;
            }else{
                if(oldSelectedJLittleGrupo>0){
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).setSelected(false);
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(1).setSelectedGrupo(false);
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(2).setSelectedGrupo(false);
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(3).setSelectedGrupo(false);
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(4).setSelectedGrupo(false);
                    if(oldSelectedJLittleForma>0){
                        this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(oldSelectedJLittleForma).setSelected(false);
                    }
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(1).colorea();
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(2).colorea();
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(3).colorea();
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(4).colorea();
                }
                //
                this.getJLittleGrupo(newSelectedJLittleGrupo).setSelected(true);
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(1).setSelectedGrupo(true);
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(2).setSelectedGrupo(true);
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(3).setSelectedGrupo(true);
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(4).setSelectedGrupo(true);
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(newSelectedJLittleForma).setSelected(true);
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(1).colorea();
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(2).colorea();
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(3).colorea();
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(4).colorea();
                //
                this.firePropertyChange("SelectedFormaInLV",oldForma,newForma);
                this._selectedForma=newForma;
            }
        }
    }
    public JLittleGrupo getJLittleGrupo(int grupo){
        switch(grupo){
            case 1:
                return this.jLittleGrupo1;
            case 2:
                return this.jLittleGrupo2;
            case 3:
                return this.jLittleGrupo3;
            case 4:
                return this.jLittleGrupo4;
            case 5:
                return this.jLittleGrupo5;
            case 6:
                return this.jLittleGrupo6;
            case 7:
                return this.jLittleGrupo7;
            case 8:
                return this.jLittleGrupo8;
            case 9:
                return this.jLittleGrupo9;
            case 10:
                return this.jLittleGrupo10;
            case 11:
                return this.jLittleGrupo11;
            case 12:
                return this.jLittleGrupo12;
            case 13:
                return this.jLittleGrupo13;
            case 14:
                return this.jLittleGrupo14;
            case 15:
                return this.jLittleGrupo15;
            case 16:
                return this.jLittleGrupo16;
            case 17:
                return this.jLittleGrupo17;
            case 18:
                return this.jLittleGrupo18;
            case 19:
                return this.jLittleGrupo19;
        }
        return null;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLittleGrupo1 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo2 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo3 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo4 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo5 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo6 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo7 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo8 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo9 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo10 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo11 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo12 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo13 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo14 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo15 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo16 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo17 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo18 = new es.atareao.ferraplan.gui.JLittleGrupo();
        jLittleGrupo19 = new es.atareao.ferraplan.gui.JLittleGrupo();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setMaximumSize(new java.awt.Dimension(520, 360));

        jPanel1.setMaximumSize(new java.awt.Dimension(480, 2280));
        jPanel1.setMinimumSize(new java.awt.Dimension(480, 2280));
        jPanel1.setPreferredSize(new java.awt.Dimension(480, 2280));
        jPanel1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jPanel1PropertyChange(evt);
            }
        });
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jLittleGrupo1.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo1.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo1.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo1PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo1);

        jLittleGrupo2.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo2.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo2.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo2PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo2);

        jLittleGrupo3.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo3.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo3.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo3PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo3);

        jLittleGrupo4.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo4.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo4.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo4PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo4);

        jLittleGrupo5.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo5.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo5.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo5.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo5PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo5);

        jLittleGrupo6.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo6.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo6.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo6.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo6PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo6);

        jLittleGrupo7.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo7.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo7.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo7.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo7PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo7);

        jLittleGrupo8.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo8.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo8.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo8.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo8PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo8);

        jLittleGrupo9.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo9.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo9.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo9.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo9PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo9);

        jLittleGrupo10.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo10.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo10.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo10.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo10PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo10);

        jLittleGrupo11.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo11.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo11.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo11.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo11PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo11);

        jLittleGrupo12.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo12.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo12.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo12.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo12PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo12);

        jLittleGrupo13.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo13.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo13.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo13.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo13PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo13);

        jLittleGrupo14.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo14.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo14.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo14.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo14PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo14);

        jLittleGrupo15.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo15.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo15.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo15.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo15PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo15);

        jLittleGrupo16.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo16.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo16.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo16.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo16PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo16);

        jLittleGrupo17.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo17.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo17.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo17.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo17PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo17);

        jLittleGrupo18.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo18.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo18.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo18.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo18PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo18);

        jLittleGrupo19.setMaximumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo19.setMinimumSize(new java.awt.Dimension(480, 120));
        jLittleGrupo19.setPreferredSize(new java.awt.Dimension(480, 120));
        jLittleGrupo19.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLittleGrupo19PropertyChange(evt);
            }
        });
        jPanel1.add(jLittleGrupo19);

        jScrollPane1.setViewportView(jPanel1);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void jLittleGrupo1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo1PropertyChange
        this.ejecutaEvento(evt,1);
    }//GEN-LAST:event_jLittleGrupo1PropertyChange

    private void jLittleGrupo2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo2PropertyChange
        this.ejecutaEvento(evt,2);
    }//GEN-LAST:event_jLittleGrupo2PropertyChange

    private void jLittleGrupo3PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo3PropertyChange
        this.ejecutaEvento(evt,3);
    }//GEN-LAST:event_jLittleGrupo3PropertyChange

    private void jLittleGrupo4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo4PropertyChange
        this.ejecutaEvento(evt,4);
    }//GEN-LAST:event_jLittleGrupo4PropertyChange

    private void jLittleGrupo5PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo5PropertyChange
        this.ejecutaEvento(evt,5);
    }//GEN-LAST:event_jLittleGrupo5PropertyChange

    private void jPanel1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jPanel1PropertyChange
        if(evt.getPropertyName().equals("SelectedFormaInLV0")){
            this.firePropertyChange("SelectedFormaInLV1",evt.getOldValue(),evt.getNewValue());
        }
}//GEN-LAST:event_jPanel1PropertyChange

    private void jLittleGrupo6PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo6PropertyChange
        this.ejecutaEvento(evt,6);
    }//GEN-LAST:event_jLittleGrupo6PropertyChange

    private void jLittleGrupo7PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo7PropertyChange
        this.ejecutaEvento(evt,7);
    }//GEN-LAST:event_jLittleGrupo7PropertyChange

    private void jLittleGrupo8PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo8PropertyChange
        this.ejecutaEvento(evt,8);
    }//GEN-LAST:event_jLittleGrupo8PropertyChange

    private void jLittleGrupo9PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo9PropertyChange
        this.ejecutaEvento(evt,9);
    }//GEN-LAST:event_jLittleGrupo9PropertyChange

    private void jLittleGrupo10PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo10PropertyChange
        this.ejecutaEvento(evt,10);
    }//GEN-LAST:event_jLittleGrupo10PropertyChange

    private void jLittleGrupo11PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo11PropertyChange
        this.ejecutaEvento(evt,11);
    }//GEN-LAST:event_jLittleGrupo11PropertyChange

    private void jLittleGrupo12PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo12PropertyChange
        this.ejecutaEvento(evt,12);
    }//GEN-LAST:event_jLittleGrupo12PropertyChange

    private void jLittleGrupo13PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo13PropertyChange
        this.ejecutaEvento(evt,13);
    }//GEN-LAST:event_jLittleGrupo13PropertyChange

    private void jLittleGrupo14PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo14PropertyChange
        this.ejecutaEvento(evt,14);
    }//GEN-LAST:event_jLittleGrupo14PropertyChange

    private void jLittleGrupo15PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo15PropertyChange
        this.ejecutaEvento(evt,15);
    }//GEN-LAST:event_jLittleGrupo15PropertyChange

    private void jLittleGrupo16PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo16PropertyChange
        this.ejecutaEvento(evt,16);
    }//GEN-LAST:event_jLittleGrupo16PropertyChange

    private void jLittleGrupo17PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo17PropertyChange
        this.ejecutaEvento(evt,17);
    }//GEN-LAST:event_jLittleGrupo17PropertyChange

    private void jLittleGrupo18PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo18PropertyChange
        this.ejecutaEvento(evt,18);
    }//GEN-LAST:event_jLittleGrupo18PropertyChange

    private void jLittleGrupo19PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLittleGrupo19PropertyChange
        this.ejecutaEvento(evt,19);
    }//GEN-LAST:event_jLittleGrupo19PropertyChange
    public void inicializa(Vector<Grupo> grupos,Vector<Forma> formas){
        this.jLittleGrupo1.setGrupo(grupos.get(0),formas);
        this.jLittleGrupo2.setGrupo(grupos.get(1),formas);
        this.jLittleGrupo3.setGrupo(grupos.get(2),formas);
        this.jLittleGrupo4.setGrupo(grupos.get(3),formas);
        this.jLittleGrupo5.setGrupo(grupos.get(4),formas);
        this.jLittleGrupo6.setGrupo(grupos.get(5),formas);
        this.jLittleGrupo7.setGrupo(grupos.get(6),formas);
        this.jLittleGrupo8.setGrupo(grupos.get(7),formas);
        this.jLittleGrupo9.setGrupo(grupos.get(8),formas);
        this.jLittleGrupo10.setGrupo(grupos.get(9),formas);
        this.jLittleGrupo11.setGrupo(grupos.get(10),formas);
        this.jLittleGrupo12.setGrupo(grupos.get(11),formas);
        this.jLittleGrupo13.setGrupo(grupos.get(12),formas);
        this.jLittleGrupo14.setGrupo(grupos.get(13),formas);
        this.jLittleGrupo15.setGrupo(grupos.get(14),formas);
        this.jLittleGrupo16.setGrupo(grupos.get(15),formas);
        this.jLittleGrupo17.setGrupo(grupos.get(16),formas);
        this.jLittleGrupo18.setGrupo(grupos.get(17),formas);
        this.jLittleGrupo19.setGrupo(grupos.get(18),formas);
        /*
        try {
            Vector<Grupo> grupos=new Grupo(conector).find("orden");
            this.jLittleGrupo1.setGrupo(grupos.get(0));
            this.jLittleGrupo2.setGrupo(grupos.get(1));
            this.jLittleGrupo3.setGrupo(grupos.get(2));
            this.jLittleGrupo4.setGrupo(grupos.get(3));
            this.jLittleGrupo5.setGrupo(grupos.get(4));
            this.jLittleGrupo6.setGrupo(grupos.get(5));
            this.jLittleGrupo7.setGrupo(grupos.get(6));
            this.jLittleGrupo8.setGrupo(grupos.get(7));
            this.jLittleGrupo9.setGrupo(grupos.get(8));
            this.jLittleGrupo10.setGrupo(grupos.get(9));
            this.jLittleGrupo11.setGrupo(grupos.get(10));
            this.jLittleGrupo12.setGrupo(grupos.get(11));
            this.jLittleGrupo13.setGrupo(grupos.get(12));
            this.jLittleGrupo14.setGrupo(grupos.get(13));
            this.jLittleGrupo15.setGrupo(grupos.get(14));
            this.jLittleGrupo16.setGrupo(grupos.get(15));
            this.jLittleGrupo17.setGrupo(grupos.get(16));
            this.jLittleGrupo18.setGrupo(grupos.get(17));
            this.jLittleGrupo19.setGrupo(grupos.get(18));
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex);
        }
         *
         */
    }
    private void setSelected(int newSelectedJLittleGrupo,int newSelectedJLittleForma){
            int oldSelectedJLittleGrupo=this.getSelectedJLittleGrupo();
            this.setSelectedJLittleGrupo(newSelectedJLittleGrupo);
            int oldSelectedJLittleForma=this.getSelectedJLittleForma();
            this.setSelectedJLittleForma(newSelectedJLittleForma);
            Forma oldForma=null;
            if((oldSelectedJLittleGrupo>0)&&(oldSelectedJLittleForma>0)){
                oldForma=this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(oldSelectedJLittleForma).getForma();
            }
            Forma newForma=this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(newSelectedJLittleForma).getForma();
        if(oldSelectedJLittleGrupo==newSelectedJLittleGrupo){//No he cambiado de grupo
                if(oldSelectedJLittleForma>0){
                    this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(oldSelectedJLittleForma).setSelected(false);
                    this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(oldSelectedJLittleForma).colorea();
                }
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(newSelectedJLittleForma).setSelected(true);
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(newSelectedJLittleForma).colorea();
                //
                this._selectedForma=newForma;
            }else{
                if(oldSelectedJLittleGrupo>0){
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).setSelected(false);
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(1).setSelectedGrupo(false);
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(2).setSelectedGrupo(false);
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(3).setSelectedGrupo(false);
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(4).setSelectedGrupo(false);
                    if(oldSelectedJLittleForma>0){
                        this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(oldSelectedJLittleForma).setSelected(false);
                    }
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(1).colorea();
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(2).colorea();
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(3).colorea();
                    this.getJLittleGrupo(oldSelectedJLittleGrupo).getJLittleForma(4).colorea();
                }
                //
                this.getJLittleGrupo(newSelectedJLittleGrupo).setSelected(true);
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(1).setSelectedGrupo(true);
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(2).setSelectedGrupo(true);
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(3).setSelectedGrupo(true);
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(4).setSelectedGrupo(true);
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(newSelectedJLittleForma).setSelected(true);
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(1).colorea();
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(2).colorea();
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(3).colorea();
                this.getJLittleGrupo(newSelectedJLittleGrupo).getJLittleForma(4).colorea();
                //
                this._selectedForma=newForma;
                }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo1;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo10;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo11;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo12;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo13;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo14;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo15;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo16;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo17;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo18;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo19;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo2;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo3;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo4;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo5;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo6;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo7;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo8;
    private es.atareao.ferraplan.gui.JLittleGrupo jLittleGrupo9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    private Forma _selectedForma=null;
    private int _selectedJLittleGrupo=0;
    private int _selectedJLittleForma=0;


    /**
     * @return the _SelectedForma
     */
    public Forma getSelectedForma() {
        return _selectedForma;
    }

    /**
     * @param SelectedForma the _SelectedForma to set
     */
    public void setSelectedForma(Forma selectedForma) {
        this._selectedForma = selectedForma;
    }

    public void setSelectedId(String id){
        for(int grupo=1;grupo<20;grupo++){
            for(int forma=1;forma<5;forma++){
                Forma sforma=this.getJLittleGrupo(grupo).getJLittleForma(forma).getForma();
                if((sforma!=null)&&(sforma.getId().equals(id))){
                    this.setSelected(grupo,forma);
                    return;
                }
            }
        }
    }
    public void selectFirstElement(){
        this.setSelected(1,1);
    }
    /**
     * @return the _selectedJLittleGrupo
     */
    public int getSelectedJLittleGrupo() {
        return _selectedJLittleGrupo;
    }

    /**
     * @param selectedJLittleGrupo the _selectedJLittleGrupo to set
     */
    public void setSelectedJLittleGrupo(int selectedJLittleGrupo) {
        this._selectedJLittleGrupo = selectedJLittleGrupo;
    }

    /**
     * @return the _selectedJLittleForma
     */
    public int getSelectedJLittleForma() {
        return _selectedJLittleForma;
    }

    /**
     * @param selectedJLittleForma the _selectedJLittleForma to set
     */
    public void setSelectedJLittleForma(int selectedJLittleForma) {
        this._selectedJLittleForma = selectedJLittleForma;
    }
}
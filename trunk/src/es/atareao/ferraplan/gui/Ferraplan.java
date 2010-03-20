/*
 * Ferraplan.java
 *
 * File created on 05-dic-2009, 20:37:40
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

import com.eteks.parser.CompilationException;
import com.eteks.parser.CompiledFunction;
import com.eteks.parser.FunctionParser;
import es.atareao.alejandria.gui.AboutDialogo;
import es.atareao.alejandria.gui.AbstractTableModelListener;
import es.atareao.alejandria.gui.ErrorDialog;
import es.atareao.alejandria.lib.Convert;
import es.atareao.alejandria.gui.JNumericFieldTableCellEditor;
import es.atareao.alejandria.gui.JNumericFieldTableCellRenderer;
import es.atareao.alejandria.lib.AppUtil;
import es.atareao.alejandria.lib.FileUtils;
import es.atareao.alejandria.lib.GeneradorUUID;
import es.atareao.alejandria.lib.INIFile;
import es.atareao.alejandria.lib.ImageUtils;
import es.atareao.alejandria.lib.Preferencias;
import es.atareao.ferraplan.lib.Acero;
import es.atareao.queensboro.db.Condition;
import es.atareao.queensboro.file.CompactDb;
import es.atareao.queensboro.gui.JWrapperComboBoxTableCellEditor;
import es.atareao.queensboro.gui.JWrapperComboBoxTableCellRenderer;
import es.atareao.queensboro.val.ValidationException;
import es.atareao.ferraplan.lib.Barra;
import es.atareao.ferraplan.lib.Diametro;
import es.atareao.ferraplan.lib.Elaborador;
import es.atareao.ferraplan.lib.Elemento;
import es.atareao.ferraplan.lib.Forma;
import es.atareao.ferraplan.lib.Grupo;
import es.atareao.ferraplan.lib.Hormigon;
import es.atareao.ferraplan.lib.Montador;
import es.atareao.ferraplan.lib.Obra;
import es.atareao.ferraplan.lib.Parte;
import es.atareao.ferraplan.lib.Revision;
import es.atareao.ferraplan.lib.Zona;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author atareao
 */
public class Ferraplan extends javax.swing.JFrame {
    //
    //********************************CONSTANTES********************************
    //
    public static final long serialVersionUID=0L;
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    //
    public final static int OP_ADD=0;
    public final static int OP_EDIT=1;
    public final static int OP_DELETE=2;
    public final static int OP_VIEW=3;
    public final static int OP_PRINTPREVIEW=4;
    public final static int OP_PRINT=5;
    public final static int OP_PARTE=6;
    public final static int OP_DUPLICATE=7;
    public final static int OP_PRINTSCHEMES=8;
    //
    private final static int AC_NUEVO=0;
    private final static int AC_ABRIR=1;
    private final static int AC_CERRAR=2;
    private final static int AC_GUARDAR=3;
    private final static int AC_GUARDAR_COMO=4;
    //
    private final static String VERSION="01.02.20100218";
    private final static String APPNAME="Ferraplan";
    private final static String EXT="fer";
    private final static String AUTHOR="Lorenzo Carbonell";
    private final static String EMAIL="atareao@atareao.es";
    private final static String YEAR="2010";

    /** Creates new form Ferraplan */
    public Ferraplan(INIFile ini){
        Cursor oldCursor=this.getCursor();
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        initComponents();
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(Convert.toInt(pantalla.width*0.95), Convert.toInt(pantalla.height*0.95));
        this.setLocationRelativeTo(null);
        this.existsTemporalDir();
        this.setPreferencias(new Preferencias(ini));
        this.setCompactDb(new CompactDb(this,APPNAME,EXT,"/es/atareao/ferraplan/sql/ferraplan.sql",true));
        AppUtil.verificaVersion(this,APPNAME,VERSION);
        this.loadRecentFiles();
        this.jTable1.setUI(new DragDropRowTableUIOrdered());
        _lap = new java.util.Date().getTime();
        _lap2 = new java.util.Date().getTime();
        //
        //Ayuda
        //
        // Carga el fichero de ayuda
        try {
            ClassLoader objClassLoader = Ferraplan.class.getClassLoader();
            String strHelpFileName = "Ferraplan.hs";
            URL hsURL = HelpSet.findHelpSet(objClassLoader, strHelpFileName);
            HelpSet helpset = new HelpSet(getClass().getClassLoader(), hsURL);
            HelpBroker hb = helpset.createHelpBroker();
            hb.enableHelpOnButton(this.jMenuItemAyuda, "top", helpset);
            hb.enableHelpKey(this.jSplitPane1,"top", helpset);
        }catch (HelpSetException ex) {
            ErrorDialog.manejaError(ex);
        }
        //
        setCursor(oldCursor);
    }
    //
    //********************************METODOS***********************************
    //
    ////
    private void inicializa(){
        DefaultMutableTreeNode dmtnArbol=new DefaultMutableTreeNode("Obras");
        try {
            this.setBarra(new Barra(this.getCompactDb().getConector()));
            Obra obra=new Obra(this.getCompactDb().getConector());
            Zona zona=new Zona(this.getCompactDb().getConector());
            Elemento elemento=new Elemento(this.getCompactDb().getConector());
            Parte parte=new Parte(this.getCompactDb().getConector());
            Revision revision=new Revision(this.getCompactDb().getConector());
            for(Obra this_obra:obra.find("orden")){
                DefaultMutableTreeNode dmtnObra=new DefaultMutableTreeNode(this_obra);
                for(Zona this_zona:zona.find(new Condition("obra_id",this_obra.getId(),Condition.IGUAL),"orden")){
                    DefaultMutableTreeNode dmtnZona=new DefaultMutableTreeNode(this_zona);
                    for(Elemento this_elemento:elemento.find(new Condition("zona_id",this_zona.getId(),Condition.IGUAL),"orden")){
                        DefaultMutableTreeNode dmtnElemento=new DefaultMutableTreeNode(this_elemento);
                        for(Parte this_parte:parte.find(new Condition("elemento_id",this_elemento.getId(),Condition.IGUAL),"orden")){
                            DefaultMutableTreeNode dmtnParte=new DefaultMutableTreeNode(this_parte);
                            for(Revision this_revision:revision.find(new Condition("parte_id",this_parte.getId(),Condition.IGUAL),"orden")){
                                DefaultMutableTreeNode dmtnRevision=new DefaultMutableTreeNode(this_revision);
                                dmtnParte.add(dmtnRevision);
                            }
                            dmtnElemento.add(dmtnParte);
                        }
                        dmtnZona.add(dmtnElemento);
                    }
                    dmtnObra.add(dmtnZona);
                }
                dmtnArbol.add(dmtnObra);
            }
            jTree1.setModel(new DefaultTreeModel(dmtnArbol));
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex,false);
        }
        Diametro diametro;
        try {
            diametro = new Diametro(this.getCompactDb().getConector());
            Vector<Diametro> diametros=diametro.findAll();
            jcbtcr_diametro.setElements(diametros);
            jcbtce_diametro.setElements(diametros);
            Grupo grupo=new Grupo(this.getCompactDb().getConector());
            Forma forma=new Forma(this.getCompactDb().getConector());
            Vector<Grupo> grupos=grupo.find("orden");
            Vector<Forma> formas=forma.find("orden");
            jfctce_forma.inicializa(grupos,formas);
            jfctcr_forma.inicializa(grupos,formas);
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex);
        }
        DefaultTreeCellRenderer dtcr=new DefaultTreeCellRenderer(){
            @Override
            public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel,boolean expanded,boolean leaf,int row,boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row,hasFocus);
                ImageIcon icon;
                if((value!=null)&&(value instanceof DefaultMutableTreeNode)){
                        Object object=((DefaultMutableTreeNode)value).getUserObject();
                        if(object instanceof Obra){
                            icon=new ImageIcon(getClass().getResource("/es/atareao/ferraplan/img/obra.png"));
                            setIcon(icon);
                            return this;
                        }
                        if(object instanceof Zona){
                            icon=new ImageIcon(getClass().getResource("/es/atareao/ferraplan/img/edificio.png"));
                            setIcon(icon);
                            return this;
                        }
                        if(object instanceof Elemento){
                            icon=new ImageIcon(getClass().getResource("/es/atareao/ferraplan/img/zapata.png"));
                            setIcon(icon);
                            return this;
                        }
                        if(object instanceof Parte){
                            icon=new ImageIcon(getClass().getResource("/es/atareao/ferraplan/img/barra.png"));
                            setIcon(icon);
                            return this;
                        }
                        if(object instanceof Revision){
                            icon=new ImageIcon(getClass().getResource("/es/atareao/ferraplan/img/revision.png"));
                            setIcon(icon);
                            return this;
                        }
                }
                return this;
            }
        };
        this.jTree1.setCellRenderer(dtcr);
        this.refresca();
    }
    private void setColumnWidth(int column,int width){
        this.jTable1.getColumnModel().getColumn(column).setPreferredWidth(width);
        this.jTable1.getColumnModel().getColumn(column).setMinWidth(width);
        this.jTable1.getColumnModel().getColumn(column).setMaxWidth(width);
        this.jTable1.getColumnModel().getColumn(column).setWidth(width);
    }
    private void refresca(){
        if((this.jTree1.getSelectionPath()!=null)&&(this.jTree1.getSelectionPath().getPathCount()==6)){
            final Revision revision = (Revision) ((DefaultMutableTreeNode)this.jTree1.getSelectionPath().getLastPathComponent()).getUserObject();
            DefaultTableModel tableModel = new DefaultTableModel() {

                private static final long serialVersionUID = 0L;

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    if ((columnIndex == 0) || (columnIndex == 1) || (columnIndex == 16) || (columnIndex == 17) || (columnIndex == 18)) {
                        return false;
                    }
                    return true;
                }

                @Override
                public Class getColumnClass(int columnIndex) {
                    if (dataVector == null) {
                        return Object.class;
                    }
                    Object[] row = ((Vector) dataVector.get(0)).toArray();
                    return row[columnIndex] == null ? Object.class : row[columnIndex].getClass();
                }
            };
            //Números                        0       1     2     3     4      5      6    7    8    9    10   11   12   13   14   15    16      17       18      19
            String[] columnNamesBarras = {"ORDEN", "ID", "REF", "DN", "N", "FORMA", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "LONG", "PESO", "TOTAL", "NOTA"};
            final int nColumns = columnNamesBarras.length;
            tableModel.setColumnIdentifiers(columnNamesBarras);
            tableModel.setColumnCount(nColumns);
            tableModel.addTableModelListener(new AbstractTableModelListener() {
                @Override
                protected void cellChanged(TableModel tableModel, int fila, int columna) {
                    if(((DragDropRowTableUIOrdered)jTable1.getUI()).isDraggingRow()){
                        return;
                    }
                    if((columna==16)||(columna==17)||(columna==18)) {
                        return;
                    }
                    getCompactDb().setModificado(true);
                    setModificado(true);
                    double cantidad=Convert.toDouble(tableModel.getValueAt(fila,4));
                    double a=Convert.toDouble(tableModel.getValueAt(fila,6));
                    double b=Convert.toDouble(tableModel.getValueAt(fila,7));
                    double c=Convert.toDouble(tableModel.getValueAt(fila,8));
                    double d=Convert.toDouble(tableModel.getValueAt(fila,9));
                    double e=Convert.toDouble(tableModel.getValueAt(fila,10));
                    double f=Convert.toDouble(tableModel.getValueAt(fila,11));
                    double g=Convert.toDouble(tableModel.getValueAt(fila,12));
                    double h=Convert.toDouble(tableModel.getValueAt(fila,13));
                    double i=Convert.toDouble(tableModel.getValueAt(fila,14));
                    double j=Convert.toDouble(tableModel.getValueAt(fila,15));
                    try{
                        Diametro dn=new Diametro(getCompactDb().getConector());
                        Forma fm=new Forma(getCompactDb().getConector());
                        dn.setId((String)tableModel.getValueAt(fila,3));
                        dn.read();
                        fm.setId((String)tableModel.getValueAt(fila,5));
                        fm.read();
                        double peso_diametro=Convert.toDouble(dn.getValue("peso"));
                        if(fm!=null){
                            String formula=fm.getValue("formula");
                            double longitud=calcula_longitud(formula,a,b,c,d,e,f,g,h,i,j);
                            double peso_unitario=peso_diametro*longitud/100;
                            double peso_total=peso_unitario*cantidad;
                            tableModel.setValueAt(Convert.toString(longitud), fila,16);
                            tableModel.setValueAt(Convert.toString(peso_unitario), fila,17);
                            tableModel.setValueAt(Convert.toString(peso_total), fila,18);
                        }
                    }catch(SQLException ex){
                        ErrorDialog.manejaError(ex);
                    }
                    saveRow(fila);
                }
                //}
            });
            //
            this.jTable1.setModel(tableModel);
            this.jTable1.setRowHeight(150);
            //diametro
            this.jTable1.getColumnModel().getColumn(3).setCellEditor(jcbtce_diametro);
            this.jTable1.getColumnModel().getColumn(3).setCellRenderer(jcbtcr_diametro);
            //cantidad
            this.jTable1.getColumnModel().getColumn(4).setCellRenderer(new JNumericFieldTableCellRenderer(0));
            this.jTable1.getColumnModel().getColumn(4).setCellEditor(new JNumericFieldTableCellEditor(0));
            //forma
            this.jTable1.getColumnModel().getColumn(5).setCellEditor(jfctce_forma);
            this.jTable1.getColumnModel().getColumn(5).setCellRenderer(jfctcr_forma);
            //a
            this.jTable1.getColumnModel().getColumn(6).setCellRenderer(new JNumericFieldTableCellRenderer(0));
            this.jTable1.getColumnModel().getColumn(6).setCellEditor(new JNumericFieldTableCellEditor(0));
            //b
            this.jTable1.getColumnModel().getColumn(7).setCellRenderer(new JNumericFieldTableCellRenderer(0));
            this.jTable1.getColumnModel().getColumn(7).setCellEditor(new JNumericFieldTableCellEditor(0));
            //c
            this.jTable1.getColumnModel().getColumn(8).setCellRenderer(new JNumericFieldTableCellRenderer(0));
            this.jTable1.getColumnModel().getColumn(8).setCellEditor(new JNumericFieldTableCellEditor(0));
            //d
            this.jTable1.getColumnModel().getColumn(9).setCellRenderer(new JNumericFieldTableCellRenderer(0));
            this.jTable1.getColumnModel().getColumn(9).setCellEditor(new JNumericFieldTableCellEditor(0));
            //e
            this.jTable1.getColumnModel().getColumn(10).setCellRenderer(new JNumericFieldTableCellRenderer(0));
            this.jTable1.getColumnModel().getColumn(10).setCellEditor(new JNumericFieldTableCellEditor(0));
            //f
            this.jTable1.getColumnModel().getColumn(11).setCellRenderer(new JNumericFieldTableCellRenderer(0));
            this.jTable1.getColumnModel().getColumn(11).setCellEditor(new JNumericFieldTableCellEditor(0));
            //g
            this.jTable1.getColumnModel().getColumn(12).setCellRenderer(new JNumericFieldTableCellRenderer(0));
            this.jTable1.getColumnModel().getColumn(12).setCellEditor(new JNumericFieldTableCellEditor(0));
            //h
            this.jTable1.getColumnModel().getColumn(13).setCellRenderer(new JNumericFieldTableCellRenderer(0));
            this.jTable1.getColumnModel().getColumn(13).setCellEditor(new JNumericFieldTableCellEditor(0));
            //i
            this.jTable1.getColumnModel().getColumn(14).setCellRenderer(new JNumericFieldTableCellRenderer(0));
            this.jTable1.getColumnModel().getColumn(14).setCellEditor(new JNumericFieldTableCellEditor(0));
            //j
            this.jTable1.getColumnModel().getColumn(15).setCellRenderer(new JNumericFieldTableCellRenderer(0));
            this.jTable1.getColumnModel().getColumn(15).setCellEditor(new JNumericFieldTableCellEditor(0));
            //longitud
            this.jTable1.getColumnModel().getColumn(16).setCellRenderer(new JNumericFieldTableCellRenderer(0));
            //peso_unitario
            this.jTable1.getColumnModel().getColumn(17).setCellRenderer(new JNumericFieldTableCellRenderer(2));
            //peso_total
            this.jTable1.getColumnModel().getColumn(18).setCellRenderer(new JNumericFieldTableCellRenderer(2));
            //
            this.setColumnWidth(0, 0);
            this.setColumnWidth(1, 0);
            this.setColumnWidth(2, 40);
            this.setColumnWidth(3, 60);
            this.setColumnWidth(4, 40);
            this.setColumnWidth(5, 200);
            for (int contador = 6; contador < 13; contador++) {
                this.setColumnWidth(contador, 40);
            }
            this.setColumnWidth(13, 0);//No se usa por ahora
            this.setColumnWidth(14, 0);//No se usa por ahora
            this.setColumnWidth(15, 0);//No se usa por ahora
            this.setColumnWidth(16, 50);
            this.setColumnWidth(17, 50);
            this.setColumnWidth(18, 50);
            //
            //
            try {
                for (Barra this_barra : this.getBarra().view3(revision.getId())) {
                    Object[] fila = {this_barra.getValue("orden"), this_barra.getValue("id"), this_barra.getValue("referencia"), this_barra.getValue("diametro_id"), this_barra.getValue("cantidad"), this_barra.getValue("forma_id"), this_barra.getValue("a"), this_barra.getValue("b"), this_barra.getValue("c"), this_barra.getValue("d"), this_barra.getValue("e"), this_barra.getValue("f"), this_barra.getValue("g"), this_barra.getValue("h"), this_barra.getValue("i"), this_barra.getValue("j"), this_barra.getValue("longitud"), this_barra.getValue("peso_unitario"), this_barra.getValue("peso_total"), this_barra.getValue("comentario")};
                    tableModel.addRow(fila);
                }
            } catch (SQLException ex) {
                ErrorDialog.manejaError(ex, false);
            }
            if((this.jTable1.getRowCount()==0)||(((String)this.jTable1.getValueAt(0,0)).equals(""))){
                this.newRow();
            }
        }
    }
    private void newRow(){
        int fila=this.jTable1.getRowCount()-1;
        if (fila<0){
            fila=0;
        }
        Object[] filadata={
            Convert.toString(fila),//Orden - 0
            "",//id - 1
            "",//referencia - 2
            "1",//diametro_id - 3
            "0",//cantidad - 4
            "1",//forma_id - 5
            "0",//a - 6
            "0",//b - 7
            "0",//c - 8
            "0",//d - 9
            "0",//e - 10
            "0",//f - 11
            "0",//g - 12
            "0",//h - 13
            "0",//i - 14
            "0",//j - 15
            "0",//longitud - 16
            "0",//peso_unitario - 17
            "0",//peso_total - 18
            ""//COMENTARIO
        };
        ((DefaultTableModel)this.jTable1.getModel()).addRow(filadata);
        this.saveRow(fila);

    }
    private void saveRow(int fila){
        Revision revision = (Revision) ((DefaultMutableTreeNode)this.jTree1.getSelectionPath().getLastPathComponent()).getUserObject();
        DefaultTableModel tableModel=(DefaultTableModel)this.jTable1.getModel();
        getBarra().setId((String) tableModel.getValueAt(fila,1));
        getBarra().setValue("id", (String) tableModel.getValueAt(fila,1));
        getBarra().setValue("orden", (String) tableModel.getValueAt(fila,0));
        getBarra().setValue("revision_id", revision.getId());
        getBarra().setValue("referencia", (String)tableModel.getValueAt(fila,2));
        getBarra().setValue("forma_id", (String) tableModel.getValueAt(fila,5));
        getBarra().setValue("diametro_id", (String) tableModel.getValueAt(fila,3));
        getBarra().setValue("cantidad", Convert.toString(tableModel.getValueAt(fila,4)));
        getBarra().setValue("comentario", Convert.toString(tableModel.getValueAt(fila,19)));
        getBarra().setValue("a", Convert.toString(tableModel.getValueAt(fila,6)));
        getBarra().setValue("b", Convert.toString(tableModel.getValueAt(fila,7)));
        getBarra().setValue("c", Convert.toString(tableModel.getValueAt(fila,8)));
        getBarra().setValue("d", Convert.toString(tableModel.getValueAt(fila,9)));
        getBarra().setValue("e", Convert.toString(tableModel.getValueAt(fila,10)));
        getBarra().setValue("f", Convert.toString(tableModel.getValueAt(fila,11)));
        getBarra().setValue("g", Convert.toString(tableModel.getValueAt(fila,12)));
        getBarra().setValue("h", Convert.toString(tableModel.getValueAt(fila,13)));
        getBarra().setValue("i", Convert.toString(tableModel.getValueAt(fila,14)));
        getBarra().setValue("j", Convert.toString(tableModel.getValueAt(fila,15)));
        getBarra().setValue("longitud", Convert.toString(tableModel.getValueAt(fila,16)));
        getBarra().setValue("peso_unitario", Convert.toString(tableModel.getValueAt(fila,17)));
        getBarra().setValue("peso_total", Convert.toString(tableModel.getValueAt(fila,18)));
        //
        try {
            if (getBarra().validate()) {
                Object valor=tableModel.getValueAt(fila, 1);
                if ((valor==null)||(valor.equals(""))) {
                    if (getBarra().insert()) {
                        tableModel.setValueAt(Convert.toString(getBarra().getLastInsertId()), fila, 1);
                    }
                } else {
                    getBarra().setId((String) tableModel.getValueAt(fila,1));
                    if (!getBarra().update()) {
                    }
                }
            }
        } catch (ValidationException ex) {
            ErrorDialog.manejaError(ex, false);
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex, false);
        }
    }
    ////
    private void operacionTree(int operacion){
        if(this.jTree1.getSelectionPath()!=null){
            DefaultMutableTreeNode nodoSeleccionado=(DefaultMutableTreeNode)this.jTree1.getSelectionPath().getLastPathComponent();
            DefaultMutableTreeNode nodoPadre=(DefaultMutableTreeNode)nodoSeleccionado.getParent();
            try {
                Obra obra=new Obra(this.getCompactDb().getConector());
                Zona zona=new Zona(this.getCompactDb().getConector());
                Elemento elemento=new Elemento(this.getCompactDb().getConector());
                Parte parte=new Parte(this.getCompactDb().getConector());
                Revision revision=new Revision(this.getCompactDb().getConector());
                int pathCount=this.jTree1.getSelectionPath().getPathCount();
                switch (operacion){
                    case OP_ADD:
                        switch (pathCount){
                            case 1:
                                DialogObra mo=new DialogObra(this,operacion,obra);
                                mo.setVisible(true);
                                if(mo.getReturnStatus()==DialogObra.RET_OK){
                                    if(mo.getObra().insert()){
                                        mo.getObra().setId(Convert.toString(mo.getObra().getLastInsertId()));
                                        this.addNodo(new DefaultMutableTreeNode(mo.getObra()));
                                    }
                                }
                                break;
                            case 2:
                                zona.setValue("obra_id",((Obra)nodoSeleccionado.getUserObject()).getId());
                                DialogZona mz=new DialogZona(this,operacion,zona);
                                mz.setVisible(true);
                                if(mz.getReturnStatus()==DialogZona.RET_OK){
                                    if(mz.getZona().insert()){
                                        mz.getZona().setId(Convert.toString(mz.getZona().getLastInsertId()));
                                        this.addNodo(new DefaultMutableTreeNode(mz.getZona()));
                                    }
                                }
                                break;
                            case 3:
                                elemento.setValue("zona_id",((Zona)nodoSeleccionado.getUserObject()).getId());
                                DialogElemento me=new DialogElemento(this,operacion,elemento);
                                me.setVisible(true);
                                if(me.getReturnStatus()==DialogElemento.RET_OK){
                                    if(me.getElemento().insert()){
                                        me.getElemento().setId(Convert.toString(me.getElemento().getLastInsertId()));
                                        this.addNodo(new DefaultMutableTreeNode(me.getElemento()));
                                    }
                                }
                                break;
                            case 4:
                                parte.setValue("elemento_id",((Elemento)nodoSeleccionado.getUserObject()).getId());
                                DialogParte mp=new DialogParte(this,operacion,parte);
                                mp.setVisible(true);
                                if(mp.getReturnStatus()==DialogParte.RET_OK){
                                    if(mp.getParte().insert()){
                                        mp.getParte().setId(Convert.toString(mp.getParte().getLastInsertId()));
                                        this.addNodo(new DefaultMutableTreeNode(mp.getParte()));
                                    }
                                }
                                break;
                            case 5:
                                revision.setValue("parte_id",((Parte)nodoSeleccionado.getUserObject()).getId());
                                Vector<Revision> revisiones=revision.find(new Condition("parte_id",((Parte)nodoSeleccionado.getUserObject()).getId(),Condition.IGUAL),"id");
                                Revision u_revision=null;
                                if(revisiones.size()>0){
                                    u_revision=revisiones.get(revisiones.size()-1);
                                }
                                if(u_revision!=null){
                                    long long_ur=Convert.toLong(u_revision.getValue("revision"))+1;
                                    revision.setValue("revision",Convert.toString(long_ur));
                                }else{
                                    revision.setValue("revision","0");
                                }
                                DialogRevision dr=new DialogRevision(this,operacion,revision);
                                dr.setVisible(true);
                                if(dr.getReturnStatus()==DialogRevision.RET_OK){
                                    if(dr.getRevision().insert()){
                                        dr.getRevision().setId(Convert.toString(dr.getRevision().getLastInsertId()));
                                        this.addNodo(new DefaultMutableTreeNode(dr.getRevision()));
                                    }
                                }
                                break;
                        }
                        break;
                    case OP_EDIT:
                        switch (pathCount){
                            case 2:
                                obra=(Obra)nodoSeleccionado.getUserObject();
                                DialogObra mo=new DialogObra(this,operacion,obra);
                                mo.setVisible(true);
                                if(mo.getReturnStatus()==DialogObra.RET_OK){
                                    if(mo.getObra().update()){
                                        this.editNodo(nodoSeleccionado,new DefaultMutableTreeNode(mo.getObra()));
                                    }
                                }
                                break;
                            case 3:
                                zona=(Zona)nodoSeleccionado.getUserObject();
                                DialogZona mz=new DialogZona(this,operacion,zona);
                                mz.setVisible(true);
                                if(mz.getReturnStatus()==DialogZona.RET_OK){
                                    if(mz.getZona().update()){
                                        this.editNodo(nodoSeleccionado,new DefaultMutableTreeNode(mz.getZona()));
                                    }
                                }
                                break;
                            case 4:
                                elemento=(Elemento)nodoSeleccionado.getUserObject();
                                DialogElemento me=new DialogElemento(this,operacion,elemento);
                                me.setVisible(true);
                                if(me.getReturnStatus()==DialogElemento.RET_OK){
                                    if(me.getElemento().update()){
                                        this.editNodo(nodoSeleccionado,new DefaultMutableTreeNode(me.getElemento()));
                                    }
                                }
                                break;
                            case 5:
                                parte=(Parte)nodoSeleccionado.getUserObject();
                                DialogParte mp=new DialogParte(this,operacion,parte);
                                mp.setVisible(true);
                                if(mp.getReturnStatus()==DialogParte.RET_OK){
                                    if(mp.getParte().update()){
                                        this.editNodo(nodoSeleccionado,new DefaultMutableTreeNode(mp.getParte()));
                                    }
                                }
                                break;
                            case 6:
                                revision=(Revision)nodoSeleccionado.getUserObject();
                                DialogRevision dr=new DialogRevision(this,operacion,revision);
                                dr.setVisible(true);
                                if(dr.getReturnStatus()==DialogRevision.RET_OK){
                                    if(dr.getRevision().update()){
                                        this.editNodo(nodoSeleccionado,new DefaultMutableTreeNode(dr.getRevision()));
                                    }
                                }
                                break;
                        }
                        break;
                    case OP_VIEW:
                        switch (pathCount){
                            case 2:
                                obra=(Obra)nodoSeleccionado.getUserObject();
                                DialogObra mo=new DialogObra(this,operacion,obra);
                                mo.setVisible(true);
                                break;
                            case 3:
                                zona=(Zona)nodoSeleccionado.getUserObject();
                                DialogZona mz=new DialogZona(this,operacion,zona);
                                mz.setVisible(true);
                                break;
                            case 4:
                                elemento=(Elemento)nodoSeleccionado.getUserObject();
                                DialogElemento me=new DialogElemento(this,operacion,elemento);
                                me.setVisible(true);
                                break;
                            case 5:
                                parte=(Parte)nodoSeleccionado.getUserObject();
                                DialogParte mp=new DialogParte(this,operacion,parte);
                                mp.setVisible(true);
                                break;
                            case 6:
                                revision=(Revision)nodoSeleccionado.getUserObject();
                                DialogRevision dr=new DialogRevision(this,operacion,revision);
                                dr.setVisible(true);
                                break;
                        }
                        break;
                    case OP_DELETE:
                        switch (pathCount){
                            case 2:
                                obra=(Obra)nodoSeleccionado.getUserObject();
                                if(obra.delete()){
                                    this.dropNodo(nodoSeleccionado);
                                }
                                break;
                            case 3:
                                zona=(Zona)nodoSeleccionado.getUserObject();
                                if(zona.delete()){
                                    this.dropNodo(nodoSeleccionado);
                                }
                                break;
                            case 4:
                                elemento=(Elemento)nodoSeleccionado.getUserObject();
                                if(elemento.delete()){
                                    this.dropNodo(nodoSeleccionado);
                                }
                                break;
                            case 5:
                                parte=(Parte)nodoSeleccionado.getUserObject();
                                if(parte.delete()){
                                    this.dropNodo(nodoSeleccionado);
                                }
                                break;
                            case 6:
                                revision=(Revision)nodoSeleccionado.getUserObject();
                                if(revision.delete()){
                                    this.dropNodo(nodoSeleccionado);
                                }
                                break;
                        }
                        break;
                    case OP_DUPLICATE:
                        switch (pathCount){
                            case 6:
                                revision=(Revision)nodoSeleccionado.getUserObject();
                                String parte_id=revision.getValue("parte_id");
                                Revision duplicada=new Revision(this.getCompactDb().getConector());
                                duplicada.setValue("parte_id",parte_id);
                                //
                                Vector<Revision> revisiones=revision.find(new Condition("parte_id",parte_id,Condition.IGUAL),"id");
                                Revision u_revision=null;
                                if(revisiones.size()>0){
                                    u_revision=revisiones.get(revisiones.size()-1);
                                }
                                //
                                if(u_revision!=null){
                                    long long_ur=Convert.toLong(u_revision.getValue("revision"))+1;
                                    duplicada.setValue("revision",Convert.toString(long_ur));
                                }else{
                                    duplicada.setValue("revision","0");
                                }
                                DialogRevision dr=new DialogRevision(this,operacion,duplicada);
                                dr.setVisible(true);
                                if(dr.getReturnStatus()==DialogRevision.RET_OK){
                                    if(dr.getRevision().insert()){
                                        dr.getRevision().setId(Convert.toString(dr.getRevision().getLastInsertId()));
                                        Barra barra=new Barra(this.getCompactDb().getConector());
                                        for(Barra this_barra:barra.find(new Condition("revision_id",revision.getId()))){
                                            Barra newBarra=new Barra(this.getCompactDb().getConector());
                                            newBarra.setValue("orden",this_barra.getValue("orden"));
                                            newBarra.setValue("revision_id",dr.getRevision().getId());
                                            newBarra.setValue("referencia",this_barra.getValue("referencia"));
                                            newBarra.setValue("forma_id",this_barra.getValue("forma_id"));
                                            newBarra.setValue("diametro_id",this_barra.getValue("diametro_id"));
                                            newBarra.setValue("cantidad",this_barra.getValue("cantidad"));
                                            newBarra.setValue("comentario",this_barra.getValue("comentario"));
                                            newBarra.setValue("a",this_barra.getValue("a"));
                                            newBarra.setValue("b",this_barra.getValue("b"));
                                            newBarra.setValue("c",this_barra.getValue("c"));
                                            newBarra.setValue("d",this_barra.getValue("d"));
                                            newBarra.setValue("e",this_barra.getValue("e"));
                                            newBarra.setValue("f",this_barra.getValue("f"));
                                            newBarra.setValue("g",this_barra.getValue("g"));
                                            newBarra.setValue("h",this_barra.getValue("h"));
                                            newBarra.setValue("i",this_barra.getValue("i"));
                                            newBarra.setValue("j",this_barra.getValue("j"));
                                            newBarra.setValue("longitud",this_barra.getValue("longitud"));
                                            newBarra.setValue("peso_unitario",this_barra.getValue("peso_unitario"));
                                            newBarra.setValue("peso_total",this_barra.getValue("peso_total"));
                                            newBarra.insert();
                                        }
                                    }
                                    this.jTree1.setSelectionPath(this.jTree1.getSelectionPath().getParentPath());
                                    this.addNodo(new DefaultMutableTreeNode(dr.getRevision()));
                                    this.refresca();
                                }
                                break;
                        }
                        break;
                }
            } catch (SQLException ex) {
                ErrorDialog.manejaError(ex,false);
            }
        }
    }
    private void doOperacion(int operacion){
        switch (operacion){
            case OP_PRINTPREVIEW:
                if((this.jTree1.getSelectionPath()!=null)&&(this.jTree1.getSelectionPath().getPathCount()==6)){
                    try{
                        Revision revision = (Revision) ((DefaultMutableTreeNode)this.jTree1.getSelectionPath().getLastPathComponent()).getUserObject();
                        Parte parte=(new Parte(this.getCompactDb().getConector())).findFirst(new Condition("id",revision.getValue("parte_id")));
                        Elemento elemento=(new Elemento(this.getCompactDb().getConector())).findFirst(new Condition("id",parte.getValue("elemento_id")));
                        Zona zona=(new Zona(this.getCompactDb().getConector())).findFirst(new Condition("id",elemento.getValue("zona_id")));
                        Obra obra=(new Obra(this.getCompactDb().getConector())).findFirst(new Condition("id",zona.getValue("obra_id")));
                        Acero acero=(new Acero(this.getCompactDb().getConector())).findFirst(new Condition("id",parte.getValue("acero_id")));
                        Hormigon hormigon=(new Hormigon(this.getCompactDb().getConector())).findFirst(new Condition("id",parte.getValue("hormigon_id")));
                        Elaborador elaborador=(new Elaborador(this.getCompactDb().getConector())).findFirst(new Condition("id",revision.getValue("elaborador_id")));
                        Barra barra = new Barra(revision.getConector());
                        Forma forma=new Forma(revision.getConector());
                        Vector<Barra> barras = barra.view3(revision.getId());
                        File dir_temporal=createTemporalDir();
                        if(dir_temporal.exists()){
                            FileUtils.deleteDirectory(dir_temporal);
                        }
                        dir_temporal.mkdir();
                        for(Barra this_barra:barras){
                            String forma_id=this_barra.getValue("forma_id");
                            forma.setId(forma_id);
                            forma.read();
                            String esquema=forma.getValue("esquema").replace("`", "\"");
                            SVGDocument svgdoc=this.getSVGDocument(esquema);
                            Element e_a=svgdoc.getElementById("valor_a");
                            if (e_a!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("a"));
                                e_a.setTextContent(Convert.toString(valor));
                            }
                            Element e_b=svgdoc.getElementById("valor_b");
                            if (e_b!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("b"));
                                e_b.setTextContent(Convert.toString(valor));

                            }
                            Element e_c=svgdoc.getElementById("valor_c");
                            if (e_c!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("c"));
                                e_c.setTextContent(Convert.toString(valor));
                            }
                            Element e_d=svgdoc.getElementById("valor_d");
                            if (e_d!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("d"));
                                e_d.setTextContent(Convert.toString(valor));
                            }
                            Element e_e=svgdoc.getElementById("valor_e");
                            if (e_e!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("e"));
                                e_e.setTextContent(Convert.toString(valor));
                            }
                            Element e_f=svgdoc.getElementById("valor_f");
                            if (e_f!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("f"));
                                e_f.setTextContent(Convert.toString(valor));
                            }
                            Element e_g=svgdoc.getElementById("valor_g");
                            if (e_g!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("g"));
                                e_g.setTextContent(Convert.toString(valor));
                            }
                            Element e_h=svgdoc.getElementById("valor_h");
                            if (e_h!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("h"));
                                e_h.setTextContent(Convert.toString(valor));
                            }
                            Element e_i=svgdoc.getElementById("valor_i");
                            if (e_i!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("i"));
                                e_i.setTextContent(Convert.toString(valor));
                            }
                            Element e_j=svgdoc.getElementById("valor_j");
                            if (e_j!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("j"));
                                e_j.setTextContent(Convert.toString(valor));
                            }
                            SVGRasterizer svgr = new SVGRasterizer(svgdoc);
                            BufferedImage image=svgr.createBufferedImage();
                            File file=FileUtils.addPathFile(dir_temporal,new File(this_barra.getId()+".png"));
                            ImageUtils.save(image,file.getAbsolutePath(),"png");
                        }
                        Map<String, Object> parameters = new HashMap<String, Object>();
                        parameters.put("PARAMETER_REVISION_ID", revision.getId());
                        parameters.put("PARAMETER_OBRA",obra.getValue("nombre"));
                        parameters.put("PARAMETER_CONTRATA",obra.getValue("contrata"));
                        parameters.put("PARAMETER_PARTE",parte.getValue("nombre"));
                        parameters.put("PARAMETER_ELEMENTO",elemento.getValue("nombre"));
                        parameters.put("PARAMETER_ZONA",zona.getValue("nombre"));
                        parameters.put("PARAMETER_REVISION",revision.getValue("revision"));
                        parameters.put("PARAMETER_FECHA",Convert.toDateSql(revision.getValue("fecha")));
                        String dir=(FileUtils.addPathFile(dir_temporal.getAbsolutePath()," ")).getAbsolutePath();
                        dir=dir.substring(0,dir.length()-1);
                        parameters.put("PARAMETER_DIRECTORIO_FORMAS",dir);
                        File reportFile=FileUtils.addPathFile("reports","planilla.jrxml");
                        JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(reportFile));
                        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, this.getCompactDb().getConector().getConexion());
                        JasperViewer.viewReport(print, false);
                        FileUtils.deleteDirectory(dir_temporal);
                    } catch (TranscoderException ex) {
                        ErrorDialog.manejaError(ex);
                    } catch (FileNotFoundException ex) {
                        ErrorDialog.manejaError(ex);
                    } catch (JRException e) {
                        ErrorDialog.manejaError(e);
                    }catch(SQLException ex){
                        ErrorDialog.manejaError(ex);
                    }catch(IOException ex){
                        ErrorDialog.manejaError(ex);
                    }
                }
                break;
            case OP_PRINT:
                if((this.jTree1.getSelectionPath()!=null)&&(this.jTree1.getSelectionPath().getPathCount()==6)){
                    try{
                        Revision revision = (Revision) ((DefaultMutableTreeNode)this.jTree1.getSelectionPath().getLastPathComponent()).getUserObject();
                        Barra barra = new Barra(revision.getConector());
                        Forma forma=new Forma(revision.getConector());
                        Vector<Barra> barras = barra.view3(revision.getId());
                        File dir_temporal=createTemporalDir();
                        if(dir_temporal.exists()){
                            FileUtils.deleteDirectory(dir_temporal);
                        }
                        dir_temporal.mkdir();
                        for(Barra this_barra:barras){
                            String forma_id=this_barra.getValue("forma_id");
                            forma.setId(forma_id);
                            forma.read();
                            String esquema=forma.getValue("esquema").replace("`", "\"");
                            SVGDocument svgdoc=this.getSVGDocument(esquema);
                            Element e_a=svgdoc.getElementById("valor_a");
                            if (e_a!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("a"));
                                e_a.setTextContent(Convert.toString(valor));
                            }
                            Element e_b=svgdoc.getElementById("valor_b");
                            if (e_b!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("b"));
                                e_b.setTextContent(Convert.toString(valor));

                            }
                            Element e_c=svgdoc.getElementById("valor_c");
                            if (e_c!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("c"));
                                e_c.setTextContent(Convert.toString(valor));
                            }
                            Element e_d=svgdoc.getElementById("valor_d");
                            if (e_d!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("d"));
                                e_d.setTextContent(Convert.toString(valor));
                            }
                            Element e_e=svgdoc.getElementById("valor_e");
                            if (e_e!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("e"));
                                e_e.setTextContent(Convert.toString(valor));
                            }
                            Element e_f=svgdoc.getElementById("valor_f");
                            if (e_f!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("f"));
                                e_f.setTextContent(Convert.toString(valor));
                            }
                            Element e_g=svgdoc.getElementById("valor_g");
                            if (e_g!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("g"));
                                e_g.setTextContent(Convert.toString(valor));
                            }
                            Element e_h=svgdoc.getElementById("valor_h");
                            if (e_h!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("h"));
                                e_h.setTextContent(Convert.toString(valor));
                            }
                            Element e_i=svgdoc.getElementById("valor_i");
                            if (e_i!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("i"));
                                e_i.setTextContent(Convert.toString(valor));
                            }
                            Element e_j=svgdoc.getElementById("valor_j");
                            if (e_j!=null){
                                int valor=(int)Convert.toDouble(this_barra.getValue("j"));
                                e_j.setTextContent(Convert.toString(valor));
                            }
                            SVGRasterizer svgr = new SVGRasterizer(svgdoc);
                            BufferedImage image=svgr.createBufferedImage();
                            File file=FileUtils.addPathFile(dir_temporal,new File(this_barra.getId()+".png"));
                            ImageUtils.save(image,file.getAbsolutePath(),"png");
                        }
                        Map<String, Object> parameters = new HashMap<String, Object>();
                        parameters.put("PARAMETER_REVISION_ID", revision.getId());
                        String dir=(FileUtils.addPathFile(dir_temporal.getAbsolutePath()," ")).getAbsolutePath();
                        dir=dir.substring(0,dir.length()-1);
                        parameters.put("PARAMETER_DIRECTORIO_FORMAS",dir);
                        File reportFile=FileUtils.addPathFile("reports","planilla.jrxml");
                        JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(reportFile));
                        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, this.getCompactDb().getConector().getConexion());
                        JasperPrintManager.printReport(print,true);
                        FileUtils.deleteDirectory(dir_temporal);
                    } catch (TranscoderException ex) {
                        ErrorDialog.manejaError(ex);
                    } catch (FileNotFoundException ex) {
                        ErrorDialog.manejaError(ex);
                    } catch (JRException e) {
                        ErrorDialog.manejaError(e);
                    }catch(SQLException ex){
                        ErrorDialog.manejaError(ex);
                    }catch(IOException ex){
                        ErrorDialog.manejaError(ex);
                    }
                }
                break;
            case OP_PRINTSCHEMES:
                try{
                    Map<String, Object> parameters = new HashMap<String, Object>();
                    String dir=(FileUtils.addPathFile(FileUtils.getApplicationPath().getAbsolutePath(),"formaspng")).getAbsolutePath();
                    dir=FileUtils.addPathFile(dir," ").getAbsolutePath();
                    dir=dir.substring(0,dir.length()-1);
                    parameters.put("PARAMETER_DIRECTORIO_FORMAS",dir);
                    File reportFile=FileUtils.addPathFile("reports","formas.jrxml");
                    JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(reportFile));
                    JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, this.getCompactDb().getConector().getConexion());
                    JasperViewer.viewReport(print, false);
                } catch (FileNotFoundException ex) {
                    ErrorDialog.manejaError(ex);
                } catch (JRException e) {
                    ErrorDialog.manejaError(e);
                }catch(IOException ex){
                    ErrorDialog.manejaError(ex);
                }
                break;
        }
    }
    private File createTemporalDir(){
        return FileUtils.addPathFile(FileUtils.getApplicationPath().getAbsolutePath(), "temporal_ferraplan_formas_"+GeneradorUUID.crearUUID());
    }
    private void doActionOpenFile(File file){
        Cursor oldCursor=this.getCursor();
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        this.getCompactDb().setDefaultDir(this.getDefaultDir());
        if(this.getCompactDb().open(file)){
            this.setDefaultDir(this.getCompactDb().getDefaultDir());
            this.addRecentFile(this.getCompactDb().getFile().getAbsolutePath());
            this.loadFile();
            this.doWhenOpenFile(true);
        }
        setCursor(oldCursor);
    }
    private void loadRecentFiles(){
        String file0=this.getPreferencias().getPreferencia("File0");
        String file1=this.getPreferencias().getPreferencia("File1");
        String file2=this.getPreferencias().getPreferencia("File2");
        String file3=this.getPreferencias().getPreferencia("File3");
        String file4=this.getPreferencias().getPreferencia("File4");
        this.jMenuArchivoFile0.setText(file0);
        this.jMenuArchivoFile1.setText(file1);
        this.jMenuArchivoFile2.setText(file2);
        this.jMenuArchivoFile3.setText(file3);
        this.jMenuArchivoFile4.setText(file4);
    }
    private void loadFile(){
        try {
            this.setBarra(new Barra(this.getCompactDb().getConector()));
            this.inicializa();
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex, true);
        }
    }
    private void doExit() {
        this.getPreferencias().save();
        try {
            File dir = FileUtils.addPathFile(System.getProperty("user.dir"),"tmp");
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        } catch (Exception ex) {
            ErrorDialog.manejaError(ex, true);
        }
        this.getCompactDb().close();
        this.existsTemporalDir();
        System.exit(0);
    }
    public void existsTemporalDir(){
        File dir=FileUtils.getApplicationPath();
        final String como="temporal_ferraplan_formas_";
        FilenameFilter ff=new FilenameFilter(){
            @Override
            public boolean accept(File dir, String name) {
                if((name!=null)&&(name.length()>=como.length())){
                    String izquierda=name.substring(0,como.length());
                    if(izquierda.equals(como)){
                        return true;
                    }
                }
                return false;
            }
        };
        File[] files=dir.listFiles(ff);
        if(files.length>0){
            for(File file:files){
                FileUtils.deleteDirectory(file);
            }
        }
    }
    private void doWhenOpenFile(boolean isFileOpened){
        this.jButton9.setEnabled(isFileOpened);
        this.jButton10.setEnabled(isFileOpened);
        this.jMenuArchivoCerrar.setEnabled(isFileOpened);
        this.jMenuArchivoGuardar.setEnabled(isFileOpened);
        this.jMenuArchivoGuardarComo.setEnabled(isFileOpened);
        this.jMenuPrintSchemes.setEnabled(isFileOpened);
        this.jMenuInformes.setEnabled(isFileOpened);
        this.jMenuEdit.setEnabled(isFileOpened);
    }

 private void doAction(int action){
        Cursor oldCursor=this.getCursor();
        boolean doit=false;
        this.getCompactDb().setDefaultDir(this.getDefaultDir());
        switch (action){
            case AC_ABRIR:
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                doit=this.getCompactDb().open();
                if(doit){
                    this.loadFile();
                    this.doWhenOpenFile(true);
                    this.setDefaultDir(this.getCompactDb().getDefaultDir());
                    this.addRecentFile(this.getCompactDb().getFile().getAbsolutePath());
                }
                break;
            case AC_CERRAR:
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                doit=this.getCompactDb().close();
                this.jTree1.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Obras")));
                this.jTable1.setModel(new DefaultTableModel());
                if(doit){
                    this.doWhenOpenFile(false);
                }
                break;
            case AC_GUARDAR:
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                doit=this.getCompactDb().save();
                this.setDefaultDir(this.getCompactDb().getDefaultDir());
                this.addRecentFile(this.getCompactDb().getFile().getAbsolutePath());
                break;
            case AC_GUARDAR_COMO:
                doit=this.getCompactDb().saveAs();
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                this.setDefaultDir(this.getCompactDb().getDefaultDir());
                this.addRecentFile(this.getCompactDb().getFile().getAbsolutePath());
                break;
            case AC_NUEVO:
                doit=this.getCompactDb().create();
                if(doit){
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    this.loadFile();
                    this.doWhenOpenFile(true);
                    this.setDefaultDir(this.getCompactDb().getDefaultDir());
                    this.addRecentFile(this.getCompactDb().getFile().getAbsolutePath());
                }
                break;
        }
        if(this.getCursor()!=oldCursor){
            this.setCursor(oldCursor);
        }
    }
    private void addRecentFile(String file){
        String file0=this.getPreferencias().getPreferencia("File0");
        String file1=this.getPreferencias().getPreferencia("File1");
        String file2=this.getPreferencias().getPreferencia("File2");
        String file3=this.getPreferencias().getPreferencia("File3");
        String file4=this.getPreferencias().getPreferencia("File4");
        if(file!=null){
            if(file.equals(file0)){
                return;
            }
            if(file.equals(file1)){
                file1=file0;
                file0=file;
            }else if(file.equals(file2)){
                file2=file0;
                file0=file;
            }else if(file.equals(file3)){
                file3=file0;
                file0=file;
            }else if(file.equals(file4)){
                file4=file0;
                file0=file;
            }else{
                file4=file3;
                file3=file2;
                file2=file1;
                file1=file0;
                file0=file;
            }
        }else{
            file0=file1;
            file1=file2;
            file2=file3;
            file3=file4;
            file4=null;

        }
        if(file0!=null){
            this.jMenuArchivoFile0.setText(file0);
            this.getPreferencias().setPreferencia("File0", file0);
        }else{
            this.jMenuArchivoFile0.setText("");
        }
        if(file1!=null){
            this.jMenuArchivoFile1.setText(file1);
            this.getPreferencias().setPreferencia("File1", file1);
        }else{
            this.jMenuArchivoFile1.setText("");
        }
        if(file2!=null){
            this.jMenuArchivoFile2.setText(file2);
            this.getPreferencias().setPreferencia("File2", file2);
        }else{
            this.jMenuArchivoFile2.setText("");
        }
        if(file3!=null){
            this.jMenuArchivoFile3.setText(file3);
            this.getPreferencias().setPreferencia("File3", file3);
        }else{
            this.jMenuArchivoFile3.setText("");
        }
        if(file4!=null){
            this.jMenuArchivoFile4.setText(file4);
            this.getPreferencias().setPreferencia("File4", file4);
        }else{
            this.jMenuArchivoFile4.setText("");
        }
        this.getPreferencias().save();
    }
    private double calcula_longitud(String formula,double a,double b,double c,double d,double e,double f,double g,double h,double i,double j){
        String function="f(a,b,c,d,e,f,g,h,i,j) = "+formula;
        double[] valores={a,b,c,d,e,f,g,h,i,j};
        try {
            FunctionParser parser = new FunctionParser ();
            CompiledFunction cfunction = parser.compileFunction(function);
            return cfunction.computeFunction (valores);
        } catch (CompilationException ex) {
            ErrorDialog.manejaError(ex,false);
        }
        return 0;
    }
    private SVGDocument getSVGDocument(String text) throws IOException{
        if(text!=null){
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            StringReader r = new StringReader(text);
            return f.createSVGDocument(null,r);
        }
        return null;
    }
    private boolean estaLaPrimeraFilaVacia(Revision revision,boolean laElimino){
        try {
            Barra barra = new Barra(revision.getConector());
            Vector<Barra> barras=barra.find(new Condition("revision_id",revision.getId()));
            if((barras!=null)&&(barras.size()==1)){
                if(barras.get(0).getValue("referencia").length()!=0) return false;
                if(!barras.get(0).getValue("forma_id").equals("1")) return false;
                if(!barras.get(0).getValue("diametro_id").equals("1")) return false;
                if(Convert.toDouble(barras.get(0).getValue("cantidad"))!=0) return false;
                if(barras.get(0).getValue("comentario").length()!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("a"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("b"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("c"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("d"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("e"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("f"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("g"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("h"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("i"))!=0) return false;
                if(Convert.toDouble(barras.get(0).getValue("j"))!=0) return false;
                if(laElimino){
                    barras.get(0).delete();
                }
                return true;
            }
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex);
        }
        return false;
    }
    //
    //************************OPERACIONES CON JTREE*****************************
    //

    private void addNodo(DefaultMutableTreeNode nodo){
        if(nodo!=null){
            DefaultMutableTreeNode padre=(DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent();
            //DefaultMutableTreeNode hijo=new DefaultMutableTreeNode(userObject);
            ((DefaultTreeModel)this.jTree1.getModel()).insertNodeInto(nodo,padre,padre.getChildCount());
            TreePath tp=new TreePath(nodo.getPath());
            jTree1.scrollPathToVisible(tp);
            jTree1.setSelectionPath(tp);
        }
    }
    private void dropNodo(DefaultMutableTreeNode nodo){
        if(nodo!=null){
            TreePath tp=new TreePath(nodo.getParent());
            ((DefaultTreeModel)this.jTree1.getModel()).removeNodeFromParent(nodo);
            jTree1.scrollPathToVisible(tp);
            jTree1.setSelectionPath(tp);
        }
    }
    private void editNodo(DefaultMutableTreeNode nodo,DefaultMutableTreeNode newnodo){
        if((nodo!=null)&&(newnodo!=null)){
            TreePath tp=new TreePath(nodo.getPath());
            if(tp!=null){
                ((DefaultMutableTreeNode)tp.getLastPathComponent()).setUserObject(newnodo.getUserObject());
                jTree1.scrollPathToVisible(tp);
                jTree1.setSelectionPath(tp);
            }
        }
    }
    private Object getUserObjectInTreePath(TreePath tp,int elemento){
        DefaultMutableTreeNode dmtn=(DefaultMutableTreeNode)tp.getPathComponent(elemento);
        return dmtn.getUserObject();

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenuAdd = new javax.swing.JMenuItem();
        jPopupMenuEdit = new javax.swing.JMenuItem();
        jPopupMenuDelete = new javax.swing.JMenuItem();
        jPopupMenuView = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jPopupMenuTableCopy = new javax.swing.JMenuItem();
        jPopupMenuTableCut = new javax.swing.JMenuItem();
        jPopupMenuTablePaste = new javax.swing.JMenuItem();
        jPopupMenuTableDelete = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jPopupMenuTableSelectAll = new javax.swing.JMenuItem();
        jPopupMenuTableSelectNothing = new javax.swing.JMenuItem();
        jToolBar1 = new javax.swing.JToolBar();
        jButton11 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        jButton16 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton13 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new es.atareao.alejandria.laf.JLAFTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuArchivo = new javax.swing.JMenu();
        jMenuArchivoNuevo = new javax.swing.JMenuItem();
        jMenuArchivoAbrir = new javax.swing.JMenuItem();
        jMenuArchivoCerrar = new javax.swing.JMenuItem();
        jMenuArchivoGuardar = new javax.swing.JMenuItem();
        jMenuArchivoGuardarComo = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuPrintSchemes = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuArchivoFiles = new javax.swing.JMenu();
        jMenuArchivoFile0 = new javax.swing.JMenuItem();
        jMenuArchivoFile1 = new javax.swing.JMenuItem();
        jMenuArchivoFile2 = new javax.swing.JMenuItem();
        jMenuArchivoFile3 = new javax.swing.JMenuItem();
        jMenuArchivoFile4 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jMenuItemSalir = new javax.swing.JMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jMenuEditFabricantes = new javax.swing.JMenuItem();
        jMenuEditElaboradores = new javax.swing.JMenuItem();
        jMenuEditMontadores = new javax.swing.JMenuItem();
        jMenuInformes = new javax.swing.JMenu();
        jMenuInformesPedido = new javax.swing.JMenuItem();
        jMenuInformesElaborada = new javax.swing.JMenuItem();
        jMenuInformesMontada = new javax.swing.JMenuItem();
        jMenuInformesFabricadaPor = new javax.swing.JMenuItem();
        jMenuInformesMontadaPor = new javax.swing.JMenuItem();
        jMenuAyuda = new javax.swing.JMenu();
        jMenuItemAcercaDe = new javax.swing.JMenuItem();
        jMenuItemAyuda = new javax.swing.JMenuItem();

        jPopupMenuAdd.setText("Añadir");
        jPopupMenuAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPopupMenuAddActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jPopupMenuAdd);

        jPopupMenuEdit.setText("Editar");
        jPopupMenuEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPopupMenuEditActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jPopupMenuEdit);

        jPopupMenuDelete.setText("Borrar");
        jPopupMenuDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPopupMenuDeleteActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jPopupMenuDelete);

        jPopupMenuView.setText("Consultar");
        jPopupMenuView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPopupMenuViewActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jPopupMenuView);

        jPopupMenuTableCopy.setText("Copiar");
        jPopupMenuTableCopy.setEnabled(false);
        jPopupMenuTableCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jPopupMenuTableCopy);

        jPopupMenuTableCut.setText("Cortar");
        jPopupMenuTableCut.setEnabled(false);
        jPopupMenuTableCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jPopupMenuTableCut);

        jPopupMenuTablePaste.setText("Pegar");
        jPopupMenuTablePaste.setEnabled(false);
        jPopupMenuTablePaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jPopupMenuTablePaste);

        jPopupMenuTableDelete.setText("Borrar");
        jPopupMenuTableDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPopupMenuTableDeleteActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jPopupMenuTableDelete);
        jPopupMenu2.add(jSeparator1);

        jPopupMenuTableSelectAll.setText("Seleccionar todo");
        jPopupMenuTableSelectAll.setEnabled(false);
        jPopupMenuTableSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPopupMenuTableSelectAllActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jPopupMenuTableSelectAll);

        jPopupMenuTableSelectNothing.setText("Borrar seleccion");
        jPopupMenuTableSelectNothing.setEnabled(false);
        jPopupMenuTableSelectNothing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPopupMenuTableSelectNothingActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jPopupMenuTableSelectNothing);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ferraplan");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/ferraplan/img/ferraplan_icon48.png")).getImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jToolBar1.setRollover(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(10000, 50));
        jToolBar1.setMinimumSize(new java.awt.Dimension(10000, 50));

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_exit.png"))); // NOI18N
        jButton11.setToolTipText("Salir");
        jButton11.setFocusable(false);
        jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton11);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_new.png"))); // NOI18N
        jButton7.setToolTipText("Nuevo");
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_open.png"))); // NOI18N
        jButton8.setToolTipText("Abrir");
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton8);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_close.png"))); // NOI18N
        jButton9.setToolTipText("Cerrar");
        jButton9.setEnabled(false);
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_save.png"))); // NOI18N
        jButton10.setToolTipText("Guardar");
        jButton10.setEnabled(false);
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton10);
        jToolBar1.add(jSeparator4);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_add.png"))); // NOI18N
        jButton1.setToolTipText("Añadir");
        jButton1.setEnabled(false);
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_new.png"))); // NOI18N
        jButton2.setToolTipText("Consutar");
        jButton2.setEnabled(false);
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_edit.png"))); // NOI18N
        jButton3.setToolTipText("Editar");
        jButton3.setEnabled(false);
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_delete.png"))); // NOI18N
        jButton4.setToolTipText("Borrar");
        jButton4.setEnabled(false);
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_duplicate.png"))); // NOI18N
        jButton12.setToolTipText("Duplicar");
        jButton12.setEnabled(false);
        jButton12.setFocusable(false);
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton12);
        jToolBar1.add(jSeparator8);

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_solape.png"))); // NOI18N
        jButton16.setToolTipText("Cálculo de longitudes de solapes");
        jButton16.setEnabled(false);
        jButton16.setFocusable(false);
        jButton16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton16);

        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_anclaje.png"))); // NOI18N
        jButton19.setToolTipText("Cálculo de longitudes de anclaje");
        jButton19.setEnabled(false);
        jButton19.setFocusable(false);
        jButton19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton19.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton19);
        jToolBar1.add(jSeparator9);

        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_zapata.png"))); // NOI18N
        jButton17.setToolTipText("Despiece de zapatas");
        jButton17.setEnabled(false);
        jButton17.setFocusable(false);
        jButton17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton17.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton17);

        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_pilar.png"))); // NOI18N
        jButton18.setToolTipText("Despiece de pilares");
        jButton18.setEnabled(false);
        jButton18.setFocusable(false);
        jButton18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton18.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton18);

        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_arranque_pilar.png"))); // NOI18N
        jButton20.setToolTipText("Despiece de arranque de pilares");
        jButton20.setEnabled(false);
        jButton20.setFocusable(false);
        jButton20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton20.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton20);

        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_viga.png"))); // NOI18N
        jButton21.setToolTipText("Despiece de vigas");
        jButton21.setEnabled(false);
        jButton21.setFocusable(false);
        jButton21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton21.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton21);
        jToolBar1.add(jSeparator5);

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_copy.png"))); // NOI18N
        jButton13.setToolTipText("Copiar");
        jButton13.setEnabled(false);
        jButton13.setFocusable(false);
        jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton13);

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_cut.png"))); // NOI18N
        jButton15.setToolTipText("Cortar");
        jButton15.setEnabled(false);
        jButton15.setFocusable(false);
        jButton15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton15.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton15);

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_paste.png"))); // NOI18N
        jButton14.setToolTipText("Pegar");
        jButton14.setEnabled(false);
        jButton14.setFocusable(false);
        jButton14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton14.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton14);
        jToolBar1.add(jSeparator6);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_print_preview.png"))); // NOI18N
        jButton5.setToolTipText("Presentación preliminar");
        jButton5.setEnabled(false);
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/atareao/img/png/button_print.png"))); // NOI18N
        jButton6.setToolTipText("Imprimir");
        jButton6.setEnabled(false);
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(150, 25));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(150, 25));

        jTree1.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Obras")));
        jTree1.setComponentPopupMenu(jPopupMenu1);
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setComponentPopupMenu(jPopupMenu2);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jTable1MouseDragged(evt);
            }
        });
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTable1FocusLost(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jSplitPane1.setRightComponent(jScrollPane2);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jMenuBar1.setPreferredSize(new java.awt.Dimension(50, 25));

        jMenuArchivo.setText("Archivo");

        jMenuArchivoNuevo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuArchivoNuevo.setText("Nuevo");
        jMenuArchivoNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuArchivoNuevoActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuArchivoNuevo);

        jMenuArchivoAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuArchivoAbrir.setText("Abrir");
        jMenuArchivoAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuArchivoAbrirActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuArchivoAbrir);

        jMenuArchivoCerrar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuArchivoCerrar.setText("Cerrar");
        jMenuArchivoCerrar.setEnabled(false);
        jMenuArchivoCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuArchivoCerrarActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuArchivoCerrar);

        jMenuArchivoGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        jMenuArchivoGuardar.setText("Guardar");
        jMenuArchivoGuardar.setEnabled(false);
        jMenuArchivoGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuArchivoGuardarActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuArchivoGuardar);

        jMenuArchivoGuardarComo.setText("Guardar como ...");
        jMenuArchivoGuardarComo.setEnabled(false);
        jMenuArchivoGuardarComo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuArchivoGuardarComoActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuArchivoGuardarComo);
        jMenuArchivo.add(jSeparator7);

        jMenuPrintSchemes.setText("Imprimir Formas");
        jMenuPrintSchemes.setEnabled(false);
        jMenuPrintSchemes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuPrintSchemesActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuPrintSchemes);
        jMenuArchivo.add(jSeparator2);

        jMenuArchivoFiles.setText("Recientes");

        jMenuArchivoFile0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuArchivoFile0ActionPerformed(evt);
            }
        });
        jMenuArchivoFiles.add(jMenuArchivoFile0);

        jMenuArchivoFile1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuArchivoFile1ActionPerformed(evt);
            }
        });
        jMenuArchivoFiles.add(jMenuArchivoFile1);

        jMenuArchivoFile2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuArchivoFile2ActionPerformed(evt);
            }
        });
        jMenuArchivoFiles.add(jMenuArchivoFile2);

        jMenuArchivoFile3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuArchivoFile3ActionPerformed(evt);
            }
        });
        jMenuArchivoFiles.add(jMenuArchivoFile3);

        jMenuArchivoFile4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuArchivoFile4ActionPerformed(evt);
            }
        });
        jMenuArchivoFiles.add(jMenuArchivoFile4);

        jMenuArchivo.add(jMenuArchivoFiles);
        jMenuArchivo.add(jSeparator3);

        jMenuItemSalir.setText("Salir");
        jMenuItemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSalirActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuItemSalir);

        jMenuBar1.add(jMenuArchivo);

        jMenuEdit.setText("Editar");

        jMenuEditFabricantes.setText("Fabricantes");
        jMenuEditFabricantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuEditFabricantesActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuEditFabricantes);

        jMenuEditElaboradores.setText("Elaboradores");
        jMenuEditElaboradores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuEditElaboradoresActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuEditElaboradores);

        jMenuEditMontadores.setText("Montadores");
        jMenuEditMontadores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuEditMontadoresActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuEditMontadores);

        jMenuBar1.add(jMenuEdit);

        jMenuInformes.setText("Informes");
        jMenuInformes.setEnabled(false);

        jMenuInformesPedido.setText("Medición de planillas pedidas");
        jMenuInformesPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuInformesPedidoActionPerformed(evt);
            }
        });
        jMenuInformes.add(jMenuInformesPedido);

        jMenuInformesElaborada.setText("Medición de planillas suministradas");
        jMenuInformesElaborada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuInformesElaboradaActionPerformed(evt);
            }
        });
        jMenuInformes.add(jMenuInformesElaborada);

        jMenuInformesMontada.setText("Medición de planillas montadas");
        jMenuInformesMontada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuInformesMontadaActionPerformed(evt);
            }
        });
        jMenuInformes.add(jMenuInformesMontada);

        jMenuInformesFabricadaPor.setText("Medición de planillas por fabricante");
        jMenuInformesFabricadaPor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuInformesFabricadaPorActionPerformed(evt);
            }
        });
        jMenuInformes.add(jMenuInformesFabricadaPor);

        jMenuInformesMontadaPor.setText("Medición de planillas por montador");
        jMenuInformesMontadaPor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuInformesMontadaPorActionPerformed(evt);
            }
        });
        jMenuInformes.add(jMenuInformesMontadaPor);

        jMenuBar1.add(jMenuInformes);

        jMenuAyuda.setText("Ayuda");

        jMenuItemAcercaDe.setText("A cerca de...");
        jMenuItemAcercaDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcercaDeActionPerformed(evt);
            }
        });
        jMenuAyuda.add(jMenuItemAcercaDe);

        jMenuItemAyuda.setText("Ayuda");
        jMenuAyuda.add(jMenuItemAyuda);

        jMenuBar1.add(jMenuAyuda);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemAcercaDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcercaDeActionPerformed
        AboutDialogo ad=new AboutDialogo(this,true);
        ad.setApplicationName(APPNAME);
        ad.setVersion(VERSION);
        ad.setYear(YEAR);
        ad.setAuthor(AUTHOR);
        ad.setEmail(EMAIL);
        String texto="" +
                "En la mitología romana Vulcano, era el dios del fuego y los metales, hijo de Jupiter y Juno y marido " +
                "de Venus. Era dios del fuego y los volcanes, forjador del hierro y creador de arte, " +
                "armas y armaduras para dioses y héroes. Corresponde con Hefesto en la mitología griega. " +
                "Otros nombres que recibe son: Mulciber (\"el que ablanda\") en la Mitología romana y " +
                "Sethlas en la Mitología etrusca.\n\n"+
                "Se creía que la fragua de Vulcano se encontraba situada bajo el Monte Edna, en Sicilia, " +
                "o bajo la isla Eólea de Vulcano, en el mar Tirreno.\n\n"+
                "La Isla de Vulcano es una isla italiana de 21 km cuadrados que forma parte de las Islas " +
                "Eóleas. Situada en el mar Mediterráneo, la mitología griega situaba allí la fundición de " +
                "Hefesto, dios del fuego y los metales, que tenía por ayudantes a los Cíclopes y " +
                "a los gigantes.\n\n"+
                "El templo de Vulcano en el Foro romano, llamado el Volcanal, era, según parece, una pieza " +
                "importante de los rituales civiles del antiguo Imperio romano. Hoy día, una Estatua de Vulcano " +
                "colocada en Birmingham, Alabama es la mayor estatua de hierro forjado en todo el mundo. Hay " +
                "un monumento en su honor en Tres Cantos, Madrid.";
        ad.setTexto(texto);
        ad.setImage("/es/atareao/ferraplan/img/ferraplan.png");
        ad.setVisible(true);
}//GEN-LAST:event_jMenuItemAcercaDeActionPerformed

    private void jMenuArchivoNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuArchivoNuevoActionPerformed
        this.doAction(AC_NUEVO);
}//GEN-LAST:event_jMenuArchivoNuevoActionPerformed

    private void jMenuArchivoAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuArchivoAbrirActionPerformed
        this.doAction(AC_ABRIR);
}//GEN-LAST:event_jMenuArchivoAbrirActionPerformed

    private void jMenuArchivoCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuArchivoCerrarActionPerformed
        this.doAction(AC_CERRAR);
}//GEN-LAST:event_jMenuArchivoCerrarActionPerformed

    private void jMenuArchivoGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuArchivoGuardarActionPerformed
        this.doAction(AC_GUARDAR);
}//GEN-LAST:event_jMenuArchivoGuardarActionPerformed

    private void jMenuArchivoGuardarComoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuArchivoGuardarComoActionPerformed
        this.doAction(AC_GUARDAR_COMO);
}//GEN-LAST:event_jMenuArchivoGuardarComoActionPerformed

    private void jMenuArchivoFile0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuArchivoFile0ActionPerformed
        File file=new File(this.jMenuArchivoFile0.getText());
        this.doActionOpenFile(file);
}//GEN-LAST:event_jMenuArchivoFile0ActionPerformed

    private void jMenuArchivoFile1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuArchivoFile1ActionPerformed
        File file=new File(this.jMenuArchivoFile1.getText());
        this.doActionOpenFile(file);
}//GEN-LAST:event_jMenuArchivoFile1ActionPerformed

    private void jMenuArchivoFile2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuArchivoFile2ActionPerformed
        File file=new File(this.jMenuArchivoFile2.getText());
        this.doActionOpenFile(file);
}//GEN-LAST:event_jMenuArchivoFile2ActionPerformed

    private void jMenuArchivoFile3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuArchivoFile3ActionPerformed
        File file=new File(this.jMenuArchivoFile3.getText());
        this.doActionOpenFile(file);
}//GEN-LAST:event_jMenuArchivoFile3ActionPerformed

    private void jMenuArchivoFile4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuArchivoFile4ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jMenuArchivoFile4ActionPerformed

    private void jMenuItemSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalirActionPerformed
        this.doExit();
}//GEN-LAST:event_jMenuItemSalirActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        this.doAction(AC_NUEVO);
}//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        this.doAction(AC_ABRIR);
}//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        this.doAction(AC_CERRAR);
}//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        this.doAction(AC_GUARDAR);
}//GEN-LAST:event_jButton10ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.operacionTree(OP_ADD);
        this.getCompactDb().setModificado(true);
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.operacionTree(OP_VIEW);
}//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.operacionTree(OP_EDIT);
        this.getCompactDb().setModificado(true);
}//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.operacionTree(OP_DELETE);
        this.getCompactDb().setModificado(true);
}//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.doOperacion(OP_PRINTPREVIEW);
}//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        this.doOperacion(OP_PRINT);
}//GEN-LAST:event_jButton6ActionPerformed

    private void jPopupMenuAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPopupMenuAddActionPerformed
        this.operacionTree(OP_ADD);
    }//GEN-LAST:event_jPopupMenuAddActionPerformed

    private void jPopupMenuEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPopupMenuEditActionPerformed
        this.operacionTree(OP_EDIT);
    }//GEN-LAST:event_jPopupMenuEditActionPerformed

    private void jPopupMenuDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPopupMenuDeleteActionPerformed
        this.operacionTree(OP_DELETE);
    }//GEN-LAST:event_jPopupMenuDeleteActionPerformed

    private void jPopupMenuViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPopupMenuViewActionPerformed
        this.operacionTree(OP_VIEW);
    }//GEN-LAST:event_jPopupMenuViewActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.doExit();
    }//GEN-LAST:event_formWindowClosing

    private void jMenuEditFabricantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuEditFabricantesActionPerformed
        DialogFabricantes df=new DialogFabricantes(this.getCompactDb().getConector());
        df.setVisible(true);
    }//GEN-LAST:event_jMenuEditFabricantesActionPerformed

    private void jMenuEditElaboradoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuEditElaboradoresActionPerformed
        DialogElaboradores de=new DialogElaboradores(this.getCompactDb().getConector());
        de.setVisible(true);
    }//GEN-LAST:event_jMenuEditElaboradoresActionPerformed

    private void jMenuEditMontadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuEditMontadoresActionPerformed
        DialogMontadores dm=new DialogMontadores(this.getCompactDb().getConector());
        dm.setVisible(true);
    }//GEN-LAST:event_jMenuEditMontadoresActionPerformed

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
        if((this.getCompactDb().getConector()!=null)&&(this.jTree1.getSelectionPath()!=null)){
            this.jButton2.setEnabled(true);
            this.jButton3.setEnabled(true);
            this.jButton4.setEnabled(true);

            if(this.jTree1.getSelectionPath().getPathCount()==6){
                this.jButton1.setEnabled(false);
                this.jButton5.setEnabled(true);
                this.jButton6.setEnabled(true);
                this.jButton12.setEnabled(true);
                this.jButton16.setEnabled(true);
                this.jButton17.setEnabled(true);
                this.jButton18.setEnabled(true);
                this.jButton19.setEnabled(true);
                this.jButton20.setEnabled(true);
                this.jButton21.setEnabled(true);
                if((this._copiado!=null)&&(this._copiado.size()>0)){
                    this.jButton14.setEnabled(true);
                    this.jPopupMenuTablePaste.setEnabled(true);
                }else{
                    this.jButton14.setEnabled(false);
                    this.jPopupMenuTablePaste.setEnabled(false);
                }
                this.jPopupMenuAdd.setEnabled(false);
                this.jPopupMenuTableSelectAll.setEnabled(true);
                this.jPopupMenuTableSelectNothing.setEnabled(true);
            }else{
                this.jButton1.setEnabled(true);
                this.jButton5.setEnabled(false);
                this.jButton6.setEnabled(false);
                this.jButton12.setEnabled(false);
                this.jButton13.setEnabled(false);
                this.jButton14.setEnabled(false);
                this.jButton15.setEnabled(false);
                this.jButton16.setEnabled(false);
                this.jButton17.setEnabled(false);
                this.jButton18.setEnabled(false);
                this.jButton19.setEnabled(false);
                this.jButton20.setEnabled(false);
                this.jButton21.setEnabled(false);
                this.jPopupMenuTableCopy.setEnabled(false);
                this.jPopupMenuTableCut.setEnabled(false);
                this.jPopupMenuTablePaste.setEnabled(false);
                this.jPopupMenuTableDelete.setEnabled(false);
                this.jPopupMenuAdd.setEnabled(true);
                ((DefaultTableModel)this.jTable1.getModel()).setNumRows(0);
                this.jPopupMenuTableSelectAll.setEnabled(false);
                this.jPopupMenuTableSelectNothing.setEnabled(false);
            }
            this.jPopupMenuDelete.setEnabled(true);
            this.jPopupMenuEdit.setEnabled(true);
            this.jPopupMenuView.setEnabled(true);
            refresca();
        }else{
            this.jButton1.setEnabled(false);
            this.jButton2.setEnabled(false);
            this.jButton3.setEnabled(false);
            this.jButton4.setEnabled(false);
            this.jButton5.setEnabled(false);
            this.jButton6.setEnabled(false);
            this.jButton12.setEnabled(false);
            this.jButton13.setEnabled(false);
            this.jButton14.setEnabled(false);
            this.jButton15.setEnabled(false);
            this.jButton16.setEnabled(false);
            this.jButton17.setEnabled(false);
            this.jButton18.setEnabled(false);
            this.jButton19.setEnabled(false);
            this.jButton20.setEnabled(false);
            this.jButton21.setEnabled(false);
            this.jPopupMenuTableCopy.setEnabled(false);
            this.jPopupMenuTableCut.setEnabled(false);
            this.jPopupMenuTablePaste.setEnabled(false);
            this.jPopupMenuTableDelete.setEnabled(false);
            this.jPopupMenuAdd.setEnabled(false);
            this.jPopupMenuDelete.setEnabled(false);
            this.jPopupMenuEdit.setEnabled(false);
            this.jPopupMenuView.setEnabled(false);
            this.jPopupMenuTableSelectAll.setEnabled(false);
            this.jPopupMenuTableSelectNothing.setEnabled(false);
            ((DefaultTableModel)this.jTable1.getModel()).setNumRows(0);
        }
    }//GEN-LAST:event_jTree1ValueChanged

    private void jTable1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseDragged
        int fromRow=((DragDropRowTableUIOrdered)this.jTable1.getUI()).getFromRow();
        int toRow=((DragDropRowTableUIOrdered)this.jTable1.getUI()).getToRow();
        this.saveRow(fromRow);
        this.saveRow(toRow);
    }//GEN-LAST:event_jTable1MouseDragged

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        int nfila=this.jTable1.getSelectedRow()+1;
        int filas=this.jTable1.getRowCount();
        if((nfila==filas)&&(evt.getKeyCode()==40)){//Tecla abajo
            this.newRow();
            return;
        }
        if(evt.getKeyCode()==127){
            evt.consume();
            int[] filass=this.jTable1.getSelectedRows();
            if(filass.length>0){
                for(int seleccionado=filass.length-1;seleccionado>-1;seleccionado--){
                    int selectedRow=filass[seleccionado];
                    if(selectedRow>=0){
                        String selectedId=(String)this.jTable1.getValueAt(selectedRow,1);
                        try {
                            Barra barra = new Barra(this.getCompactDb().getConector());
                            barra.setId(selectedId);
                            barra.read();
                            if(barra.delete()){
                                this.jTable1.removeRowSelectionInterval(selectedRow,selectedRow);
                                this.getCompactDb().setModificado(true);
                                //this.refresca();
                            }
                        } catch (SQLException ex) {
                            ErrorDialog.manejaError(ex);
                        }
                    }
                }
                this.refresca();
            }
            this.jButton13.setEnabled(false);
            this.jButton14.setEnabled(false);
            this.jButton15.setEnabled(false);
            this.jPopupMenuTableCopy.setEnabled(false);
            this.jPopupMenuTableCut.setEnabled(false);
            this.jPopupMenuTableDelete.setEnabled(false);
            this.jPopupMenuTablePaste.setEnabled(false);
            this._copiado=null;
            this._cortado=false;
            return;
        }
        String numeros="0123456789";
        int fila=this.jTable1.getSelectedRow();
        int columna=this.jTable1.getSelectedColumn();
        if(columna==3){
            if((evt.getKeyChar()==KeyEvent.VK_BACK_SPACE)&&(_searchFor2.length()>0)){
                _searchFor2=_searchFor2.substring(0,_searchFor2.length()-1);
                return;
            }
            if(numeros.indexOf(evt.getKeyChar())>-1){
                long now = new java.util.Date().getTime();
                if(_lap2+1000>now){
                    _searchFor2+=evt.getKeyChar();
                }else{
                    _searchFor2=""+evt.getKeyChar();
                }
                _lap2=now;
                try {
                    Diametro diametro=new Diametro(this.getCompactDb().getConector());
                    diametro=diametro.findFirst(new Condition("diametro",_searchFor2,Condition.IGUAL));
                    if(diametro!=null){
                        this.jTable1.setValueAt(diametro.getId(),fila,columna);
                        ((JWrapperComboBoxTableCellRenderer)this.jTable1.getCellRenderer(fila, columna)).setSelectedItemModified(diametro.getId());
                        ((JWrapperComboBoxTableCellEditor)this.jTable1.getCellEditor(fila, columna)).setSelectedId(diametro.getId());
                        saveRow(fila);
                    }
                } catch (SQLException ex) {
                    ErrorDialog.manejaError(ex);
                }
            }
            return;
        }
        if(columna==5){
            if((evt.getKeyChar()==KeyEvent.VK_BACK_SPACE)&&(_searchFor.length()>0)){
                _searchFor=_searchFor.substring(0,_searchFor.length()-1);
                return;
            }
            if(numeros.indexOf(evt.getKeyChar())>-1){
                long now = new java.util.Date().getTime();
                if(_lap+1000>now){
                    _searchFor+=evt.getKeyChar();
                }else{
                    _searchFor=""+evt.getKeyChar();
                }
                _lap=now;
                if(_searchFor.length()==3){
                    try {
                        Forma forma = new Forma(this.getCompactDb().getConector());
                        forma=forma.findFirst(new Condition("nombre", _searchFor, Condition.IGUAL));
                        if(forma!=null){
                            this.jTable1.setValueAt(forma.getId(),fila,columna);
                            ((JFormaChooserTableCellRenderer)this.jTable1.getCellRenderer(fila, columna)).setSelectedItemModified(forma.getId());
                            ((JFormaChooserTableCellEditor)this.jTable1.getCellEditor(fila, columna)).setSelectedId(forma.getId());
                            saveRow(fila);
                        }
                    } catch (SQLException ex) {
                        ErrorDialog.manejaError(ex);
                    }
                    _searchFor="";
                }
            }
        }
    }//GEN-LAST:event_jTable1KeyPressed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        this.doExit();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        this.operacionTree(OP_DUPLICATE);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        if(this.jTree1.getSelectionPath().getPathCount()==6){
            if(this.jTable1.getSelectedRowCount()>0){
                this._copiado=new ArrayList<Barra>();
                for(int fila:this.jTable1.getSelectedRows()){
                    String id=(String)this.jTable1.getValueAt(fila,1);
                    if(id!=null){
                        try {
                            Barra barra = new Barra(this.getCompactDb().getConector());
                            barra.setId(id);
                            barra.read();
                            this._copiado.add(barra);
                        } catch (SQLException ex) {
                            ErrorDialog.manejaError(ex);
                        }
                    }
                }
                if((this._copiado!=null)&&(this._copiado.size()>0)){
                    this.jButton14.setEnabled(true);
                    this.jPopupMenuTablePaste.setEnabled(true);
                    this._cortado=false;
                }else{
                    this._copiado=null;
                    this.jButton14.setEnabled(false);
                    this.jPopupMenuTablePaste.setEnabled(true);
                }
            }
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        if((this._copiado!=null)&&(this._copiado.size()>0)){
            if(this.jTree1.getSelectionPath().getPathCount()==6){
                Revision revision = (Revision) ((DefaultMutableTreeNode)this.jTree1.getSelectionPath().getLastPathComponent()).getUserObject();
                if(this.estaLaPrimeraFilaVacia(revision,true)){
                    ((DefaultTableModel)this.jTable1.getModel()).removeRow(0);
                }
                for(Barra barra:this._copiado){
                    Object[] filadata={
                        "",//Orden - 0
                        "",//id - 1
                        "",//referencia - 2
                        "1",//diametro_id - 3
                        "0",//cantidad - 4
                        "1",//forma_id - 5
                        "0",//a - 6
                        "0",//b - 7
                        "0",//c - 8
                        "0",//d - 9
                        "0",//e - 10
                        "0",//f - 11
                        "0",//g - 12
                        "0",//h - 13
                        "0",//i - 14
                        "0",//j - 15
                        "0",//longitud - 16
                        "0",//peso_unitario - 17
                        "0",//peso_total - 18
                        ""//COMENTARIO
                    };
                    //((DefaultTableModel)this.jTable1.getModel()).addRow(filadata);
                    int fila=this.jTable1.getRowCount();
                    filadata[0]=Convert.toString(fila);
                    filadata[1]="";
                    filadata[2]=barra.getValue("referencia");
                    filadata[3]=barra.getValue("diametro_id");
                    filadata[4]=barra.getValue("cantidad");
                    filadata[5]=barra.getValue("forma_id");
                    filadata[6]=barra.getValue("a");
                    filadata[7]=barra.getValue("b");
                    filadata[8]=barra.getValue("c");
                    filadata[9]=barra.getValue("d");
                    filadata[10]=barra.getValue("e");
                    filadata[11]=barra.getValue("f");
                    filadata[12]=barra.getValue("g");
                    filadata[13]=barra.getValue("h");
                    filadata[14]=barra.getValue("i");
                    filadata[15]=barra.getValue("j");
                    filadata[16]=barra.getValue("longitud");
                    filadata[17]=barra.getValue("peso_unitario");
                    filadata[18]=barra.getValue("peso_total");
                    filadata[19]=barra.getValue("comentario");
                    ((DefaultTableModel)this.jTable1.getModel()).addRow(filadata);
                    this.saveRow(this.jTable1.getRowCount()-1);
                    if(this._cortado){
                        try {
                            barra.delete();
                        } catch (SQLException ex) {
                            ErrorDialog.manejaError(ex);
                        }
                    }
                }
                this._copiado=null;
                this._cortado=false;
                this.jButton13.setEnabled(false);
                this.jButton14.setEnabled(false);
                this.jButton15.setEnabled(false);
                this.jPopupMenuTableCopy.setEnabled(false);
                this.jPopupMenuTableCut.setEnabled(false);
                this.jPopupMenuTablePaste.setEnabled(false);
            }
        }else{
            this._copiado=null;
            this._cortado=false;
                this.jButton13.setEnabled(false);
                this.jButton14.setEnabled(false);
                this.jButton15.setEnabled(false);
                this.jPopupMenuTableCopy.setEnabled(false);
                this.jPopupMenuTableCut.setEnabled(false);
                this.jPopupMenuTablePaste.setEnabled(false);
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jTable1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusLost
        if(this.jTable1.getSelectedRowCount()>0){
            this.jButton13.setEnabled(true);
            this.jButton15.setEnabled(true);
            this.jPopupMenuTableCopy.setEnabled(true);
            this.jPopupMenuTableCut.setEnabled(true);
            this.jPopupMenuTableDelete.setEnabled(true);
        }else{
            this.jButton13.setEnabled(false);
            this.jButton15.setEnabled(false);
            this.jPopupMenuTableCopy.setEnabled(false);
            this.jPopupMenuTableCut.setEnabled(false);
            this.jPopupMenuTableDelete.setEnabled(false);
        }
    }//GEN-LAST:event_jTable1FocusLost

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if(this.jTable1.getSelectedRowCount()>0){
            this.jButton13.setEnabled(true);
            this.jButton15.setEnabled(true);
            this.jPopupMenuTableCopy.setEnabled(true);
            this.jPopupMenuTableCut.setEnabled(true);
            this.jPopupMenuTableDelete.setEnabled(true);
        }else{
            this.jButton13.setEnabled(false);
            this.jButton15.setEnabled(false);
            this.jPopupMenuTableCopy.setEnabled(false);
            this.jPopupMenuTableCut.setEnabled(false);
            this.jPopupMenuTableDelete.setEnabled(false);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        if(this.jTree1.getSelectionPath().getPathCount()==6){
            if(this.jTable1.getSelectedRowCount()>0){
                this._copiado=new ArrayList<Barra>();
                for(int fila:this.jTable1.getSelectedRows()){
                    String id=(String)this.jTable1.getValueAt(fila,1);
                    if(id!=null){
                        try {
                            Barra barra = new Barra(this.getCompactDb().getConector());
                            barra.setId(id);
                            barra.read();
                            this._copiado.add(barra);
                        } catch (SQLException ex) {
                            ErrorDialog.manejaError(ex);
                        }
                    }
                }
                if((this._copiado!=null)&&(this._copiado.size()>0)){
                    this.jButton14.setEnabled(true);
                    this.jPopupMenuTablePaste.setEnabled(true);
                    this._cortado=true;
                }else{
                    this._copiado=null;
                    this.jButton14.setEnabled(false);
                    this.jPopupMenuTablePaste.setEnabled(false);
                }
            }
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jPopupMenuTableSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPopupMenuTableSelectAllActionPerformed
        this.jTable1.selectAll();
        this.jButton13.setEnabled(true);
        this.jButton15.setEnabled(true);
        this.jPopupMenuTableCopy.setEnabled(true);
        this.jPopupMenuTableCut.setEnabled(true);
        this.jPopupMenuTableDelete.setEnabled(true);
    }//GEN-LAST:event_jPopupMenuTableSelectAllActionPerformed

    private void jPopupMenuTableSelectNothingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPopupMenuTableSelectNothingActionPerformed
        this.jTable1.getSelectionModel().clearSelection();
        this.jButton13.setEnabled(false);
        this.jButton15.setEnabled(false);
        this.jPopupMenuTableCopy.setEnabled(false);
        this.jPopupMenuTableCut.setEnabled(false);
        this.jPopupMenuTableDelete.setEnabled(false);
    }//GEN-LAST:event_jPopupMenuTableSelectNothingActionPerformed

    private void jPopupMenuTableDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPopupMenuTableDeleteActionPerformed
        int[] filass=this.jTable1.getSelectedRows();
        if(filass.length>0){
            for(int seleccionado=filass.length-1;seleccionado>-1;seleccionado--){
                int selectedRow=filass[seleccionado];
                if(selectedRow>=0){
                    String selectedId=(String)this.jTable1.getValueAt(selectedRow,1);
                    try {
                        Barra barra = new Barra(this.getCompactDb().getConector());
                        barra.setId(selectedId);
                        barra.read();
                        if(barra.delete()){
                            this.jTable1.removeRowSelectionInterval(selectedRow,selectedRow);
                            this.getCompactDb().setModificado(true);
                            //this.refresca();
                        }
                    } catch (SQLException ex) {
                        ErrorDialog.manejaError(ex);
                    }
                }
            }
        }
        this.refresca();
        this.jButton13.setEnabled(false);
        this.jButton14.setEnabled(false);
        this.jButton15.setEnabled(false);
        this.jPopupMenuTableCopy.setEnabled(false);
        this.jPopupMenuTableCut.setEnabled(false);
        this.jPopupMenuTableDelete.setEnabled(false);
        this.jPopupMenuTablePaste.setEnabled(false);
        this._copiado=null;
        this._cortado=false;
    }//GEN-LAST:event_jPopupMenuTableDeleteActionPerformed

    private void jMenuPrintSchemesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPrintSchemesActionPerformed
        this.doOperacion(OP_PRINTSCHEMES);
    }//GEN-LAST:event_jMenuPrintSchemesActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        DialogSolapo ds=new DialogSolapo(this,this.getCompactDb());
        ds.setVisible(true);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        if((jTree1.getSelectionPath()!=null)&&(jTree1.getSelectionPath().getPathCount()==6)){
            Revision revision=(Revision)((DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent()).getUserObject();
            DialogZapata dz=new DialogZapata(this,revision);
            dz.setVisible(true);
            this.refresca();
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        if((jTree1.getSelectionPath()!=null)&&(jTree1.getSelectionPath().getPathCount()==6)){
            Revision revision=(Revision)((DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent()).getUserObject();
            DialogPilar dp=new DialogPilar(this,revision);
            dp.setVisible(true);
            this.refresca();
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        DialogAnclaje da=new DialogAnclaje(this,this.getCompactDb());
        da.setVisible(true);
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        if((jTree1.getSelectionPath()!=null)&&(jTree1.getSelectionPath().getPathCount()==6)){
            Revision revision=(Revision)((DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent()).getUserObject();
            DialogArranquePilar dp=new DialogArranquePilar(this,revision);
            dp.setVisible(true);
            this.refresca();
        }
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        if((jTree1.getSelectionPath()!=null)&&(jTree1.getSelectionPath().getPathCount()==6)){
            Revision revision=(Revision)((DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent()).getUserObject();
            DialogViga dv=new DialogViga(this,revision);
            dv.setVisible(true);
            this.refresca();
        }
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jMenuInformesPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuInformesPedidoActionPerformed
        DialogReport dr=new DialogReport(this,true);
        dr.setTitle("Planillas Pedidas");
        dr.setVisible(true);
        if(dr.getReturnStatus()==DialogReport.RET_OK){
            try {
                Date desde = dr.getDesde();
                Date hasta = dr.getHasta();
                Map<String, Object> parameters = new HashMap<String, Object>();
                if((jTree1.getSelectionPath()!=null)&&(jTree1.getSelectionPath().getPathCount()>1)){
                    Obra obra=(Obra)((DefaultMutableTreeNode)jTree1.getSelectionPath().getPath()[1]).getUserObject();
                    parameters.put("PARAMETER_OBRA", obra.getValue("nombre"));
                }else{
                    parameters.put("PARAMETER_OBRA","FERRAPLAN");
                }
                parameters.put("PARAMETER_FECHA_INICIO", desde);
                parameters.put("PARAMETER_FECHA_FIN", hasta);
                File reportFile = FileUtils.addPathFile("reports", "medicion_pedida.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(reportFile));
                JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, this.getCompactDb().getConector().getConexion());
                JasperViewer.viewReport(print, false);
            } catch (FileNotFoundException ex) {
                ErrorDialog.manejaError(ex);
            } catch (JRException ex) {
                ErrorDialog.manejaError(ex);
            }
        }
    }//GEN-LAST:event_jMenuInformesPedidoActionPerformed

    private void jMenuInformesElaboradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuInformesElaboradaActionPerformed
        DialogReport dr=new DialogReport(this,true);
        dr.setTitle("Planillas fabricadas");
        dr.setVisible(true);
        if(dr.getReturnStatus()==DialogReport.RET_OK){
            try {
                Date desde = dr.getDesde();
                Date hasta = dr.getHasta();
                Map<String, Object> parameters = new HashMap<String, Object>();
                if((jTree1.getSelectionPath()!=null)&&(jTree1.getSelectionPath().getPathCount()>1)){
                    Obra obra=(Obra)((DefaultMutableTreeNode)jTree1.getSelectionPath().getPath()[1]).getUserObject();
                    parameters.put("PARAMETER_OBRA", obra.getValue("nombre"));
                }else{
                    parameters.put("PARAMETER_OBRA","FERRAPLAN");
                }
                parameters.put("PARAMETER_FECHA_INICIO", desde);
                parameters.put("PARAMETER_FECHA_FIN", hasta);
                File reportFile = FileUtils.addPathFile("reports", "medicion_suministrada.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(reportFile));
                JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, this.getCompactDb().getConector().getConexion());
                JasperViewer.viewReport(print, false);
            } catch (FileNotFoundException ex) {
                ErrorDialog.manejaError(ex);
            } catch (JRException ex) {
                ErrorDialog.manejaError(ex);
            }
        }
    }//GEN-LAST:event_jMenuInformesElaboradaActionPerformed

    private void jMenuInformesMontadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuInformesMontadaActionPerformed
        DialogReport dr=new DialogReport(this,true);
        dr.setTitle("Planillas montadas");
        dr.setVisible(true);
        if(dr.getReturnStatus()==DialogReport.RET_OK){
            try {
                Date desde = dr.getDesde();
                Date hasta = dr.getHasta();
                Map<String, Object> parameters = new HashMap<String, Object>();
                if((jTree1.getSelectionPath()!=null)&&(jTree1.getSelectionPath().getPathCount()>1)){
                    Obra obra=(Obra)((DefaultMutableTreeNode)jTree1.getSelectionPath().getPath()[1]).getUserObject();
                    parameters.put("PARAMETER_OBRA", obra.getValue("nombre"));
                }else{
                    parameters.put("PARAMETER_OBRA","FERRAPLAN");
                }
                parameters.put("PARAMETER_FECHA_INICIO", desde);
                parameters.put("PARAMETER_FECHA_FIN", hasta);
                File reportFile = FileUtils.addPathFile("reports", "medicion_montada.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(reportFile));
                JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, this.getCompactDb().getConector().getConexion());
                JasperViewer.viewReport(print, false);
            } catch (FileNotFoundException ex) {
                ErrorDialog.manejaError(ex);
            } catch (JRException ex) {
                ErrorDialog.manejaError(ex);
            }
        }
    }//GEN-LAST:event_jMenuInformesMontadaActionPerformed

    private void jMenuInformesFabricadaPorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuInformesFabricadaPorActionPerformed
        DialogReportElaborador dre=new DialogReportElaborador(this,true,this.getCompactDb());
        dre.setTitle("Planillas suministradas");
        dre.setVisible(true);
        if(dre.getReturnStatus()==DialogReport.RET_OK){
            try {
                Date desde = dre.getDesde();
                Date hasta = dre.getHasta();
                Elaborador elaborador=dre.getElaborador();
                Map<String, Object> parameters = new HashMap<String, Object>();
                if((jTree1.getSelectionPath()!=null)&&(jTree1.getSelectionPath().getPathCount()>1)){
                    Obra obra=(Obra)((DefaultMutableTreeNode)jTree1.getSelectionPath().getPath()[1]).getUserObject();
                    parameters.put("PARAMETER_OBRA", obra.getValue("nombre"));
                }else{
                    parameters.put("PARAMETER_OBRA","FERRAPLAN");
                }
                parameters.put("PARAMETER_FECHA_INICIO", desde);
                parameters.put("PARAMETER_FECHA_FIN", hasta);
                parameters.put("PARAMETER_ELABORADOR_ID",elaborador.getId());
                parameters.put("PARAMETER_ELABORADOR_NOMBRE",elaborador.getValue("nombre"));
                File reportFile = FileUtils.addPathFile("reports", "medicion_suministrada_por.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(reportFile));
                JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, this.getCompactDb().getConector().getConexion());
                JasperViewer.viewReport(print, false);
            } catch (FileNotFoundException ex) {
                ErrorDialog.manejaError(ex);
            } catch (JRException ex) {
                ErrorDialog.manejaError(ex);
            }
        }
    }//GEN-LAST:event_jMenuInformesFabricadaPorActionPerformed

    private void jMenuInformesMontadaPorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuInformesMontadaPorActionPerformed
        DialogReportMontador drm=new DialogReportMontador(this,true,this.getCompactDb());
        drm.setTitle("Planillas montadas");
        drm.setVisible(true);
        if(drm.getReturnStatus()==DialogReport.RET_OK){
            try {
                Date desde = drm.getDesde();
                Date hasta = drm.getHasta();
                Montador montador=drm.getMontador();
                Map<String, Object> parameters = new HashMap<String, Object>();
                if((jTree1.getSelectionPath()!=null)&&(jTree1.getSelectionPath().getPathCount()>1)){
                    Obra obra=(Obra)((DefaultMutableTreeNode)jTree1.getSelectionPath().getPath()[1]).getUserObject();
                    parameters.put("PARAMETER_OBRA", obra.getValue("nombre"));
                }else{
                    parameters.put("PARAMETER_OBRA","FERRAPLAN");
                }
                parameters.put("PARAMETER_FECHA_INICIO", desde);
                parameters.put("PARAMETER_FECHA_FIN", hasta);
                parameters.put("PARAMETER_MONTADOR_ID",montador.getId());
                parameters.put("PARAMETER_MONTADOR_NOMBRE",montador.getValue("nombre"));
                File reportFile = FileUtils.addPathFile("reports", "medicion_montada_por.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(reportFile));
                JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, this.getCompactDb().getConector().getConexion());
                JasperViewer.viewReport(print, false);
            } catch (FileNotFoundException ex) {
                ErrorDialog.manejaError(ex);
            } catch (JRException ex) {
                ErrorDialog.manejaError(ex);
            }
        }
    }//GEN-LAST:event_jMenuInformesMontadaPorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JMenu jMenuArchivo;
    private javax.swing.JMenuItem jMenuArchivoAbrir;
    private javax.swing.JMenuItem jMenuArchivoCerrar;
    private javax.swing.JMenuItem jMenuArchivoFile0;
    private javax.swing.JMenuItem jMenuArchivoFile1;
    private javax.swing.JMenuItem jMenuArchivoFile2;
    private javax.swing.JMenuItem jMenuArchivoFile3;
    private javax.swing.JMenuItem jMenuArchivoFile4;
    private javax.swing.JMenu jMenuArchivoFiles;
    private javax.swing.JMenuItem jMenuArchivoGuardar;
    private javax.swing.JMenuItem jMenuArchivoGuardarComo;
    private javax.swing.JMenuItem jMenuArchivoNuevo;
    private javax.swing.JMenu jMenuAyuda;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenuItem jMenuEditElaboradores;
    private javax.swing.JMenuItem jMenuEditFabricantes;
    private javax.swing.JMenuItem jMenuEditMontadores;
    private javax.swing.JMenu jMenuInformes;
    private javax.swing.JMenuItem jMenuInformesElaborada;
    private javax.swing.JMenuItem jMenuInformesFabricadaPor;
    private javax.swing.JMenuItem jMenuInformesMontada;
    private javax.swing.JMenuItem jMenuInformesMontadaPor;
    private javax.swing.JMenuItem jMenuInformesPedido;
    private javax.swing.JMenuItem jMenuItemAcercaDe;
    private javax.swing.JMenuItem jMenuItemAyuda;
    private javax.swing.JMenuItem jMenuItemSalir;
    private javax.swing.JMenuItem jMenuPrintSchemes;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JMenuItem jPopupMenuAdd;
    private javax.swing.JMenuItem jPopupMenuDelete;
    private javax.swing.JMenuItem jPopupMenuEdit;
    private javax.swing.JMenuItem jPopupMenuTableCopy;
    private javax.swing.JMenuItem jPopupMenuTableCut;
    private javax.swing.JMenuItem jPopupMenuTableDelete;
    private javax.swing.JMenuItem jPopupMenuTablePaste;
    private javax.swing.JMenuItem jPopupMenuTableSelectAll;
    private javax.swing.JMenuItem jPopupMenuTableSelectNothing;
    private javax.swing.JMenuItem jPopupMenuView;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JSplitPane jSplitPane1;
    private es.atareao.alejandria.laf.JLAFTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
    //
    private Preferencias _preferencias;
    private CompactDb _compactDb;
    private Barra _barra;
    private boolean _modificado=false;
    private String _searchFor="";
    private long _lap;
    private String _searchFor2="";
    private long _lap2;
    private ArrayList<Barra> _copiado=null;
    private boolean _cortado=false;
    //
    private JWrapperComboBoxTableCellRenderer jcbtcr_diametro = new JWrapperComboBoxTableCellRenderer();
    private final JWrapperComboBoxTableCellEditor jcbtce_diametro = new JWrapperComboBoxTableCellEditor();
    private JFormaChooserTableCellRenderer jfctcr_forma=new JFormaChooserTableCellRenderer();
    private final JFormaChooserTableCellEditor jfctce_forma=new JFormaChooserTableCellEditor();
    //
    private void setDefaultDir(File file){
        String str=file.toURI().toString();
        this.getPreferencias().setPreferencia("DefaultDir", str);
    }
    private File getDefaultDir(){
        String str=this.getPreferencias().getPreferencia("DefaultDir");
        if((str!=null)&&(str.length()>0)){
            try {
                URI uri = new URI(str);
                return new File(uri);
            } catch (URISyntaxException ex) {
                return FileUtils.getApplicationPath();
            }
        }
        return FileUtils.getApplicationPath();
    }
    //
    /**
     * @return the _preferencias
     */
    public Preferencias getPreferencias() {
        return _preferencias;
    }

    /**
     * @param preferencias the _preferencias to set
     */
    public void setPreferencias(Preferencias preferencias) {
        this._preferencias = preferencias;
    }

    /**
     * @return the _compactDb
     */
    public CompactDb getCompactDb() {
        return _compactDb;
    }

    /**
     * @param compactDb the _compactDb to set
     */
    public void setCompactDb(CompactDb compactDb) {
        this._compactDb = compactDb;
    }

    /**
     * @return the _barra
     */
    public Barra getBarra() {
        return _barra;
    }

    /**
     * @param barra the _barra to set
     */
    public void setBarra(Barra barra) {
        this._barra = barra;
    }

    /**
     * @return the _modificado
     */
    public boolean isModificado() {
        return _modificado;
    }

    /**
     * @param modificado the _modificado to set
     */
    public void setModificado(boolean modificado) {
        this._modificado = modificado;
    }
    //

}

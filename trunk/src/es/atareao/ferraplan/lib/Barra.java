/*
 * Barra
 *
 * File created on 06-dic-2009
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

package es.atareao.ferraplan.lib;

import es.atareao.alejandria.gui.ErrorDialog;
import es.atareao.queensboro.db.Condition;
import es.atareao.queensboro.db.Conector;
import es.atareao.queensboro.db.Order;
import es.atareao.queensboro.db.WrapperImage;
import es.atareao.queensboro.db.WrapperTable;
import es.atareao.queensboro.val.NotEmptyValidator;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.ImageIcon;

/**
 *
 * @author atareao
 */

public class Barra  extends WrapperTable<Barra> implements WrapperImage{
    //
    //********************************CONSTANTES********************************
    //

    //
    // *********************************CAMPOS*********************************
    //

    //
    //******************************CONSTRUCTORES*******************************
    //
    public Barra() throws SQLException {
        this(null);
    }

    /** Creates a new instance of Barra
     * @param conector
     * @throws java.sql.SQLException
     */
     public Barra(Conector conector) throws SQLException {
        super(Barra.class,conector, "public", "barra");
        this.addValidator(new NotEmptyValidator(this,"revision_id"));
        this.addValidator(new NotEmptyValidator(this,"forma_id"));
        this.addValidator(new NotEmptyValidator(this,"diametro_id"));
    }

    //
    //********************************METODOS***********************************
    //
    public Vector<Barra> view(Vector<Condition> conditions) throws SQLException {
        return this.find(conditions);
    }

    public Vector<Barra> view(Condition condition) throws SQLException {
        Vector<Condition> conditions = new Vector<Condition>();
        conditions.add(condition);
        return this.view(conditions);
    }

    @Override
    public Vector<Barra> view() throws SQLException {
        Vector<Condition> conditions = new Vector<Condition>();
        return this.view(conditions);
    }
    public Vector<Barra> view2(Vector<Condition> conditions) throws SQLException{
        return this.find(conditions,"orden");

    }
    public Vector<Barra> view2(Condition condition) throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        conditions.add(condition);
        return this.view2(conditions);
    }
    public Vector<Barra> view2() throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        return this.view2(conditions);
    }
    public Vector<Barra> view2(String revision_id) throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        Condition condition=new Condition("revision_id",revision_id);
        conditions.add(condition);
        return this.view2(conditions);
    }
    public Vector<Barra> view2(String revision_id,String id) throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        Condition condition=new Condition("revision_id",revision_id);
        Condition condition2=new Condition("id",id);
        conditions.add(condition);
        conditions.add(condition2);
        return this.view2(conditions);
    }

    public Vector<Barra> view3(Vector<Condition> conditions) throws SQLException {
        return this.find(conditions,"orden", true);
    }
    public Vector<Barra> view3(Condition condition) throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        conditions.add(condition);
        return this.view3(conditions);
    }
    public Vector<Barra> view3() throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        return this.view3(conditions);
    }
    public Vector<Barra> view3(String revision_id) throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        Condition condition=new Condition("revision_id",revision_id);
        conditions.add(condition);
        return this.view3(conditions);
    }
    public Vector<Barra> view3(String revision_id,String id) throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        Condition condition=new Condition("revision_id",revision_id);
        Condition condition2=new Condition("id",id);
        conditions.add(condition);
        conditions.add(condition2);
        return this.view3(conditions);
    }
    public long count(String revision_id) throws SQLException{
        Condition condition=new Condition("revision_id",revision_id,Condition.IGUAL);
        return count(condition);
    }
    @Override
    public String toString(){
        return this.getValue("referencia");
    }

    @Override
    public ImageIcon getImageIcon() throws MalformedURLException {
        try {
            String forma = this.view2(this.getId()).firstElement().getValue("esquema");
            ImageIcon icon=new ImageIcon(forma);
            return icon;
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex);
        }
        return null;
    }

    @Override
    public String getDescripcion() {
        try {
            String forma = this.view2(this.getId()).firstElement().getValue("referencia");
            return forma;
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex);
        }
        return "";
    }
    //
    //**************************METODOS AUXILIARES******************************
    //

    //
    //**************************METODOS DE ACCESO*******************************
    //
}

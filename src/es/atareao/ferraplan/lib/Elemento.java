/*
 * Elemento
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

import es.atareao.queensboro.db.Condition;
import es.atareao.queensboro.db.Conector;
import es.atareao.queensboro.db.WrapperTable;
import es.atareao.queensboro.val.NotEmptyValidator;
import es.atareao.queensboro.val.UniqueValidator;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author atareao
 */

public class Elemento  extends WrapperTable<Elemento>{
    //
    //********************************CONSTANTES********************************
    //

    //
    // *********************************CAMPOS*********************************
    //

    //
    //******************************CONSTRUCTORES*******************************
    //
    public Elemento() throws SQLException {
        this(null);
    }

    /** Creates a new instance of Familia
     * @param conector
     * @throws java.sql.SQLException
     */
    public Elemento(Conector conector) throws SQLException {
        super(Elemento.class,conector, "public", "elemento");
        this.addValidator(new NotEmptyValidator(this,"zona_id"));
        this.addValidator(new NotEmptyValidator(this,"nombre"));
        Vector<String> columnNames=new Vector<String>();
        columnNames.add("zona_id");
        columnNames.add("nombre");
        this.addValidator(new UniqueValidator(this,columnNames));
    }

    //
    //********************************METODOS***********************************
    //
    public Vector<Elemento> view(Vector<Condition> conditions) throws SQLException {
        return this.find(conditions);
    }

    public Vector<Elemento> view(Condition condition) throws SQLException {
        Vector<Condition> conditions = new Vector<Condition>();
        conditions.add(condition);
        return this.view(conditions);
    }

    @Override
    public Vector<Elemento> view() throws SQLException {
        Vector<Condition> conditions = new Vector<Condition>();
        return this.view(conditions);
    }
    @Override
    public String toString(){
        return this.getValue("nombre");
    }
    //
    //**************************METODOS AUXILIARES******************************
    //

    //
    //**************************METODOS DE ACCESO*******************************
    //
}

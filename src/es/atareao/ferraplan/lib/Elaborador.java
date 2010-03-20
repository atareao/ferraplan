/*
 * Obra
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

public class Elaborador  extends WrapperTable<Elaborador>{
    //
    //********************************CONSTANTES********************************
    //

    //
    // *********************************CAMPOS*********************************
    //

    //
    //******************************CONSTRUCTORES*******************************
    //
    public Elaborador() throws SQLException {
        this(null);
    }

    /** Creates a new instance of Familia
     * @param conector
     * @throws java.sql.SQLException
     */
    public Elaborador(Conector conector) throws SQLException {
        super(Elaborador.class,conector, "public", "elaborador");
        this.addValidator(new NotEmptyValidator(this,"nombre"));
        this.addValidator(new UniqueValidator(this,"nombre"));
    }

    //
    //********************************METODOS***********************************
    //
    public Vector<Elaborador> view(Vector<Condition> conditions) throws SQLException {
        return this.find(conditions);
    }

    public Vector<Elaborador> view(Condition condition) throws SQLException {
        Vector<Condition> conditions = new Vector<Condition>();
        conditions.add(condition);
        return this.view(conditions);
    }

    @Override
    public Vector<Elaborador> view() throws SQLException {
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

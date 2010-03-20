/*
 * Forma
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
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.ImageIcon;

/**
 *
 * @author atareao
 */

public class Forma  extends WrapperTable<Forma> implements WrapperSVG{
    //
    //********************************CONSTANTES********************************
    //

    //
    // *********************************CAMPOS*********************************
    //

    //
    //******************************CONSTRUCTORES*******************************
    //
    public Forma() throws SQLException {
        this(null);
    }

    /** Creates a new instance of Familia
     * @param conector
     * @throws java.sql.SQLException
     */
    public Forma(Conector conector) throws SQLException {
        super(Forma.class,conector, "public", "forma");
        this.addValidator(new NotEmptyValidator(this,"nombre"));
        this.addValidator(new UniqueValidator(this,"nombre"));
        this.addValidator(new NotEmptyValidator(this,"grupo_id"));
        this.addValidator(new NotEmptyValidator(this,"esquema"));
        this.addValidator(new NotEmptyValidator(this,"formula"));
    }

    //
    //********************************METODOS***********************************
    //
    public Vector<Forma> view(Vector<Condition> conditions) throws SQLException {
        return this.find(conditions);
    }

    public Vector<Forma> view(Condition condition) throws SQLException {
        Vector<Condition> conditions = new Vector<Condition>();
        conditions.add(condition);
        return this.view(conditions);
    }

    @Override
    public Vector<Forma> view() throws SQLException {
        Vector<Condition> conditions = new Vector<Condition>();
        return this.view(conditions);
    }
    @Override
    public String toString(){
        return this.getValue("nombre");
    }

    public ImageIcon getImageIcon() throws MalformedURLException {
        String forma = this.getValue("esquema");
        ImageIcon icon=new ImageIcon(forma);
        return icon;
    }

    @Override
    public Vector<WrapperSVG> findAllWraperSVG() throws SQLException{
        Vector<WrapperSVG> res=new Vector<WrapperSVG>();
        for(Forma elemento:this.findAll()){
            res.add(elemento);
        }
        return res;
    }

    @Override
    public String getDescripcion() {
        return this.getValue("nombre");
    }

    @Override
    public String getSVGText() {
        return this.getValue("esquema");
    }
    public String getUrl(){
        return this.getValue("url");
    }
    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object object){
        if((this.getId()==null)||(this.getId().length()==0)){
            return false;
        }
        if(object!=null){
            if(object instanceof Forma){
                String id=((Forma)object).getId();
                if((id!=null)&&(id.length()>0)){
                    if(this.getId().equals(id)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = this.getId().hashCode();
        return hash;
    }
    //
    //**************************METODOS AUXILIARES******************************
    //

    //
    //**************************METODOS DE ACCESO*******************************
    //
}

/*
 * ***********************Software description*********************************
 * WrapperSVG.java
 * 
 * 
 * ***********************Software description*********************************
 * 
 * Copyright (C) 2008 - Lorenzo Carbonell
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
 */

package es.atareao.ferraplan.lib;

import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author Propietario
 */
public interface WrapperSVG {
    /**
     * 
     * @return
     */
    public String getId();
    /**
     * 
     * @return
     */
    public String getSVGText();
    /**
     * 
     * @return
     */
    public String getDescripcion();

    public Vector<WrapperSVG> findAllWraperSVG() throws SQLException;
}

/*
 * Main
 *
 * File created on 05-dic-2009
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

package es.atareao.ferraplan.main;

import es.atareao.alejandria.gui.ErrorDialog;
import es.atareao.alejandria.gui.SplashScreen;
import es.atareao.alejandria.lib.FileUtils;
import es.atareao.alejandria.lib.INIFile;
import es.atareao.ferraplan.gui.Ferraplan;
import javax.swing.UIManager;

/**
 *
 * @author atareao
 */
public class Main {

    public static void main(String[] args) {
        String iniFileName=FileUtils.addPathFile(System.getProperty("user.dir"),"ferraplan.ini").toString();
        INIFile iniFile=new INIFile(iniFileName);
        //
        try {
            UIManager.setLookAndFeel(iniFile.getStringProperty("Preferencias","LookAndFeel"));
        } catch (Exception e) {
            ErrorDialog.manejaError(e,false);
        }
        SplashScreen splashScreen = new SplashScreen ("/es/atareao/ferraplan/img/ferraplan_presen.png");
        splashScreen.open(1500);
        //
        Ferraplan ferraplan=new Ferraplan(iniFile);
        ferraplan.setVisible(true);
    }

}

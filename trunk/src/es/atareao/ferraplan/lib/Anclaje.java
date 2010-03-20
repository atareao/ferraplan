/*
 * Anclaje
 *
 * File created on 27-feb-2010
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

import es.atareao.alejandria.lib.Convert;

/**
 *
 * @author atareao
 */

public class Anclaje {
    //
    //********************************CONSTANTES********************************
    //
    public static enum Posicion {I,II};
    public static enum Esfuerzo {COMPRESION,TRACCION20,TRACCION25,TRACCION33,TRACCION50,TRACCIONS50};
    public static enum Tipo {PROLONGACION_RECTA,PATILLA_GANCHO,BARRA_TRANSVERSAL_SOLDADA}

    //
    // *********************************CAMPOS*********************************
    //

    //
    //******************************CONSTRUCTORES*******************************
    //

    //
    //********************************METODOS***********************************
    //
    public static double calcula_longitud_basica(Posicion posicion,Diametro diametro,Acero acero,Hormigon hormigon){
        if((diametro!=null)&&(acero!=null)&&(hormigon!=null)){
            if((diametro.getValue("diametro")!=null)&&(acero.getValue("limite_elastico")!=null)){
                double d=Convert.toDouble(diametro.getValue("diametro"))/10.0;
                double le=Convert.toDouble(acero.getValue("limite_elastico"));
                double lb=0.0;
                double lb1=0.0;
                double lb2=0.0;
                switch(posicion){
                    case I:
                        lb1=calcula_m(hormigon,acero)*Math.pow(d,2.0);
                        lb2=le/20.0*d;
                        if(lb1>lb2){
                            lb=lb1;
                        }else{
                            lb=lb2;
                        }
                        break;
                    case II:
                        lb1=1.4*calcula_m(hormigon,acero)*Math.pow(d,2.0);
                        lb2=le/14.0*d;
                        if(lb1>lb2){
                            lb=lb1;
                        }else{
                            lb=lb2;
                        }
                        break;
                }
                return lb;
            }
        }
        return 0.0;
    }
    public static double calcula_longitud_anclaje(Posicion posicion,Diametro diametro,Acero acero,Hormigon hormigon,Esfuerzo esfuerzo,double an,double ar,Tipo tipo){
        if((diametro!=null)&&(acero!=null)&&(hormigon!=null)){
            double lb=calcula_longitud_basica(posicion,diametro,acero,hormigon);
            double beta=calcula_beta(tipo,esfuerzo);
            double d=Convert.toDouble(diametro.getValue("diametro"))/10.0;
            double ln=lb*beta*an/ar;
            if(ln<d*10.0){
                ln=d*10.0;
            }
            if(ln<15.0){
                ln=15.0;
            }
            if(esfuerzo==Esfuerzo.COMPRESION){
                if(ln<2.0/3.0*lb){
                    ln=2.0/3.0*lb;
                }
            }else{
                if(ln<1.0/3.0*lb){
                    ln=1.0/3.0*lb;
                }
            }
            return ln;
        }
        return 0.0;
    }
    public static double calcula_beta(Tipo tipo, Esfuerzo esfuerzo){
        switch(esfuerzo){
            case COMPRESION:
                switch(tipo){
                    case PROLONGACION_RECTA:
                        return 1.0;
                    case PATILLA_GANCHO:
                        return 1.0;
                    case BARRA_TRANSVERSAL_SOLDADA:
                        return 0.7;
                }
                break;
            default:
                switch(tipo){
                    case PROLONGACION_RECTA:
                        return 1.0;
                    case PATILLA_GANCHO:
                        return 0.7;
                    case BARRA_TRANSVERSAL_SOLDADA:
                        return 0.7;
                }
                break;
        }
        return 0.0;
    }
    public static double calcula_longitud_solapo(Posicion posicion,Diametro diametro,Acero acero,Hormigon hormigon,double reparto,Esfuerzo esfuerzo){
        double lb=calcula_longitud_basica(posicion,diametro,acero,hormigon);
        double alfa=calcula_alfa(diametro,reparto,esfuerzo);
        double d=Convert.toDouble(diametro.getValue("diametro"))/10.0;
        double ls=lb*alfa;
        if(ls<d*10.0){
            ls=d*10.0;
        }
        if(ls<15.0){
            ls=15.0;
        }
        if(esfuerzo==Esfuerzo.COMPRESION){
            if(ls<2.0/3.0*lb){
                ls=2.0/3.0*lb;
            }
        }else{
            if(ls<1.0/3.0*lb){
                ls=1.0/3.0*lb;
            }
        }
        return ls;
    }
    public static double calcula_alfa(Diametro diametro,double distancia,Esfuerzo esfuerzo){
        double d=Convert.toDouble(diametro.getValue("diametro"))/10.0;
        if(distancia<=10.0*d){
            switch(esfuerzo){
                case COMPRESION:
                    return 1.0;
                case TRACCION20:
                    return 1.2;
                case TRACCION25:
                    return 1.4;
                case TRACCION33:
                    return 1.6;
                case TRACCION50:
                    return 1.8;
                case TRACCIONS50:
                    return 2.0;
            }
        }else{
            switch(esfuerzo){
                case COMPRESION:
                    return 1.0;
                case TRACCION20:
                    return 1.0;
                case TRACCION25:
                    return 1.1;
                case TRACCION33:
                    return 1.2;
                case TRACCION50:
                    return 1.3;
                case TRACCIONS50:
                    return 1.4;
            }
        }
        return 0.0;
    }

    public static double calcula_m(Hormigon hormigon,Acero acero){
        if((acero.getValue("nombre").equals("B400S"))||(acero.getValue("nombre").equals("B400SD"))){
            if(hormigon.getValue("resistencia").equals("25")){
                return 12.0;
            }
            if(hormigon.getValue("resistencia").equals("30")){
                return 10.0;
            }
            if(hormigon.getValue("resistencia").equals("35")){
                return 9.0;
            }
            if(hormigon.getValue("resistencia").equals("40")){
                return 8.0;
            }
            if(hormigon.getValue("resistencia").equals("45")){
                return 7.0;
            }
            if(hormigon.getValue("resistencia").equals("50")){
                return 7.0;
            }
        }else{
            if(hormigon.getValue("resistencia").equals("25")){
                return 15.0;
            }
            if(hormigon.getValue("resistencia").equals("30")){
                return 13.0;
            }
            if(hormigon.getValue("resistencia").equals("35")){
                return 12.0;
            }
            if(hormigon.getValue("resistencia").equals("40")){
                return 11.0;
            }
            if(hormigon.getValue("resistencia").equals("45")){
                return 10.0;
            }
            if(hormigon.getValue("resistencia").equals("50")){
                return 10.0;
            }
        }
        return 0.0;
    }
    //
    //**************************METODOS AUXILIARES******************************
    //

    //
    //**************************METODOS DE ACCESO*******************************
    //
}

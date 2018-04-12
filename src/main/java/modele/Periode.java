/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.GregorianCalendar;

/**
 *
 * @author boussanl
 */
public class Periode {
    private GregorianCalendar dateDebut;
    private GregorianCalendar dateFin;
    private GregorianCalendar dateActuelle;

    public Periode(GregorianCalendar dateDebut, GregorianCalendar dateFin) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;    
        this.dateActuelle = new GregorianCalendar();     
    }
    
    public Periode(){
        this.dateActuelle = new GregorianCalendar(); 
    }
    
    public Periode(String dateDebut, String dateFin) {
        String[] debut = dateDebut.split("-");
        String[] fin = dateFin.split("-");
        this.dateDebut = new GregorianCalendar(Integer.parseInt(debut[0].trim()), Integer.parseInt(debut[1].trim()), Integer.parseInt(debut[2].trim()));
        this.dateFin = new GregorianCalendar(Integer.parseInt(fin[0].trim()), Integer.parseInt(fin[1].trim()), Integer.parseInt(fin[2].trim()));
        this.dateActuelle = new GregorianCalendar();
    }

    public GregorianCalendar getDateDebut() {
        return dateDebut;
    }
    
    public boolean estEnCours() {
        return dateActuelle.after(dateDebut) && dateActuelle.before(dateFin);
    }
    
    public String dateToString(GregorianCalendar date) {
        String result = "";
        int year = date.get(GregorianCalendar.YEAR);
        int month = date.get(GregorianCalendar.MONTH);
        int day = date.get(GregorianCalendar.DAY_OF_MONTH);
        if (year < 1000) {
            result += "0";
        }
        result += year + "-";
        if (month < 10) {
            result += "0";
        }
        result += month + "-";
        if (day < 10) {
            result += "0";
        }
        result += day;
        return result;
    }
    
    public String debutToString() {
        return dateToString(dateDebut);
    }
    
    public String dateActuelleToString() {
        return dateToString(dateActuelle);
    }
    
    public String finToString() {
        return dateToString(dateFin);
    }

    public GregorianCalendar getDateFin() {
        return dateFin;
    }

    public GregorianCalendar getDateActuelle() {
        return dateActuelle;
    }
    
}

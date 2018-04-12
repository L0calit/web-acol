/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;


/**
 *
 * @author boussanl
 */
public class Periode {
    private Date dateDebut;
    private Date dateFin;
    private final String dateActuelle;
    private Date jour;

    public Periode(Date dateDebut, Date dateFin) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        GregorianCalendar date = new GregorianCalendar(); 
        this.dateActuelle = date.get(GregorianCalendar.DAY_OF_MONTH)+"/" + 
                            date.get(GregorianCalendar.MONTH) + "/" + 
                            date.get(GregorianCalendar.YEAR);
        this.jour = Calendar.getInstance().getTime();
        
    }
    
    public Periode(){
        GregorianCalendar date = new GregorianCalendar(); 
        this.dateActuelle = date.get(GregorianCalendar.DAY_OF_MONTH)+"/" + 
                            date.get(GregorianCalendar.MONTH) + "/" + 
                            date.get(GregorianCalendar.YEAR);
        
    }
    public Date getDateDebut() {
        return dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public String getDateActuelle() {
        return dateActuelle;
    }
    
    public Date getJour(){
        return jour;
    }
            
    

    
}

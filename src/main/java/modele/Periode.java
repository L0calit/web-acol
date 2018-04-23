package modele;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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

    public Periode() {
        this.dateActuelle = new GregorianCalendar();
    }

    public Periode(String dateDebut, String dateFin) {
        String[] debut = dateDebut.split("-");
        String[] fin = dateFin.split("-");
        this.dateDebut = new GregorianCalendar(Integer.parseInt(debut[0].trim()), 
                Integer.parseInt(debut[1].trim()) - 1, Integer.parseInt(debut[2].trim()));
        this.dateFin = new GregorianCalendar(Integer.parseInt(fin[0].trim()),
                Integer.parseInt(fin[1].trim()) - 1, Integer.parseInt(fin[2].trim()));
        this.dateActuelle = new GregorianCalendar();
    }

    public GregorianCalendar getDateDebut() {
        return dateDebut;
    }

    /**
     * Verifie si la periode est finie ou non
     * 
     * @return true si la periode est finie, false sinon
     */
    public boolean estFini() {
        return dateActuelle.after(dateFin);
    }

    /**
     * Verifie si la date de Debut est bien avant la date de fin 
     * 
     * @return true si elle l'est, false sinon
     */
    public boolean periodeIncorrecte() {
        return dateDebut.after(dateFin);
    }

    /**
     * Verifie si la date de debut est avant la date actuelle
     * 
     * @return true si elle l'est, false sinon
     */
    public boolean avantDateActuelle() {
        return dateActuelle.after(dateDebut);
    }

    /**
     * Recupere le prochain créneaux disponible
     * 
     * @param creneauxJour
     * @param creneauxHeure
     * @return 
     */
    public String getProchainCreneaux(String creneauxJour, String creneauxHeure) {
        int jourActuel = dateActuelle.get(Calendar.DAY_OF_WEEK);
        GregorianCalendar prochainCreneaux = new GregorianCalendar();
        int creneaux = parserJour(creneauxJour.trim());
        System.out.println("Creneaux :" + creneaux);
        System.out.println("JourActuel :" + jourActuel);
        if ((creneaux - jourActuel) <= 2) {
            return "";
        }
        prochainCreneaux.add(Calendar.DAY_OF_MONTH, (creneaux - jourActuel) % 7);
        return dateToString(prochainCreneaux);
    }

    /**
     * Permet de parser le jour 
     * @param creneauxJour
     * @return un entier correspondant à un jour
     */
    public int parserJour(String creneauxJour) {
        switch (creneauxJour) {
            case "lundi":
                return 2;
            case "mardi":
                return 3;
            case "mercredi":
                return 4;
            case "jeudi":
                return 5;
            case "vendredi":
                return 6;
            case "samedi":
                return 7;
            default:
                return 1;
        }
    }

    /**
     * Verifie si la periode donnée chevauche la periode actuelle
     * 
     * @param periode
     * @return true si elle chevauche, false sinon
     */
    public boolean chevauchement(Periode periode) {
        if (periode.getDateDebut().after(dateDebut) && periode.getDateDebut().before(dateFin)) {
            return true;
        } else if (periode.getDateFin().after(dateDebut) && periode.getDateFin().before(dateFin)) {
            return true;
        }
        return false;
    }

    /**
     * Verifie si la liste des périodes données chevauche bien la période actuelle
     * 
     * @param periodes
     * @return true si elle chevauche, false sinon
     */
    public boolean chevauchementList(List<Periode> periodes) {
        for (Periode periode : periodes) {
            if (chevauchement(periode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifie si la periode est en cours ou non
     * 
     * @return true si elle est en cours, false sinon
     */
    public boolean estEnCours() {
        return dateActuelle.after(dateDebut) && dateActuelle.before(dateFin);
    }

    /**
     * Recupère le nombre de semaines dans la période
     * 
     * @return un entier correspondant au nombres de semaines dans la période
     */
    public int getNbSemaines() {
        long nbSemaines = (dateDebut.getTimeInMillis() - dateFin.getTimeInMillis()) / (86400000 * 7);
        return (int) Math.abs(nbSemaines);
    }

    /**
     * Permet de transformer une date donnée en string
     * 
     * @param date
     * @return un string représentant la date donnée
     */
    public String dateToString(GregorianCalendar date) {
        String result = "";
        int year = date.get(GregorianCalendar.YEAR);
        int month = date.get(GregorianCalendar.MONTH) + 1;
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

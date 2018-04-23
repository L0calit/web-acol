CREATE TABLE ABSENCES (NOMACTIVITE CHAR(20) NOT NULL, CRENEAUXJOUR CHAR(50) NOT NULL, CRENEAUXHEURE CHAR(50) NOT NULL, DATEABSENCE CHAR(50) NOT NULL, LOGINPARENT CHAR(20) NOT NULL, NOMENFANT CHAR(20) NOT NULL, PRIMARY KEY (NOMACTIVITE, CRENEAUXJOUR, CRENEAUXHEURE, DATEABSENCE, LOGINPARENT, NOMENFANT));

CREATE TABLE ACCOMPAGNATEUR (MAIL CHAR(100) NOT NULL, NOM CHAR(20), PRENOM CHAR(20), NUMTELEPHONE CHAR(20), PRIMARY KEY (MAIL));
INSERT INTO BOUSSANL.ACCOMPAGNATEUR (MAIL, NOM, PRENOM, NUMTELEPHONE)
	VALUES ('manu-macron.com                                                                                     ', 'macron              ', 'manu                ', '0678965821          ');
INSERT INTO BOUSSANL.ACCOMPAGNATEUR (MAIL, NOM, PRENOM, NUMTELEPHONE)
	VALUES ('nico-sarko.com                                                                                      ', 'sarko               ', 'nico                ', '0674859612          ');
INSERT INTO BOUSSANL.ACCOMPAGNATEUR (MAIL, NOM, PRENOM, NUMTELEPHONE)
	VALUES ('francis-holland.com                                                                                 ', 'holland             ', 'francis             ', '0614253678          ');
INSERT INTO BOUSSANL.ACCOMPAGNATEUR (MAIL, NOM, PRENOM, NUMTELEPHONE)
	VALUES ('angel-merko.com                                                                                     ', 'merko               ', 'angel               ', '0748592635          ');

CREATE TABLE ACTIVITERESERVE (PRENOMENFANT CHAR(20) NOT NULL, LOGINPARENT CHAR(20) NOT NULL, NOMACTIVITE CHAR(20) NOT NULL, CRENEAUXJOUR CHAR(10) NOT NULL, CRENEAUXHEURE CHAR(3) NOT NULL, DATEDEBUT CHAR(50) NOT NULL, DATEFIN CHAR(50) NOT NULL, PRIMARY KEY (PRENOMENFANT, LOGINPARENT, NOMACTIVITE, CRENEAUXJOUR, CRENEAUXHEURE, DATEDEBUT, DATEFIN));
INSERT INTO BOUSSANL.ACTIVITERESERVE (PRENOMENFANT, LOGINPARENT, NOMACTIVITE, CRENEAUXJOUR, CRENEAUXHEURE, DATEDEBUT, DATEFIN)
	VALUES ('Josiane             ', 'coude               ', 'Garderie            ', 'lundi     ', '7h ', '2018-04-13                                        ', '2018-04-28                                        ');
INSERT INTO BOUSSANL.ACTIVITERESERVE (PRENOMENFANT, LOGINPARENT, NOMACTIVITE, CRENEAUXJOUR, CRENEAUXHEURE, DATEDEBUT, DATEFIN)
	VALUES ('Josiane             ', 'coude               ', 'Garderie            ', 'mardi     ', '7h ', '2018-04-13                                        ', '2018-04-28                                        ');
INSERT INTO BOUSSANL.ACTIVITERESERVE (PRENOMENFANT, LOGINPARENT, NOMACTIVITE, CRENEAUXJOUR, CRENEAUXHEURE, DATEDEBUT, DATEFIN)
	VALUES ('Josiane             ', 'coude               ', 'Garderie            ', 'vendredi  ', '7h ', '2018-04-13                                        ', '2018-04-28                                        ');
INSERT INTO BOUSSANL.ACTIVITERESERVE (PRENOMENFANT, LOGINPARENT, NOMACTIVITE, CRENEAUXJOUR, CRENEAUXHEURE, DATEDEBUT, DATEFIN)
	VALUES ('Josiane             ', 'coude               ', 'football            ', 'mercredi  ', '17h', '2018-04-13                                        ', '2018-04-28                                        ');
INSERT INTO BOUSSANL.ACTIVITERESERVE (PRENOMENFANT, LOGINPARENT, NOMACTIVITE, CRENEAUXJOUR, CRENEAUXHEURE, DATEDEBUT, DATEFIN)
	VALUES ('Louis               ', 'coude               ', 'Garderie            ', 'jeudi     ', '16h', '2018-04-13                                        ', '2018-04-28                                        ');
INSERT INTO BOUSSANL.ACTIVITERESERVE (PRENOMENFANT, LOGINPARENT, NOMACTIVITE, CRENEAUXJOUR, CRENEAUXHEURE, DATEDEBUT, DATEFIN)
	VALUES ('Louis               ', 'coude               ', 'Garderie            ', 'mardi     ', '15h', '2018-04-13                                        ', '2018-04-28                                        ');

CREATE TABLE ACTIVITES (NOM CHAR(20) NOT NULL, CRENEAUXJOUR CHAR(10) NOT NULL, CRENEAUXHEURE CHAR(3) NOT NULL, CLASSE CHAR(20), MAILACCOMPAGNATEUR1 CHAR(100), MAILACCOMPAGNATEUR2 CHAR(100), PRIX NUMBER(38), EFFECTIF NUMBER(38), DATEDEBUT CHAR(50), DATEFIN CHAR(50), PRIMARY KEY (NOM, CRENEAUXJOUR, CRENEAUXHEURE));
INSERT INTO BOUSSANL.ACTIVITES (NOM, CRENEAUXJOUR, CRENEAUXHEURE, CLASSE, MAILACCOMPAGNATEUR1, MAILACCOMPAGNATEUR2, PRIX, EFFECTIF, DATEDEBUT, DATEFIN)
	VALUES ('tennis              ', 'vendredi  ', '14h', 'MS/GS/CP            ', 'nico-sarko.com                                                                                      ', 'angel-merko.com                                                                                     ', 10, 25, '2018-12-05                                        ', '2018-12-20                                        ');
INSERT INTO BOUSSANL.ACTIVITES (NOM, CRENEAUXJOUR, CRENEAUXHEURE, CLASSE, MAILACCOMPAGNATEUR1, MAILACCOMPAGNATEUR2, PRIX, EFFECTIF, DATEDEBUT, DATEFIN)
	VALUES ('football            ', 'mercredi  ', '17h', 'MS/GS/CP            ', 'angel-merko.com                                                                                     ', 'manu-macron.com                                                                                     ', 45, 11, '2018-07-07                                        ', '2018-09-07                                        ');

CREATE TABLE BIBLIOGRAPHIE (ID NUMBER(6) DEFAULT "BOUSSANL"."ID_SEQ"."NEXTVAL"  NOT NULL, AUTEUR NVARCHAR2(100) NOT NULL, TITRE NVARCHAR2(100) NOT NULL, PRIMARY KEY (ID));
INSERT INTO BOUSSANL.BIBLIOGRAPHIE (ID, AUTEUR, TITRE)
	VALUES (3, 'Victor Hugo', 'Les misérables');
INSERT INTO BOUSSANL.BIBLIOGRAPHIE (ID, AUTEUR, TITRE)
	VALUES (5, 'Voltaire', 'Candide');
INSERT INTO BOUSSANL.BIBLIOGRAPHIE (ID, AUTEUR, TITRE)
	VALUES (21, 'Molière', 'Don Juan');

CREATE TABLE COMPTES (NC NUMBER(38) NOT NULL, NOM VARCHAR2(10), SOLDE NUMBER(38), PRIMARY KEY (NC));
INSERT INTO BOUSSANL.COMPTES (NC, NOM, SOLDE)
	VALUES (1, 'Paul', 1249);
INSERT INTO BOUSSANL.COMPTES (NC, NOM, SOLDE)
	VALUES (2, 'Paul', 1000000249);
INSERT INTO BOUSSANL.COMPTES (NC, NOM, SOLDE)
	VALUES (10, 'Claude', 150);
INSERT INTO BOUSSANL.COMPTES (NC, NOM, SOLDE)
	VALUES (11, 'Claude', 1050);

CREATE TABLE EMPLOYE (LOGIN CHAR(20) NOT NULL, MDP CHAR(20) NOT NULL, PRIMARY KEY (LOGIN));
INSERT INTO BOUSSANL.EMPLOYE (LOGIN, MDP)
	VALUES ('root                ', 'root                ');
INSERT INTO BOUSSANL.EMPLOYE (LOGIN, MDP)
	VALUES ('Marie               ', '123                 ');
INSERT INTO BOUSSANL.EMPLOYE (LOGIN, MDP)
	VALUES ('Leon             ', '1234                 ');

CREATE TABLE ENFANT (SEXE CHAR(1), NOM CHAR(20) NOT NULL, PRENOM CHAR(20) NOT NULL, LOGINPARENT CHAR(20) NOT NULL, CLASSE CHAR(5), DATEDENAISSANCE CHAR(20), CANTINE CHAR(100), REGIME CHAR(50), DIVERS VARCHAR2(200), PRIMARY KEY (NOM, PRENOM, LOGINPARENT));
INSERT INTO BOUSSANL.ENFANT (SEXE, NOM, PRENOM, LOGINPARENT, CLASSE, DATEDENAISSANCE, CANTINE, REGIME, DIVERS)
	VALUES ('A', 'Couderc             ', 'Josiane             ', 'coude               ', 'CP   ', '30/06/1996          ', 'me/                                                                                                 ', 'que du domac                                      ', ' Rien à signaler');
INSERT INTO BOUSSANL.ENFANT (SEXE, NOM, PRENOM, LOGINPARENT, CLASSE, DATEDENAISSANCE, CANTINE, REGIME, DIVERS)
	VALUES ('A', 'Couderc             ', 'Louis               ', 'coude               ', 'CE2  ', '30/06/1885          ', 'lu/ma/me/je/ve/                                                                                     ', 'healthy                                           ', 'Travailleur');

CREATE TABLE FACTURES (LOGINPARENT CHAR(20) NOT NULL, DATEDEBUT CHAR(50) NOT NULL, DATEFIN CHAR(50) NOT NULL, PRIXTOTAL NUMBER(38), MONTANTENLEVE NUMBER(38), MONTANTFINAL NUMBER(38), PRIMARY KEY (LOGINPARENT, DATEDEBUT, DATEFIN));
INSERT INTO BOUSSANL.FACTURES (LOGINPARENT, DATEDEBUT, DATEFIN, PRIXTOTAL, MONTANTENLEVE, MONTANTFINAL)
	VALUES ('boussanl            ', '2018-04-13                                        ', '2018-04-28                                        ', 0, 0, 0);
INSERT INTO BOUSSANL.FACTURES (LOGINPARENT, DATEDEBUT, DATEFIN, PRIXTOTAL, MONTANTENLEVE, MONTANTFINAL)
	VALUES ('coude               ', '2018-04-13                                        ', '2018-04-28                                        ', 68, 0, 68);
INSERT INTO BOUSSANL.FACTURES (LOGINPARENT, DATEDEBUT, DATEFIN, PRIXTOTAL, MONTANTENLEVE, MONTANTFINAL)
	VALUES ('azerty              ', '2018-04-13                                        ', '2018-04-28                                        ', 0, 0, 0);
INSERT INTO BOUSSANL.FACTURES (LOGINPARENT, DATEDEBUT, DATEFIN, PRIXTOTAL, MONTANTENLEVE, MONTANTFINAL)
	VALUES ('vadordark           ', '2018-04-13                                        ', '2018-04-28                                        ', 0, 0, 0);

CREATE TABLE PARENT (NOM CHAR(20), PRENOM CHAR(20), ADRESSE CHAR(250), LOGIN CHAR(20) NOT NULL, MDP CHAR(20), PRIMARY KEY (LOGIN));
INSERT INTO BOUSSANL.PARENT (NOM, PRENOM, ADRESSE, LOGIN, MDP)
	VALUES ('Couderc             ', 'Maman               ', 'Annecy                                                                                                                                                                                                                                                 ', 'coude               ', 'coupe               ');
INSERT INTO BOUSSANL.PARENT (NOM, PRENOM, ADRESSE, LOGIN, MDP)
	VALUES ('Bernard             ', 'JeanMi              ', 'lyon                                                                                                                                                                                                                                                      ', 'azerty              ', '1234                ');
INSERT INTO BOUSSANL.PARENT (NOM, PRENOM, ADRESSE, LOGIN, MDP)
	VALUES ('Boussant            ', 'Luc                 ', 'sdfsdfsdf                                                                                                                                                                                                                                                 ', 'boussanl            ', '123                 ');
INSERT INTO BOUSSANL.PARENT (NOM, PRENOM, ADRESSE, LOGIN, MDP)
	VALUES ('vador               ', 'dark                ', 'death star                                                                                                                                                                                                                                                ', 'vadordark           ', 'vadordark           ');

CREATE TABLE PERIODE (DATEDEBUT CHAR(50) NOT NULL, DATEFIN CHAR(50) NOT NULL, PRIMARY KEY (DATEDEBUT, DATEFIN));
INSERT INTO BOUSSANL.PERIODE (DATEDEBUT, DATEFIN)
	VALUES ('2018-04-13                                        ', '2018-04-28                                        ');
INSERT INTO BOUSSANL.PERIODE (DATEDEBUT, DATEFIN)
	VALUES ('2018-07-07                                        ', '2018-09-07                                        ');
INSERT INTO BOUSSANL.PERIODE (DATEDEBUT, DATEFIN)
	VALUES ('2018-12-05                                        ', '2018-12-20                                        ');

CREATE TABLE REGIMES (REGIME CHAR(20));
INSERT INTO BOUSSANL.REGIMES (REGIME)
	VALUES ('sans porc           ');
INSERT INTO BOUSSANL.REGIMES (REGIME)
	VALUES ('sans viande         ');
INSERT INTO BOUSSANL.REGIMES (REGIME)
	VALUES ('vege                ');
INSERT INTO BOUSSANL.REGIMES (REGIME)
	VALUES ('healthy             ');
INSERT INTO BOUSSANL.REGIMES (REGIME)
	VALUES ('vegan               ');
INSERT INTO BOUSSANL.REGIMES (REGIME)
	VALUES ('que du domac        ');

CREATE TABLE USERS (LOGIN VARCHAR2(10) NOT NULL, PASSWORD VARCHAR2(100) NOT NULL, VILLE CHAR(3) NOT NULL, PRIMARY KEY (LOGIN));
INSERT INTO BOUSSANL.USERS (LOGIN, PASSWORD, VILLE)
	VALUES ('toto', 'toto', 'GRE');
INSERT INTO BOUSSANL.USERS (LOGIN, PASSWORD, VILLE)
	VALUES ('titi', 'titi', 'SMH');
INSERT INTO BOUSSANL.USERS (LOGIN, PASSWORD, VILLE)
	VALUES ('orange', 'presse', 'GRE');
INSERT INTO BOUSSANL.USERS (LOGIN, PASSWORD, VILLE)
	VALUES ('test1', 'carisme', 'GRE');

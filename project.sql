.open project.db

PRAGMA auto_vacuum = 1;
PRAGMA automatic_index = 1;
PRAGMA case_sensitive_like = 0;
PRAGMA defer_foreign_keys = 0;
PRAGMA encoding = 'UTF-8';
PRAGMA foreign_keys = 1;
PRAGMA ignore_check_constraints = 0;
PRAGMA journal_mode = WAL;
PRAGMA query_only = 0;
PRAGMA recursive_triggers = 1;
PRAGMA reverse_unordered_selects = 0;
PRAGMA secure_delete = 0;
PRAGMA synchronous = NORMAL;

CREATE TABLE Benutzer(
	EMail text, 
	Passwort text,
	Vorname text,
	Nachname text, 

	constraint check_EMail check(glob('*@*.*', EMail)), 
	constraint check_EMailZ check(length(substr(EMail, instr(EMail, '.') + 1)) > 0 AND NOT glob('*[^a-zA-Z]*', substr(EMail, instr(EMail, '.') + 1))),
	constraint check_EMailY check(length(substr(EMail, instr(EMail, '@') + 1, length(EMail) - instr(EMail, '.') - 1))>0 AND 
									NOT glob('*[^a-zA-Z]*', substr(EMail, instr(EMail, '@') + 1, length(EMail) - instr(EMail, '.') - 1))),
	constraint check_EMailX check(length(substr(EMail, 1, instr(EMail, '@') - 1))>0 AND NOT glob('*[^a-zA-Z]*', substr(EMail, instr(EMail, '.') + 1))),
	constraint check_Passwort check(length(Passwort) > 5 AND glob('*[A-Z]*', Passwort) AND glob('*[0-9]*', Passwort)),
	constraint check_Vorname check(length(Vorname) > 0 AND NOT glob('*[0-9]*', Vorname) AND NOT glob('*[^a-zA-Z]*',Vorname)),
	constraint check_Nachname check(length(Nachname) > 0 AND NOT glob('*[0-9]*', Nachname) AND NOT glob('*[^a-zA-Z]*', Nachname)),

	PRIMARY KEY (EMail)
);

CREATE TABLE Autor(
	EMail text,
	Preis real(10, 2),
	Pseudonym text,
	Avatar blob,

	constraint check_text check(length(EMail) > 0),
	constraint check_preis check(Preis >= 0),
	constraint check_Pseudonym check(length(Pseudonym) > 0),
	
	PRIMARY KEY (EMail), 

	FOREIGN KEY (EMail) REFERENCES Benutzer(EMail) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Transaktion(
	tid integer,
	EMailA text,
	EMailB text,
	Zweck text,
	Betrag real(10, 2),
	Datum varchar default CURRENT_TIMESTAMP,
	Zahlungsmittelwahl text collate nocase ,
	Gutscheincode real(10, 2) default 0.0,
	
	constraint check_EMails check((length(EMailA) > 0) AND (length(EMailB) > 0)),
	constraint check_Zweck check(length(Zweck) > 0),
	constraint check_date check(date(Datum) is not NULL),
	constraint check_Zahlungsmittelwahl check(length(Zahlungsmittelwahl) > 0 
				AND (Zahlungsmittelwahl IN ('paypal', 'rechnung', 'kreditkarte', 'paysafe'))),
	constraint check_Betrag check(Betrag > 0),
	constraint check_Gutscheincode check(Gutscheincode >= 0),
	
	PRIMARY KEY (tid), 

	FOREIGN KEY (EMailA) REFERENCES Autor(EMail) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (EMailB) REFERENCES Benutzer(EMail) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Seite(
	sid integer,
	EMail text,
	Datum varchar,
	Nummer integer,
	Typ text collate nocase,

	constraint check_EMail check(length(EMail) > 0), 
	constraint check_datum check(date(Datum) is not NULL),
	constraint check_Nummer check(Nummer > 0),
	constraint check_typ check(Typ IN ('private', 'public')),
	
	PRIMARY KEY (sid), 

	FOREIGN KEY (EMail) REFERENCES Autor(EMail) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TRIGGER tagebuchPreis AFTER INSERT ON Autor 
WHEN new.Preis > 0 AND new.Preis < 0
BEGIN 
	UPDATE Autor SET Preis = 0 
	WHERE Preis = new.Preis;
END;

CREATE TRIGGER psen AFTER UPDATE ON Autor
WHEN (SELECT sid FROM seite WHERE seite.email = new.EMail and seite.typ = 'private') IS NULL AND new.Preis > 0
BEGIN
	UPDATE Autor SET Preis = 0
	WHERE EMail = new.EMail;
END;


CREATE TRIGGER MaxEineSeiteProTag BEFORE INSERT ON Seite BEGIN
	SELECT CASE 
		WHEN (SELECT Seite.sid From Seite WHERE Seite.EMail = new.EMail AND (new.Datum = Seite.Datum)) IS NOT NULL
		THEN RAISE(ABORT, "Pro Tag darf ein Autor nur 1 Seite erstellen!") 
	END;
END;

CREATE TABLE Eintrag(
	eid integer,
	sid integer, 
	Titel text,
	Uhrzeit varchar default CURRENT_TIMESTAMP,
	Text text,
	
	constraint check_titel check(length(Titel) > 0),
	constraint check_uhrzeit check(glob('[0-9][0-9]:[0-9][0-9]', Uhrzeit)),
	constraint check_text check(length(Text) > 0),

	PRIMARY KEY (eid),
 
	FOREIGN KEY (sid) REFERENCES Seite(sid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Bild(
	bid integer,
	eid integer, 
	Bild blob,
	
	PRIMARY KEY (bid),
	
	FOREIGN KEY (eid) REFERENCES Eintrag(eid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Tag(
	Tag text collate nocase,

	constraint check_tag check(length(Tag) > 0 AND NOT glob('*[0-9]*', tag)),
	
	PRIMARY KEY (Tag) 
);

CREATE TABLE GPS(
	gid integer,
	Laengengrad real check(length(Laengengrad) > 0),
	Breitengrad real check(length(Breitengrad) > 0),

	PRIMARY KEY (gid)
);

CREATE TABLE Benutzer_bewertet_Autor(
	EMailB text, 
	EMailA text,
	Benotung integer,
	Text text,
	
	constraint check_emails check(EMailA != EMailB),
	constraint check_benotung check(0 < Benotung AND Benotung < 6),
	constraint check_text check(length(Text) > 0),
	
	PRIMARY KEY (EMailB, EMailA),

	FOREIGN KEY (EMailB) REFERENCES Benutzer(EMail) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (EMailA) REFERENCES Autor(EMail) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Benutzer_abonniert_Autor(
	EMailB text,
	EMailA text,
	
	constraint check_email check(EMailA != EMailB),
	
	PRIMARY KEY (EMailB, EMailA),

	FOREIGN KEY (EMailB) REFERENCES Benutzer(EMail) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (EMailA) REFERENCES Autor(EMail) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Autor_empfiehlt_Autor(
	EMailB text,
	EMailA text,
	
	constraint check_email check(EMailA != EMailB),
	
	PRIMARY KEY (EMailB, EMailA),

	FOREIGN KEY (EMailB) REFERENCES Autor(EMail) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (EMailA) REFERENCES Autor(EMail) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Bild_setzt_Tag(
	bid integer,
	Tag text,
	
	PRIMARY KEY (bid, Tag),
	
	FOREIGN KEY (bid) REFERENCES Bild(bid) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (Tag) REFERENCES Tag(Tag) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Bild_hat_GPS(
	bid integer,
	gid integer,

	PRIMARY KEY (bid, gid),

	FOREIGN KEY (bid) REFERENCES Bild(bid) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (gid) REFERENCES GPS(gid) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Benutzer Values("bauec@hhu.de", "123456Aa", "Baris", "Uectas");
INSERT INTO Benutzer Values("lameh@hhu.de", "asd123A", "Lars", "Mehnert");
INSERT INTO Benutzer Values("kigoe@hhu.de", "lsadklA2", "Goekhan", "Kiziltan");
INSERT INTO Benutzer Values("cantu@hhu.de", "as51A51", "Yunus", "Cantuerk");
INSERT INTO Benutzer Values("kleini@hhu.de", "asdnkJ8", "Nils", "Klein");
INSERT INTO Benutzer Values("alga@hhu.de", "sdahaA1s", "Gabriel", "Alves");

INSERT INTO Autor Values("bauec@hhu.de", 1.00, "Rayleigh8", null);
INSERT INTO Autor Values("kleini@hhu.de", 12.00, "Armas", null);
INSERT INTO Autor Values("lameh@hhu.de", 14.550, "DiggeHummel", null);

INSERT INTO Transaktion Values(0, "bauec@hhu.de", "cantu@hhu.de", "Zweck", 20.0, '2018-11-06 18:18:18', "paypal", 0.0);
INSERT INTO Transaktion Values(1, "bauec@hhu.de", "lameh@hhu.de", "Zweck", 15.0, '2018-11-05 18:18:18', "PayPal", 15.0);
INSERT INTO Transaktion Values(2, "bauec@hhu.de", "kigoe@hhu.de", "Zweck", 12.0, '2018-11-05 18:18:18', "PayPal", 13.0);
INSERT INTO Transaktion Values(3, "kleini@hhu.de", "lameh@hhu.de", "Zweck", 22.0, '2018-11-02 18:18:18', "PayPal", 0.0);
INSERT INTO Transaktion Values(4, "bauec@hhu.de", "alga@hhu.de", "Zweck", 1.0, '2018-11-05 18:18:18', "PayPal", 20.0);
INSERT INTO Transaktion Values(5, "kleini@hhu.de", "lameh@hhu.de", "Zweck", 5.0, '2018-11-02 18:18:18', "PayPal", 0.0);
INSERT INTO Transaktion Values(6, "lameh@hhu.de", "kleini@hhu.de", "Zweck", 7.0, '2018-11-05 18:18:18', "PayPal", 0.0);
INSERT INTO Transaktion Values(7, "kleini@hhu.de", "lameh@hhu.de", "Zweck", 20, '2018-11-02 18:18:18', "PayPal", 0.0);

INSERT INTO Seite Values(0, "bauec@hhu.de", "2018-11-06", 1, "private");
INSERT INTO Seite Values(1, "bauec@hhu.de", "2018-11-07", 1, "privAte");
INSERT INTO Seite Values(2, "bauec@hhu.de", "2018-11-08", 1, "private");
INSERT INTO Seite Values(3, "bauec@hhu.de", "2018-11-09", 1, "puBlic");
INSERT INTO Seite Values(4, "bauec@hhu.de", "2018-11-10", 1, "public");
INSERT INTO Seite Values(5, "bauec@hhu.de", "2018-11-11", 1, "public");
INSERT INTO Seite Values(6, "bauec@hhu.de", "2018-11-12", 1, "PUBLIC");
INSERT INTO Seite Values(7, "kleini@hhu.de", "2018-11-10", 1, "private");
INSERT INTO Seite Values(8, "kleini@hhu.de", "2018-11-11", 1, "public");
INSERT INTO Seite Values(9, "lameh@hhu.de", "2018-11-12", 1, "public");
INSERT INTO Seite Values(10, "lameh@hhu.de", "2018-11-13", 1, "public");
INSERT INTO Seite Values(11, "lameh@hhu.de", "2018-11-14", 1, "public");
INSERT INTO Seite Values(12, "lameh@hhu.de", "2018-11-15", 1, "public");

INSERT INTO Eintrag Values(0, 0, "Datenbanken", '18:13', "Hallo heute mache ich mein Projekt");
INSERT INTO Eintrag Values(1, 0, "Datenbanken 2", '19:16', "Hier gehts weiter");
INSERT INTO Eintrag Values(2, 1, "Rechnernetze", '18:13', "Ich schreibe einen WebServer in Java");
INSERT INTO Eintrag Values(3, 1, "Rechnernetze 2", '18:14', "Ich komme nicht weiter ");
INSERT INTO Eintrag Values(4, 2, "Rechnernetze 3", '18:13', "Der Socket schließt sich und ich kann nichts dagegen tun!");
INSERT INTO Eintrag Values(5, 3, "Training", '18:13', "Heute war ich trainiern!");
INSERT INTO Eintrag Values(6, 4, "Datenbanken", '18:13', "Das ist schön!");
INSERT INTO Eintrag Values(7, 7, "Ausbildung", '00:00', "Ich kann nicht schlafen");
INSERT INTO Eintrag Values(8, 3, "Geheimnis", '20:24', "Ich mag RennrŠder!");
INSERT INTO Eintrag Values(9, 4, "Bachelor-Arbeit", '18:13', "Ich will meine Bachelorarbeit erledigen...");
INSERT INTO Eintrag Values(10, 4, "Silvester in London", '18:13', "London ist schšn");
INSERT INTO Eintrag Values(11, 4, "2018 Silvester in London 2019", '18:13', "Astonishing");
INSERT INTO Eintrag Values(12, 5, "Test", '18:13', "Player");
INSERT INTO Eintrag Values(13, 6, "Silvester", '18:13', "Keine");
INSERT INTO Eintrag Values(14, 7, "2018", '18:13', "kaese");
INSERT INTO Eintrag Values(15, 8, "Bachelor", '18:13', "auto_vacuum");
INSERT INTO Eintrag Values(16, 9, "Silvester", '18:13', "schuhe");
INSERT INTO Eintrag Values(17, 10, "London 2019", '18:13', "riegel");
INSERT INTO Eintrag Values(18, 11, "Arbeit", '18:13', "...");
INSERT INTO Eintrag Values(19, 12, "in", '18:13', "schšn");
INSERT INTO Eintrag Values(20, 12, "in London 2019", '18:13', "Test");

INSERT INTO Bild Values(1, 2, null);
INSERT INTO Bild Values(2, 3, null);
INSERT INTO Bild Values(3, 4, null);
INSERT INTO Bild Values(4, 1, null);
INSERT INTO Bild Values(5, 2, null);

INSERT INTO Tag Values("HHU");
INSERT INTO Tag Values("Rechnernetze");
INSERT INTO Tag Values("Datenbanken");
INSERT INTO Tag Values("Projekt");
INSERT INTO Tag Values("SQL");

INSERT INTO GPS Values(0, -13.00000, 64.00000002);
INSERT INTO GPS Values(1, 45.00000, 21.05423);
INSERT INTO GPS Values(2, -2.000089, 0.0);
INSERT INTO GPS Values(3, -13.0000550, 64.00000002);
INSERT INTO GPS Values(4, -13.00000888, 64.0440000002);
INSERT INTO GPS Values(5, 0.0, 64.0440000002);

INSERT INTO Benutzer_bewertet_Autor Values("alga@hhu.de", "bauec@hhu.de", 5, "Du bist super!");
INSERT INTO Benutzer_bewertet_Autor Values("kleini@hhu.de", "bauec@hhu.de", 5, "Du bist Wahnsinn!");
INSERT INTO Benutzer_bewertet_Autor Values("kigoe@hhu.de", "bauec@hhu.de", 5, "Fantastischer Autor!");
INSERT INTO Benutzer_bewertet_Autor Values("lameh@hhu.de", "bauec@hhu.de", 5, "Wer ist JK ROLLING");
INSERT INTO Benutzer_bewertet_Autor Values("alga@hhu.de", "lameh@hhu.de", 2, "Net so intressant.");
INSERT INTO Benutzer_bewertet_Autor Values("bauec@hhu.de", "lameh@hhu.de", 1, "Laaaangweilig.");
INSERT INTO Benutzer_bewertet_Autor Values("kleini@hhu.de", "lameh@hhu.de", 5, "Einfach Wow");
INSERT INTO Benutzer_bewertet_Autor Values("lameh@hhu.de", "kleini@hhu.de", 3, "Intressant.");

INSERT INTO Benutzer_abonniert_Autor Values("bauec@hhu.de", "lameh@hhu.de");
INSERT INTO Benutzer_abonniert_Autor Values("kleini@hhu.de", "lameh@hhu.de");
INSERT INTO Benutzer_abonniert_Autor Values("alga@hhu.de", "lameh@hhu.de");
INSERT INTO Benutzer_abonniert_Autor Values("lameh@hhu.de", "bauec@hhu.de");
INSERT INTO Benutzer_abonniert_Autor Values("kleini@hhu.de", "bauec@hhu.de");
INSERT INTO Benutzer_abonniert_Autor Values("kigoe@hhu.de", "bauec@hhu.de");
INSERT INTO Benutzer_abonniert_Autor Values("cantu@hhu.de", "bauec@hhu.de");
INSERT INTO Benutzer_abonniert_Autor Values("bauec@hhu.de", "kleini@hhu.de");

INSERT INTO Autor_empfiehlt_Autor Values("bauec@hhu.de", "lameh@hhu.de");
INSERT INTO Autor_empfiehlt_Autor Values("kleini@hhu.de", "bauec@hhu.de");
INSERT INTO Autor_empfiehlt_Autor Values("bauec@hhu.de", "kleini@hhu.de");
INSERT INTO Bild_setzt_Tag Values("1", "HHU");
INSERT INTO Bild_setzt_Tag Values("1", "SQL");
INSERT INTO Bild_setzt_Tag Values("1", "Projekt");
INSERT INTO Bild_setzt_Tag Values("2", "HHU");
INSERT INTO Bild_setzt_Tag Values("2", "Datenbanken");
INSERT INTO Bild_setzt_Tag Values("2", "Rechnernetze");
INSERT INTO Bild_setzt_Tag Values("3", "HHU");
INSERT INTO Bild_setzt_Tag Values("4", "Datenbanken");
INSERT INTO Bild_setzt_Tag Values("5", "Rechnernetze");

INSERT INTO Bild_hat_GPS Values(1, 1);
INSERT INTO Bild_hat_GPS Values(2, 2);
INSERT INTO Bild_hat_GPS Values(3, 5);
INSERT INTO Bild_hat_GPS Values(4, 5);
INSERT INTO Bild_hat_GPS Values(5, 5);

UPDATE Autor Set Preis = 20.00 WHERE EMail = "bauec@hhu.de";
UPDATE Autor Set Preis = 12.00 WHERE EMail = "lameh@hhu.de";
UPDATE Autor Set Preis = 15.00 WHERE EMail = "kleini@hhu.de";










Feature: Käyttäjä voi merkitä vinkin luetuksi

Scenario: käyttäjä voi valikosta valita luetuksi merkistemisen
        Given lisätään lukuvinkki otsikolla "SQLite Tutorial"
        When käyttäjä valitsee luettu, antaa otsikon "SQLite Tutorial", valitsee luettu ja keskeyttää
        And poistutaan
        Then ohjelma vastaa "Merkitäänkö lukuvinkki luetuksi tänään vai aiempana päivänä? Valitse t/a. Keskeytä painamalla mitä tahansa muuta näppäintä."

    Scenario: käyttäjä voi merkitä lukuvinkin lukupäiväksi minkä tahansa päivän
        Given lisätään lukuvinkki otsikolla "SQLite Tutorial"
        When käyttäjä valitsee luettu, antaa otsikon "SQLite Tutorial", valitsee luettu ja a
        And antaa päivämäärän "2020-11-30"
        And poistutaan
        Then ohjelma vastaa "Lukuvinkin lukupäiväksi tallennettiin: 2020-11-30"


    Scenario: käyttäjä voi merkitä lukuvinkin lukupäiväksi merkitsemispäivän
        Given lisätään lukuvinkki otsikolla "SQLite Tutorial"
        When käyttäjä valitsee luettu, antaa otsikon "SQLite Tutorial", valitsee luettu ja t
        And poistutaan
        Then ohjelma palauttaa merkitsemispäivän päivämäärän
        
    Scenario: päivämäärä validoidaan
        Given lisätään lukuvinkki otsikolla "SQLite Tutorial"
        When käyttäjä valitsee luettu, antaa otsikon "SQLite Tutorial", valitsee luettu ja a
        And antaa päivämäärän "2020-11-31"
        And poistutaan
        Then ohjelma vastaa "Merkkaus epäonnistui, päivämäärä oli virheellinen."

    Scenario: päivämäärä validoidaan
        Given lisätään lukuvinkki otsikolla "SQLite Tutorial"
        When käyttäjä valitsee luettu, antaa otsikon "SQLite Tutorial", valitsee luettu ja a
        And antaa päivämäärän "30.11.2020"
        And poistutaan
        Then ohjelma vastaa "Merkkaus epäonnistui, päivämäärä oli virheellinen." 

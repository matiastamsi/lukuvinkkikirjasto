Feature: Käyttäjä voi poistaa linkin

    Scenario: Vinkki etsitään otsikolla ja poistetaan.
        Given lisätään lukuvinkki otsikolla "testi"
        When poistetaan lukuvinkki otsikolla "testi"
        And poistutaan
        Then ohjelma vastaa "Lukuvinkki poistettu!"

    Scenario: Vinkki poistetaan, kun ensin on löytynyt monta osumaa
        Given lisätään lukuvinkki otsikolla "testi1"
        And lisätään lukuvinkki otsikolla "testi2"
        When etsitään poistettavaa vinkkiä ensin otsikolla "testi" ja tarkennetaan hakua "testi1"
        And poistutaan
        Then ohjelma vastaa "Tarkenna hakua!" ja "Lukuvinkki poistettu!"
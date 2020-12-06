Feature: Käyttäjä voi etsiä vinkkejä tagien perusteella

    Scenario: Käyttäjä voi etsiä vinkin tagilla
        Given lisätään lukuvinkki otsikolla "testi1" ja tageilla "asd"
        When etsitään tagilla "asd" ja poistutaan
        Then ohjelma vastaa "testi1"

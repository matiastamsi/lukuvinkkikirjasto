Feature: Käyttäjä voi lisätä vinkin otsikolla

    Scenario: Käyttäjä voi valikosta valita vinkin lisäämisen
        Given valikosta valitaan vinkin lisäys ja annetaan jokin otsikko ja poistutaan
        Then ohjelma vastaa "Anna lukuvinkin otsikko: "

    Scenario: Käyttäjä ei voi lisätä tyhjää vinkkiä
        Given valikosta valitaan vinkin lisäys ja yritetään tyhjällä otsikolla ja poistutaan
        Then ohjelma vastaa "Otsikossa täytyy olla vähintään yksi kirjain."


Feature: Käyttäjä voi lisätä vinkin otsikolla

    Scenario: Käyttäjä voi valikosta valita vinkin lisäämisen
        Given valikosta valitaan linkin lisäys
        When käyttäjä lisää vinkin otsikolla "ttk91-opas"
        Then ohjelma vastaa "Anna lukuvinkin otsikko: "


    Scenario: Käyttäjä ei voi lisätä tyhjää vinkkiä
        Given valikosta valitaan linkin lisäys
        When käyttäjä lisää tyhjän "" otsikon
        Then ohjelma vastaa "Otsikossa täytyy olla vähintään yksi kirjain."

    Scenario: Käyttäjän annettua otsikon linkki lisätään
        Given valikosta valitaan linkin lisäys
        When käyttäjä lisää vinkin otsikolla "Tirakirja"
        Then ohjelma vastaa "Lukuvinkin lisääminen onnistui!"
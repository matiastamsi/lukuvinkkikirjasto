Feature: Käyttäjä voi muokata lukuvinkin tageja

    Scenario: Lukuvinkkiin voi lisätä tagin jälkikäteen
        Given lisätään lukuvinkki otsikolla "vinkki" ja tageilla "auto::mersu"
        When etsitään tagilla "mersu" ja valitaan vaihtoehto 0
        And lisätään uusi tagi "volvo" ja poistutaan
        And käyttäjä valitsee valikosta listauksen ja poistuu
        Then ohjelma näyttää lukuvinkin "vinkki::Ei lisättyä linkkiä::Ei luettu::Tagit: auto mersu volvo "

    Scenario: Lukuvinkiltä voi poistaa tagin jälkikäteen
        Given lisätään lukuvinkki otsikolla "vinkki" ja tageilla "auto::mersu"
        When etsitään tagilla "mersu" ja valitaan vaihtoehto 0
        And poistetaan tagi "mersu" ja poistutaan
        And käyttäjä valitsee valikosta listauksen ja poistuu
        Then ohjelma näyttää lukuvinkin "vinkki::Ei lisättyä linkkiä::Ei luettu::Tagit: auto "
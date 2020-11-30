Feature: Vinkit tallentuvat pysyvästi

    Scenario: Kun käyttäjä käynnistää sovelluksen uudelleen lukuvinkit ovat tallessa
        Given valikosta valitaan vinkin lisäys ja annetaan jokin otsikko ja poistutaan
        When ohjelma sammutetaan
        And ohjelma käynnistetään
        And käyttäjä valitsee valikosta listauksen ja poistuu
        Then ohjelma näyttää lukuvinkin "testiOtsikko::Ei lisättyä linkkiä::Ei tageja"

    

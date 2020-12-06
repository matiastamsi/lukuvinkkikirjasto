Feature: Käyttäjä voi nähdä lisättyjen vinkkien otsikot

    Scenario: käyttäjä voi valikosta valita vinkkien listauksen
        Given lisätään lukuvinkki otsikolla "testiOtsikko"
        When käyttäjä valitsee valikosta listauksen ja poistuu
        Then ohjelma näyttää lukuvinkin "testiOtsikko::Ei lisättyä linkkiä::Ei luettu::Ei tageja"

    Scenario: valikossa annetusta väärästä symbolista ilmoitetaan käyttäjälle
        Given käyttäjä antaa virheellisen merkin ja poistuu
        Then ohjelma vastaa "Virheellinen näppäinvalinta. Yritä uudestaan."


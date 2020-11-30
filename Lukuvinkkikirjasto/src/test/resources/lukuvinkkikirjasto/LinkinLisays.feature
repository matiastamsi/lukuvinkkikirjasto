Feature: Käyttäjä voi lisätä vinkille linkin

    Scenario: Käyttäjä voi lisätä validin linkin
        Given lisätään lukuvinkki otsikolla "testi" ja linkillä "http://google.com"
        When käyttäjä valitsee valikosta listauksen ja poistuu
        Then ohjelma näyttää lukuvinkin "testi::http://google.com::Ei tageja"

    Scenario: Käyttäjä ei voi lisätä virheellistä linkkiä
        Given yritetään lisätä lukuvinkki otsikolla "testi" ja virheellisellä linkillä "huonolink.com"
        When poistutaan
        Then ohjelma vastaa "Linkki ei ollut validi!"
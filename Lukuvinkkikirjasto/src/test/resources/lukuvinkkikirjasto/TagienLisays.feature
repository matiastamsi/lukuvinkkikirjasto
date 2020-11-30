Feature: Käyttäjä voi lisätä vinkkiin vapaavalintaisen määrän tageja

    Scenario: Pilkulla eroteltuna voi määritellä listan tageja
        Given lisätään lukuvinkki otsikolla "testi" ja tageilla "tagiA,tagiB,tagiC"
        When käyttäjä valitsee valikosta listauksen ja poistuu
        Then ohjelma näyttää lukuvinkin "testi::Ei lisättyä linkkiä::[tagiA,tagiB,tagiC] "

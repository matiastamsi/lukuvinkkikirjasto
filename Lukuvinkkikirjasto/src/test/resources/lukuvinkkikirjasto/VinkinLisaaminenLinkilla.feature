Feature: Käyttäjä voi lisätä lukuvinkin syöttämällä pelkän linkin

    Scenario: Käyttäjä voi lisätä lukuvinkin URLilla
        Given valikosta valitaan vinkin lisäys linkillä
        When annetaan URL "https://github.com/"
        And poistutaan
        Then ohjelma vastaa "Lukuvinkin lisääminen onnistui!"

    Scenario: Annetun linkin pitää olla validi
        Given valikosta valitaan vinkin lisäys linkillä
        When annetaan virheellinen URL "google.fi"
        And poistutaan
        Then ohjelma vastaa "URL ei ollut validi, yritä uudelleen."

    Scenario: Käyttäjä ei voi lisätä linkkiä jolla ei ole otsikkoa
        Given valikosta valitaan vinkin lisäys linkillä
        When annetaan virheellinen URL "https://still-beach-47583.herokuapp.com/index"
        And poistutaan
        Then ohjelma vastaa "URLin otsikko on tyhjä, lisää otsikko ja linkki manuaalisesti: "
Feature: Käyttäjä voi nähdä lisättyjen vinkkien otsikot
    
    Scenario: Käyttäjä voi valikosta valita vinkkien listauksen
        Given valikosta valitaan linkin lisäys
        Then käyttäjä lisää vinkin otsikolla "java-opas"
        And valikosta valitaan vinkkien listaus
        Then ohjelma vastaa "java-opas" 

    Scenario: Käyttäjän annettua valikkoon väärän symbolin, tulee ilmoitus
        Given valikosta valitaan linkin lisäys
        Then käyttäjä lisää vinkin otsikolla "python"
        And käyttäjä syöttää valikkoon väärän symbolin
        Then ohjelma vastaa "Virheellinen näppäinvalinta. Yritä uudestaan."
package lukuvinkkikirjasto;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.io.File;
import java.sql.SQLException;

import static org.junit.Assert.*;
import java.util.ArrayList;
import lukuvinkkikirjasto.dao.LukuvinkkiDAO;
import lukuvinkkikirjasto.databaseconnection.ConnectionToDatabase;
import lukuvinkkikirjasto.userinterface.*;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StepDefinitions {

    UserInterface ui;
    ArrayList<String> inputs;
    StubIO io;
    ConnectionToDatabase connection;
    LukuvinkkiDAO dao;
    
    @Before
    public void setUp() {
        connection = new ConnectionToDatabase("jdbc:sqlite:cucumberTest.db");
        dao = new LukuvinkkiDAO(connection);
        dao.createDatabase();
        inputs = new ArrayList<>();
    }
    
    @Given("valikosta valitaan vinkin lisäys ja annetaan jokin otsikko ja poistutaan")
    public void valitaanLinkinLisaysAnnetaanOtsikkoJaPoistutaan() {
        String[] i = {"u", "testiOtsikko", "e", "e", "p"};
        Collections.addAll(inputs, i);
    }
    
    @Given("valikosta valitaan vinkin lisäys ja yritetään tyhjällä otsikolla ja poistutaan")
    public void valitaanLinkinLisaysAnnetaanTyhjaJaPoistutaan() {
        String[] i = {"u", "", "eiTyhja", "e", "e", "p"};
        Collections.addAll(inputs, i);
    }
    
    @Given("lisätään lukuvinkki otsikolla {string}")
    public void lisataanLukuvinkkiOtsikolla(String otsikko) {
        String[] i = {"u", otsikko, "e", "e"};
        Collections.addAll(inputs, i);
    }
    
    @Given("lisätään lukuvinkki otsikolla {string} ja linkillä {string}")
    public void lisataanLukuvinkkiOtsikollaJaLinkilla(String otsikko, String linkki) {
        String[] i = {"u", otsikko, "e", "k", linkki};
        Collections.addAll(inputs, i);
    }
    
    @Given("avataan vinkin muokkaus vinkille {string}")
    public void muokataanVinkkia(String vinkki) {
        String[] i = {"e", vinkki, "", "p"};
        Collections.addAll(inputs, i);
    }

    @Given("yritetään lisätä lukuvinkki otsikolla {string} ja virheellisellä linkillä {string}")
    public void lisataanLukuvinkkiOtsikollaJaVirheellisellaLinkilla(String otsikko, String huonoLinkki) {
        String[] i = {"u", otsikko, "e", "k", huonoLinkki, "http://google.com"};
        Collections.addAll(inputs, i);
    }
    
    @Given("lisätään lukuvinkki otsikolla {string} ja tageilla {string}")
    public void lisataanLukuvinkkiOtsikollaJaTageilla(String otsikko, String tagit) {
        String[] i = {"u", otsikko, "k", tagit, "", "e"};
        Collections.addAll(inputs, i);
    }
    
    @Given("käyttäjä antaa virheellisen merkin ja poistuu")
    public void annetaanVirheellinenMerkki() {
        String[] i = {"xd", "p"};
        Collections.addAll(inputs, i);
    }

    @Given("valitaan et ja etsitään tagilla {string} ja poistutaan")
    public void etsitaanTagilla(String tagi) {
        String[] i = {"et", tagi, "", "p"};
        Collections.addAll(inputs, i);
    }
    
    @When("poistutaan")
    public void poistutaan() {
        inputs.add("p");
    }
    
    @When("ohjelma sammutetaan")
    public void sammutetaanOhjelma() throws SQLException {
        io = new StubIO(inputs);
        ui = new UserInterface(io, dao);
        ui.run();
        dao.close();
    }
    
    @When("ohjelma käynnistetään")
    public void kaynnistaOhjelma() {
        connection = new ConnectionToDatabase("jdbc:sqlite:cucumberTest.db");
        dao = new LukuvinkkiDAO(connection);
        inputs = new ArrayList<>();
    }
    
    @When("käyttäjä valitsee valikosta listauksen ja poistuu") 
    public void listataanJaPoistutaan() {
        String[] i = {"l", "p"};
        Collections.addAll(inputs, i);
    }
    
    @When("etsitään poistettavaa vinkkiä ensin otsikolla {string} ja tarkennetaan hakua {string}")
    public void tarkennetaanHakua(String otsikko, String tarkennettuOtsikko) {
        String[] i = {"x", otsikko, tarkennettuOtsikko, ""};
        Collections.addAll(inputs, i);
    }
    
    @When("poistetaan lukuvinkki otsikolla {string}")
    public void poistetaanLukuvinkkiOtsikolla(String otsikko) {
        String[] i = {"x", otsikko, ""};
        Collections.addAll(inputs, i);
    }
    
    @Then("ohjelma vastaa {string}")
    public void ohjelmaVastaa(String vastaus) throws SQLException {
        io = new StubIO(inputs);
        ui = new UserInterface(io, dao);
        ui.run();
        assertTrue(io.getOutputs().contains(vastaus));
    }
    
    @Then("ohjelma vastaa {string} ja {string}")
    public void ohjelmaVastaaKahdesti(String vastaus1, String vastaus2) throws SQLException {
        io = new StubIO(inputs);
        ui = new UserInterface(io, dao);
        ui.run();
        assertTrue(io.getOutputs().contains(vastaus1));
        assertTrue(io.getOutputs().contains(vastaus2));
    }
    
    @Then("ohjelma näyttää lukuvinkin {string}")
    public void ohjelmaNayttaaVinkin(String vastaus) throws SQLException {
        String[] parts = vastaus.split("::");
        String lukuvinkki = parts[0] + "\n" + parts[1] + "\n" + parts[2];
        System.out.println(lukuvinkki);
        io = new StubIO(inputs);
        ui = new UserInterface(io, dao);
        ui.run();
        assertTrue(io.getOutputs().contains(lukuvinkki));
    }
    
    @After()
    public void tearDown() {
        dao.close();
        File db = new File("cucumberTest.db");
        db.delete();
    }
    
}

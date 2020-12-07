package readingtiplibrary;

import readingtiplibrary.userinterface.UserInterface;
import readingtiplibrary.userinterface.StubIO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.Assert.*;
import java.util.ArrayList;
import readingtiplibrary.dao.ReadingTipDAO;
import readingtiplibrary.databaseconnection.ConnectionToDatabase;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import java.util.Collections;


public class StepDefinitions {

    UserInterface ui;
    ArrayList<String> inputs;
    StubIO io;
    ConnectionToDatabase connection;
    ReadingTipDAO dao;
    
    @Before
    public void setUp() {
        connection = new ConnectionToDatabase("jdbc:sqlite:cucumberTest.db");
        dao = new ReadingTipDAO(connection);
        dao.createDatabase();
        inputs = new ArrayList<>();
    }
    
    @Given("valikosta valitaan vinkin lisäys ja annetaan jokin otsikko ja poistutaan")
    public void valitaanLinkinLisaysAnnetaanOtsikkoJaPoistutaan() {
        String[] i = {"u", "o", "testiOtsikko", "e", "e", "p"};
        Collections.addAll(inputs, i);
    }
    
    @Given("valikosta valitaan vinkin lisäys ja yritetään tyhjällä otsikolla ja poistutaan")
    public void valitaanLinkinLisaysAnnetaanTyhjaJaPoistutaan() {
        String[] i = {"u", "o", "", "eiTyhja", "e", "e", "p"};
        Collections.addAll(inputs, i);
    }
    
    @Given("lisätään lukuvinkki otsikolla {string}")
    public void lisataanLukuvinkkiOtsikolla(String otsikko) {
        String[] i = {"u", "o", otsikko, "e", "e"};
        Collections.addAll(inputs, i);
    }
    
    @Given("lisätään lukuvinkki otsikolla {string} ja linkillä {string}")
    public void lisataanLukuvinkkiOtsikollaJaLinkilla(String otsikko, String linkki) {
        String[] i = {"u", "o", otsikko, "e", "k", linkki};
        Collections.addAll(inputs, i);
    }
    
    @Given("avataan vinkin muokkaus vinkille {string}")
    public void muokataanVinkkia(String vinkki) {
        String[] i = {"e", "o", vinkki};
        Collections.addAll(inputs, i);
    }

    @Given("yritetään lisätä lukuvinkki otsikolla {string} ja virheellisellä linkillä {string}")
    public void lisataanLukuvinkkiOtsikollaJaVirheellisellaLinkilla(String otsikko, String huonoLinkki) {
        String[] i = {"u", "o", otsikko, "e", "k", huonoLinkki, "http://google.com"};
        Collections.addAll(inputs, i);
    }
    
    @Given("lisätään lukuvinkki otsikolla {string} ja tageilla {string}")
    public void lisataanLukuvinkkiOtsikollaJaTageilla(String otsikko, String tagit) {
        String[] i1 = {"u", "o", otsikko, "k"};
        String[] tags = tagit.split("::");
        String[] i2 = {"", "e"};
        Collections.addAll(inputs, i1);
        Collections.addAll(inputs, tags);
        Collections.addAll(inputs, i2);
    }
    
    @Given("käyttäjä antaa virheellisen merkin ja poistuu")
    public void annetaanVirheellinenMerkki() {
        String[] i = {"xd", "p"};
        Collections.addAll(inputs, i);
    }

    @Given("etsitään tagilla {string} ja poistutaan")
    public void etsitaanTagilla(String tagi) {
        String[] i = {"e", "t", tagi, "", "p"};
        Collections.addAll(inputs, i);
    }
    
    @Given("valikosta valitaan vinkin lisäys linkillä") 
    public void lisataanLinkilla() {
        String[] i = {"u", "u"};
        Collections.addAll(inputs, i);
    }

    @When("poistutaan")
    public void poistutaan() {
        String[] i = {"p", "", "p"};
        Collections.addAll(inputs, i);
    }
    
    @When("annetaan URL {string}")
    public void annetaanUrl(String url) {
        inputs.add(url);
    }

    @When("annetaan virheellinen URL {string}")
    public void annetaanVirheellinenURL(String url) {
        String[] i = {url, "https://google.fi"};
        Collections.addAll(inputs, i);
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
        dao = new ReadingTipDAO(connection);
        inputs = new ArrayList<>();
    }
    
    @When("käyttäjä valitsee valikosta listauksen ja poistuu") 
    public void listataanJaPoistutaan() {
        String[] i = {"l", "p"};
        Collections.addAll(inputs, i);
    }
    
    @When("etsitään poistettavaa vinkkiä ensin otsikolla {string} ja tarkennetaan hakua {string}")
    public void tarkennetaanHakua(String otsikko, String tarkennettuOtsikko) {
        String[] i = {"e", "o", otsikko, tarkennettuOtsikko};
        Collections.addAll(inputs, i);
    }
    
    @When("poistetaan lukuvinkki otsikolla {string}")
    public void poistetaanLukuvinkkiOtsikolla(String otsikko) {
        String[] i = {"e", "o", otsikko, "poista", ""};
        Collections.addAll(inputs, i);
    }
    
    @When("poistetaan lukuvinkki")
    public void poistetaanJoHaettuLukuvinkki() {
        String[] i = {"poista", ""};
        Collections.addAll(inputs, i);
    }

    @When("ohjelma hakee merkitsemispäivän päivämäärän") 
    public void ohjelmaHakeeMerkitsemispaivanPaivamaaran() {
        String[] i = {LocalDate.now().toString()};
        Collections.addAll(inputs, i);
    }

    @When("käyttäjä valitsee luettu, antaa otsikon {string}, valitsee luettu ja keskeyttää")
    public void halutaanMerkitäLuetuksi(String otsikko) {
        String[] i = {"e", "o", otsikko, "luettu", "e"};
        Collections.addAll(inputs, i);
    }

    @When("käyttäjä valitsee luettu, antaa otsikon {string}, valitsee luettu ja a")
    public void annetaanMikaTahansaPaivamaara(String otsikko) {
        String[] i = {"e", "o", otsikko, "luettu", "a"};
        Collections.addAll(inputs, i);
    }

    @When("käyttäjä valitsee luettu, antaa otsikon {string}, valitsee luettu ja t")
    public void valitaanLukupaivaksiTamaPaiva(String otsikko) {
        String[] i = {"e", "o", otsikko, "luettu", "t"};
        Collections.addAll(inputs, i);
    }

    @When("antaa päivämäärän {string}")
    public void antaaPaivamaaran(String paivamaara) {
        String[] i = {paivamaara};
        Collections.addAll(inputs, i);
    }

    @Then("ohjelma palauttaa merkitsemispäivän päivämäärän")
    public void ohjelmaPalauttaaMerkitsemispaivanPaivamaaran() throws SQLException {
        String vastaus = "Lukuvinkin lukupäiväksi tallennettiin: " + LocalDate.now().toString();
        io = new StubIO(inputs);
        ui = new UserInterface(io, dao);
        ui.run();
        //System.out.println("outputs: " + io.getOutputs());
        assertTrue(io.getOutputs().contains(vastaus));
    }
    
    @Then("ohjelma vastaa {string}")
    public void ohjelmaVastaa(String vastaus) throws SQLException {
        io = new StubIO(inputs);
        ui = new UserInterface(io, dao);
        ui.run();
        //System.out.println("outputs: " + io.getOutputs());
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
        String lukuvinkki = parts[0] + "\n" + parts[1] + "\n" + parts[2] + "\n" + parts[3] + "\n";
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

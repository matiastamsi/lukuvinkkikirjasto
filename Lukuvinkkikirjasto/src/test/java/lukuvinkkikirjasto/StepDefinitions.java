package lukuvinkkikirjasto;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.junit.Assert.*;
import java.util.ArrayList;
import lukuvinkkikirjasto.dao.LukuvinkkiDAO;
import lukuvinkkikirjasto.databaseconnection.ConnectionToDatabase;
import lukuvinkkikirjasto.userinterface.*;
import org.junit.Before;

public class StepDefinitions {

    UserInterface ui;
    ArrayList<String> syotteet = new ArrayList<>();
    StubIO io;
    ConnectionToDatabase connection;
    LukuvinkkiDAO dao;

    @Given("valikosta valitaan linkin lisäys")
    public void vinkinLisäys() {
        syotteet.add("u");
    }

    @When("valikosta valitaan vinkkien listaus")
    public void vinkkienListaus() {
        syotteet.add("l");
    }

    @When("käyttäjä lisää vinkin otsikolla {string}")
    public void linkinLisäysOtsikolla(String otsikko) {
        syotteet.add(otsikko);
        syotteet.add("e");
        syotteet.add("e");
    }

    @When("käyttäjä lisää tyhjän {string} otsikon")
    public void linkinLisäysTyhjäOtsikko(String tyhjä) {
        syotteet.add(tyhjä);
        syotteet.add("ei tyhjä");
        syotteet.add("e");
        syotteet.add("e");
    }

    @When("käyttäjä syöttää valikkoon väärän symbolin")
    public void vääräSymboliValikkoon() {
        syotteet.add("t");
    }

    @Then("ohjelma vastaa {string}")
    public void ohjelmaVastaa(String vastaus) throws SQLException {
        syotteet.add("p");
        connection = new ConnectionToDatabase("jdbc:sqlite:tietokantaTest.db");
        dao = new LukuvinkkiDAO(connection);
        dao.initializeDatabase(Paths.get("tietokantaTest.db"));
        io = new StubIO(syotteet);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> vastaukset = io.getOutputs();
        assertTrue(vastaukset.contains(vastaus));
    }

}

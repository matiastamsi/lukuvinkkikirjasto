package lukuvinkkikirjasto;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.Assert.*;
import java.util.ArrayList;
import lukuvinkkikirjasto.userinterface.*;

public class StepDefinitions {
    UserInterface ui;
    String[] syotteet;
    StubIO io;
    int i;

    @Before 
    public void setup() {
        syotteet = new String[4];
        i = 0;
    }

    @Given("valikosta valitaan linkin lisäys")
        public void ohjelmaKäynnistetään() {
            syotteet[i] = "u";
            i++;
    }

    @When("valikosta valitaan vinkkien listaus")
        public void vinkkienListaus() {
            syotteet[i] = "l";
            i++;
        }
 
    @When("käyttäjä lisää vinkin otsikolla {string}")
        public void linkinLisäysOtsikolla(String otsikko) {
            syotteet[i] = otsikko;
            i++;
        }
    
    @When("käyttäjä lisää tyhjän {string} otsikon")
        public void linkinLisäysTyhjäOtsikko(String tyhjä) {
            syotteet[i] = tyhjä;
            i++;
        }

    @When("käyttäjä syöttää valikkoon väärän symbolin")
        public void vääräSymboliValikkoon() {
            syotteet[i] = "t";
            i++;
        }


    @Then("ohjelma vastaa {string}") 
        public void ohjelmaVastaa(String vastaus) {            
            syotteet[i] = "p";
            io = new StubIO(syotteet);
            ui = new UserInterface(io);
            ui.run();
            ArrayList<String> vastaukset = io.getOutputs();
            assertTrue(vastaukset.contains(vastaus));
        }
    
}
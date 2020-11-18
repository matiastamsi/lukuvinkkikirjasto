@CucumberOptions(
    plugin = "pretty", 
    features = "src/test/resources/lukuvinkkikirjasto", 
    snippets = SnippetType.CAMELCASE 
)

public class RunCucumberTest {}
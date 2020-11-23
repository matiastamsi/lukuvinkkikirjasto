
package lukuvinkkikirjasto.userinterface;

import java.util.ArrayList;

/**
 * Mock IO used by unit tests.
 * @author henri
 */
public class StubIO implements InputOutput {
    
    private int pointer;
    private String[] inputs;
    private ArrayList<String> outputs;
    
    public StubIO(final String[] inputs) {
        this.inputs = inputs;
        this.pointer = 0;
        this.outputs = new ArrayList<>();
    }
    
    @Override
    public String nextLine() {
        if (pointer < inputs.length) {
            String line = inputs[pointer];
            pointer++;
            return line;
        }
        return "";
    }
    
    @Override

    public void print(final String input) {
        outputs.add(input);
    }
    
    public ArrayList<String> getOutputs() {
        return outputs;
    }
    
    public String getLatestPrint() {
        return outputs.get(outputs.size() - 1);
    }
    
}

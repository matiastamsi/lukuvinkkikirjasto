package lukuvinkkikirjasto.userinterface;

import java.util.Scanner;

public class ConsoleIO implements InputOutput {
    private Scanner reader;

    public ConsoleIO() {
        reader = new Scanner(System.in);
    }

    @Override
    public String nextLine() {
        return reader.nextLine();
    }

    @Override
    public void print(String input) {
        System.out.println(input);
    }

}
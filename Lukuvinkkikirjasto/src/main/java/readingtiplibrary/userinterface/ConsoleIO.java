package readingtiplibrary.userinterface;

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
    public void print(final String input) {
        System.out.println(input);
    }

    public Scanner getReader() {
        return this.reader;
    }

    public void setReader(Scanner scanner) {
        this.reader = scanner;
    }

}

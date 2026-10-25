package university;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Scanner;

final class ConsoleInput {
    private final Scanner input;
    private final PrintStream output;
    private boolean ended;

    ConsoleInput(Scanner input, PrintStream output) {
        this.input = input;
        this.output = output;
    }

    String line(String prompt) {
        if (ended) {
            return null;
        }
        output.println(prompt);
        if (!input.hasNextLine()) {
            ended = true;
            return null;
        }
        return input.nextLine();
    }

    String text(String label, int maximumLength) {
        while (true) {
            String value = line(label + ":");
            if (value == null) {
                return null;
            }
            try {
                return Person.requireText(value, label, maximumLength);
            } catch (IllegalArgumentException exception) {
                output.println(exception.getMessage());
            }
        }
    }

    Integer integer(String label, int maximum) {
        while (true) {
            String value = line(label + ":");
            if (value == null) {
                return null;
            }
            value = value.strip();
            try {
                if (value.matches("[0-9]{1,10}")) {
                    int number = Integer.parseInt(value);
                    if (number >= 1 && number <= maximum) {
                        return number;
                    }
                }
            } catch (NumberFormatException ignored) {
                // Ten digits can still exceed Integer.MAX_VALUE.
            }
            output.println(label + " harus bilangan bulat 1 sampai " + maximum + ".");
        }
    }

    BigDecimal money() {
        while (true) {
            String value = line("Gaji (angka, maksimal dua desimal):");
            if (value == null) {
                return null;
            }
            value = value.strip();
            if (value.matches("[0-9]{1,18}(?:\\.[0-9]{1,2})?")) {
                return new BigDecimal(value);
            }
            output.println("Gaji harus angka nonnegatif, maksimal 18 digit bulat dan dua angka desimal dengan titik.");
        }
    }
}

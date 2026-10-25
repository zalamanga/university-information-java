package university;

import java.io.PrintStream;

public abstract class Person {
    private final String firstName;
    private final String lastName;
    private final int age;

    protected Person(String firstName, String lastName, int age) {
        this.firstName = requireText(firstName, "Nama depan", 100);
        this.lastName = requireText(lastName, "Nama belakang", 100);
        if (age < 1 || age > 150) {
            throw new IllegalArgumentException("Usia harus 1 sampai 150.");
        }
        this.age = age;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public int age() {
        return age;
    }

    public final void showDetails(PrintStream output) {
        output.println("Nama depan: " + firstName);
        output.println("Nama belakang: " + lastName);
        output.println("Usia: " + age);
        showSpecificDetails(output);
    }

    public abstract String kind();

    protected abstract void showSpecificDetails(PrintStream output);

    static String requireText(String value, String label, int maximumLength) {
        if (value == null || value.isBlank() || value.strip().length() > maximumLength
                || value.codePoints().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException(label + " wajib diisi, maksimal " + maximumLength + " karakter tanpa karakter kontrol.");
        }
        return value.strip();
    }
}

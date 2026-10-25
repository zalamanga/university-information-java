package university;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public final class MainMenu {
    private final ConsoleInput input;
    private final PrintStream output;
    private final List<Person> records = new ArrayList<>();

    public MainMenu(Scanner input, PrintStream output) {
        this.output = Objects.requireNonNull(output, "output");
        this.input = new ConsoleInput(Objects.requireNonNull(input, "input"), output);
    }

    public static void main(String[] args) {
        new MainMenu(new Scanner(System.in, StandardCharsets.UTF_8),
                new PrintStream(System.out, true, StandardCharsets.UTF_8)).run();
    }

    public List<Person> records() {
        return List.copyOf(records);
    }

    public void run() {
        output.println("INFORMASI UNIVERSITAS | Latihan pewarisan kelas");
        output.println("Data hanya disimpan selama program berjalan.");
        while (true) {
            output.println("1. Tambah mahasiswa");
            output.println("2. Tambah pegawai");
            output.println("3. Lihat data sementara");
            output.println("0. Keluar");
            String choice = input.line("Pilih menu:");
            if (choice == null) {
                return;
            }
            choice = choice.strip();
            switch (choice) {
                case "1", "2" -> {
                    if (!addPerson(choice.equals("1"))) {
                        output.println("Input berakhir; data yang belum lengkap tidak disimpan.");
                        return;
                    }
                }
                case "3" -> showRecords();
                case "0" -> {
                    output.println("Selesai.");
                    return;
                }
                default -> output.println("Pilihan tidak valid. Masukkan 0, 1, 2, atau 3.");
            }
        }
    }

    private boolean addPerson(boolean student) {
        String firstName = input.text("Nama depan", 100);
        String lastName = input.text("Nama belakang", 100);
        Integer age = input.integer("Usia", 150);
        if (firstName == null || lastName == null || age == null) {
            return false;
        }
        if (student) {
            String university = input.text("Universitas", 150);
            String course = input.text("Program studi", 150);
            Integer studentId = input.integer("ID mahasiswa", Integer.MAX_VALUE);
            if (university == null || course == null || studentId == null) {
                return false;
            }
            records.add(new Student(firstName, lastName, age, university, course, studentId));
        } else {
            String department = input.text("Departemen", 150);
            String designation = input.text("Jabatan", 150);
            BigDecimal salary = input.money();
            if (department == null || designation == null || salary == null) {
                return false;
            }
            records.add(new Employee(firstName, lastName, age, department, designation, salary));
        }
        output.println("Data disimpan sementara. Pilih 3 untuk melihatnya.");
        return true;
    }

    private void showRecords() {
        if (records.isEmpty()) {
            output.println("Belum ada data.");
            return;
        }
        for (int index = 0; index < records.size(); index++) {
            Person person = records.get(index);
            output.println("Data " + (index + 1) + " | " + person.kind());
            person.showDetails(output);
        }
    }
}

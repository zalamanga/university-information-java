package university;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class MainMenuTest {
    private record Session(MainMenu app, String output) { }

    private Session run(String input) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        MainMenu app = new MainMenu(new Scanner(input), new PrintStream(bytes, true, StandardCharsets.UTF_8));
        app.run();
        return new Session(app, bytes.toString(StandardCharsets.UTF_8));
    }

    @Test
    void addsStudentAndEmployeeAndListsBothPolymorphically() {
        Session session = run("1\nAyu\nDemo\n20\nUniversitas Contoh\nInformatika\n101\n"
                + "2\nBudi\nDemo\n30\nTeknologi Informasi\nAsisten Laboratorium\n5000000.50\n3\n0\n");
        assertEquals(2, session.app.records().size());
        assertInstanceOf(Student.class, session.app.records().get(0));
        Employee employee = assertInstanceOf(Employee.class, session.app.records().get(1));
        assertEquals(new BigDecimal("5000000.50"), employee.salary());
        assertTrue(session.output.contains("Data 1 | Mahasiswa"));
        assertTrue(session.output.contains("Data 2 | Pegawai"));
        assertTrue(session.output.contains("Jabatan: Asisten Laboratorium"));
        assertThrows(UnsupportedOperationException.class, () -> session.app.records().clear());
    }

    @Test
    void invalidFieldsAreRetriedBeforeSaving() {
        Session session = run("1\n\nAyu\t\nAyu\nDemo\nabc\n-1\n151\n20\nU\nC\n0\n2147483648\n101\n0\n");
        assertEquals(1, session.app.records().size());
        assertTrue(session.output.contains("Nama depan wajib diisi"));
        assertTrue(session.output.contains("Usia harus bilangan bulat"));
        assertTrue(session.output.contains("ID mahasiswa harus bilangan bulat"));
    }

    @Test
    void invalidSalaryRetriesWithoutLosingOtherFields() {
        Session session = run("2\nBudi\nDemo\n30\nD\nJ\n-1\n1e3\n1,50\n0.001\n0.50\n0\n");
        Employee employee = assertInstanceOf(Employee.class, session.app.records().get(0));
        assertEquals(new BigDecimal("0.50"), employee.salary());
        assertTrue(session.output.contains("Gaji harus angka nonnegatif"));
    }

    @Test
    void eofNeverSavesPartialStudentOrEmployee() {
        for (String prefix : List.of("1\n", "1\nA\n", "1\nA\nB\n20\nU\nC\n",
                "2\nA\nB\n30\nD\nJ\n")) {
            Session session = assertTimeoutPreemptively(Duration.ofSeconds(2), () -> run(prefix));
            assertTrue(session.app.records().isEmpty());
            assertTrue(session.output.contains("data yang belum lengkap tidak disimpan"));
        }
    }

    @Test
    void emptyStateAndInvalidMenuAreUsable() {
        Session session = run("bad\n9\n3\n0\n");
        assertTrue(session.output.contains("Pilihan tidak valid."));
        assertTrue(session.output.contains("Belum ada data."));
        assertTrue(session.output.endsWith("Selesai." + System.lineSeparator()));
        assertDoesNotThrow(() -> run(""));
    }

    @Test
    void dataDoesNotPersistBetweenApplicationInstances() {
        assertEquals(1, run("1\nA\nB\n20\nU\nC\n1\n0\n").app.records().size());
        assertTrue(run("0\n").app.records().isEmpty());
    }
}

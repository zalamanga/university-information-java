package university;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class PersonTest {
    @Test
    void studentInheritsBasicFieldsAndDisplaysAcademicFields() {
        Student student = new Student(" Ayu ", "Demo", 20, "Universitas Contoh", "Informatika", 101);
        Person person = student;
        assertEquals("Ayu", person.firstName());
        assertEquals("Demo", person.lastName());
        assertEquals(20, person.age());
        assertEquals("Mahasiswa", person.kind());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        person.showDetails(new PrintStream(bytes, true, StandardCharsets.UTF_8));
        String text = bytes.toString(StandardCharsets.UTF_8);
        assertTrue(text.contains("Universitas: Universitas Contoh"));
        assertTrue(text.contains("Program studi: Informatika"));
        assertTrue(text.contains("ID mahasiswa: 101"));
    }

    @Test
    void employeeKeepsExactSalaryAndProfessionalFields() {
        Employee employee = new Employee("Budi", "Demo", 30, "Teknologi Informasi", "Asisten", new BigDecimal("1234.50"));
        assertInstanceOf(Person.class, employee);
        assertEquals(new BigDecimal("1234.50"), employee.salary());
        assertEquals("Teknologi Informasi", employee.department());
        assertEquals("Asisten", employee.designation());
        assertEquals("Pegawai", employee.kind());
    }

    @Test
    void invalidFieldsCannotCreateRecords() {
        assertThrows(IllegalArgumentException.class, () -> new Student("", "Demo", 20, "U", "C", 1));
        assertThrows(IllegalArgumentException.class, () -> new Student("A", "Demo", 0, "U", "C", 1));
        assertThrows(IllegalArgumentException.class, () -> new Student("A", "Demo", 151, "U", "C", 1));
        assertThrows(IllegalArgumentException.class, () -> new Student("A", "Demo", 20, " ", "C", 1));
        assertThrows(IllegalArgumentException.class, () -> new Student("A", "Demo", 20, "U", "C", 0));
        assertThrows(IllegalArgumentException.class, () -> new Student("A\u001b", "Demo", 20, "U", "C", 1));
        assertThrows(IllegalArgumentException.class, () -> new Student("A".repeat(101), "Demo", 20, "U", "C", 1));
    }

    @Test
    void salaryRejectsNegativesExcessPrecisionAndExcessMagnitude() {
        for (BigDecimal salary : new BigDecimal[]{null, new BigDecimal("-1"), new BigDecimal("0.001"), new BigDecimal("1000000000000000000")}) {
            assertThrows(IllegalArgumentException.class, () -> new Employee("A", "B", 30, "D", "J", salary));
        }
        assertEquals(new BigDecimal("0.00"), new Employee("A", "B", 30, "D", "J", BigDecimal.ZERO).salary());
    }
}

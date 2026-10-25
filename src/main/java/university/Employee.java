package university;

import java.io.PrintStream;
import java.math.BigDecimal;

public final class Employee extends Person {
    private static final BigDecimal MAXIMUM_SALARY = new BigDecimal("999999999999999999.99");
    private final String department;
    private final String designation;
    private final BigDecimal salary;

    public Employee(String firstName, String lastName, int age,
                    String department, String designation, BigDecimal salary) {
        super(firstName, lastName, age);
        this.department = requireText(department, "Departemen", 150);
        this.designation = requireText(designation, "Jabatan", 150);
        if (salary == null || salary.signum() < 0 || salary.compareTo(MAXIMUM_SALARY) > 0
                || salary.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException("Gaji harus 0 sampai 999999999999999999.99, maksimal dua angka desimal.");
        }
        this.salary = salary.setScale(2);
    }

    public String department() {
        return department;
    }

    public String designation() {
        return designation;
    }

    public BigDecimal salary() {
        return salary;
    }

    @Override
    public String kind() {
        return "Pegawai";
    }

    @Override
    protected void showSpecificDetails(PrintStream output) {
        output.println("Departemen: " + department);
        output.println("Jabatan: " + designation);
        output.println("Gaji: " + salary.toPlainString());
    }
}

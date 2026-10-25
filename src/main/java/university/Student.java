package university;

import java.io.PrintStream;

public final class Student extends Person {
    private final String university;
    private final String course;
    private final int studentId;

    public Student(String firstName, String lastName, int age,
                   String university, String course, int studentId) {
        super(firstName, lastName, age);
        this.university = requireText(university, "Universitas", 150);
        this.course = requireText(course, "Program studi", 150);
        if (studentId < 1) {
            throw new IllegalArgumentException("ID mahasiswa harus bilangan bulat positif.");
        }
        this.studentId = studentId;
    }

    public String university() {
        return university;
    }

    public String course() {
        return course;
    }

    public int studentId() {
        return studentId;
    }

    @Override
    public String kind() {
        return "Mahasiswa";
    }

    @Override
    protected void showSpecificDetails(PrintStream output) {
        output.println("Universitas: " + university);
        output.println("Program studi: " + course);
        output.println("ID mahasiswa: " + studentId);
    }
}

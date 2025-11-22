package model;

public class Patient {
    private int id;           // patients.id (table PK)
    private int userId;       // users.id (FK)
    private int age;
    private String gender;    // 'M','F','O'
    private String phone;
    private String medicalHistory;

    public Patient() {}

    public Patient(int userId, int age, String gender, String phone, String medicalHistory) {
        this.userId = userId;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.medicalHistory = medicalHistory;
    }

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
}

package com.tanvis.hostel;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class Hostel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "full_name")
    String full_name;
    String username;
    String email;
    String role;
    String phone;
    String password;
    String course;
    String year;
    String gender;
    String program_level;

    public int getId() { return id; }
    public String getFull_name() { return full_name; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public String getCourse() { return course; }
    public String getYear() { return year; }
    public String getGender() { return gender; }
    public String getProgram_level() { return program_level; }

    public void setId(int id) { this.id = id; }
    public void setFull_name(String full_name) { this.full_name = full_name; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPassword(String password) { this.password = password; }
    public void setCourse(String course) { this.course = course; }
    public void setYear(String year) { this.year = year; }
    public void setGender(String gender) { this.gender = gender; }
    public void setProgram_level(String program_level) { this.program_level = program_level; }
}
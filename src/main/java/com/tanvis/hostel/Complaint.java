package com.tanvis.hostel;

import jakarta.persistence.*;

@Entity
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    Hostel student;

    String title;
    String descrp;
    String categ;
    String prio;
    String status;
    String createdOn;

    public int getId() { return id; }
    public Hostel getStudent() { return student; }
    public String getTitle() { return title; }
    public String getDescrp() { return descrp; }
    public String getCateg() { return categ; }
    public String getPrio() { return prio; }
    public String getStatus() { return status; }
    public String getCreatedOn() { return createdOn; }

    public void setId(int id) { this.id = id; }
    public void setStudent(Hostel student) { this.student = student; }
    public void setTitle(String title) { this.title = title; }
    public void setDescrp(String descrp) { this.descrp = descrp; }
    public void setCateg(String categ) { this.categ = categ; }
    public void setPrio(String prio) { this.prio = prio; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedOn(String createdOn) { this.createdOn = createdOn; }
}
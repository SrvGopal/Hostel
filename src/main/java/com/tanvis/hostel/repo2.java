package com.tanvis.hostel;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface repo2 extends JpaRepository<Complaint, Integer> {
    List<Complaint> findByStudent(Hostel student);
}
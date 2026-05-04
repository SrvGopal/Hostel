package com.tanvis.hostel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface repo1 extends JpaRepository<Hostel, Integer> {
    Hostel findByUsernameAndPassword(String username, String password);
}
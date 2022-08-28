package com.example.loadbdbirthday.repository;

import com.example.loadbdbirthday.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExcelRepository extends JpaRepository<Person,Long> {

}

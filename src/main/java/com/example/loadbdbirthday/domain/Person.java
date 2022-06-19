package com.example.loadbdbirthday.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   // private LocalDate birthday;
    private String birthday;
    private String fio;
    private String photo;
    private String position;

//    public Person(LocalDate birthday, String fio, String photo, String position) {
//        this.birthday = birthday;
//        this.fio = fio;
//        this.photo = photo;
//        this.position = position;
//    }
public Person(String birthday, String fio, String photo, String position) {
    this.birthday = birthday;
    this.fio = fio;
    this.photo = photo;
    this.position = position;
}
}
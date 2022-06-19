package com.example.loadbdbirthday.service;

import com.example.loadbdbirthday.domain.Person;
import com.example.loadbdbirthday.repository.ExcelRepository;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class LoaderService {
    public static final String DATE_XLSX = "src/main/resources/files/date.xlsx";
    public static final String NO_PHOTO_JPG = "src/main/resources/photo/noPhoto.jpg";
    private static ExcelRepository excelRepository;

    @SneakyThrows
    @Autowired
    public LoaderService(ExcelRepository excelRepository) {
        this.excelRepository = excelRepository;
    }

    @SneakyThrows
    public static List<Person> loadfile() {

        List<Person> personListBD = new ArrayList<>(); // список сотрудников из Эксель

        //читаем файл
        FileInputStream file = new FileInputStream(DATE_XLSX);
        XSSFWorkbook myExcelBook = new XSSFWorkbook(file);
        XSSFSheet myExcelSheet = myExcelBook.getSheet("Sheet1");
        Iterator<Row> ri = myExcelSheet.rowIterator();

        //пробегаемся по строкам и считываем данные в определенные переменные
        while (ri.hasNext()) {

            String name = null;
            String birthdate = null;
            String photo = null;
            String position = null;
            //LocalDate date = null;
            String date = null;
            XSSFRow row = (XSSFRow) ri.next();
            try {
                birthdate = row.getCell(0).getStringCellValue();
               // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH);
               // date = LocalDate.parse(birthdate, formatter);
                date = birthdate;
                name = row.getCell(1).getStringCellValue();
                position = row.getCell(2).getStringCellValue();
                photo = row.getCell(3).getStringCellValue();

            } catch (NullPointerException e) {
                //если не записана должность, то так и оставляем поле пустым
                if (position == null) position = " ";
                //если не указано фото, то используем шаблон noPhoto
                if (photo == null)
                    photo = NO_PHOTO_JPG;
            }
            //создаем пользователя
            Person person = new Person(date, name, photo, position);
            personListBD.add(person);

            myExcelBook.close();
        }

        return personListBD;

    }

    //метод для сравнения с БД и добавления в БД или удаления из БД
    public static void check(List<Person> person) {
        List<Person> personList = excelRepository.findAll(); //personList - список пользователей из БД
        // personListCurrent - список пользователей, которые получается при сравнением с эксель (те, что есть и в БД и в эксель + те, которые есть только в эксель )
        List<Person> personListCurrent = new ArrayList<>();

        for (Person p:person) {
            // срвниваем список из Эксель со списком из БД
            Person personCurrent = compareListToPerson(personList, p);

            if (personCurrent != null) {
                personListCurrent.add(p);//если есть и в эксель и в бд, добавлем в текущий список пользователей
            } else {
                //если в БД нет
                personListCurrent.add(p);//добавляем в текущий список
                // и сохраняем в БД
                System.out.printf("Добавляем пользователя  %s в БД", p.getFio());
                excelRepository.save(p);
                System.out.println("Добавление прошло");
                System.out.println("");

            }
        }
        delBD(personList, personListCurrent);
    }


    //метод для удаления сторок из БД, которых нет в эксель, но етсь в БД
    public static void delBD(List<Person> list1, List<Person> list2) {
        List<Person> delList = new ArrayList<>();
        for (Person n : list1) {
            Person nn = compareListToPerson(list2, n);
            if (nn == null) {
                delList.add(n);
                excelRepository.delete(n);
            }

        }

    }

    //сравниваем список и пользователя
    public static Person compareListToPerson(List<Person> personList, Person person) {
        boolean existsFromBD = false;//нет в БД
        for (Person p : personList) {
            existsFromBD = false;
            //сравниваем по полю фио, дате рождения и должности
            if ((p.getBirthday().equals(person.getBirthday()) && (p.getFio().equals(person.getFio())) && (p.getPosition().equals(person.getPosition())))) {
                System.out.printf("Пользователь %s уже существует", person.getFio());
                System.out.println("");
                existsFromBD = true;
                break;
            } else {
                existsFromBD = false;
                System.out.println("Сравниваем со следующим");
            }
        }
        if (!existsFromBD) {
            return null;
        } else {
            return person;
        }
    }

    public static void start() {
        //считываем данные из файла
        List<Person> list = loadfile();
        //сравниваем и добавляем или удаляем из БД
        check(list);
    }


}

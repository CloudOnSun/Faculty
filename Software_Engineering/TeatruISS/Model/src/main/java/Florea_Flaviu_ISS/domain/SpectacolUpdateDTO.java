package Florea_Flaviu_ISS.domain;

import java.io.Serializable;

public class SpectacolUpdateDTO implements Serializable {
    public Integer id;
    public Genuri gen;
    public String regizor;
    public String titlu;
    public Integer year;
    public Integer month;
    public Integer day;
    public Integer hour;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer minute;
    public Manager manager;
    public Teatru teatru;

    public SpectacolUpdateDTO(Integer id, Genuri gen, String regizor, String titlu, Integer year, Integer month, Integer day,
                        Integer hour, Integer minute, Manager manager, Teatru teatru) {
        this.id = id;
        this.gen = gen;
        this.regizor = regizor;
        this.titlu = titlu;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.manager = manager;
        this.teatru = teatru;
    }

    public Genuri getGen() {
        return gen;
    }

    public void setGen(Genuri gen) {
        this.gen = gen;
    }

    public String getRegizor() {
        return regizor;
    }

    public void setRegizor(String regizor) {
        this.regizor = regizor;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Teatru getTeatru() {
        return teatru;
    }

    public void setTeatru(Teatru teatru) {
        this.teatru = teatru;
    }
    @Override
    public String toString() {
        return "";
    }
}

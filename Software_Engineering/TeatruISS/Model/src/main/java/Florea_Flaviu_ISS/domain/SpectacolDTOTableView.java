package Florea_Flaviu_ISS.domain;

import java.util.Objects;

public class SpectacolDTOTableView {
    public Genuri gen;
    public String regizor;
    public String titlu;
    public Integer year;
    public Integer month;
    public Integer day;
    public Integer hour;
    public Integer minute;
    public String teatru;

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SpectacolDTOTableView(Genuri gen, String regizor, String titlu, Integer year, Integer month,
                                 Integer day, Integer hour, Integer minute, String teatru) {
        this.gen = gen;
        this.regizor = regizor;
        this.titlu = titlu;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.teatru = teatru;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpectacolDTOTableView that = (SpectacolDTOTableView) o;
        return gen == that.gen && Objects.equals(regizor, that.regizor) && Objects.equals(titlu, that.titlu) && Objects.equals(year, that.year) && Objects.equals(month, that.month) && Objects.equals(day, that.day) && Objects.equals(hour, that.hour) && Objects.equals(minute, that.minute) && Objects.equals(teatru, that.teatru);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gen, regizor, titlu, year, month, day, hour, minute, teatru);
    }

    @Override
    public String toString() {
        return "";
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

    public String getTeatru() {
        return teatru;
    }

    public void setTeatru(String teatru) {
        this.teatru = teatru;
    }

}

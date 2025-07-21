package twitter.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "person_info")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class Person extends User {

    @Column(name = "first_name")
    private String name;

    @Column(name = "last_name")
    private String surname;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    public Person() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String beautify() {
        return String.format("[PERSON] {\n    ID: %d;\n    Логин: %s;\n    Полное имя: %s %s;\n    Дата рождения: %s;\n}", 
                this.id, this.login, this.surname, this.name, this.birthDate);
    }

    @Override
    public String whatIsYourName() {
        return this.surname +  " " + this.name;
    }

    @Override
    public String toFileString() {
        DateTimeFormatter fDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter fDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String registrationDateAsString = this.registrationDate.format(fDateTime);
        String birthDateAsString = this.birthDate.format(fDate);
        return String.format("{%d}{%s}{%s}{%s}{%s}{%s}{%s}{%s}", this.id, this.login, this.password, registrationDateAsString, this.userType, this.name, this.surname, birthDateAsString);
    }
}

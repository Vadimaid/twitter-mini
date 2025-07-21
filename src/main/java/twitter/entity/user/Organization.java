package twitter.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "organization_info")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class Organization extends User {

    @Column(name = "title")
    private String title;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "date_of_foundation")
    private LocalDate dateOfFoundation;

    public Organization() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public LocalDate getDateOfFoundation() {
        return dateOfFoundation;
    }

    public void setDateOfFoundation(LocalDate dateOfFoundation) {
        this.dateOfFoundation = dateOfFoundation;
    }

    @Override
    public String beautify() {
        return String.format("[ORGANIZATION] {\n    ID: %d;\n    Логин: %s;\n    Наименование: %s;\n    Род занятий: %s;\n    Дата основания: %s;\n}",
                this.id, this.login, this.title, this.occupation, this.dateOfFoundation);

    }

    @Override
    public String whatIsYourName() {
        return this.title;
    }

    @Override
    public String toFileString() {
        DateTimeFormatter fDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter fDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String registrationDateAsString = this.registrationDate.format(fDateTime);
        String dateOfFoundationAsString = this.dateOfFoundation.format(fDate);
        return String.format("{%d}{%s}{%s}{%s}{%s}{%s}{%s}{%s}", this.id, this.login, this.password, registrationDateAsString, this.userType, this.title, this.occupation, dateOfFoundationAsString);
    }
}

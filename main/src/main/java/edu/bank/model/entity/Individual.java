package edu.bank.model.entity;

public class Individual extends User {

    private String lastName;
    private String patronymic;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                "} " + super.toString();
    }
}

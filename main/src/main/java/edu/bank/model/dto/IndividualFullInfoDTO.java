package edu.bank.model.dto;

import java.util.List;

public class IndividualFullInfoDTO {

    private String firstName;
    private String lastName;
    private String patronymic;
    private String phone;
    private List<AccountMainInfoDTO> accounts;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<AccountMainInfoDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountMainInfoDTO> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "Full info about individual:\n" +
                "Firstname: " + firstName + "\n" +
                "Lastname: " + lastName + "\n" +
                "Patronymic: " + patronymic + "\n" +
                "Phone: " + phone + "\n" +
                "All accounts of individual: " + accounts;
    }
}

package edu.bank.model.dto;

import java.util.List;

public class LegalEntityFullInfoDTO {

    private String name;
    private String phone;
    private List<AccountMainInfoDTO> accounts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "Full info about legal entity:\n" +
                "Name: " + name + "\n" +
                "Phone: " + phone + "\n" +
                "All accounts of legal entity: " + accounts;
    }
}

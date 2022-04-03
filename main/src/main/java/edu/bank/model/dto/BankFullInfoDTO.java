package edu.bank.model.dto;

public class BankFullInfoDTO {

    private String name;
    private String ibanPrefix;
    private double individualsFee;
    private double legalEntitiesFee;
    private int usersCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIbanPrefix() {
        return ibanPrefix;
    }

    public void setIbanPrefix(String ibanPrefix) {
        this.ibanPrefix = ibanPrefix;
    }

    public double getIndividualsFee() {
        return individualsFee;
    }

    public void setIndividualsFee(double individualsFee) {
        this.individualsFee = individualsFee;
    }

    public double getLegalEntitiesFee() {
        return legalEntitiesFee;
    }

    public void setLegalEntitiesFee(double legalEntitiesFee) {
        this.legalEntitiesFee = legalEntitiesFee;
    }

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

    @Override
    public String toString() {
        return "Full info about bank:" + "\n" +
                "Name: " + name + "\n" +
                "IBAN prefix: " + ibanPrefix + "\n" +
                "Individuals fee: " + individualsFee + "\n" +
                "Legal entities fee: " + legalEntitiesFee + "\n" +
                "Users count: " + usersCount + "\n";
    }
}

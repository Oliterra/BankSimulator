package edu.bank.entity;

import java.util.Objects;

public class Bank {

    private long id;
    private String name;
    private String ibanPrefix;
    private double individualsFee;
    private double legalEntitiesFee;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIbanPrefix() {
        return ibanPrefix;
    }

    public double getIndividualsFee() {
        return individualsFee;
    }

    public double getLegalEntitiesFee() {
        return legalEntitiesFee;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIbanPrefix(String ibanPrefix) {
        this.ibanPrefix = ibanPrefix;
    }

    public void setIndividualsFee(double individualsFee) {
        this.individualsFee = individualsFee;
    }

    public void setLegalEntitiesFee(double legalEntitiesFee) {
        this.legalEntitiesFee = legalEntitiesFee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return id == bank.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Bank{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ibanPrefix='" + ibanPrefix + '\'' +
                ", individualsFee=" + individualsFee +
                ", legalEntitiesFee=" + legalEntitiesFee +
                '}';
    }
}

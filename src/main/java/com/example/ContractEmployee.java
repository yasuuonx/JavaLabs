package com.example;

import java.util.Objects;
import java.util.UUID;

/**
 * Клас описує працівника на строковому договорі.
 */
public class ContractEmployee extends Employee {
    private int contractDurationMonths;

    public ContractEmployee(String name, String position, double salary, int experienceYears, Department department, int contractDurationMonths) {
        super(name, position, salary, experienceYears, department);
        setContractDurationMonths(contractDurationMonths);
    }

    public ContractEmployee(UUID uuid, String name, String position, double salary, int experienceYears, Department department, int contractDurationMonths) {
        super(uuid, name, position, salary, experienceYears, department);
        setContractDurationMonths(contractDurationMonths);
    }

    public int getContractDurationMonths() {
        return contractDurationMonths;
    }

    public void setContractDurationMonths(int contractDurationMonths) {
        if (contractDurationMonths <= 0) {
            throw new IllegalArgumentException("Тривалість контракту повинна бути більшою за нуль");
        }
        this.contractDurationMonths = contractDurationMonths;
    }

    @Override
    public String toFileString() {
        return "CONTRACT;" + getUuid() + ";" + getName() + ";" + getPosition() + ";" + getSalary() + ";" + getExperienceYears() + ";" + getDepartment().name() + ";" + contractDurationMonths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ContractEmployee that = (ContractEmployee) o;
        return contractDurationMonths == that.contractDurationMonths;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contractDurationMonths);
    }

    @Override
    public String toString() {
        return String.format("ContractEmployee { UUID: %s, Ім'я: '%s', Посада: '%s', Зарплата: %.2f грн, Стаж: %d р., Відділ: %s, Тривалість контракту: %d міс. }",
                getUuid(), getName(), getPosition(), getSalary(), getExperienceYears(), getDepartment().getTitle(), contractDurationMonths);
    }
}
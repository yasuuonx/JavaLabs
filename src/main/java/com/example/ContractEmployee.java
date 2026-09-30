package com.example;

import java.util.Objects;

/**
 * Клас описує тимчасового контрактного співробітника.
 */
public class ContractEmployee extends Employee {
    private int contractDurationMonths;

    /**
     * Конструктор контрактного співробітника.
     *
     * @param name                   ім'я
     * @param position               посада
     * @param salary                 ставка зарплати
     * @param experienceYears        стаж
     * @param department             відділ
     * @param contractDurationMonths тривалість контракту в місяцях
     */
    public ContractEmployee(String name, String position, double salary, int experienceYears, Department department, int contractDurationMonths) {
        super(name, position, salary, experienceYears, department);
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
        return String.format("ContractEmployee { Ім'я: '%s', Посада: '%s', Зарплата: %.2f грн, Стаж: %d р., Відділ: %s, Контракт: %d міс. }",
                             getName(), getPosition(), getSalary(), getExperienceYears(), getDepartment().getTitle(), contractDurationMonths);
    }
}
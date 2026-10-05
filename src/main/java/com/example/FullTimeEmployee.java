package com.example;

import java.util.Objects;
import java.util.UUID;

/**
 * Клас описує штатного працівника з річним бонусом.
 */
public class FullTimeEmployee extends Employee {
    private double annualBonus;

    public FullTimeEmployee(String name, String position, double salary, int experienceYears, Department department, double annualBonus) {
        super(name, position, salary, experienceYears, department);
        setAnnualBonus(annualBonus);
    }

    public FullTimeEmployee(UUID uuid, String name, String position, double salary, int experienceYears, Department department, double annualBonus) {
        super(uuid, name, position, salary, experienceYears, department);
        setAnnualBonus(annualBonus);
    }

    public double getAnnualBonus() {
        return annualBonus;
    }

    public void setAnnualBonus(double annualBonus) {
        if (annualBonus < 0) {
            throw new IllegalArgumentException("Річний бонус не може бути від'ємним");
        }
        this.annualBonus = annualBonus;
    }

    @Override
    public String toFileString() {
        return "FULL_TIME;" + getUuid() + ";" + getName() + ";" + getPosition() + ";" + getSalary() + ";" + getExperienceYears() + ";" + getDepartment().name() + ";" + annualBonus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FullTimeEmployee that = (FullTimeEmployee) o;
        return Double.compare(that.annualBonus, annualBonus) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), annualBonus);
    }

    @Override
    public String toString() {
        return String.format("FullTimeEmployee { UUID: %s, Ім'я: '%s', Посада: '%s', Зарплата: %.2f грн, Стаж: %d р., Відділ: %s, Річний бонус: %.2f грн }",
                getUuid(), getName(), getPosition(), getSalary(), getExperienceYears(), getDepartment().getTitle(), annualBonus);
    }
}
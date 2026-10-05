package com.example;

import java.util.Objects;
import java.util.UUID;

/**
 * Клас описує фрилансера з погодинною ставкою.
 */
public class Freelancer extends ContractEmployee {
    private double hourlyRate;

    public Freelancer(String name, String position, double salary, int experienceYears, Department department, int contractDurationMonths, double hourlyRate) {
        super(name, position, salary, experienceYears, department, contractDurationMonths);
        setHourlyRate(hourlyRate);
    }

    public Freelancer(UUID uuid, String name, String position, double salary, int experienceYears, Department department, int contractDurationMonths, double hourlyRate) {
        super(uuid, name, position, salary, experienceYears, department, contractDurationMonths);
        setHourlyRate(hourlyRate);
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        if (hourlyRate <= 0) {
            throw new IllegalArgumentException("Погодинна ставка повинна бути більшою за нуль");
        }
        this.hourlyRate = hourlyRate;
    }

    @Override
    public String toFileString() {
        return "FREELANCER;" + getUuid() + ";" + getName() + ";" + getPosition() + ";" + getSalary() + ";" + getExperienceYears() + ";" + getDepartment().name() + ";" + getContractDurationMonths() + ";" + hourlyRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Freelancer that = (Freelancer) o;
        return Double.compare(that.hourlyRate, hourlyRate) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hourlyRate);
    }

    @Override
    public String toString() {
        return String.format("Freelancer { UUID: %s, Ім'я: '%s', Посада: '%s', Зарплата: %.2f грн, Стаж: %d р., Відділ: %s, Контракт: %d міс., Ставка: %.2f грн/год }",
                getUuid(), getName(), getPosition(), getSalary(), getExperienceYears(), getDepartment().getTitle(), getContractDurationMonths(), hourlyRate);
    }
}
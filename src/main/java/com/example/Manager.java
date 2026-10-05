package com.example;

import java.util.Objects;
import java.util.UUID;

/**
 * Клас описує менеджера з бонусом та кількістю підлеглих.
 */
public class Manager extends FullTimeEmployee {
    private int teamSize;

    public Manager(String name, String position, double salary, int experienceYears, Department department, double annualBonus, int teamSize) {
        super(name, position, salary, experienceYears, department, annualBonus);
        setTeamSize(teamSize);
    }

    public Manager(UUID uuid, String name, String position, double salary, int experienceYears, Department department, double annualBonus, int teamSize) {
        super(uuid, name, position, salary, experienceYears, department, annualBonus);
        setTeamSize(teamSize);
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        if (teamSize < 0) {
            throw new IllegalArgumentException("Кількість підлеглих не може бути від'ємною");
        }
        this.teamSize = teamSize;
    }

    @Override
    public String toFileString() {
        return "MANAGER;" + getUuid() + ";" + getName() + ";" + getPosition() + ";" + getSalary() + ";" + getExperienceYears() + ";" + getDepartment().name() + ";" + getAnnualBonus() + ";" + teamSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Manager manager = (Manager) o;
        return teamSize == manager.teamSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), teamSize);
    }

    @Override
    public String toString() {
        return String.format("Manager { UUID: %s, Ім'я: '%s', Посада: '%s', Зарплата: %.2f грн, Стаж: %d р., Відділ: %s, Бонус: %.2f грн, Підлеглих: %d }",
                getUuid(), getName(), getPosition(), getSalary(), getExperienceYears(), getDepartment().getTitle(), getAnnualBonus(), teamSize);
    }
}
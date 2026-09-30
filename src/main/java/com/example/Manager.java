package com.example;

import java.util.Objects;

/**
 * Клас описує менеджера підрозділу, який є штатним співробітником і керує командою.
 */
public class Manager extends FullTimeEmployee {
    private int teamSize;

    /**
     * Конструктор менеджера.
     *
     * @param name            ім'я
     * @param position        посада
     * @param salary          ставка зарплати
     * @param experienceYears стаж
     * @param department      відділ
     * @param annualBonus     річний бонус
     * @param teamSize        кількість підлеглих у команді
     */
    public Manager(String name, String position, double salary, int experienceYears, Department department, double annualBonus, int teamSize) {
        super(name, position, salary, experienceYears, department, annualBonus);
        setTeamSize(teamSize);
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        if (teamSize < 0) {
            throw new IllegalArgumentException("Розмір команди не може бути від'ємним");
        }
        this.teamSize = teamSize;
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
        return String.format("Manager { Ім'я: '%s', Посада: '%s', Зарплата: %.2f грн, Стаж: %d р., Відділ: %s, Бонус: %.2f грн, Команда: %d ос. }",
                getName(), getPosition(), getSalary(), getExperienceYears(), getDepartment().getTitle(), getAnnualBonus(), teamSize);
    }
}
package com.example;

import java.util.Objects;

/**
 * Клас описує співробітника з перевіркою коректності параметрів.
 */
public class Employee {
    private String name;
    private String position;
    private double salary;
    private int experienceYears;

    /**
     * Конструктор з валідацією через виклик сетерів.
     *
     * @param name            ім'я співробітника
     * @param position        посада співробітника
     * @param salary          заробітна плата
     * @param experienceYears стаж роботи у роках
     * @throws IllegalArgumentException якщо передано некоректні дані
     */
    public Employee(String name, String position, double salary, int experienceYears) {
        setName(name);
        setPosition(position);
        setSalary(salary);
        setExperienceYears(experienceYears);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Ім'я не може бути порожнім");
        }
        this.name = name.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        if (position == null || position.trim().isEmpty()) {
            throw new IllegalArgumentException("Посада не може бути порожньою");
        }
        this.position = position.trim();
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        if (salary <= 0) {
            throw new IllegalArgumentException("Заробітна плата повинна бути більшою за нуль");
        }
        this.salary = salary;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        if (experienceYears < 0) {
            throw new IllegalArgumentException("Стаж не може бути від'ємним");
        }
        this.experienceYears = experienceYears;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Double.compare(employee.salary, salary) == 0 &&
                experienceYears == employee.experienceYears &&
                Objects.equals(name, employee.name) &&
                Objects.equals(position, employee.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, position, salary, experienceYears);
    }

    @Override
    public String toString() {
        return String.format("Employee { Ім'я: '%s', Посада: '%s', Зарплата: %.2f грн, Стаж: %d р. }",
                name, position, salary, experienceYears);
    }
}
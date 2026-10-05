package com.example;

import java.util.Objects;
import java.util.UUID;

/**
 * Базовий абстрактний клас співробітника.
 */
public abstract class Employee implements Comparable<Employee>, Identifiable {
    private final UUID uuid;
    private String name;
    private String position;
    private double salary;
    private int experienceYears;
    private Department department;

    public Employee(String name, String position, double salary, int experienceYears, Department department) {
        this(UUID.randomUUID(), name, position, salary, experienceYears, department);
    }

    public Employee(UUID uuid, String name, String position, double salary, int experienceYears, Department department) {
        if (uuid == null) {
            throw new InvalidFieldValueException("UUID не може бути null");
        }
        this.uuid = uuid;
        setName(name);
        setPosition(position);
        setSalary(salary);
        setExperienceYears(experienceYears);
        setDepartment(department);
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidFieldValueException("Ім'я співробітника не може бути порожнім");
        }
        this.name = name.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        if (position == null || position.trim().isEmpty()) {
            throw new InvalidFieldValueException("Посада не може бути порожньою");
        }
        this.position = position.trim();
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        if (salary < 0) {
            throw new InvalidFieldValueException("Заробітна плата не може бути від'ємною");
        }
        this.salary = salary;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        if (experienceYears < 0) {
            throw new InvalidFieldValueException("Стаж роботи не може бути від'ємним");
        }
        this.experienceYears = experienceYears;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        if (department == null) {
            throw new InvalidFieldValueException("Відділ не може бути null");
        }
        this.department = department;
    }

    public abstract String toFileString();

    public String toShortString() {
        return String.format("[%s] %s (%s, %.2f грн)", uuid, name, position, salary);
    }

    @Override
    public int compareTo(Employee other) {
        if (other == null) {
            return 1;
        }
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(uuid, employee.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
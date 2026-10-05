package com.example;

import java.util.Objects;
import java.util.UUID;

/**
 * Абстрактний базовий клас співробітника підприємства.
 * Реалізує Comparable для сортування за ПІБ та Identifiable для отримання UUID.
 */
public abstract class Employee implements Comparable<Employee>, Identifiable {
    private final UUID uuid;
    private String name;
    private String position;
    private double salary;
    private int experienceYears;
    private Department department;

    /**
     * Конструктор з автоматичною генерацією UUID.
     */
    public Employee(String name, String position, double salary, int experienceYears, Department department) {
        this(UUID.randomUUID(), name, position, salary, experienceYears, department);
    }

    /**
     * Конструктор із явним передаванням UUID.
     */
    public Employee(UUID uuid, String name, String position, double salary, int experienceYears, Department department) {
        if (uuid == null) {
            this.uuid = UUID.randomUUID();
        } else {
            this.uuid = uuid;
        }
        setName(name);
        setPosition(position);
        setSalary(salary);
        setExperienceYears(experienceYears);
        setDepartment(department);
    }

    /**
     * Конструктор копіювання.
     */
    public Employee(Employee other) {
        if (other == null) {
            throw new IllegalArgumentException("Об'єкт для копіювання не може бути null");
        }
        this.uuid = UUID.randomUUID();
        this.name = other.name;
        this.position = other.position;
        this.salary = other.salary;
        this.experienceYears = other.experienceYears;
        this.department = other.department;
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Відділ не може бути null");
        }
        this.department = department;
    }

    @Override
    public int compareTo(Employee other) {
        if (other == null) {
            return 1;
        }
        int nameComparison = this.name.compareToIgnoreCase(other.name);
        if (nameComparison != 0) {
            return nameComparison;
        }
        return this.position.compareToIgnoreCase(other.position);
    }

    /**
     * Скорочений рядок для GUI та списків: назва + UUID.
     */
    public String toShortString() {
        return String.format("%s (%s) | UUID: %s", name, position, uuid);
    }

    public String toFileString() {
        return "EMPLOYEE;" + uuid + ";" + name + ";" + position + ";" + salary + ";" + experienceYears + ";" + department.name();
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

    @Override
    public String toString() {
        return String.format("Employee { UUID: %s, Ім'я: '%s', Посада: '%s', Зарплата: %.2f грн, Стаж: %d р., Відділ: %s }",
                uuid, name, position, salary, experienceYears, department.getTitle());
    }
}
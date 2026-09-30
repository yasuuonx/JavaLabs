package com.example;

import java.util.Objects;

/**
 * Клас описує співробітника підприємства.
 */
public class Employee {
    private static int totalEmployeesCreated = 0;

    private String name;
    private String position;
    private double salary;
    private int experienceYears;
    private Department department;

    /**
     * Конструктор з параметрами та валідацією через сетери.
     *
     * @param name            ім'я співробітника
     * @param position        посада співробітника
     * @param salary          заробітна плата
     * @param experienceYears стаж роботи у роках
     * @param department      відділ компанії
     * @throws IllegalArgumentException якщо передано некоректні дані
     */
    public Employee(String name, String position, double salary, int experienceYears, Department department) {
        setName(name);
        setPosition(position);
        setSalary(salary);
        setExperienceYears(experienceYears);
        setDepartment(department);
        totalEmployeesCreated++;
    }

    /**
     * Конструктор копіювання.
     *
     * @param other об'єкт Employee для клонування
     * @throws IllegalArgumentException якщо переданий об'єкт є null
     */
    public Employee(Employee other) {
        if (other == null) {
            throw new IllegalArgumentException("Об'єкт для копіювання не може бути null");
        }
        this.name = other.name;
        this.position = other.position;
        this.salary = other.salary;
        this.experienceYears = other.experienceYears;
        this.department = other.department;
        totalEmployeesCreated++;
    }

    /**
     * Отримує загальну кількість створених об'єктів Employee.
     *
     * @return кількість екземплярів
     */
    public static int getTotalEmployeesCreated() {
        return totalEmployeesCreated;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Double.compare(employee.salary, salary) == 0 &&
                experienceYears == employee.experienceYears &&
                Objects.equals(name, employee.name) &&
                Objects.equals(position, employee.position) &&
                department == employee.department;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, position, salary, experienceYears, department);
    }

    @Override
    public String toString() {
        return String.format("Employee { Ім'я: '%s', Посада: '%s', Зарплата: %.2f грн, Стаж: %d р., Відділ: %s }",
                name, position, salary, experienceYears, department.getTitle());
    }
}
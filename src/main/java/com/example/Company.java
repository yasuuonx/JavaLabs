package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас агрегує об'єкти співробітників у межах компанії.
 */
public class Company {
    private String companyName;
    private List<Employee> employees;

    /**
     * Конструктор компанії.
     *
     * @param companyName назва компанії
     * @throws IllegalArgumentException якщо назва порожня
     */
    public Company(String companyName) {
        setCompanyName(companyName);
        this.employees = new ArrayList<>();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва компанії не може бути порожньою");
        }
        this.companyName = companyName.trim();
    }

    /**
     * Додає співробітника до штату компанії (агрегація).
     *
     * @param employee об'єкт співробітника
     * @throws IllegalArgumentException якщо передано null
     */
    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Співробітник не може бути null");
        }
        this.employees.add(employee);
    }

    /**
     * Повертає список співробітників компанії.
     *
     * @return копія списку співробітників
     */
    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    /**
     * Повертає кількість співробітників у компанії.
     *
     * @return розмір штату
     */
    public int getEmployeeCount() {
        return employees.size();
    }
}
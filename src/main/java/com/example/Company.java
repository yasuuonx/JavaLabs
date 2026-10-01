package com.example;

import java.util.ArrayList;

/**
 * Клас-контейнер, який агрегує колекцію співробітників та веде облік їх кількості.
 */
public class Company {
    private String name;
    private ArrayList<Employee> employees;
    private ArrayList<Integer> quantities;

    /**
     * Конструктор компанії.
     *
     * @param name назва компанії
     */
    public Company(String name) {
        setName(name);
        this.employees = new ArrayList<Employee>();
        this.quantities = new ArrayList<Integer>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва компанії не може бути порожньою");
        }
        this.name = name.trim();
    }

    /**
     * Додає співробітника або збільшує кількість, якщо такий уже існує.
     *
     * @param emp      об'єкт співробітника
     * @param quantity кількість для додавання
     */
    public void addNewEmployee(Employee emp, int quantity) {
        if (emp == null) {
            throw new IllegalArgumentException("Співробітник не може бути null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Кількість повинна бути більшою за нуль");
        }

        for (int i = 0; i < employees.size(); i++) {
            Employee existing = employees.get(i);
            if (existing.equals(emp)) {
                int currentQty = quantities.get(i).intValue();
                quantities.set(i, Integer.valueOf(currentQty + quantity));
                return;
            }
        }

        employees.add(emp);
        quantities.add(Integer.valueOf(quantity));
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public ArrayList<Integer> getQuantities() {
        return quantities;
    }

    public int getQuantity(int index) {
        return quantities.get(index).intValue();
    }

    public int size() {
        return employees.size();
    }

    /**
     * Пошук співробітників за посадою (без урахування регістру).
     *
     * @param position назва посади
     * @return список знайдених співробітників
     */
    public ArrayList<Employee> searchByPosition(String position) {
        ArrayList<Employee> result = new ArrayList<Employee>();
        if (position == null || position.trim().isEmpty()) {
            return result;
        }
        String target = position.trim().toLowerCase();
        for (Employee emp : employees) {
            if (emp.getPosition().toLowerCase().contains(target)) {
                result.add(emp);
            }
        }
        return result;
    }

    /**
     * Пошук співробітників за діапазоном заробітної плати.
     *
     * @param minSalary мінімальна зарплата
     * @param maxSalary максимальна зарплата
     * @return список знайдених співробітників
     */
    public ArrayList<Employee> searchBySalaryRange(double minSalary, double maxSalary) {
        ArrayList<Employee> result = new ArrayList<Employee>();
        for (Employee emp : employees) {
            if (emp.getSalary() >= minSalary && emp.getSalary() <= maxSalary) {
                result.add(emp);
            }
        }
        return result;
    }

    /**
     * Пошук співробітників за відділом.
     *
     * @param department відділ
     * @return список знайдених співробітників
     */
    public ArrayList<Employee> searchByDepartment(Department department) {
        ArrayList<Employee> result = new ArrayList<Employee>();
        if (department == null) {
            return result;
        }
        for (Employee emp : employees) {
            if (emp.getDepartment() == department) {
                result.add(emp);
            }
        }
        return result;
    }
}
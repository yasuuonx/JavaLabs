package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

/**
 * Клас-контейнер, який агрегує колекцію співробітників та реалізує
 * операції додавання, оновлення (update), видалення (delete), пошуку та сортування.
 */
public class Company {
    private String name;
    private ArrayList<Employee> employees;
    private ArrayList<Integer> quantities;

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

    /**
     * Модифікація об'єкта в колекції (Завдання 1, ЛР 17).
     * @param existingObject об'єкт, який потрібно знайти та оновити
     * @param newObject новий об'єкт з оновленими значеннями
     * @return true, якщо об'єкт знайдено та оновлено; false - якщо не знайдено
     */
    public boolean update(Employee existingObject, Employee newObject) {
        if (existingObject == null || newObject == null) {
            return false;
        }

        for (int i = 0; i < employees.size(); i++) {
            Employee current = employees.get(i);
            if (current.equals(existingObject)) {
                employees.set(i, newObject);
                return true;
            }
        }
        return false;
    }

    /**
     * Видалення об'єкта з колекції (Завдання 2, ЛР 17).
     * @param existingObject об'єкт, який потрібно видалити
     * @return true, якщо видалено успішно; false - якщо об'єкт не знайдено
     */
    public boolean delete(Employee existingObject) {
        if (existingObject == null) {
            return false;
        }

        for (int i = 0; i < employees.size(); i++) {
            Employee current = employees.get(i);
            if (current.equals(existingObject)) {
                employees.remove(i);
                quantities.remove(i);
                return true;
            }
        }
        return false;
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

    public ArrayList<Employee> getSortedEmployees() {
        ArrayList<Employee> sortedList = new ArrayList<Employee>(this.employees);
        Collections.sort(sortedList);
        return sortedList;
    }

    public ArrayList<Employee> getSortedEmployees(Comparator<Employee> comparator) {
        ArrayList<Employee> sortedList = new ArrayList<Employee>(this.employees);
        if (comparator != null) {
            Collections.sort(sortedList, comparator);
        }
        return sortedList;
    }

    public Employee findByUuid(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        for (Employee emp : employees) {
            if (uuid.equals(emp.getUuid())) {
                return emp;
            }
        }
        return null;
    }

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

    public ArrayList<Employee> searchBySalaryRange(double minSalary, double maxSalary) {
        ArrayList<Employee> result = new ArrayList<Employee>();
        for (Employee emp : employees) {
            if (emp.getSalary() >= minSalary && emp.getSalary() <= maxSalary) {
                result.add(emp);
            }
        }
        return result;
    }

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
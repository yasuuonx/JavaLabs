package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Головний клас для взаємодії з користувачем через меню.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Employee> employees = new ArrayList<>();
        boolean isRunning = true;

        while (isRunning) {
            System.out.println();
            System.out.println("Головне меню:");
            System.out.println("1. Створити новий об'єкт");
            System.out.println("2. Вивести інформацію про всі об'єкти");
            System.out.println("3. Завершити роботу");
            System.out.print("Оберіть дію: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createNewEmployee(scanner, employees);
                    break;
                case "2":
                    displayAllEmployees(employees);
                    break;
                case "3":
                    isRunning = false;
                    System.out.println("Роботу програми завершено.");
                    break;
                default:
                    System.out.println("Помилка: невідома опція меню. Оберіть пункт від 1 до 3.");
                    break;
            }
        }

        scanner.close();
    }

    private static void createNewEmployee(Scanner scanner, List<Employee> employees) {
        try {
            System.out.print("Введіть ім'я: ");
            String name = scanner.nextLine();

            System.out.print("Введіть посаду: ");
            String position = scanner.nextLine();

            System.out.print("Введіть заробітну плату: ");
            double salary = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Введіть стаж роботи (роки): ");
            int experience = Integer.parseInt(scanner.nextLine().trim());

            Employee employee = new Employee(name, position, salary, experience);
            employees.add(employee);
            System.out.println("Співробітника успішно додано.");
        } catch (NumberFormatException e) {
            System.out.println("Помилка введення: для числових параметрів введіть число.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка валідації даних: " + e.getMessage());
        }
    }

    private static void displayAllEmployees(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("Список співробітників порожній.");
            return;
        }

        System.out.println();
        System.out.println("Список співробітників (" + employees.size() + "):");
        for (Employee emp : employees) {
            System.out.println(emp);
        }
    }
}
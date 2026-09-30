package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Драйвер програми для демонстрації успадкування та поліморфізму.
 */
public class Main {

    public static void main(String[] args) {
        printHeader();

        Scanner scanner = new Scanner(System.in);
        List<Employee> employees = new ArrayList<>();
        boolean isRunning = true;

        while (isRunning) {
            System.out.println();
            System.out.println("Головне меню:");
            System.out.println("1. Створити базового співробітника (Employee)");
            System.out.println("2. Створити штатного співробітника (FullTimeEmployee)");
            System.out.println("3. Створити контрактного співробітника (ContractEmployee)");
            System.out.println("4. Вивести інформацію про всіх співробітників (Поліморфізм)");
            System.out.println("5. Завершити роботу");
            System.out.print("Оберіть пункт меню: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createBaseEmployee(scanner, employees);
                    break;
                case "2":
                    createFullTimeEmployee(scanner, employees);
                    break;
                case "3":
                    createContractEmployee(scanner, employees);
                    break;
                case "4":
                    displayAllEmployees(employees);
                    break;
                case "5":
                    isRunning = false;
                    System.out.println("Роботу завершено.");
                    break;
                default:
                    System.out.println("Помилка: невідома дія. Оберіть число від 1 до 5.");
                    break;
            }
        }

        scanner.close();
    }

    private static void printHeader() {
        System.out.println("Практична робота №7");
        System.out.println("Тема: Наслідування, поліморфізм, колекції (ArrayList)");
        System.out.println("Виконав: студент Демченко Станіслав");
    }

    private static void createBaseEmployee(Scanner scanner, List<Employee> list) {
        try {
            System.out.print("Введіть ім'я: ");
            String name = scanner.nextLine();

            System.out.print("Введіть посаду: ");
            String position = scanner.nextLine();

            System.out.print("Введіть заробітну плату: ");
            double salary = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Введіть стаж роботи (роки): ");
            int experience = Integer.parseInt(scanner.nextLine().trim());

            Department department = chooseDepartment(scanner);

            Employee employee = new Employee(name, position, salary, experience, department);
            list.add(employee);
            System.out.println("Базового співробітника успішно додано.");
        } catch (NumberFormatException e) {
            System.out.println("Помилка введення: очікується числове значення.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка валідації даних: " + e.getMessage());
        }
    }

    private static void createFullTimeEmployee(Scanner scanner, List<Employee> list) {
        try {
            System.out.print("Введіть ім'я: ");
            String name = scanner.nextLine();

            System.out.print("Введіть посаду: ");
            String position = scanner.nextLine();

            System.out.print("Введіть заробітну плату: ");
            double salary = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Введіть стаж роботи (роки): ");
            int experience = Integer.parseInt(scanner.nextLine().trim());

            Department department = chooseDepartment(scanner);

            System.out.print("Введіть розмір річного бонусу: ");
            double bonus = Double.parseDouble(scanner.nextLine().trim());

            FullTimeEmployee fullTimeEmployee = new FullTimeEmployee(name, position, salary, experience, department, bonus);
            list.add(fullTimeEmployee);
            System.out.println("Штатного співробітника успішно додано.");
        } catch (NumberFormatException e) {
            System.out.println("Помилка введення: очікується числове значення.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка валідації даних: " + e.getMessage());
        }
    }

    private static void createContractEmployee(Scanner scanner, List<Employee> list) {
        try {
            System.out.print("Введіть ім'я: ");
            String name = scanner.nextLine();

            System.out.print("Введіть посаду: ");
            String position = scanner.nextLine();

            System.out.print("Введіть заробітну плату: ");
            double salary = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Введіть стаж роботи (роки): ");
            int experience = Integer.parseInt(scanner.nextLine().trim());

            Department department = chooseDepartment(scanner);

            System.out.print("Введіть тривалість контракту (місяців): ");
            int duration = Integer.parseInt(scanner.nextLine().trim());

            ContractEmployee contractEmployee = new ContractEmployee(name, position, salary, experience, department, duration);
            list.add(contractEmployee);
            System.out.println("Контрактного співробітника успішно додано.");
        } catch (NumberFormatException e) {
            System.out.println("Помилка введення: очікується числове значення.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка валідації даних: " + e.getMessage());
        }
    }

    private static Department chooseDepartment(Scanner scanner) {
        System.out.println("Оберіть відділ (1 - IT, 2 - HR, 3 - FINANCE, 4 - MARKETING): ");
        String deptChoice = scanner.nextLine().trim();
        switch (deptChoice) {
            case "1": return Department.IT;
            case "2": return Department.HR;
            case "3": return Department.FINANCE;
            case "4": return Department.MARKETING;
            default: throw new IllegalArgumentException("Обрано неіснуючий відділ");
        }
    }

    private static void displayAllEmployees(List<Employee> list) {
        if (list.isEmpty()) {
            System.out.println("Список співробітників порожній.");
            return;
        }

        System.out.println();
        System.out.println("Загальний список співробітників (Поліморфний вивід, всього: " + list.size() + "):");
        for (Employee emp : list) {
            System.out.println(emp);
        }
    }
}
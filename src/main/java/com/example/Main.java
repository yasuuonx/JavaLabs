package com.example;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Драйвер програми з інтерактивним меню, завантаженням та збереженням стану у файли.
 */
public class Main {
    private static final String TEXT_FILE = "input.txt";
    private static final String JSON_FILE = "input.json";

    public static void main(String[] args) {
        printHeader();

        ArrayList<Employee> employees = FileManager.loadFromTextFile(TEXT_FILE);
        System.out.println("Завантажено об'єктів із файлу " + TEXT_FILE + ": " + employees.size());

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println();
            System.out.println("Головне меню:");
            System.out.println("1. Створити новий об'єкт");
            System.out.println("2. Вивести інформацію про всі об'єкти");
            System.out.println("3. Завершити роботу програми");
            System.out.print("Оберіть дію: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleCreateObjectMenu(scanner, employees);
                    break;
                case "2":
                    displayAllEmployees(employees);
                    break;
                case "3":
                    FileManager.saveToTextFile(TEXT_FILE, employees);
                    FileManager.saveToJsonFile(JSON_FILE, employees);
                    System.out.println("Дані успішно збережено у файли " + TEXT_FILE + " та " + JSON_FILE);
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

    private static void printHeader() {
        System.out.println("Практична робота №9");
        System.out.println("Тема: Ієрархія успадкування, робота з файлами для зберігання інформації");
        System.out.println("Виконав: студент Демченко Станіслав");
    }

    private static void handleCreateObjectMenu(Scanner scanner, ArrayList<Employee> list) {
        System.out.println();
        System.out.println("Оберіть тип об'єкта для створення:");
        System.out.println("1. Базовий співробітник (Employee)");
        System.out.println("2. Штатний співробітник (FullTimeEmployee)");
        System.out.println("3. Контрактний співробітник (ContractEmployee)");
        System.out.println("4. Менеджер (Manager)");
        System.out.println("5. Фрилансер (Freelancer)");
        System.out.println("0. Повернутися до головного меню");
        System.out.print("Ваш вибір: ");

        String typeChoice = scanner.nextLine().trim();

        if ("0".equals(typeChoice)) {
            System.out.println("Повернення до головного меню без створення об'єкта.");
            return;
        }

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

            switch (typeChoice) {
                case "1":
                    list.add(new Employee(name, position, salary, experience, department));
                    System.out.println("Об'єкт Employee успішно створено.");
                    break;
                case "2":
                    System.out.print("Введіть розмір річного бонусу: ");
                    double bonus = Double.parseDouble(scanner.nextLine().trim());
                    list.add(new FullTimeEmployee(name, position, salary, experience, department, bonus));
                    System.out.println("Об'єкт FullTimeEmployee успішно створено.");
                    break;
                case "3":
                    System.out.print("Введіть тривалість контракту (місяців): ");
                    int duration = Integer.parseInt(scanner.nextLine().trim());
                    list.add(new ContractEmployee(name, position, salary, experience, department, duration));
                    System.out.println("Об'єкт ContractEmployee успішно створено.");
                    break;
                case "4":
                    System.out.print("Введіть розмір річного бонусу: ");
                    double mgrBonus = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Введіть кількість підлеглих у команді: ");
                    int teamSize = Integer.parseInt(scanner.nextLine().trim());
                    list.add(new Manager(name, position, salary, experience, department, mgrBonus, teamSize));
                    System.out.println("Об'єкт Manager успішно створено.");
                    break;
                case "5":
                    System.out.print("Введіть тривалість контракту (місяців): ");
                    int flDuration = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Введіть погодинну ставку (грн/год): ");
                    double hourlyRate = Double.parseDouble(scanner.nextLine().trim());
                    list.add(new Freelancer(name, position, salary, experience, department, flDuration, hourlyRate));
                    System.out.println("Об'єкт Freelancer успішно створено.");
                    break;
                default:
                    System.out.println("Помилка: невідомий тип об'єкта. Створення скасовано.");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Помилка введення: для числового поля необхідно вводити коректне число.");
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

    private static void displayAllEmployees(ArrayList<Employee> list) {
        if (list.isEmpty()) {
            System.out.println("Колекція порожня. Додайте об'єкти через меню створення.");
            return;
        }

        System.out.println();
        System.out.println("Список зареєстрованих об'єктів у колекції (всього: " + list.size() + "):");
        for (Employee emp : list) {
            System.out.println(emp);
        }
    }
}
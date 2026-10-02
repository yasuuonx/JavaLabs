package com.example;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Драйвер програми з інтеграцією збереження в базу даних через JDBC.
 */
public class Main {
    private static final String TEXT_FILE = "input.txt";

    public static void main(String[] args) {
        printHeader();

        String configPath = "db.properties";
        if (args.length > 0 && args[0] != null && !args[0].trim().isEmpty()) {
            configPath = args[0].trim();
        }
        System.out.println("Конфігураційний файл БД: " + configPath);

        DatabaseManager dbManager = new DatabaseManager(configPath);
        Company company = FileManager.loadCompanyFromFile(TEXT_FILE);
        System.out.println("Компанія: " + company.getName() + ", завантажено унікальних позицій: " + company.size());

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println();
            System.out.println("Головне меню (" + company.getName() + "):");
            System.out.println("1. Пошук об'єкта");
            System.out.println("2. Створити новий об'єкт (додати до компанії та БД)");
            System.out.println("3. Вивести інформацію про всі об'єкти");
            System.out.println("4. Завершити роботу програми");
            System.out.print("Оберіть дію: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleSearchMenu(scanner, company);
                    break;
                case "2":
                    handleCreateObjectMenu(scanner, company, dbManager);
                    break;
                case "3":
                    displayAllEmployees(company);
                    break;
                case "4":
                    FileManager.saveCompanyToFile(TEXT_FILE, company);
                    System.out.println("Дані компанії збережено у файл " + TEXT_FILE);
                    isRunning = false;
                    System.out.println("Роботу програми завершено.");
                    break;
                default:
                    System.out.println("Помилка: невідома опція меню. Оберіть пункт від 1 до 4.");
                    break;
            }
        }

        scanner.close();
    }

    private static void printHeader() {
        System.out.println("Практична робота №12");
        System.out.println("Тема: Збереження даних у базі даних (JDBC)");
        System.out.println("Виконав: студент Демченко Станіслав");
    }

    private static void handleSearchMenu(Scanner scanner, Company company) {
        if (company.size() == 0) {
            System.out.println("Колекція компанії порожня. Немає об'єктів для пошуку.");
            return;
        }

        System.out.println();
        System.out.println("Підменю пошуку:");
        System.out.println("1. Пошук за посадою");
        System.out.println("2. Пошук за діапазоном заробітної плати");
        System.out.println("3. Пошук за відділом компанії");
        System.out.println("0. Повернутися до головного меню");
        System.out.print("Ваш вибір: ");

        String searchChoice = scanner.nextLine().trim();

        switch (searchChoice) {
            case "1":
                System.out.print("Введіть посаду (або частину назви): ");
                String targetPosition = scanner.nextLine().trim();
                ArrayList<Employee> positionResults = company.searchByPosition(targetPosition);
                displaySearchResults(positionResults);
                break;
            case "2":
                try {
                    System.out.print("Введіть мінімальну зарплату: ");
                    double minSalary = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Введіть максимальну зарплату: ");
                    double maxSalary = Double.parseDouble(scanner.nextLine().trim());
                    if (minSalary > maxSalary) {
                        System.out.println("Помилка: мінімальна зарплата не може перевищувати максимальну.");
                        return;
                    }
                    ArrayList<Employee> salaryResults = company.searchBySalaryRange(minSalary, maxSalary);
                    displaySearchResults(salaryResults);
                } catch (NumberFormatException e) {
                    System.out.println("Помилка введення: очікується числове значення.");
                }
                break;
            case "3":
                try {
                    Department department = chooseDepartment(scanner);
                    ArrayList<Employee> departmentResults = company.searchByDepartment(department);
                    displaySearchResults(departmentResults);
                } catch (IllegalArgumentException e) {
                    System.out.println("Помилка: " + e.getMessage());
                }
                break;
            case "0":
                System.out.println("Повернення до головного меню.");
                break;
            default:
                System.out.println("Помилка: невідомий критерій пошуку.");
                break;
        }
    }

    private static void displaySearchResults(ArrayList<Employee> results) {
        if (results.isEmpty()) {
            System.out.println("Результат: жоден об'єкт не відповідає умовам пошуку.");
            return;
        }
        System.out.println();
        System.out.println("Знайдено об'єктів (" + results.size() + "):");
        for (Employee emp : results) {
            System.out.println(emp);
        }
    }

    private static void handleCreateObjectMenu(Scanner scanner, Company company, DatabaseManager dbManager) {
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

            System.out.print("Введіть кількість таких штатних позицій: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());

            Employee createdEmployee = null;

            switch (typeChoice) {
                case "1":
                    createdEmployee = new Employee(name, position, salary, experience, department);
                    break;
                case "2":
                    System.out.print("Введіть розмір річного бонусу: ");
                    double bonus = Double.parseDouble(scanner.nextLine().trim());
                    createdEmployee = new FullTimeEmployee(name, position, salary, experience, department, bonus);
                    break;
                case "3":
                    System.out.print("Введіть тривалість контракту (місяців): ");
                    int duration = Integer.parseInt(scanner.nextLine().trim());
                    createdEmployee = new ContractEmployee(name, position, salary, experience, department, duration);
                    break;
                case "4":
                    System.out.print("Введіть розмір річного бонусу: ");
                    double mgrBonus = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Введіть кількість підлеглих у команді: ");
                    int teamSize = Integer.parseInt(scanner.nextLine().trim());
                    createdEmployee = new Manager(name, position, salary, experience, department, mgrBonus, teamSize);
                    break;
                case "5":
                    System.out.print("Введіть тривалість контракту (місяців): ");
                    int flDuration = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Введіть погодинну ставку (грн/год): ");
                    double hourlyRate = Double.parseDouble(scanner.nextLine().trim());
                    createdEmployee = new Freelancer(name, position, salary, experience, department, flDuration, hourlyRate);
                    break;
                default:
                    System.out.println("Помилка: невідомий тип об'єкта. Створення скасовано.");
                    return;
            }

            company.addNewEmployee(createdEmployee, quantity);
            System.out.println("Об'єкт успішно додано до колекції компанії.");

            dbManager.saveEmployee(createdEmployee);

        } catch (NumberFormatException e) {
            System.out.println("Помилка введення: для числових даних введіть коректне число.");
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

    private static void displayAllEmployees(Company company) {
        if (company.size() == 0) {
            System.out.println("У компанії " + company.getName() + " немає співробітників.");
            return;
        }

        System.out.println();
        System.out.println("Список співробітників компанії " + company.getName() + " (всього позицій: " + company.size() + "):");
        for (int i = 0; i < company.size(); i++) {
            Employee emp = company.getEmployees().get(i);
            int qty = company.getQuantity(i);
            System.out.println(emp + " | Кількість: " + qty);
        }
    }
}
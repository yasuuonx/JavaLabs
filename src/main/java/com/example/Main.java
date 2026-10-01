package com.example;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Драйвер програми з підтримкою пошуку об'єктів за критеріями,
 * інтерактивним меню, завантаженням та збереженням у файли.
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
            System.out.println("1. Пошук об'єкта");
            System.out.println("2. Створити новий об'єкт");
            System.out.println("3. Вивести інформацію про всі об'єкти");
            System.out.println("4. Завершити роботу програми");
            System.out.print("Оберіть дію: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleSearchMenu(scanner, employees);
                    break;
                case "2":
                    handleCreateObjectMenu(scanner, employees);
                    break;
                case "3":
                    displayAllEmployees(employees);
                    break;
                case "4":
                    FileManager.saveToTextFile(TEXT_FILE, employees);
                    FileManager.saveToJsonFile(JSON_FILE, employees);
                    System.out.println("Дані успішно збережено у файли " + TEXT_FILE + " та " + JSON_FILE);
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
        System.out.println("Практична робота №10");
        System.out.println("Тема: Пошук у колекціях");
        System.out.println("Виконав: студент Демченко Станіслав");
    }

    /**
     * Меню вибору критерію пошуку об'єктів.
     *
     * @param scanner   сканер консолі
     * @param employees оригінальна незмінна колекція співробітників
     */
    private static void handleSearchMenu(Scanner scanner, ArrayList<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("Колекція порожня. Немає об'єктів для пошуку.");
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
                ArrayList<Employee> positionResults = searchByPosition(employees, targetPosition);
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
                    ArrayList<Employee> salaryResults = searchBySalaryRange(employees, minSalary, maxSalary);
                    displaySearchResults(salaryResults);
                } catch (NumberFormatException e) {
                    System.out.println("Помилка введення: очікується числове значення.");
                }
                break;
            case "3":
                try {
                    Department department = chooseDepartment(scanner);
                    ArrayList<Employee> departmentResults = searchByDepartment(employees, department);
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

    /**
     * Критерій 1: Пошук за посадою (без урахування регістру).
     *
     * @param sourceList джерело даних (не змінюється)
     * @param position   шукана посада
     * @return список знайдених співробітників
     */
    public static ArrayList<Employee> searchByPosition(ArrayList<Employee> sourceList, String position) {
        ArrayList<Employee> result = new ArrayList<Employee>();
        if (position == null || position.trim().isEmpty()) {
            return result;
        }
        String normalizedTarget = position.trim().toLowerCase();
        for (Employee emp : sourceList) {
            if (emp.getPosition().toLowerCase().contains(normalizedTarget)) {
                result.add(emp);
            }
        }
        return result;
    }

    /**
     * Критерій 2: Пошук за діапазоном заробітної плати.
     *
     * @param sourceList джерело даних (не змінюється)
     * @param minSalary  нижня межа зарплати
     * @param maxSalary  верхня межа зарплати
     * @return список знайдених співробітників
     */
    public static ArrayList<Employee> searchBySalaryRange(ArrayList<Employee> sourceList, double minSalary, double maxSalary) {
        ArrayList<Employee> result = new ArrayList<Employee>();
        for (Employee emp : sourceList) {
            if (emp.getSalary() >= minSalary && emp.getSalary() <= maxSalary) {
                result.add(emp);
            }
        }
        return result;
    }

    /**
     * Критерій 3: Пошук за відділом.
     *
     * @param sourceList джерело даних (не змінюється)
     * @param department шуканий відділ
     * @return список знайдених співробітників
     */
    public static ArrayList<Employee> searchByDepartment(ArrayList<Employee> sourceList, Department department) {
        ArrayList<Employee> result = new ArrayList<Employee>();
        if (department == null) {
            return result;
        }
        for (Employee emp : sourceList) {
            if (emp.getDepartment() == department) {
                result.add(emp);
            }
        }
        return result;
    }

    /**
     * Виводить результати пошуку або повідомляє про їх відсутність.
     *
     * @param results список знайдених об'єктів
     */
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
package com.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.UUID;

/**
 * Консольний інтерфейс з реалізацією модифікації (update) та видалення (delete) об'єктів.
 */
public class Main {
    private static final String TEXT_FILE = "input.txt";

    public static void main(String[] args) {
        printHeader();

        Company company = FileManager.loadCompanyFromFile(TEXT_FILE);
        System.out.println("Компанія: " + company.getName() + ", завантажено унікальних позицій: " + company.size());

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println();
            System.out.println("Головне меню (" + company.getName() + "):");
            System.out.println("1. Пошук об'єкта");
            System.out.println("2. Створити новий об'єкт (додати до компанії)");
            System.out.println("3. Модифікувати співробітника");
            System.out.println("4. Видалити співробітника");
            System.out.println("5. Вивести інформацію про всі об'єкти");
            System.out.println("6. Вивести відсортовану інформацію про всі об'єкти");
            System.out.println("7. Завершити роботу програми");
            System.out.print("Оберіть дію: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleSearchMenu(scanner, company);
                    break;
                case "2":
                    handleCreateObjectMenu(scanner, company);
                    break;
                case "3":
                    handleUpdateEmployee(scanner, company);
                    break;
                case "4":
                    handleDeleteEmployee(scanner, company);
                    break;
                case "5":
                    displayAllEmployees(company);
                    break;
                case "6":
                    handleSortMenu(scanner, company);
                    break;
                case "7":
                    FileManager.saveCompanyToFile(TEXT_FILE, company);
                    System.out.println("Дані компанії збережено у файл " + TEXT_FILE);
                    isRunning = false;
                    System.out.println("Роботу програми завершено.");
                    break;
                default:
                    System.out.println("Помилка: невідома опція меню. Оберіть пункт від 1 до 7.");
                    break;
            }
        }

        scanner.close();
    }

    private static void printHeader() {
        System.out.println("Лабораторна робота №17");
        System.out.println("Тема: Модифікація та видалення елементів у колекціях");
        System.out.println("Виконав: студент Демченко Станіслав");
    }

    private static void handleUpdateEmployee(Scanner scanner, Company company) {
        if (company.size() == 0) {
            System.out.println("Колекція порожня. Немає об'єктів для модифікації.");
            return;
        }

        displayAllEmployees(company);
        System.out.print("Введіть номер співробітника для модифікації (1-" + company.size() + ") або 0 для скасування: ");

        int index;
        try {
            index = Integer.parseInt(scanner.nextLine().trim()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Помилка: введіть коректне число.");
            return;
        }

        if (index == -1) {
            System.out.println("Модифікацію скасовано.");
            return;
        }

        if (index < 0 || index >= company.size()) {
            System.out.println("Помилка: співробітника з таким номером не існує.");
            return;
        }

        Employee target = company.getEmployees().get(index);
        System.out.println("Обрано для модифікації: " + target);
        System.out.println("Оберіть атрибут для зміни:");
        System.out.println("1. Ім'я");
        System.out.println("2. Посада");
        System.out.println("3. Заробітна плата");
        System.out.println("4. Стаж роботи");
        System.out.println("5. Відділ");
        System.out.println("6. Специфічний атрибут (бонус / контракт / ставка)");
        System.out.println("0. Скасувати");
        System.out.print("Ваш вибір: ");

        String attrChoice = scanner.nextLine().trim();
        if ("0".equals(attrChoice)) {
            System.out.println("Зміни скасовано.");
            return;
        }

        try {
            Employee updated = copyEmployee(target);

            switch (attrChoice) {
                case "1":
                    System.out.print("Введіть нове ім'я: ");
                    String newName = scanner.nextLine();
                    updated.setName(newName);
                    break;
                case "2":
                    System.out.print("Введіть нову посаду: ");
                    String newPosition = scanner.nextLine();
                    updated.setPosition(newPosition);
                    break;
                case "3":
                    System.out.print("Введіть нову заробітну плату: ");
                    double newSalary = Double.parseDouble(scanner.nextLine().trim());
                    updated.setSalary(newSalary);
                    break;
                case "4":
                    System.out.print("Введіть новий стаж роботи (роки): ");
                    int newExp = Integer.parseInt(scanner.nextLine().trim());
                    updated.setExperienceYears(newExp);
                    break;
                case "5":
                    Department newDept = chooseDepartment(scanner);
                    updated.setDepartment(newDept);
                    break;
                case "6":
                    updateSpecificAttribute(scanner, updated);
                    break;
                default:
                    System.out.println("Помилка: невідомий атрибут.");
                    return;
            }

            boolean isUpdated = company.update(target, updated);
            if (isUpdated) {
                System.out.println("Об'єкт успішно модифіковано:");
                System.out.println(updated);
            } else {
                System.out.println("Помилка: не вдалося оновити об'єкт у колекції.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Помилка введення: очікується числове значення.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка валідації: " + e.getMessage());
        }
    }

    private static Employee copyEmployee(Employee source) {
        if (source instanceof Manager) {
            Manager m = (Manager) source;
            return new Manager(m.getUuid(), m.getName(), m.getPosition(), m.getSalary(), m.getExperienceYears(), m.getDepartment(), m.getAnnualBonus(), m.getTeamSize());
        } else if (source instanceof Freelancer) {
            Freelancer f = (Freelancer) source;
            return new Freelancer(f.getUuid(), f.getName(), f.getPosition(), f.getSalary(), f.getExperienceYears(), f.getDepartment(), f.getContractDurationMonths(), f.getHourlyRate());
        } else if (source instanceof FullTimeEmployee) {
            FullTimeEmployee ft = (FullTimeEmployee) source;
            return new FullTimeEmployee(ft.getUuid(), ft.getName(), ft.getPosition(), ft.getSalary(), ft.getExperienceYears(), ft.getDepartment(), ft.getAnnualBonus());
        } else if (source instanceof ContractEmployee) {
            ContractEmployee c = (ContractEmployee) source;
            return new ContractEmployee(c.getUuid(), c.getName(), c.getPosition(), c.getSalary(), c.getExperienceYears(), c.getDepartment(), c.getContractDurationMonths());
        }
        throw new IllegalStateException("Невідомий підтип співробітника");
    }

    private static void updateSpecificAttribute(Scanner scanner, Employee emp) {
        if (emp instanceof Manager) {
            Manager m = (Manager) emp;
            System.out.println("1. Змінити річний бонус");
            System.out.println("2. Змінити кількість підлеглих");
            System.out.print("Вибір: ");
            String subChoice = scanner.nextLine().trim();
            if ("1".equals(subChoice)) {
                System.out.print("Новий бонус: ");
                m.setAnnualBonus(Double.parseDouble(scanner.nextLine().trim()));
            } else if ("2".equals(subChoice)) {
                System.out.print("Нова кількість підлеглих: ");
                m.setTeamSize(Integer.parseInt(scanner.nextLine().trim()));
            }
        } else if (emp instanceof Freelancer) {
            Freelancer f = (Freelancer) emp;
            System.out.println("1. Змінити тривалість контракту (міс)");
            System.out.println("2. Змінити погодинну ставку");
            System.out.print("Вибір: ");
            String subChoice = scanner.nextLine().trim();
            if ("1".equals(subChoice)) {
                System.out.print("Нова тривалість контракту: ");
                f.setContractDurationMonths(Integer.parseInt(scanner.nextLine().trim()));
            } else if ("2".equals(subChoice)) {
                System.out.print("Нова погодинна ставка: ");
                f.setHourlyRate(Double.parseDouble(scanner.nextLine().trim()));
            }
        } else if (emp instanceof FullTimeEmployee) {
            FullTimeEmployee ft = (FullTimeEmployee) emp;
            System.out.print("Новий річний бонус: ");
            ft.setAnnualBonus(Double.parseDouble(scanner.nextLine().trim()));
        } else if (emp instanceof ContractEmployee) {
            ContractEmployee c = (ContractEmployee) emp;
            System.out.print("Нова тривалість контракту (міс): ");
            c.setContractDurationMonths(Integer.parseInt(scanner.nextLine().trim()));
        }
    }

    private static void handleDeleteEmployee(Scanner scanner, Company company) {
        if (company.size() == 0) {
            System.out.println("Колекція порожня. Немає об'єктів для видалення.");
            return;
        }

        displayAllEmployees(company);
        System.out.print("Введіть номер співробітника для видалення (1-" + company.size() + ") або 0 для скасування: ");

        int index;
        try {
            index = Integer.parseInt(scanner.nextLine().trim()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Помилка: введіть коректне число.");
            return;
        }

        if (index == -1) {
            System.out.println("Видалення скасовано.");
            return;
        }

        if (index < 0 || index >= company.size()) {
            System.out.println("Помилка: співробітника з таким номером не існує.");
            return;
        }

        Employee target = company.getEmployees().get(index);
        System.out.println("Ви обрали: " + target.toShortString());
        System.out.print("Підтверджуєте видалення? (1 - Так, 0 - Ні): ");
        String confirm = scanner.nextLine().trim();

        if ("1".equals(confirm)) {
            boolean isDeleted = company.delete(target);
            if (isDeleted) {
                System.out.println("Співробітника успішно видалено з компанії.");
            } else {
                System.out.println("Помилка: об'єкт не знайдено для видалення.");
            }
        } else {
            System.out.println("Видалення скасовано користувачем.");
        }
    }

    private static void handleSortMenu(Scanner scanner, Company company) {
        if (company.size() == 0) {
            System.out.println("Колекція компанії порожня. Немає об'єктів для сортування.");
            return;
        }

        System.out.println();
        System.out.println("Оберіть критерій сортування:");
        System.out.println("1. Сортувати за заробітною платою (за спаданням)");
        System.out.println("2. Сортувати за стажем роботи (за зростанням)");
        System.out.println("3. Сортувати за відділом компанії (за алфавітом)");
        System.out.println("0. Повернутися в головне меню");
        System.out.print("Ваш вибір: ");

        String sortChoice = scanner.nextLine().trim();

        Comparator<Employee> comparator = null;
        String criteriaTitle = "";

        switch (sortChoice) {
            case "1":
                criteriaTitle = "за заробітною платою (від вищої до нижчої)";
                comparator = (o1, o2) -> Double.compare(o2.getSalary(), o1.getSalary());
                break;
            case "2":
                criteriaTitle = "за стажем роботи (від меншого до більшого)";
                comparator = (o1, o2) -> Integer.compare(o1.getExperienceYears(), o2.getExperienceYears());
                break;
            case "3":
                criteriaTitle = "за назвою відділу компанії (за алфавітом)";
                comparator = (o1, o2) -> o1.getDepartment().getTitle().compareToIgnoreCase(o2.getDepartment().getTitle());
                break;
            case "0":
                System.out.println("Повернення до головного меню без сортування.");
                return;
            default:
                System.out.println("Помилка: невідомий критерій сортування.");
                return;
        }

        ArrayList<Employee> sortedList = company.getSortedEmployees(comparator);
        System.out.println();
        System.out.println("Відсортований список співробітників " + criteriaTitle + " (всього: " + sortedList.size() + "):");
        for (Employee emp : sortedList) {
            System.out.println(emp);
        }
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
        System.out.println("4. Пошук за UUID");
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
            case "4":
                System.out.print("Введіть UUID: ");
                String uuidString = scanner.nextLine().trim();
                try {
                    UUID searchUuid = UUID.fromString(uuidString);
                    Employee found = company.findByUuid(searchUuid);
                    if (found != null) {
                        System.out.println("Об'єкт знайдено:");
                        System.out.println(found);
                    } else {
                        System.out.println("Об'єкт з UUID '" + uuidString + "' не знайдено.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Помилка: некоректний формат UUID.");
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

    private static void handleCreateObjectMenu(Scanner scanner, Company company) {
        System.out.println();
        System.out.println("Оберіть підклас об'єкта для створення:");
        System.out.println("1. Штатний співробітник (FullTimeEmployee)");
        System.out.println("2. Контрактний співробітник (ContractEmployee)");
        System.out.println("3. Менеджер (Manager)");
        System.out.println("4. Фрилансер (Freelancer)");
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
                    System.out.print("Введіть розмір річного бонусу: ");
                    double bonus = Double.parseDouble(scanner.nextLine().trim());
                    createdEmployee = new FullTimeEmployee(name, position, salary, experience, department, bonus);
                    break;
                case "2":
                    System.out.print("Введіть тривалість контракту (місяців): ");
                    int duration = Integer.parseInt(scanner.nextLine().trim());
                    createdEmployee = new ContractEmployee(name, position, salary, experience, department, duration);
                    break;
                case "3":
                    System.out.print("Введіть розмір річного бонусу: ");
                    double mgrBonus = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Введіть кількість підлеглих у команді: ");
                    int teamSize = Integer.parseInt(scanner.nextLine().trim());
                    createdEmployee = new Manager(name, position, salary, experience, department, mgrBonus, teamSize);
                    break;
                case "4":
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
            System.out.println("Об'єкт успішно додано. Його UUID: " + createdEmployee.getUuid());
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
            System.out.println((i + 1) + ". " + emp + " | Кількість: " + qty);
        }
    }
}
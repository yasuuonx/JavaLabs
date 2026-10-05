package com.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.UUID;

/**
 * Консольний драйвер із коректною обробкою власних винятків.
 */
public class Main {
    private static final String TEXT_FILE = "input.txt";

    public static void main(String[] args) {
        printHeader();

        Company company = FileManager.loadCompanyFromFile(TEXT_FILE);
        System.out.println("Компанія: " + company.getName() + ", завантажено позицій: " + company.size());

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

            try {
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
            } catch (InvalidFieldValueException e) {
                System.out.println("[Помилка валідації даних]: " + e.getMessage());
            } catch (ObjectNotFoundException e) {
                System.out.println("[Помилка пошуку об'єкта]: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("[Непередбачена помилка]: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void printHeader() {
        System.out.println("Лабораторна робота №18");
        System.out.println("Тема: Custom exceptions + автотести для перевірки винятків");
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
            throw new InvalidFieldValueException("Очікується ціле число для вибору позиції");
        }

        if (index == -1) {
            System.out.println("Модифікацію скасовано.");
            return;
        }

        if (index < 0 || index >= company.size()) {
            throw new ObjectNotFoundException("Співробітника за вказаним номером " + (index + 1) + " не існує");
        }

        Employee target = company.getEmployees().get(index);
        System.out.println("Обрано для модифікації: " + target);
        System.out.println("Оберіть атрибут для зміни:");
        System.out.println("1. Ім'я");
        System.out.println("2. Посада");
        System.out.println("3. Заробітна плата");
        System.out.println("4. Стаж роботи");
        System.out.println("5. Відділ");
        System.out.println("6. Специфічний атрибут");
        System.out.println("0. Скасувати");
        System.out.print("Ваш вибір: ");

        String attrChoice = scanner.nextLine().trim();
        if ("0".equals(attrChoice)) {
            System.out.println("Зміни скасовано.");
            return;
        }

        Employee updated = copyEmployee(target);

        switch (attrChoice) {
            case "1":
                System.out.print("Введіть нове ім'я: ");
                updated.setName(scanner.nextLine());
                break;
            case "2":
                System.out.print("Введіть нову посаду: ");
                updated.setPosition(scanner.nextLine());
                break;
            case "3":
                System.out.print("Введіть нову заробітну плату: ");
                try {
                    updated.setSalary(Double.parseDouble(scanner.nextLine().trim()));
                } catch (NumberFormatException e) {
                    throw new InvalidFieldValueException("Заробітна плата повинна бути числом");
                }
                break;
            case "4":
                System.out.print("Введіть новий стаж роботи (роки): ");
                try {
                    updated.setExperienceYears(Integer.parseInt(scanner.nextLine().trim()));
                } catch (NumberFormatException e) {
                    throw new InvalidFieldValueException("Стаж повинен бути цілим числом");
                }
                break;
            case "5":
                Department newDept = chooseDepartment(scanner);
                updated.setDepartment(newDept);
                break;
            case "6":
                updateSpecificAttribute(scanner, updated);
                break;
            default:
                throw new InvalidFieldValueException("Обрано неіснуючий атрибут");
        }

        company.update(target, updated);
        System.out.println("Об'єкт успішно модифіковано:");
        System.out.println(updated);
    }

    private static Employee copyEmployee(Employee source) {
        if (source instanceof Manager) {
            Manager m = (Manager) source;
            return new Manager(m.getUuid(), m.getName(), m.getPosition(), m.getSalary(), m.getExperienceYears(), m.getDepartment(), m.getAnnualBonus(), m.getTeamSize());
        } else if (source instanceof Freelancer) {
            Freelancer f = (Freelancer) source;
            return new Freelancer(f.getUuid(), f.getName(), f.getPosition(), f.getSalary(), f.getExperienceYears(), f.getDepartment(), f.getHourlyRate(), f.getContractDurationMonths());
        } else if (source instanceof FullTimeEmployee) {
            FullTimeEmployee ft = (FullTimeEmployee) source;
            return new FullTimeEmployee(ft.getUuid(), ft.getName(), ft.getPosition(), ft.getSalary(), ft.getExperienceYears(), ft.getDepartment(), ft.getAnnualBonus());
        } else if (source instanceof ContractEmployee) {
            ContractEmployee c = (ContractEmployee) source;
            return new ContractEmployee(c.getUuid(), c.getName(), c.getPosition(), c.getSalary(), c.getExperienceYears(), c.getDepartment(), c.getContractDurationMonths());
        }
        throw new InvalidFieldValueException("Невідомий тип співробітника");
    }

    private static void updateSpecificAttribute(Scanner scanner, Employee emp) {
        try {
            if (emp instanceof Manager) {
                Manager m = (Manager) emp;
                System.out.println("1. Змінити річний бонус");
                System.out.println("2. Змінити кількість підлеглих");
                System.out.print("Вибір: ");
                String sub = scanner.nextLine().trim();
                if ("1".equals(sub)) {
                    System.out.print("Новий бонус: ");
                    m.setAnnualBonus(Double.parseDouble(scanner.nextLine().trim()));
                } else if ("2".equals(sub)) {
                    System.out.print("Нова кількість підлеглих: ");
                    m.setTeamSize(Integer.parseInt(scanner.nextLine().trim()));
                }
            } else if (emp instanceof Freelancer) {
                Freelancer f = (Freelancer) emp;
                System.out.println("1. Змінити погодинну ставку");
                System.out.println("2. Змінити тривалість контракту (міс)");
                System.out.print("Вибір: ");
                String sub = scanner.nextLine().trim();
                if ("1".equals(sub)) {
                    System.out.print("Нова погодинна ставка: ");
                    f.setHourlyRate(Double.parseDouble(scanner.nextLine().trim()));
                } else if ("2".equals(sub)) {
                    System.out.print("Нова тривалість контракту: ");
                    f.setContractDurationMonths(Integer.parseInt(scanner.nextLine().trim()));
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
        } catch (NumberFormatException e) {
            throw new InvalidFieldValueException("Некоректний формат числового значення специфічного атрибута");
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
            throw new InvalidFieldValueException("Очікується ціле число для вибору");
        }

        if (index == -1) {
            System.out.println("Видалення скасовано.");
            return;
        }

        if (index < 0 || index >= company.size()) {
            throw new ObjectNotFoundException("Співробітника за номером " + (index + 1) + " не існує");
        }

        Employee target = company.getEmployees().get(index);
        System.out.println("Ви обрали: " + target.toShortString());
        System.out.print("Підтверджуєте видалення? (1 - Так, 0 - Ні): ");
        String confirm = scanner.nextLine().trim();

        if ("1".equals(confirm)) {
            company.delete(target);
            System.out.println("Співробітника успішно видалено з компанії.");
        } else {
            System.out.println("Видалення скасовано користувачем.");
        }
    }

    private static void handleSortMenu(Scanner scanner, Company company) {
        if (company.size() == 0) {
            System.out.println("Колекція порожня. Немає об'єктів для сортування.");
            return;
        }

        System.out.println();
        System.out.println("Оберіть критерій сортування:");
        System.out.println("1. За заробітною платою (за спаданням)");
        System.out.println("2. За стажем роботи (за зростанням)");
        System.out.println("3. За назвою відділу (за алфавітом)");
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
                return;
            default:
                throw new InvalidFieldValueException("Обрано невірний пункт сортування");
        }

        ArrayList<Employee> sortedList = company.getSortedEmployees(comparator);
        System.out.println();
        System.out.println("Відсортований список (" + sortedList.size() + "):");
        for (Employee emp : sortedList) {
            System.out.println(emp);
        }
    }

    private static void handleSearchMenu(Scanner scanner, Company company) {
        if (company.size() == 0) {
            System.out.println("Колекція порожня. Немає об'єктів для пошуку.");
            return;
        }

        System.out.println();
        System.out.println("Підменю пошуку:");
        System.out.println("1. Пошук за посадою");
        System.out.println("2. Пошук за діапазоном зарплати");
        System.out.println("3. Пошук за відділом");
        System.out.println("4. Пошук за UUID");
        System.out.println("0. Повернутися до головного меню");
        System.out.print("Ваш вибір: ");

        String searchChoice = scanner.nextLine().trim();

        switch (searchChoice) {
            case "1":
                System.out.print("Введіть посаду: ");
                displaySearchResults(company.searchByPosition(scanner.nextLine().trim()));
                break;
            case "2":
                try {
                    System.out.print("Мінімальна зарплата: ");
                    double min = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Максимальна зарплата: ");
                    double max = Double.parseDouble(scanner.nextLine().trim());
                    if (min > max) {
                        throw new InvalidFieldValueException("Мінімальна зарплата не може бути більшою за максимальну");
                    }
                    displaySearchResults(company.searchBySalaryRange(min, max));
                } catch (NumberFormatException e) {
                    throw new InvalidFieldValueException("Введіть коректні числа для діапазону зарплати");
                }
                break;
            case "3":
                Department department = chooseDepartment(scanner);
                displaySearchResults(company.searchByDepartment(department));
                break;
            case "4":
                System.out.print("Введіть UUID: ");
                try {
                    UUID uuid = UUID.fromString(scanner.nextLine().trim());
                    Employee found = company.findByUuid(uuid);
                    if (found != null) {
                        System.out.println("Знайдено: " + found);
                    } else {
                        throw new ObjectNotFoundException("Співробітника з таким UUID не знайдено");
                    }
                } catch (IllegalArgumentException e) {
                    throw new InvalidFieldValueException("Некоректний формат UUID");
                }
                break;
            case "0":
                break;
            default:
                throw new InvalidFieldValueException("Обрано неіснуючий критерій пошуку");
        }
    }

    private static void displaySearchResults(ArrayList<Employee> results) {
        if (results.isEmpty()) {
            System.out.println("Нічого не знайдено.");
            return;
        }
        System.out.println("Знайдено (" + results.size() + "):");
        for (Employee emp : results) {
            System.out.println(emp);
        }
    }

    private static void handleCreateObjectMenu(Scanner scanner, Company company) {
        System.out.println();
        System.out.println("Оберіть підклас об'єкта:");
        System.out.println("1. FullTimeEmployee");
        System.out.println("2. ContractEmployee");
        System.out.println("3. Manager");
        System.out.println("4. Freelancer");
        System.out.println("0. Скасувати");
        System.out.print("Ваш вибір: ");

        String typeChoice = scanner.nextLine().trim();
        if ("0".equals(typeChoice)) return;

        try {
            System.out.print("Ім'я: ");
            String name = scanner.nextLine();
            System.out.print("Посада: ");
            String position = scanner.nextLine();
            System.out.print("Зарплата: ");
            double salary = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Стаж (роки): ");
            int exp = Integer.parseInt(scanner.nextLine().trim());
            Department dept = chooseDepartment(scanner);
            System.out.print("Кількість позицій: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());

            Employee emp = null;
            switch (typeChoice) {
                case "1":
                    System.out.print("Річний бонус: ");
                    emp = new FullTimeEmployee(name, position, salary, exp, dept, Double.parseDouble(scanner.nextLine().trim()));
                    break;
                case "2":
                    System.out.print("Тривалість контракту (міс): ");
                    emp = new ContractEmployee(name, position, salary, exp, dept, Integer.parseInt(scanner.nextLine().trim()));
                    break;
                case "3":
                    System.out.print("Річний бонус: ");
                    double b = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Підлеглих: ");
                    int t = Integer.parseInt(scanner.nextLine().trim());
                    emp = new Manager(name, position, salary, exp, dept, b, t);
                    break;
                case "4":
                    System.out.print("Погодинна ставка: ");
                    double rate = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Тривалість контракту (міс): ");
                    int dur = Integer.parseInt(scanner.nextLine().trim());
                    emp = new Freelancer(name, position, salary, exp, dept, rate, dur);
                    break;
                default:
                    throw new InvalidFieldValueException("Обрано неіснуючий тип співробітника");
            }

            company.addNewEmployee(emp, qty);
            System.out.println("Додано об'єкт: " + emp.toShortString());
        } catch (NumberFormatException e) {
            throw new InvalidFieldValueException("Очікується числове значення при створенні об'єкта");
        }
    }

    private static Department chooseDepartment(Scanner scanner) {
        System.out.println("Відділ (1 - IT, 2 - HR, 3 - FINANCE, 4 - MARKETING): ");
        String c = scanner.nextLine().trim();
        switch (c) {
            case "1": return Department.IT;
            case "2": return Department.HR;
            case "3": return Department.FINANCE;
            case "4": return Department.MARKETING;
            default: throw new InvalidFieldValueException("Некоректний номер відділу");
        }
    }

    private static void displayAllEmployees(Company company) {
        if (company.size() == 0) {
            System.out.println("Колекція порожня.");
            return;
        }
        System.out.println();
        System.out.println("Список співробітників (" + company.size() + "):");
        for (int i = 0; i < company.size(); i++) {
            System.out.println((i + 1) + ". " + company.getEmployees().get(i) + " | Кількість: " + company.getQuantity(i));
        }
    }
}
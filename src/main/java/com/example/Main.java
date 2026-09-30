package com.example;

import java.util.List;
import java.util.Scanner;

/**
 * Драйвер програми з консольним інтерфейсом.
 */
public class Main {

    public static void main(String[] args) {
        printHeader();

        Scanner scanner = new Scanner(System.in);
        Company company = new Company("TechCorp Solutions");
        boolean isRunning = true;

        while (isRunning) {
            System.out.println();
            System.out.println("Головне меню:");
            System.out.println("1. Створити нового співробітника");
            System.out.println("2. Створити копію останнього співробітника (конструктор копіювання)");
            System.out.println("3. Вивести інформацію про всіх співробітників");
            System.out.println("4. Показати загальну кількість створених об'єктів (статичне поле)");
            System.out.println("5. Завершити роботу");
            System.out.print("Оберіть пункт меню: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createNewEmployee(scanner, company);
                    break;
                case "2":
                    copyLastEmployee(company);
                    break;
                case "3":
                    displayEmployees(company);
                    break;
                case "4":
                    System.out.println("Всього екземплярів Employee створено в пам'яті: " + Employee.getTotalEmployeesCreated());
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
        System.out.println("Практична робота №6");
        System.out.println("Тема: Статичні члени, агрегація, перерахування (enum)");
        System.out.println("Виконав: студент Демченко Станіслав");
    }

    private static void createNewEmployee(Scanner scanner, Company company) {
        try {
            System.out.print("Введіть ім'я: ");
            String name = scanner.nextLine();

            System.out.print("Введіть посаду: ");
            String position = scanner.nextLine();

            System.out.print("Введіть заробітну плату: ");
            double salary = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Введіть стаж роботи (роки): ");
            int experience = Integer.parseInt(scanner.nextLine().trim());

            System.out.println("Оберіть відділ (1 - IT, 2 - HR, 3 - FINANCE, 4 - MARKETING): ");
            String deptChoice = scanner.nextLine().trim();
            Department department;
            switch (deptChoice) {
                case "1":
                    department = Department.IT;
                    break;
                case "2":
                    department = Department.HR;
                    break;
                case "3":
                    department = Department.FINANCE;
                    break;
                case "4":
                    department = Department.MARKETING;
                    break;
                default:
                    throw new IllegalArgumentException("Обрано неіснуючий відділ");
            }

            Employee employee = new Employee(name, position, salary, experience, department);
            company.addEmployee(employee);
            System.out.println("Співробітника успішно додано до компанії " + company.getCompanyName());
        } catch (NumberFormatException e) {
            System.out.println("Помилка введення: для числових даних введіть коректне число.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка валідації даних: " + e.getMessage());
        }
    }

    private static void copyLastEmployee(Company company) {
        List<Employee> list = company.getEmployees();
        if (list.isEmpty()) {
            System.out.println("Помилка: штат порожній, немає кого копіювати.");
            return;
        }

        Employee lastEmployee = list.get(list.size() - 1);
        Employee cloned = new Employee(lastEmployee);
        cloned.setName(lastEmployee.getName() + " (Копія)");
        company.addEmployee(cloned);
        System.out.println("Створено дублікат об'єкта через конструктор копіювання: " + cloned);
    }

    private static void displayEmployees(Company company) {
        List<Employee> list = company.getEmployees();
        if (list.isEmpty()) {
            System.out.println("У компанії " + company.getCompanyName() + " поки немає співробітників.");
            return;
        }

        System.out.println();
        System.out.println("Список співробітників компанії " + company.getCompanyName() + " (всього: " + list.size() + "):");
        for (Employee emp : list) {
            System.out.println(emp);
        }
    }
}
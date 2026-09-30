package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Employee> employees = new ArrayList<>();

        System.out.println("Система обліку співробітників");
        System.out.print("Введіть кількість співробітників: ");
        int count = Integer.parseInt(scanner.nextLine().trim());

        for (int i = 0; i < count; i++) {
            System.out.println("\nВведення даних для співробітника #" + (i + 1) + ":");

            System.out.print("Ім'я: ");
            String name = scanner.nextLine().trim();

            System.out.print("Посада: ");
            String position = scanner.nextLine().trim();

            System.out.print("Зарплата: ");
            double salary = Double.parseDouble(scanner.nextLine().trim());

            employees.add(new Employee(name, position, salary));
        }

        System.out.println("\nСписок зареєстрованих співробітників (" + employees.size() + "):");

        for (Employee emp : employees) {
            System.out.println(emp);
        }

        scanner.close();
    }
}
package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Клас для читання та збереження стану об'єкта Company у файл input.txt.
 */
public class FileManager {

    /**
     * Завантажує дані компанії та її співробітників із файлу input.txt.
     *
     * @param fileName назва файлу
     * @return об'єкт Company
     */
    public static Company loadCompanyFromFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return new Company("Default Company");
        }

        Company company = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");
                if ("COMPANY".equals(parts[0])) {
                    company = new Company(parts[1]);
                    continue;
                }

                if (company == null) {
                    company = new Company("Default Company");
                }

                try {
                    String type = parts[0];
                    String name = parts[1];
                    String position = parts[2];
                    double salary = Double.parseDouble(parts[3]);
                    int experience = Integer.parseInt(parts[4]);
                    Department department = Department.valueOf(parts[5]);

                    Employee employee = null;
                    int quantity = 1;

                    switch (type) {
                        case "EMPLOYEE":
                            employee = new Employee(name, position, salary, experience, department);
                            quantity = Integer.parseInt(parts[6]);
                            break;
                        case "FULL_TIME":
                            double bonus = Double.parseDouble(parts[6]);
                            quantity = Integer.parseInt(parts[7]);
                            employee = new FullTimeEmployee(name, position, salary, experience, department, bonus);
                            break;
                        case "CONTRACT":
                            int duration = Integer.parseInt(parts[6]);
                            quantity = Integer.parseInt(parts[7]);
                            employee = new ContractEmployee(name, position, salary, experience, department, duration);
                            break;
                        case "MANAGER":
                            double mgrBonus = Double.parseDouble(parts[6]);
                            int teamSize = Integer.parseInt(parts[7]);
                            quantity = Integer.parseInt(parts[8]);
                            employee = new Manager(name, position, salary, experience, department, mgrBonus, teamSize);
                            break;
                        case "FREELANCER":
                            int flDuration = Integer.parseInt(parts[6]);
                            double hourlyRate = Double.parseDouble(parts[7]);
                            quantity = Integer.parseInt(parts[8]);
                            employee = new Freelancer(name, position, salary, experience, department, flDuration, hourlyRate);
                            break;
                        default:
                            break;
                    }

                    if (employee != null) {
                        company.addNewEmployee(employee, quantity);
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (IOException ignored) {
        }

        if (company == null) {
            company = new Company("Default Company");
        }

        return company;
    }

    /**
     * Зберігає дані компанії та всіх співробітників у файл input.txt.
     *
     * @param fileName назва файлу
     * @param company  об'єкт компанії
     */
    public static void saveCompanyToFile(String fileName, Company company) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("COMPANY;" + company.getName());
            writer.newLine();

            for (int i = 0; i < company.size(); i++) {
                Employee emp = company.getEmployees().get(i);
                int qty = company.getQuantity(i);
                writer.write(emp.toFileString() + ";" + qty);
                writer.newLine();
            }
        } catch (IOException ignored) {
        }
    }
}
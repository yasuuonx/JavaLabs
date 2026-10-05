package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * Клас для збереження та завантаження стану Company у текстовий файл.
 */
public class FileManager {

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
                    int index = 1;
                    UUID uuid = null;

                    try {
                        uuid = UUID.fromString(parts[index]);
                        index++;
                    } catch (IllegalArgumentException e) {
                        uuid = UUID.randomUUID();
                    }

                    String name = parts[index++];
                    String position = parts[index++];
                    double salary = Double.parseDouble(parts[index++]);
                    int experience = Integer.parseInt(parts[index++]);
                    Department department = Department.valueOf(parts[index++]);

                    Employee employee = null;
                    int quantity = 1;

                    switch (type) {
                        case "EMPLOYEE":
                            quantity = Integer.parseInt(parts[index]);
                            employee = new FullTimeEmployee(uuid, name, position, salary, experience, department, 0.0);
                            break;
                        case "FULL_TIME":
                            double bonus = Double.parseDouble(parts[index++]);
                            quantity = Integer.parseInt(parts[index]);
                            employee = new FullTimeEmployee(uuid, name, position, salary, experience, department, bonus);
                            break;
                        case "CONTRACT":
                            int duration = Integer.parseInt(parts[index++]);
                            quantity = Integer.parseInt(parts[index]);
                            employee = new ContractEmployee(uuid, name, position, salary, experience, department, duration);
                            break;
                        case "MANAGER":
                            double mgrBonus = Double.parseDouble(parts[index++]);
                            int teamSize = Integer.parseInt(parts[index++]);
                            quantity = Integer.parseInt(parts[index]);
                            employee = new Manager(uuid, name, position, salary, experience, department, mgrBonus, teamSize);
                            break;
                        case "FREELANCER":
                            int flDuration = Integer.parseInt(parts[index++]);
                            double hourlyRate = Double.parseDouble(parts[index++]);
                            quantity = Integer.parseInt(parts[index]);
                            employee = new Freelancer(uuid, name, position, salary, experience, department, flDuration, hourlyRate);
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
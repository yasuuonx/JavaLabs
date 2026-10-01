package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

    public static ArrayList<Employee> loadFromTextFile(String fileName) {
        ArrayList<Employee> list = new ArrayList<Employee>();
        File file = new File(fileName);
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";");
                try {
                    String type = parts[0];
                    String name = parts[1];
                    String position = parts[2];
                    double salary = Double.parseDouble(parts[3]);
                    int experience = Integer.parseInt(parts[4]);
                    Department department = Department.valueOf(parts[5]);

                    switch (type) {
                        case "EMPLOYEE":
                            list.add(new Employee(name, position, salary, experience, department));
                            break;
                        case "FULL_TIME":
                            double bonus = Double.parseDouble(parts[6]);
                            list.add(new FullTimeEmployee(name, position, salary, experience, department, bonus));
                            break;
                        case "CONTRACT":
                            int duration = Integer.parseInt(parts[6]);
                            list.add(new ContractEmployee(name, position, salary, experience, department, duration));
                            break;
                        case "MANAGER":
                            double mgrBonus = Double.parseDouble(parts[6]);
                            int teamSize = Integer.parseInt(parts[7]);
                            list.add(new Manager(name, position, salary, experience, department, mgrBonus, teamSize));
                            break;
                        case "FREELANCER":
                            int flDuration = Integer.parseInt(parts[6]);
                            double hourlyRate = Double.parseDouble(parts[7]);
                            list.add(new Freelancer(name, position, salary, experience, department, flDuration, hourlyRate));
                            break;
                    }
                } catch (Exception ignored) {}
            }
        } catch (IOException ignored) {}

        return list;
    }

    public static void saveToTextFile(String fileName, ArrayList<Employee> list) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Employee employee : list) {
                writer.write(employee.toFileString());
                writer.newLine();
            }
        } catch (IOException ignored) {}
    }

    public static void saveToJsonFile(String fileName, ArrayList<Employee> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray array = new JsonArray();

        for (Employee emp : list) {
            JsonObject jsonObject = gson.toJsonTree(emp).getAsJsonObject();
            jsonObject.addProperty("classType", emp.getClass().getSimpleName());
            array.add(jsonObject);
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(array, writer);
        } catch (IOException ignored) {}
    }

    public static ArrayList<Employee> loadFromJsonFile(String fileName) {
        ArrayList<Employee> list = new ArrayList<Employee>();
        File file = new File(fileName);
        if (!file.exists()) return list;

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            JsonElement parsed = JsonParser.parseReader(reader);
            if (parsed == null || !parsed.isJsonArray()) return list;

            JsonArray array = parsed.getAsJsonArray();
            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                if (!obj.has("classType")) continue;
                String classType = obj.get("classType").getAsString();

                if ("FullTimeEmployee".equals(classType)) {
                    list.add(gson.fromJson(obj, FullTimeEmployee.class));
                } else if ("ContractEmployee".equals(classType)) {
                    list.add(gson.fromJson(obj, ContractEmployee.class));
                } else if ("Manager".equals(classType)) {
                    list.add(gson.fromJson(obj, Manager.class));
                } else if ("Freelancer".equals(classType)) {
                    list.add(gson.fromJson(obj, Freelancer.class));
                } else {
                    list.add(gson.fromJson(obj, Employee.class));
                }
            }
        } catch (Exception ignored) {}

        return list;
    }
}
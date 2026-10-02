package com.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

/**
 * Клас для підключення до бази даних та збереження об'єктів ієрархії Employee.
 */
public class DatabaseManager {
    private String url;
    private String user;
    private String password;

    /**
     * Конструктор зчитує конфігураційний файл properties.
     *
     * @param configPath шлях до файлу конфігурації
     */
    public DatabaseManager(String configPath) {
        loadProperties(configPath);
    }

    private void loadProperties(String configPath) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.load(fis);
            this.url = properties.getProperty("db.url");
            this.user = properties.getProperty("db.user");
            this.password = properties.getProperty("db.password");
            if (this.url == null || this.user == null || this.password == null) {
                System.out.println("Помилка конфігурації: відсутні обов'язкові параметри в " + configPath);
            }
        } catch (IOException e) {
            System.out.println("Не вдалося завантажити конфігураційний файл: " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Зберігає співробітника в базу даних через PreparedStatement.
     *
     * @param employee об'єкт співробітника
     */
    public void saveEmployee(Employee employee) {
        if (employee == null) {
            return;
        }

        String sql = "INSERT INTO employees (type, name, position, salary, experience_years, department, " +
                "annual_bonus, contract_duration_months, team_size, hourly_rate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String type = employee.getClass().getSimpleName();
            ps.setString(1, type);
            ps.setString(2, employee.getName());
            ps.setString(3, employee.getPosition());
            ps.setDouble(4, employee.getSalary());
            ps.setInt(5, employee.getExperienceYears());
            ps.setString(6, employee.getDepartment().name());

            if (employee instanceof Manager) {
                Manager mgr = (Manager) employee;
                ps.setDouble(7, mgr.getAnnualBonus());
                ps.setNull(8, Types.INTEGER);
                ps.setInt(9, mgr.getTeamSize());
                ps.setNull(10, Types.NUMERIC);
            } else if (employee instanceof FullTimeEmployee) {
                FullTimeEmployee ft = (FullTimeEmployee) employee;
                ps.setDouble(7, ft.getAnnualBonus());
                ps.setNull(8, Types.INTEGER);
                ps.setNull(9, Types.INTEGER);
                ps.setNull(10, Types.NUMERIC);
            } else if (employee instanceof Freelancer) {
                Freelancer fl = (Freelancer) employee;
                ps.setNull(7, Types.NUMERIC);
                ps.setInt(8, fl.getContractDurationMonths());
                ps.setNull(9, Types.INTEGER);
                ps.setDouble(10, fl.getHourlyRate());
            } else if (employee instanceof ContractEmployee) {
                ContractEmployee ct = (ContractEmployee) employee;
                ps.setNull(7, Types.NUMERIC);
                ps.setInt(8, ct.getContractDurationMonths());
                ps.setNull(9, Types.INTEGER);
                ps.setNull(10, Types.NUMERIC);
            } else {
                ps.setNull(7, Types.NUMERIC);
                ps.setNull(8, Types.INTEGER);
                ps.setNull(9, Types.INTEGER);
                ps.setNull(10, Types.NUMERIC);
            }

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Об'єкт [" + type + "] успішно збережено в базі даних (таблиця employees).");
            }
        } catch (SQLException e) {
            System.out.println("Помилка виконання SQL-запиту: " + e.getMessage());
        }
    }
}
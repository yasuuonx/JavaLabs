package com.example;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для перевірки абстрактного класу, агрегації Company та інтерфейсу Comparable.
 */
class EmployeeTest {

    @Test
    void shouldThrowExceptionWhenInvalidValueInSetter() {
        Employee employee = new FullTimeEmployee("Олексій", "Інженер", 30000.0, 2, Department.IT, 5000.0);

        assertThrows(IllegalArgumentException.class, () -> employee.setSalary(-1000.0));
        assertThrows(IllegalArgumentException.class, () -> employee.setExperienceYears(-3));
        assertThrows(IllegalArgumentException.class, () -> employee.setName("   "));
        assertThrows(IllegalArgumentException.class, () -> employee.setPosition(""));
        assertThrows(IllegalArgumentException.class, () -> employee.setDepartment(null));
    }

    @Test
    void shouldIncreaseQuantityWhenAddingExistingEmployee() {
        Company company = new Company("Test Corp");
        Employee emp1 = new FullTimeEmployee("Іван", "Стажер", 15000.0, 1, Department.HR, 1000.0);
        Employee emp2 = new FullTimeEmployee("Іван", "Стажер", 15000.0, 1, Department.HR, 1000.0);

        company.addNewEmployee(emp1, 2);
        company.addNewEmployee(emp2, 3);

        assertEquals(1, company.size());
        assertEquals(5, company.getQuantity(0));
    }

    @Test
    void shouldCorrectlySortEmployeesUsingComparable() {
        Company company = new Company("Test Corp");
        Employee empZ = new FullTimeEmployee("Ярослав", "Розробник", 60000.0, 4, Department.IT, 5000.0);
        Employee empA = new FullTimeEmployee("Андрій", "Тестувальник", 35000.0, 2, Department.IT, 2000.0);
        Employee empB = new Manager("Богдан", "Керівник", 85000.0, 7, Department.FINANCE, 15000.0, 6);

        company.addNewEmployee(empZ, 1);
        company.addNewEmployee(empA, 1);
        company.addNewEmployee(empB, 1);

        ArrayList<Employee> sorted = company.getSortedEmployees();

        assertEquals(3, sorted.size());
        assertEquals("Андрій", sorted.get(0).getName());
        assertEquals("Богдан", sorted.get(1).getName());
        assertEquals("Ярослав", sorted.get(2).getName());

        // Перевірка, що початковий список у компанії не змінив порядок
        assertEquals("Ярослав", company.getEmployees().get(0).getName());
    }

    @Test
    void shouldHandleSortingForEmptyAndSingleElementList() {
        Company emptyCompany = new Company("Empty Corp");
        ArrayList<Employee> emptySorted = emptyCompany.getSortedEmployees();
        assertTrue(emptySorted.isEmpty());

        Company singleCompany = new Company("Single Corp");
        singleCompany.addNewEmployee(new FullTimeEmployee("Василь", "Адмін", 40000.0, 3, Department.IT, 3000.0), 1);
        ArrayList<Employee> singleSorted = singleCompany.getSortedEmployees();
        assertEquals(1, singleSorted.size());
        assertEquals("Василь", singleSorted.get(0).getName());
    }

    @Test
    void shouldCorrectlySaveAndLoadCompanyFromFile() {
        String testFileName = "test_company_input_13.txt";
        Company originalCompany = new Company("AlphaSoft");
        originalCompany.addNewEmployee(new FullTimeEmployee("Тарас", "Аналітик", 35000.0, 2, Department.FINANCE, 4000.0), 3);
        originalCompany.addNewEmployee(new Manager("Олена", "Директор", 95000.0, 9, Department.IT, 20000.0, 12), 1);

        FileManager.saveCompanyToFile(testFileName, originalCompany);
        Company loadedCompany = FileManager.loadCompanyFromFile(testFileName);

        assertEquals("AlphaSoft", loadedCompany.getName());
        assertEquals(2, loadedCompany.size());
        assertEquals(3, loadedCompany.getQuantity(0));
        assertTrue(loadedCompany.getEmployees().get(1) instanceof Manager);

        new File(testFileName).delete();
    }
}
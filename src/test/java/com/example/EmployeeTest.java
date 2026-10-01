package com.example;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для перевірки агрегації Company, кількості об'єктів та пошуку.
 */
class EmployeeTest {

    @Test
    void shouldThrowExceptionWhenInvalidValueInSetter() {
        Employee employee = new Employee("Олексій", "Інженер", 30000.0, 2, Department.IT);

        assertThrows(IllegalArgumentException.class, () -> employee.setSalary(-1000.0));
        assertThrows(IllegalArgumentException.class, () -> employee.setExperienceYears(-3));
        assertThrows(IllegalArgumentException.class, () -> employee.setName("   "));
        assertThrows(IllegalArgumentException.class, () -> employee.setPosition(""));
        assertThrows(IllegalArgumentException.class, () -> employee.setDepartment(null));
    }

    @Test
    void shouldIncreaseQuantityWhenAddingExistingEmployee() {
        Company company = new Company("Test Corp");
        Employee emp1 = new Employee("Іван", "Стажер", 15000.0, 1, Department.HR);
        Employee emp2 = new Employee("Іван", "Стажер", 15000.0, 1, Department.HR);

        company.addNewEmployee(emp1, 2);
        company.addNewEmployee(emp2, 3);

        assertEquals(1, company.size());
        assertEquals(5, company.getQuantity(0));
    }

    @Test
    void shouldCorrectlySearchUsingCompanyMethods() {
        Company company = new Company("Test Corp");
        company.addNewEmployee(new Employee("Олександр", "Java Розробник", 45000.0, 3, Department.IT), 1);
        company.addNewEmployee(new FullTimeEmployee("Ірина", "HR Менеджер", 30000.0, 2, Department.HR, 5000.0), 2);
        company.addNewEmployee(new Manager("Богдан", "Senior Менеджер", 80000.0, 7, Department.IT, 15000.0, 8), 1);

        ArrayList<Employee> foundByPosition = company.searchByPosition("розробник");
        assertEquals(1, foundByPosition.size());

        ArrayList<Employee> foundBySalary = company.searchBySalaryRange(25000.0, 50000.0);
        assertEquals(2, foundBySalary.size());

        ArrayList<Employee> foundByDept = company.searchByDepartment(Department.IT);
        assertEquals(2, foundByDept.size());
    }

    @Test
    void shouldCorrectlySaveAndLoadCompanyFromFile() {
        String testFileName = "test_company_input.txt";
        Company originalCompany = new Company("AlphaSoft");
        originalCompany.addNewEmployee(new Employee("Тарас", "Аналітик", 35000.0, 2, Department.FINANCE), 3);
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
package com.example;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для перевірки Identifiable, UUID та пошуку.
 */
class EmployeeTest {

    @Test
    void shouldCreateEmployeeWithValidAutomaticUuid() {
        Employee emp = new FullTimeEmployee("Олексій", "Інженер", 30000.0, 2, Department.IT, 5000.0);
        assertNotNull(emp.getUuid());
        assertTrue(emp instanceof Identifiable);
        assertTrue(emp.toShortString().contains(emp.getUuid().toString()));
    }

    @Test
    void shouldFindEmployeeByUuid() {
        Company company = new Company("Test Corp");
        Employee emp1 = new FullTimeEmployee("Богдан", "Lead", 80000.0, 5, Department.IT, 10000.0);
        Employee emp2 = new ContractEmployee("Андрій", "Tester", 30000.0, 1, Department.HR, 12);

        company.addNewEmployee(emp1, 1);
        company.addNewEmployee(emp2, 1);

        Employee found = company.findByUuid(emp1.getUuid());
        assertNotNull(found);
        assertEquals("Богдан", found.getName());

        Employee notFound = company.findByUuid(UUID.randomUUID());
        assertNull(notFound);
    }

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
    void shouldCorrectlySaveAndLoadCompanyWithUuid() {
        String testFileName = "test_company_input_16.txt";
        Company originalCompany = new Company("AlphaSoft");
        FullTimeEmployee emp = new FullTimeEmployee("Тарас", "Аналітик", 35000.0, 2, Department.FINANCE, 4000.0);
        originalCompany.addNewEmployee(emp, 2);

        FileManager.saveCompanyToFile(testFileName, originalCompany);
        Company loadedCompany = FileManager.loadCompanyFromFile(testFileName);

        assertEquals("AlphaSoft", loadedCompany.getName());
        assertEquals(1, loadedCompany.size());
        assertEquals(emp.getUuid(), loadedCompany.getEmployees().get(0).getUuid());

        new File(testFileName).delete();
    }
}
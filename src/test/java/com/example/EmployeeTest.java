package com.example;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void shouldSuccessfullyUpdateExistingEmployee() {
        Company company = new Company("UpdateCorp");
        FullTimeEmployee original = new FullTimeEmployee("Олексій", "Junior Dev", 25000.0, 1, Department.IT, 3000.0);
        company.addNewEmployee(original, 1);

        FullTimeEmployee updated = new FullTimeEmployee("Олексій", "Middle Dev", 45000.0, 2, Department.IT, 5000.0);
        boolean result = company.update(original, updated);

        assertTrue(result);
        assertEquals(1, company.size());
        assertEquals("Middle Dev", company.getEmployees().get(0).getPosition());
        assertEquals(45000.0, company.getEmployees().get(0).getSalary());
    }

    @Test
    void shouldReturnFalseWhenUpdatingNonExistentEmployee() {
        Company company = new Company("UpdateCorp");
        FullTimeEmployee emp1 = new FullTimeEmployee("Олексій", "Dev", 30000.0, 1, Department.IT, 3000.0);
        FullTimeEmployee emp2 = new FullTimeEmployee("Іван", "Lead", 80000.0, 5, Department.IT, 10000.0);
        company.addNewEmployee(emp1, 1);

        boolean result = company.update(emp2, emp1);
        assertFalse(result);
        assertFalse(company.update(null, emp1));
    }

    @Test
    void shouldSuccessfullyDeleteExistingEmployee() {
        Company company = new Company("DeleteCorp");
        ContractEmployee emp = new ContractEmployee("Андрій", "Консультант", 35000.0, 3, Department.FINANCE, 6);
        company.addNewEmployee(emp, 2);

        assertEquals(1, company.size());
        boolean result = company.delete(emp);

        assertTrue(result);
        assertEquals(0, company.size());
        assertEquals(0, company.getQuantities().size());
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistentEmployee() {
        Company company = new Company("DeleteCorp");
        ContractEmployee emp1 = new ContractEmployee("Андрій", "Консультант", 35000.0, 3, Department.FINANCE, 6);
        ContractEmployee emp2 = new ContractEmployee("Сергій", "Дизайнер", 30000.0, 2, Department.MARKETING, 12);
        company.addNewEmployee(emp1, 1);

        boolean result = company.delete(emp2);
        assertFalse(result);
        assertFalse(company.delete(null));
        assertEquals(1, company.size());
    }

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
        company.addNewEmployee(emp1, 1);

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
        String testFileName = "test_company_input_17.txt";
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
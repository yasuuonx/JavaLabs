package com.example;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для перевірки бізнес-логіки та кидання власних винятків (ЛР18).
 */
class EmployeeTest {

    @Test
    void shouldThrowInvalidFieldValueExceptionWhenSettingNegativeSalary() {
        Employee employee = new FullTimeEmployee("Олексій", "Інженер", 30000.0, 2, Department.IT, 5000.0);

        InvalidFieldValueException exception = assertThrows(InvalidFieldValueException.class, () -> {
            employee.setSalary(-500.0);
        });

        assertEquals("Заробітна плата не може бути від'ємною", exception.getMessage());
    }

    @Test
    void shouldThrowInvalidFieldValueExceptionWhenSettingEmptyName() {
        Employee employee = new FullTimeEmployee("Олексій", "Інженер", 30000.0, 2, Department.IT, 5000.0);

        assertThrows(InvalidFieldValueException.class, () -> {
            employee.setName("   ");
        });
    }

    @Test
    void shouldThrowObjectNotFoundExceptionWhenDeletingNonExistingObject() {
        Company company = new Company("TestCompany");
        FullTimeEmployee existing = new FullTimeEmployee("Іван", "Lead", 60000.0, 4, Department.IT, 8000.0);
        FullTimeEmployee nonExisting = new FullTimeEmployee("Петро", "Junior", 20000.0, 1, Department.IT, 2000.0);

        company.addNewEmployee(existing, 1);

        assertThrows(ObjectNotFoundException.class, () -> {
            company.delete(nonExisting);
        });
    }

    @Test
    void shouldThrowObjectNotFoundExceptionWhenUpdatingNonExistingObject() {
        Company company = new Company("TestCompany");
        FullTimeEmployee existing = new FullTimeEmployee("Іван", "Lead", 60000.0, 4, Department.IT, 8000.0);
        FullTimeEmployee nonExisting = new FullTimeEmployee("Петро", "Junior", 20000.0, 1, Department.IT, 2000.0);

        company.addNewEmployee(existing, 1);

        assertThrows(ObjectNotFoundException.class, () -> {
            company.update(nonExisting, existing);
        });
    }

    @Test
    void shouldThrowObjectNotFoundExceptionWhenDeletingNull() {
        Company company = new Company("TestCompany");
        assertThrows(ObjectNotFoundException.class, () -> {
            company.delete(null);
        });
    }

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
    void shouldCorrectlySaveAndLoadCompanyWithUuid() {
        String testFileName = "test_company_input_18.txt";
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
package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для перевірки базового класу та похідних класів.
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
    void shouldThrowExceptionWhenInvalidFullTimeEmployeeBonus() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FullTimeEmployee("Олег", "Директор", 80000.0, 10, Department.FINANCE, -500.0);
        });
    }

    @Test
    void shouldThrowExceptionWhenInvalidContractDuration() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ContractEmployee("Денис", "Дизайнер", 35000.0, 1, Department.MARKETING, 0);
        });
    }

    @Test
    void shouldCorrectlyInstantiateDerivedClasses() {
        FullTimeEmployee fullTime = new FullTimeEmployee("Іван", "Тімлід", 90000.0, 7, Department.IT, 15000.0);
        ContractEmployee contract = new ContractEmployee("Марина", "HR", 28000.0, 2, Department.HR, 6);

        assertEquals(15000.0, fullTime.getAnnualBonus());
        assertEquals(6, contract.getContractDurationMonths());
        assertTrue(fullTime instanceof Employee);
        assertTrue(contract instanceof Employee);
    }
}
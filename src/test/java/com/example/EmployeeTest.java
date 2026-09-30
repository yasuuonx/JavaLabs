package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для перевірки валідації, копіювання та enum.
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
    void shouldThrowExceptionWhenInvalidConstructorData() {
        assertThrows(IllegalArgumentException.class, () -> new Employee("", "Менеджер", 25000.0, 1, Department.HR));
        assertThrows(IllegalArgumentException.class, () -> new Employee("Андрій", null, 25000.0, 1, Department.HR));
        assertThrows(IllegalArgumentException.class, () -> new Employee("Василь", "Водій", 0.0, 2, Department.FINANCE));
        assertThrows(IllegalArgumentException.class, () -> new Employee("Сергій", "Охоронець", 15000.0, -1, Department.MARKETING));
        assertThrows(IllegalArgumentException.class, () -> new Employee("Ольга", "Аналітик", 20000.0, 2, null));
    }

    @Test
    void shouldCorrectlyCopyEmployeeUsingCopyConstructor() {
        Employee original = new Employee("Ірина", "Бухгалтер", 32000.0, 6, Department.FINANCE);
        Employee copy = new Employee(original);

        assertEquals(original, copy);
        assertNotSame(original, copy);
    }
}
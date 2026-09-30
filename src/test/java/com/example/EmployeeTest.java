package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для перевірки валідації та генерації винятків у класі Employee.
 */
class EmployeeTest {

    @Test
    void shouldThrowExceptionWhenInvalidValueInSetter() {
        Employee employee = new Employee("Олексій", "Інженер", 30000.0, 2);

        assertThrows(IllegalArgumentException.class, () -> {
            employee.setSalary(-1000.0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            employee.setExperienceYears(-3);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            employee.setName("   ");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            employee.setPosition("");
        });
    }

    @Test
    void shouldThrowExceptionWhenInvalidConstructorData() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Employee("", "Менеджер", 25000.0, 1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Employee("Андрій", null, 25000.0, 1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Employee("Василь", "Водій", 0.0, 2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Employee("Сергій", "Охоронець", 15000.0, -1);
        });
    }

    @Test
    void shouldCreateEmployeeWhenValidDataProvided() {
        Employee employee = new Employee("Анна", "Бухгалтер", 28000.0, 4);

        assertEquals("Анна", employee.getName());
        assertEquals("Бухгалтер", employee.getPosition());
        assertEquals(28000.0, employee.getSalary());
        assertEquals(4, employee.getExperienceYears());
    }
}
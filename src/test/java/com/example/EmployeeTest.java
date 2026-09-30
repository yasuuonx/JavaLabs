package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для перевірки повної ієрархії класів Employee.
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
    void shouldThrowExceptionWhenInvalidManagerTeamSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Manager("Віктор", "Керівник", 70000.0, 8, Department.IT, 10000.0, -1);
        });
    }

    @Test
    void shouldThrowExceptionWhenInvalidFreelancerHourlyRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Freelancer("Максим", "Дизайнер", 20000.0, 3, Department.MARKETING, 3, 0.0);
        });
    }

    @Test
    void shouldCorrectlyInstantiateAllHierarchyClasses() {
        Employee emp = new Employee("Сергій", "Охоронець", 15000.0, 1, Department.HR);
        FullTimeEmployee ft = new FullTimeEmployee("Іван", "Тімлід", 90000.0, 7, Department.IT, 15000.0);
        ContractEmployee ct = new ContractEmployee("Марина", "HR", 28000.0, 2, Department.HR, 6);
        Manager mgr = new Manager("Ольга", "CTO", 120000.0, 10, Department.IT, 25000.0, 15);
        Freelancer fl = new Freelancer("Денис", "Розробник", 40000.0, 4, Department.IT, 3, 450.0);

        assertEquals(15, mgr.getTeamSize());
        assertEquals(450.0, fl.getHourlyRate());
        assertTrue(mgr instanceof Employee);
        assertTrue(fl instanceof Employee);
    }
}
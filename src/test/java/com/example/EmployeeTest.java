package com.example;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для перевірки валідації, збереження у файл та методів пошуку.
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
    void shouldCorrectlySaveAndLoadFromTextFile() {
        String testFileName = "test_input.txt";
        ArrayList<Employee> originalList = new ArrayList<Employee>();
        originalList.add(new Employee("Іван", "Стажер", 15000.0, 1, Department.HR));
        originalList.add(new FullTimeEmployee("Ольга", "Розробник", 50000.0, 4, Department.IT, 8000.0));
        originalList.add(new Manager("Петро", "Керівник", 90000.0, 8, Department.IT, 15000.0, 10));

        FileManager.saveToTextFile(testFileName, originalList);
        ArrayList<Employee> loadedList = FileManager.loadFromTextFile(testFileName);

        assertEquals(originalList.size(), loadedList.size());
        assertEquals(originalList.get(0).getName(), loadedList.get(0).getName());
        assertTrue(loadedList.get(2) instanceof Manager);

        new File(testFileName).delete();
    }

    @Test
    void shouldCorrectlySearchByCriteriaWithoutMutatingSource() {
        ArrayList<Employee> list = new ArrayList<Employee>();
        list.add(new Employee("Олександр", "Java Розробник", 45000.0, 3, Department.IT));
        list.add(new FullTimeEmployee("Ірина", "HR Менеджер", 30000.0, 2, Department.HR, 5000.0));
        list.add(new Manager("Богдан", "Senior Менеджер", 80000.0, 7, Department.IT, 15000.0, 8));
        list.add(new Freelancer("Дмитро", "UI Дизайнер", 35000.0, 4, Department.MARKETING, 3, 400.0));

        // Пошук за посадою
        ArrayList<Employee> foundByPosition = Main.searchByPosition(list, "розробник");
        assertEquals(1, foundByPosition.size());
        assertEquals("Олександр", foundByPosition.get(0).getName());

        // Пошук за діапазоном заробітної плати
        ArrayList<Employee> foundBySalary = Main.searchBySalaryRange(list, 32000.0, 50000.0);
        assertEquals(2, foundBySalary.size());

        // Пошук за відділом
        ArrayList<Employee> foundByDept = Main.searchByDepartment(list, Department.IT);
        assertEquals(2, foundByDept.size());

        // Перевірка на відсутність збігів
        ArrayList<Employee> notFound = Main.searchByDepartment(list, Department.FINANCE);
        assertTrue(notFound.isEmpty());

        // Перевірка незмінності початкової колекції
        assertEquals(4, list.size());
    }
}
package com.example;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для перевірки агрегації Company та сортування через лямбда-вирази Comparator.
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
    void shouldCorrectlySortBySalaryDescendingUsingLambda() {
        Company company = new Company("Test Corp");
        company.addNewEmployee(new FullTimeEmployee("Андрій", "Інженер", 25000.0, 2, Department.IT, 1000.0), 1);
        company.addNewEmployee(new FullTimeEmployee("Богдан", "Lead", 80000.0, 6, Department.IT, 5000.0), 1);
        company.addNewEmployee(new FullTimeEmployee("Віктор", "Middle", 50000.0, 4, Department.IT, 3000.0), 1);

        Comparator<Employee> salaryDescComparator = (o1, o2) -> Double.compare(o2.getSalary(), o1.getSalary());

        ArrayList<Employee> sorted = company.getSortedEmployees(salaryDescComparator);
        assertEquals(80000.0, sorted.get(0).getSalary());
        assertEquals(50000.0, sorted.get(1).getSalary());
        assertEquals(25000.0, sorted.get(2).getSalary());
    }

    @Test
    void shouldCorrectlySortByExperienceAscendingUsingLambda() {
        Company company = new Company("Test Corp");
        company.addNewEmployee(new FullTimeEmployee("Богдан", "Lead", 80000.0, 7, Department.IT, 5000.0), 1);
        company.addNewEmployee(new FullTimeEmployee("Андрій", "Junior", 20000.0, 1, Department.IT, 1000.0), 1);
        company.addNewEmployee(new FullTimeEmployee("Віктор", "Middle", 50000.0, 4, Department.IT, 3000.0), 1);

        Comparator<Employee> expAscComparator = (o1, o2) -> Integer.compare(o1.getExperienceYears(), o2.getExperienceYears());

        ArrayList<Employee> sorted = company.getSortedEmployees(expAscComparator);
        assertEquals(1, sorted.get(0).getExperienceYears());
        assertEquals(4, sorted.get(1).getExperienceYears());
        assertEquals(7, sorted.get(2).getExperienceYears());
    }

    @Test
    void shouldCorrectlySortByDepartmentUsingLambda() {
        Company company = new Company("Test Corp");
        company.addNewEmployee(new FullTimeEmployee("Іван", "Маркетолог", 30000.0, 2, Department.MARKETING, 1000.0), 1);
        company.addNewEmployee(new FullTimeEmployee("Олег", "Рекрутер", 25000.0, 1, Department.HR, 500.0), 1);
        company.addNewEmployee(new FullTimeEmployee("Максим", "Фінансист", 40000.0, 5, Department.FINANCE, 2000.0), 1);

        Comparator<Employee> deptComparator = (o1, o2) -> o1.getDepartment().getTitle().compareToIgnoreCase(o2.getDepartment().getTitle());

        ArrayList<Employee> sorted = company.getSortedEmployees(deptComparator);
        assertEquals(Department.HR, sorted.get(0).getDepartment());
        assertEquals(Department.MARKETING, sorted.get(1).getDepartment());
        assertEquals(Department.FINANCE, sorted.get(2).getDepartment());
    }

    @Test
    void shouldCorrectlySaveAndLoadCompanyFromFile() {
        String testFileName = "test_company_input_15.txt";
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
package com.example;

/**
 * Перерахування фіксованого набору відділів компанії.
 */
public enum Department {
    IT("Інформаційні технології"),
    HR("Відділ кадрів"),
    FINANCE("Фінансовий відділ"),
    MARKETING("Маркетинг");

    private final String title;

    Department(String title) {
        this.title = title;
    }

    /**
     * Отримує назву відділу українською мовою.
     *
     * @return зрозуміла назва відділу
     */
    public String getTitle() {
        return title;
    }
}
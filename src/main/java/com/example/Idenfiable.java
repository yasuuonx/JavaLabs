package com.example;

import java.util.UUID;

/**
 * Інтерфейс для сутностей з унікальним ідентифікатором UUID.
 */
public interface Identifiable {
    UUID getUuid();
}
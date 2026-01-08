package fr.esiea.shop2026.domain.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entity, UUID id) {
        super(entity + " with ID " + id + " not found.");
    }
}
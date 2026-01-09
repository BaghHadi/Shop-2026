package fr.esiea.shop2026.usecase.dto;

import java.util.UUID;

public class OrderUpdateDTO {
    public UUID orderId;
    public String status; // Ex: "CONFIRMED"
}
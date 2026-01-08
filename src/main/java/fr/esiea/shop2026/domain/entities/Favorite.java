package fr.esiea.shop2026.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Favorite {
    private UUID id;
    private UUID linkedUserId;
    private List<Product> products = new ArrayList<>();
}
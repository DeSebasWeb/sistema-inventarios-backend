package com.stockmart.api.repository.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.stockmart.api.entity.usuario.Vendedor;

import java.util.List;

@Repository
public interface VendedorRepositorio extends JpaRepository<Vendedor, Integer> {
    List<Vendedor> findByZonaTrabajo(String zonaTrabajo);
}

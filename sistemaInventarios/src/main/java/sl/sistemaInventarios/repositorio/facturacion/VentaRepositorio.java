package sl.sistemaInventarios.repositorio.facturacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sl.sistemaInventarios.modelo.facturacion.Venta;
import sl.sistemaInventarios.modelo.usuario.Vendedor;

import java.util.List;

@Repository
public interface VentaRepositorio extends JpaRepository<Venta, Integer> {
    List<Venta> findByVendedor(Vendedor vendedor);
}

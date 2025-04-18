package sl.sistemaInventarios.modelo.facturacion;

import jakarta.persistence.*;
import lombok.*;
import sl.sistemaInventarios.modelo.estado.Estado;
import sl.sistemaInventarios.modelo.usuario.Usuario;
import sl.sistemaInventarios.modelo.usuario.Vendedor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "venta")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long idVenta;

    @Column(name = "total")
    private Double total;

    @ManyToOne
    @JoinColumn(name = "id_vendedor")
    private Vendedor vendedor;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "fecha_venta", insertable = false, updatable = false)
    private LocalDateTime fechaVenta;

    @Column(name = "fecha_modificacion", insertable = false, updatable = false)
    private LocalDateTime fechaModificacion;

    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminacion;

    @ManyToOne
    @JoinColumn(name = "id_estado_venta")
    private Estado estado;

    @OneToMany(mappedBy = "venta")
    private List<DetalleVenta> detalles;
}

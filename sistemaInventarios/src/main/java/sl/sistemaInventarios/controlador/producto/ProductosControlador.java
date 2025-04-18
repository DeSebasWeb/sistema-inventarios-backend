package sl.sistemaInventarios.controlador.producto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sl.sistemaInventarios.excepcion.RecursoNoEncontradoExcepcion;
import sl.sistemaInventarios.excepcion.MostrarMensajeExcepcion;
import sl.sistemaInventarios.modelo.producto.Producto;
import sl.sistemaInventarios.servicio.producto.clases.ProductosServicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("inventario-app/productos")
@CrossOrigin("http://localhost:4200")
//    http://localhost:8080/inventario-app/productos
public class ProductosControlador {
    private static final Logger logger = LoggerFactory.getLogger(ProductosControlador.class);
    @Autowired
    private ProductosServicio productosServicio;
    //Muestra los productos
    @GetMapping("/mostrar")
    public List<Producto> mostrarProductos(){
        List<Producto> productos = this.productosServicio.mostrarProductos();
        logger.info("Productos obtenidos: ");
        productos.forEach(producto -> {
            logger.info(producto.toString());
        });
        return productos;
    }

    //Encuentra el producto por el id
    @GetMapping("/id/{id}")
    public ResponseEntity<?> buscarProductoPorId(@PathVariable Integer id){
        Producto producto = this.productosServicio.buscarProductoPorId(id);
        if (producto != null){
            return ResponseEntity.ok(producto);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MostrarMensajeExcepcion("No se ha encontrado el producto con el id: " + id));
        }
    }
    //Agrega los productos
    @PostMapping("/agregar")
    public Producto agregarProducto(@RequestBody Producto producto){
        if (producto!= null){
            logger.info("Se ha guardado la informacion correctamente");
            return this.productosServicio.guardarProducto(producto);
        }else {
            throw new RecursoNoEncontradoExcepcion("No ha sido posible guardar el producto");
        }
    }
    //Edita los produtcos
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarProducto(@PathVariable Integer id, @RequestBody Producto productoRecibido){
        Producto productoEncontrado = this.productosServicio.buscarProductoPorId(id);
        if (productoEncontrado == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MostrarMensajeExcepcion("No se ha encontrado"));
        }
        if (productoRecibido.getNombre() != null) productoEncontrado.setNombre(productoRecibido.getNombre());
        if (productoRecibido.getDescripcion() != null) productoEncontrado.setDescripcion(productoRecibido.getDescripcion());
        if (productoRecibido.getPrecioCompra() != null) productoEncontrado.setPrecioCompra(productoRecibido.getPrecioCompra());
        if (productoRecibido.getPrecioVenta() != null) productoEncontrado.setPrecioVenta(productoRecibido.getPrecioVenta());
        if (productoRecibido.getStock() != null) productoEncontrado.setStock(productoRecibido.getStock());
        if (productoRecibido.getProductoCategoria() != null) productoEncontrado.setProductoCategoria(productoRecibido.getProductoCategoria());

        this.productosServicio.guardarProducto(productoEncontrado);
        return ResponseEntity.ok(productoEncontrado);
    }
    //borra los productos
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Integer id){
        Producto producto = this.productosServicio.buscarProductoPorId(id);
        if(producto != null){
            this.productosServicio.eliminarProducto(producto);
            Map<String, Boolean> respuesta = new HashMap<>();
            respuesta.put("eliminado", Boolean.TRUE);
            return ResponseEntity.ok(respuesta);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MostrarMensajeExcepcion("No se ha encontrado el id a eliminar: "+id));
        }
    }
}

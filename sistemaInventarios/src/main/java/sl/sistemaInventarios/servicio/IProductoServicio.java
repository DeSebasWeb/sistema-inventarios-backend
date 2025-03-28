package sl.sistemaInventarios.servicio;

import sl.sistemaInventarios.modelo.Producto;

import java.util.List;

public interface IProductoServicio {
    public List<Producto> mostrarProductos();

    public Producto buscarProductoPorId(Integer id);

    public void eliminarProducto(Producto producto);

    public List<Producto> buscarPorCategoria(String categoria);

    public Producto guardarProducto(Producto producto);
}

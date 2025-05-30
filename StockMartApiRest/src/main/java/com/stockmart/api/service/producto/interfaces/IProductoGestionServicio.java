package com.stockmart.api.service.producto.interfaces;

import com.stockmart.api.entity.facturacion.Venta;
import com.stockmart.api.entity.producto.Producto;

public interface IProductoGestionServicio {
    public Producto softDelete(Integer idProducto);

    public void hardDelete(Integer idProducto);

    public Producto recuperarProducto(Integer idProducto);

    public Producto guardarOActualizarProducto(Producto producto);

    public void actualizarStock(Venta venta);
}

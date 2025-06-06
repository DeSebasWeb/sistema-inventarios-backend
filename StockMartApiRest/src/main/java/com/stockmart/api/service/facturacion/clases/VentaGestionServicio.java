package com.stockmart.api.service.facturacion.clases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.stockmart.api.entity.facturacion.DetalleVenta;
import com.stockmart.api.entity.facturacion.Venta;
import com.stockmart.api.repository.facturacion.VentaRepositorio;
import com.stockmart.api.service.estado.clases.EstadoGestionServicio;
import com.stockmart.api.service.facturacion.interfaces.IVentaGestionServicio;
import com.stockmart.api.service.producto.clases.ProductoGestionServicio;
import com.stockmart.api.service.producto.clases.ProductoLecturaServicio;
import com.stockmart.api.service.usuario.clases.VendedorGestionServicio;

import java.util.List;

@Service
@Transactional
public class VentaGestionServicio implements IVentaGestionServicio {

    private final VentaRepositorio ventaRepositorio;

    private final EstadoGestionServicio estadoServicio;

    private final ProductoLecturaServicio productoLecturaServicio;

    private final VentaConsultaServicio ventaConsultaServicio;

    private final VendedorGestionServicio VendedorGestionServicio;

    private final DetalleVentaGestionServicio detalleVentaGestionServicio;

    private final ProductoGestionServicio productoGestionServicio;
    @Autowired
    public VentaGestionServicio(EstadoGestionServicio estadoServicio, ProductoLecturaServicio productoLecturaServicio, VentaRepositorio ventaRepositorio, VentaConsultaServicio ventaConsultaServicio, VendedorGestionServicio VendedorGestionServicio, DetalleVentaGestionServicio detalleVentaGestionServicio, ProductoGestionServicio productoGestionServicio) {
        this.estadoServicio = estadoServicio;
        this.productoLecturaServicio = productoLecturaServicio;
        this.ventaRepositorio = ventaRepositorio;
        this.ventaConsultaServicio = ventaConsultaServicio;
        this.VendedorGestionServicio = VendedorGestionServicio;
        this.detalleVentaGestionServicio = detalleVentaGestionServicio;
        this.productoGestionServicio = productoGestionServicio;
    }


    @Override
    public Venta guardarVenta(Venta venta) {
        Venta ventaGuardada = this.ventaRepositorio.save(venta);
        return  ventaGuardada;
    }

    @Override
    public Venta softDelete(Venta venta) {
        Venta ventaEncontrada = this.ventaConsultaServicio.buscarVentaPorId(venta);
        if (ventaEncontrada.getEstado() == this.estadoServicio.estaEstadoActivo()){
            ventaEncontrada.setEstado(this.estadoServicio.estaEstadoInactivo());
            Venta ventaGuardada = this.guardarVenta(ventaEncontrada);
            return ventaGuardada;
        }else {
            return null;
        }
    }

    @Override
    public void hardDelete(Venta venta) {
        if (venta.getEstado() == this.estadoServicio.estaEstadoInactivo()){
            this.ventaRepositorio.delete(venta);
        }else {
            throw new RuntimeException("La venta tiene estado ACTIVO, si quiere eliminarla, primero pongala en estado INACTIVO");
        }
    }

    @Override
    public Venta recuperar(Venta venta) {
        Venta ventaEncontrada = this.ventaConsultaServicio.buscarVentaPorId(venta);
        if (ventaEncontrada == null){
            throw new RuntimeException("La venta que trata de recuperar no existe");
        }
        if (ventaEncontrada.getEstado() == this.estadoServicio.estaEstadoInactivo()){
            ventaEncontrada.setEstado(this.estadoServicio.estaEstadoActivo());
            Venta ventaGuardada = this.guardarVenta(ventaEncontrada);
            return ventaEncontrada;
        }else {
            throw new RuntimeException("La venta esta activa, no es necesario recuperarla");
        }
    }

    @Override
    public Venta calcularTotalVenta(Venta venta, List<DetalleVenta> detalleVentas) {
        Double total = 0.0;
        if (detalleVentas.isEmpty()){
            throw new IllegalArgumentException("No hay ningun producto para calcular");
        }else {
            for (DetalleVenta productoDetalleVendido : detalleVentas){
                total += productoDetalleVendido.getSubtotal();
            }
            venta.setTotal(total);
            return venta;
        }
    }

    @Override
    public Venta realizarVenta(List<DetalleVenta> detalleVentas, Venta venta) {
        Venta ventaGuardada = this.guardarVenta(venta);
        List<DetalleVenta> detallesGuardados = this.detalleVentaGestionServicio.guardarDetallesVenta(detalleVentas, ventaGuardada);
        Venta ventaCompleta = this.calcularTotalVenta(venta, detallesGuardados);
        this.VendedorGestionServicio.incrementarVenta(venta);
        this.productoGestionServicio.actualizarStock(venta);
        return ventaCompleta;
    }


}

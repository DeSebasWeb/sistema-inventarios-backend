package com.stockmart.api.service.facturacion.clases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.stockmart.api.entity.facturacion.DetalleVenta;
import com.stockmart.api.entity.facturacion.Venta;
import com.stockmart.api.repository.facturacion.DetalleVentaRepositorio;
import com.stockmart.api.service.facturacion.interfaces.IDetalleVentaGestionServicio;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DetalleVentaGestionServicio implements IDetalleVentaGestionServicio {
    private final DetalleVentaConsultaServicio detalleVentaConsultaServicio;
    private final DetalleVentaRepositorio detalleVentaRepositorio;

    @Autowired
    public DetalleVentaGestionServicio(DetalleVentaConsultaServicio detalleVentaConsultaServicio, DetalleVentaRepositorio detalleVentaRepositorio) {
        this.detalleVentaConsultaServicio = detalleVentaConsultaServicio;
        this.detalleVentaRepositorio = detalleVentaRepositorio;
    }

    @Override
    public List<DetalleVenta> guardarDetallesVenta(List<DetalleVenta> detallesVenta, Venta venta) {
        List<DetalleVenta> detalleVentaAGuardar =new ArrayList<>();
        if (detallesVenta.isEmpty()){
            throw new RuntimeException("No hay ningun producto en el detalle de la venta");
        }else {
            for (DetalleVenta detalleVenta : this.calcularSubtotalPorProducto(detallesVenta)){
                detalleVenta.setVenta(venta);
                detalleVentaAGuardar.add(detalleVenta);
            }
            List<DetalleVenta> detallesVentaGuardados = this.detalleVentaRepositorio.saveAll(detalleVentaAGuardar);
            return detallesVentaGuardados;
        }
    }

    @Override
    public List<DetalleVenta> calcularSubtotalPorProducto(List<DetalleVenta> productosDetalleVenta) {
        List<DetalleVenta> detallesVentaConSubtotal = new ArrayList<>();
        for (DetalleVenta detalleVenta : productosDetalleVenta){
            Double precioUnitario = detalleVenta.getPrecioUnitario();
            Integer cantidad = detalleVenta.getCantidad();
            detalleVenta.getProducto().setCantProductoVendido(detalleVenta.getProducto().getCantProductoVendido()+cantidad);
            detalleVenta.setSubtotal(precioUnitario * cantidad);
            detallesVentaConSubtotal.add(detalleVenta);
        }
        return detallesVentaConSubtotal;
    }
}

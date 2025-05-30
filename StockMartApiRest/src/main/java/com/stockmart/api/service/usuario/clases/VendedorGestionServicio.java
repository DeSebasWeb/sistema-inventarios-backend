package com.stockmart.api.service.usuario.clases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.stockmart.api.entity.facturacion.Venta;
import com.stockmart.api.entity.usuario.Usuario;
import com.stockmart.api.entity.usuario.Vendedor;
import com.stockmart.api.repository.usuario.VendedorRepositorio;

@Service
@Transactional
public class VendedorGestionServicio implements com.stockmart.api.service.usuario.interfaces.IVendedorGestionServicio {
    private final VendedorRepositorio vendedorRepositorio;
    private final VendedorConsultaServicio vendedorConsultaServicio;

    @Autowired
    public VendedorGestionServicio(VendedorRepositorio vendedorRepositorio, VendedorConsultaServicio vendedorConsultaServicio) {
        this.vendedorRepositorio = vendedorRepositorio;
        this.vendedorConsultaServicio = vendedorConsultaServicio;
    }

    @Override
    public Vendedor guardarDatosVendedor(Vendedor vendedor) {
        if (vendedor.getIdVendedor() != null){
            Vendedor vendedorEncontrado = this.vendedorConsultaServicio.buscarVendedorPorId(vendedor.getIdVendedor());
            vendedorEncontrado.setUsuario(vendedor.getUsuario());
            vendedorEncontrado.setNumeroVentas(vendedor.getNumeroVentas());
            vendedorEncontrado.setZonaTrabajo(vendedor.getZonaTrabajo());
            return this.vendedorRepositorio.save(vendedorEncontrado);
        }else {
            return this.vendedorRepositorio.save(vendedor);
        }
    }

    @Override
    public Vendedor incrementarVenta(Venta venta) {
        Vendedor vendedorEncontrado = this.vendedorConsultaServicio.buscarVendedorPorId(venta.getVendedor().getIdVendedor());
        if (vendedorEncontrado != null){
            vendedorEncontrado.setNumeroVentas(vendedorEncontrado.getNumeroVentas()+1);
            return this.guardarDatosVendedor(vendedorEncontrado);
        }else {
            throw new RuntimeException("El vendedor con ID: "+ vendedorEncontrado.getIdVendedor()+ " no existe");
        }
    }

    @Override
    public Vendedor crearDesdeUsuario(Usuario usuario) {
        if (usuario != null){
            Vendedor vendedor = new Vendedor();
            vendedor.setUsuario(usuario);
            Vendedor vendedorGuardado = this.vendedorRepositorio.save(vendedor);
            return vendedorGuardado;
        }else {
            throw new RuntimeException("No se puede crear el vendedor");
        }
    }
}
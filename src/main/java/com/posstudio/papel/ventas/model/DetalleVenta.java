package com.posstudio.papel.ventas.model;

import java.math.BigDecimal;

import com.posstudio.papel.inventario.model.Producto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    Producto producto;

    @Column(nullable = false)
    Integer cantidad;
    @Column(name = "precio_unitario", nullable = false)
    BigDecimal precioUnitario;
    @Column(nullable = false)
    BigDecimal subtotal;

    BigDecimal descuento;

    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        if (cantidad != null) {
            // Si existe descuento y es diferente de 0, usar el descuento como precio
            if (descuento != null && descuento.compareTo(BigDecimal.ZERO) != 0) {
                // El descuento ES el precio final del producto
                this.subtotal = descuento.multiply(BigDecimal.valueOf(cantidad));
            } else if (precioUnitario != null) {
                // Si no hay descuento, usar el precio unitario original
                this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
            }
        }
    }
}

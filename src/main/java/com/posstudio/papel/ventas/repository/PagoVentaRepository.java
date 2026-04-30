package com.posstudio.papel.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posstudio.papel.ventas.model.Pagoventa;

public interface PagoVentaRepository extends JpaRepository<Pagoventa, Long> {

}

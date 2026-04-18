package com.posstudio.papel.turnos.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import com.posstudio.papel.common.enums.EstadoTurno;
import com.posstudio.papel.common.enums.TipoTurno;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "turno")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "tipo_turno", nullable = false)
    TipoTurno tipoTurno;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "estado", nullable = false)
    EstadoTurno estadoTurno;

    @Column(name = "dinero_apertura", nullable = false, precision = 12, scale = 2)
    BigDecimal dineroApertura;
    @Column(name = "dinero_cierre")
    BigDecimal dineroCierre;
    BigDecimal diferencia;
    @Column(name = "fecha_apertura", nullable = false)
    LocalDateTime fechaApertura;
    @Column(name = "fecha_cierre")
    LocalDateTime fechaCierre;
}

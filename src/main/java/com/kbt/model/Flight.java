package com.kbt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "flight")
public class Flight implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idflight")
    private Long idFlight;

    @NotNull
    @Column(name = "nombreavion", nullable = false, length = 80)
    private String nombreAvion;

    @NotNull
    @Column(name = "numerovuelo", nullable = false, length = 80)
    private String numeroVuelo;

    @NotNull
    @Column(name = "origen", nullable = false, length = 80)
    private String origen;

    @NotNull
    @Column(name = "destino", nullable = false, length = 80)
    private String destino;

    @NotNull
    @Column(name = "capacidad", nullable = false)
    private int capacidad;

    @NotNull
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Column(name = "rating", nullable = false)
    private int rating;

    @NotNull
    @Column(name = "planvuelo", nullable = false)
    private long planvuelo;

    @Column(name = "cumplido")
    private Boolean cumplido;

    // ======= CONSTRUCTORES =======

    public Flight() {}

    public Flight(String nombreAvion, String numeroVuelo, String origen, String destino, int capacidad, int rating, long planvuelo, Boolean cumplido) {
        this.nombreAvion = nombreAvion;
        this.numeroVuelo = numeroVuelo;
        this.origen = origen;
        this.destino = destino;
        this.capacidad = capacidad;
        this.rating = rating;
        this.planvuelo = planvuelo;
        this.cumplido = cumplido;
    }

    // ======= GETTERS Y SETTERS =======

    public Long getIdFlight() {
        return idFlight;
    }

    public void setIdFlight(Long idFlight) {
        this.idFlight = idFlight;
    }

    public String getNombreAvion() {
        return nombreAvion;
    }

    public void setNombreAvion(String nombreAvion) {
        this.nombreAvion = nombreAvion;
    }

    public String getNumeroVuelo() {
        return numeroVuelo;
    }

    public void setNumeroVuelo(String numeroVuelo) {
        this.numeroVuelo = numeroVuelo;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getPlanvuelo() {
        return planvuelo;
    }

    public void setPlanvuelo(long planvuelo) {
        this.planvuelo = planvuelo;
    }

    public Boolean getCumplido() {
        return cumplido;
    }

    public void setCumplido(Boolean cumplido) {
        this.cumplido = cumplido;
    }

    // ======= MÉTODOS UTILITARIOS =======

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flight)) return false;
        Flight flight = (Flight) o;
        return Objects.equals(idFlight, flight.idFlight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFlight);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "idFlight=" + idFlight +
                ", nombreAvion='" + nombreAvion + '\'' +
                ", numeroVuelo='" + numeroVuelo + '\'' +
                ", origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", capacidad=" + capacidad +
                ", rating=" + rating +
                ", planvuelo=" + planvuelo +
                ", cumplido=" + cumplido +
                '}';
    }
}


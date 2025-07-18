package com.kbt.repository;



import com.kbt.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFlightDAO extends JpaRepository<Flight, Long> {

    // Buscar vuelos por rating
    List<Flight> findByRating(int rating);

    // Buscar vuelos por origen y destino
    List<Flight> findByOrigenAndDestino(String origen, String destino);

    // Buscar si el vuelo fue cumplido o no
    List<Flight> findByCumplido(Boolean cumplido);

    List<Flight> findByRatingGreaterThanEqual(int rating);

}

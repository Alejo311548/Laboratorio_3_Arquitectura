package com.kbt.service;



import com.kbt.model.Flight;
import com.kbt.repository.IFlightDAO;
import com.kbt.exception.FlightNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private final IFlightDAO dao;

    // Inyección por constructor
    public FlightService(IFlightDAO dao) {
        this.dao = dao;
    }

    public Flight save(Flight flight) {
        return dao.save(flight);
    }

    public String delete(long id) {
        dao.deleteById(id);
        return "Flight removed";
    }

    public List<Flight> list() {
        return dao.findAll();
    }

    public Optional<Flight> listId(long id) {
        return dao.findById(id);
    }

    public Flight update(Flight updated) {
        return dao.findById(updated.getIdFlight())
                .map(existing -> {
                    existing.setNombreAvion(updated.getNombreAvion());
                    existing.setNumeroVuelo(updated.getNumeroVuelo());
                    existing.setOrigen(updated.getOrigen());
                    existing.setDestino(updated.getDestino());
                    existing.setRating(updated.getRating());
                    existing.setPlanvuelo(updated.getPlanvuelo());
                    existing.setCapacidad(updated.getCapacidad());
                    existing.setCumplido(updated.getCumplido());
                    return dao.save(existing);
                })
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with ID: " + updated.getIdFlight()));
    }

    public List<Flight> viewBestFlights() {
        List<Flight> flights = dao.findByRatingGreaterThanEqual(4);
        if (flights.isEmpty()) {
            throw new FlightNotFoundException("No flights found with rating >= 4");
        }
        return flights;
    }
}


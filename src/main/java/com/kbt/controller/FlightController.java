package com.kbt.controller;

import com.kbt.exception.InvalidRating;
import com.kbt.exception.ModelNotFoundException;
import com.kbt.model.Flight;
import com.kbt.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/flight")
@CrossOrigin("*")
public class FlightController {

    // Constants for relation names
    private static final String ALL_FLIGHTS_REL = "all-flights";
    private static final String DELETE_REL = "delete";
    private static final String UPDATE_REL = "update";
    private static final String TOP_FLIGHTS_REL = "top-flights";
    private static final String CREATE_FLIGHT_REL = "create-flight";

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @Operation(summary = "Crear un nuevo vuelo",
            description = "Crea un nuevo vuelo y retorna el recurso creado con enlaces HATEOAS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vuelo creado exitosamente",
                    content = @Content(schema = @Schema(implementation = Flight.class))),
            @ApiResponse(responseCode = "400", description = "Rating inválido (debe ser <= 5)",
                    content = @Content)
    })
    @PostMapping("/save")
    public ResponseEntity<EntityModel<Flight>> save(@RequestBody Flight flight) {
        if (flight.getRating() > 5) {
            throw new InvalidRating("Rating must be <= 5");
        }
        Flight saved = flightService.save(flight);

        EntityModel<Flight> resource = EntityModel.of(saved,
                linkTo(methodOn(FlightController.class).listFlightById(saved.getIdFlight())).withSelfRel(),
                linkTo(methodOn(FlightController.class).listAllFlights()).withRel(ALL_FLIGHTS_REL),
                linkTo(methodOn(FlightController.class).deleteFlight(saved.getIdFlight()))
                        .withRel(DELETE_REL)
                        .withType("DELETE"),
                linkTo(methodOn(FlightController.class).updateFlight(saved))
                        .withRel(UPDATE_REL)
                        .withType("PUT"),
                linkTo(methodOn(FlightController.class).viewBestFlights())
                        .withRel(TOP_FLIGHTS_REL)
        );

        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos los vuelos con enlaces HATEOAS",
            description = "Retorna una lista completa de vuelos con enlaces HATEOAS para navegación")
    @ApiResponse(responseCode = "200", description = "Lista de vuelos con enlaces",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Flight.class))))
    @GetMapping("/listAll")
    public ResponseEntity<CollectionModel<EntityModel<Flight>>> listAllFlights() {
        List<EntityModel<Flight>> flights = flightService.list().stream()
                .map(flight -> EntityModel.of(flight,
                        linkTo(methodOn(FlightController.class).listFlightById(flight.getIdFlight())).withSelfRel(),
                        linkTo(methodOn(FlightController.class).deleteFlight(flight.getIdFlight()))
                                .withRel(DELETE_REL)
                                .withType("DELETE"),
                        linkTo(methodOn(FlightController.class).updateFlight(flight))
                                .withRel(UPDATE_REL)
                                .withType("PUT"),
                        linkTo(methodOn(FlightController.class).viewBestFlights())
                                .withRel(TOP_FLIGHTS_REL)
                )).collect(Collectors.toList());

        CollectionModel<EntityModel<Flight>> collectionModel = CollectionModel.of(flights,
                linkTo(methodOn(FlightController.class).listAllFlights()).withSelfRel(),
                linkTo(methodOn(FlightController.class).save(null))
                        .withRel(CREATE_FLIGHT_REL)
                        .withType("POST"),
                linkTo(methodOn(FlightController.class).viewBestFlights())
                        .withRel(TOP_FLIGHTS_REL)
        );

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Buscar un vuelo por su ID",
            description = "Retorna un vuelo específico con todos sus enlaces HATEOAS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vuelo encontrado con enlaces",
                    content = @Content(schema = @Schema(implementation = Flight.class))),
            @ApiResponse(responseCode = "404", description = "Vuelo no encontrado",
                    content = @Content)
    })
    @GetMapping("/list/{id}")
    public ResponseEntity<EntityModel<Flight>> listFlightById(@PathVariable("id") long id) {
        Optional<Flight> flight = flightService.listId(id);
        if (flight.isEmpty()) {
            throw new ModelNotFoundException("Flight ID not found: " + id);
        }

        Flight f = flight.get();

        EntityModel<Flight> resource = EntityModel.of(f,
                linkTo(methodOn(FlightController.class).listFlightById(id)).withSelfRel(),
                linkTo(methodOn(FlightController.class).listAllFlights()).withRel(ALL_FLIGHTS_REL),
                linkTo(methodOn(FlightController.class).deleteFlight(id))
                        .withRel(DELETE_REL)
                        .withType("DELETE"),
                linkTo(methodOn(FlightController.class).updateFlight(f))
                        .withRel(UPDATE_REL)
                        .withType("PUT"),
                linkTo(methodOn(FlightController.class).viewBestFlights())
                        .withRel(TOP_FLIGHTS_REL),
                linkTo(methodOn(FlightController.class).save(null))
                        .withRel(CREATE_FLIGHT_REL)
                        .withType("POST")
        );

        // Conditional link example
        if(f.getRating() >= 4) {
            resource.add(linkTo(methodOn(FlightController.class)
                    .viewBestFlights())
                    .withRel("featured")
                    .withTitle("Featured Flight"));
        }

        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Listar vuelos con rating >= 4",
            description = "Retorna una lista de los mejores vuelos (rating 4 o 5) con enlaces HATEOAS")
    @ApiResponse(responseCode = "202", description = "Lista de mejores vuelos con enlaces",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Flight.class))))
    @GetMapping("/topFlights")
    public ResponseEntity<CollectionModel<EntityModel<Flight>>> viewBestFlights() {
        List<Flight> topFlights = flightService.viewBestFlights();

        List<EntityModel<Flight>> models = topFlights.stream()
                .map(flight -> EntityModel.of(flight,
                        linkTo(methodOn(FlightController.class).listFlightById(flight.getIdFlight())).withSelfRel(),
                        linkTo(methodOn(FlightController.class).deleteFlight(flight.getIdFlight()))
                                .withRel(DELETE_REL)
                                .withType("DELETE"),
                        linkTo(methodOn(FlightController.class).updateFlight(flight))
                                .withRel(UPDATE_REL)
                                .withType("PUT")
                )).collect(Collectors.toList());

        CollectionModel<EntityModel<Flight>> collectionModel = CollectionModel.of(models,
                linkTo(methodOn(FlightController.class).viewBestFlights()).withSelfRel(),
                linkTo(methodOn(FlightController.class).listAllFlights()).withRel(ALL_FLIGHTS_REL),
                linkTo(methodOn(FlightController.class).save(null))
                        .withRel(CREATE_FLIGHT_REL)
                        .withType("POST")
        );

        return new ResponseEntity<>(collectionModel, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Actualizar un vuelo existente",
            description = "Actualiza un vuelo y retorna el recurso actualizado con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Vuelo actualizado con enlaces",
            content = @Content(schema = @Schema(implementation = Flight.class)))
    @PutMapping("/update")
    public ResponseEntity<EntityModel<Flight>> updateFlight(@RequestBody Flight flight) {
        Flight updated = flightService.update(flight);

        EntityModel<Flight> resource = EntityModel.of(updated,
                linkTo(methodOn(FlightController.class).listFlightById(updated.getIdFlight())).withSelfRel(),
                linkTo(methodOn(FlightController.class).listAllFlights()).withRel(ALL_FLIGHTS_REL),
                linkTo(methodOn(FlightController.class).deleteFlight(updated.getIdFlight()))
                        .withRel(DELETE_REL)
                        .withType("DELETE"),
                linkTo(methodOn(FlightController.class).viewBestFlights())
                        .withRel(TOP_FLIGHTS_REL)
        );

        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Eliminar un vuelo por ID",
            description = "Elimina un vuelo específico y retorna un mensaje de confirmación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vuelo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Vuelo no encontrado")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable long id) {
        String msg = flightService.delete(id);
        return ResponseEntity.ok(msg);
    }
}
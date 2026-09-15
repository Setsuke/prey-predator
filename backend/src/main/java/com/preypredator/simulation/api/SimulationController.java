package com.preypredator.simulation.api;

import com.preypredator.simulation.domain.SimulationParameters;
import com.preypredator.simulation.service.SimulationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/simulations")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimulationResponse createSimulation(@Valid @RequestBody(required = false) SimulationRequest request) {
        SimulationParameters parameters = request == null ? SimulationParameters.defaults() : toParameters(request);
        return SimulationResponse.from(simulationService.createSimulation(parameters));
    }

    @GetMapping("/{id}")
    public SimulationResponse getSimulation(@PathVariable UUID id) {
        return SimulationResponse.from(simulationService.getSimulation(id));
    }

    @PostMapping("/{id}/steps")
    public SimulationResponse stepSimulation(@PathVariable UUID id) {
        return SimulationResponse.from(simulationService.stepSimulation(id));
    }

    private SimulationParameters toParameters(SimulationRequest request) {
        return new SimulationParameters(
                request.rows(),
                request.columns(),
                request.initialRabbits(),
                request.initialFoxes(),
                request.rabbitSpeed(),
                request.foxSpeed(),
                request.grassGrowthRate(),
                request.maxGrass(),
                request.startGrass(),
                request.rabbitInitialFood(),
                request.foxInitialFood(),
                request.rabbitVision(),
                request.foxVision(),
                request.rabbitBreedFrequency(),
                request.foxBreedFrequency(),
                request.rabbitBreedFood(),
                request.foxBreedFood(),
                request.rabbitMaxAge(),
                request.foxMaxAge(),
                request.seed()
        );
    }
}

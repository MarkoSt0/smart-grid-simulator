/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.fon.marko.grid.actuator.service.GridService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import rs.ac.bg.fon.marko.grid.common.dto.request.*;
import rs.ac.bg.fon.marko.grid.common.dto.response.*;

/**
 *
 * @author Marko
 */

/** Endpointi:
 * - /api/topology/*     - Staticka topologija mreze
 * - /api/telemetry/*    - Senzorski podaci
 * - /api/state/*        - Trenutno stanje sistema
 * - /api/commands/*     - Izvršavanje komandi
 * - /api/alerts/*       - Alarmi i upozorenja
*/


@RestController
@RequestMapping("/api/grid")
@RequiredArgsConstructor
@Slf4j
public class GridController {
    
    private final GridService gridService;
    
    // Topology ENDPOINTS
    
    /**
     * GET /api/topology/nodes
     * Cilj: Vraca sve cvorove (resource, customer, hub, substation, storage)
     * Koriste: Frontend za prikaz grafa, SNA mikroservis za analizu
    */
    @GetMapping("/topology/nodes")
    public ResponseEntity<List<NodeDTO>> getAllNodes() {
        log.debug("GET /api/topology/nodes");
        List<NodeDTO> nodes = gridService.getAllNodes();
        return ResponseEntity.ok(nodes);
    }

    /**
     * GET /api/topology/nodes/{id}
     * Cilj: Vraca jedan cvor po ID-ju 
     * Koristi: Frontend za detalje jednog cvora
    */
    @GetMapping("/topology/nodes/{id}")
    public ResponseEntity<NodeDTO> getNodeById(@PathVariable String id) {
        log.debug("GET /api/topology/nodes/{}", id);
        NodeDTO node = gridService.getNodeById(id);
        return ResponseEntity.ok(node);
    }
    
    
    /**
     * GET /api/topology/edges
     * Cilj: Vraca sve ivice(kablove)
     * Koristi: Frontend za prikaz grafa, SNA za analizu
     */
    @GetMapping("/topology/edges")
    public ResponseEntity<List<EdgeDTO>> getAllEdges() {
        log.debug("GET /api/topology/edges");
        List<EdgeDTO> edges = gridService.getAllEdges();
        return ResponseEntity.ok(edges);
    }
    
    // Telemetry ENDPOINTS
    
    /**
     * POST /api/telemetry/batch
     * Cilj: Prima batch telemetrijskih podataka od Simulation-Service
     * Body: { "timestamp": "...", "readings": [ {nodeId, reportedMw}, ... ] }
     * Koristi: Simulation-Service (svake 2s)
     */
    @PostMapping("/telemetry/batch")
    public ResponseEntity<Void> receiveTelemetryBatch(@RequestBody TelemetryBatchRequest request) {
        log.info("POST /api/telemetry/batch - {} readings", request.getReadings().size());
        gridService.receiveTelemetryBatch(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    /**
     * GET /api/telemetry/latest
     * Cilj: Vraca najnoviju telemetriju za sve cvorove
     * Koristi: Smart-Orchestrator (za racunanje balansa)
     */
    @GetMapping("/telemetry/latest")
    public ResponseEntity<List<TelemetryDTO>> getLatestTelemetry() {
        log.debug("GET /api/telemetry/latest");
        List<TelemetryDTO> telemetry = gridService.getLatestTelemetry();
        return ResponseEntity.ok(telemetry);
    }
    
    /**
     * GET /api/telemetry/nodes/{nodeId}
     * Cilj: Vraca telemetriju za jedan cvor
     * Koristi: Frontend za istorijske grafike
     */
    @GetMapping("/telemetry/nodes/{nodeId}")
    public ResponseEntity<List<TelemetryDTO>> getTelemetryForNode(@PathVariable String nodeId) {
        log.debug("GET /api/telemetry/nodes/{}", nodeId);
        List<TelemetryDTO> telemetry = gridService.getTelemetryForNode(nodeId);
        return ResponseEntity.ok(telemetry);
    }
    
    // State ENDPOINTS
    
    /**
     * GET /api/state/nodes
     * Cilj: Vraca trenutno stanje svih cvorova (currentMw, SoC)
     * Koristi: Frontend, SNA
     */
    @GetMapping("/state/nodes")
    public ResponseEntity<List<NodeStateDTO>> getAllNodeStates() {
        log.debug("GET /api/state/nodes");
        List<NodeStateDTO> states = gridService.getAllNodeStates();
        return ResponseEntity.ok(states);
    }
    
    /**
     * GET /api/state/edges
     * Cilj: Vraca protok kroz sve kablove (currentFlowMw, utilization)
     * Koristi: Frontend, SNA
     */
    @GetMapping("/state/edges")
    public ResponseEntity<List<EdgeStateDTO>> getAllEdgeStates() {
        log.debug("GET /api/state/edges");
        List<EdgeStateDTO> states = gridService.getAllEdgeStates();
        return ResponseEntity.ok(states);
    }
    
    /**
     * GET /api/state/full
     * Cilj: Vraca kompletan snapshot sistema (nodes, edges, states, alerts)
     * Koristi: Frontend (glavni poziv za ceo UI)
     */
    @GetMapping("/state/full")
    public ResponseEntity<FullStateResponse> getFullState() {
        log.debug("GET /api/state/full");
        FullStateResponse state = gridService.getFullState();
        return ResponseEntity.ok(state);
    }
    
    // Command ENDPOINTS
    
    /**
     * POST /api/commands/execute
     * Cilj: Izvrsava komandu (CHARGE_BATTERY, DISCHARGE_BATTERY, SHED_LOAD)
     * Body: { "commandType": "...", "targetNode": "...", "value": ..., "issuedBy": "..." }
     * Koristi: Smart-Orchestrator
     */
    @PostMapping("/commands/execute")
    public ResponseEntity<Void> executeCommand(@RequestBody CommandRequest request) {
        log.info("POST /api/commands/execute - {} on {} with value {}", 
                request.getCommandType(), request.getTargetNode(), request.getValue());
        gridService.executeCommand(request);
        return ResponseEntity.ok().build();
    }
    
    /**
     * GET /api/commands/history/{nodeId}
     * Cilj: Vraca istoriju komandi za jedan cvor
     * Koristi: Frontend za audit log
     */
    @GetMapping("/commands/history/{nodeId}")
    public ResponseEntity<List<CommandDTO>> getCommandHistory(@PathVariable String nodeId) {
        log.debug("GET /api/commands/history/{}", nodeId);
        List<CommandDTO> commands = gridService.getCommandHistory(nodeId);
        return ResponseEntity.ok(commands);
    }
    
    // Alert ENDPOINTS
    
    /**
     * POST /api/alerts
     * Cilj: Kreiranje novog alarma
     * Body: { "alertType": "...", "severity": "...", "description": "..." }
     * Koristi: Smart-Orchestrator, Topology-SNA
     */
    @PostMapping("/alerts")
    public ResponseEntity<Void> createAlert(@RequestBody AlertRequest request) {
        log.warn("POST /api/alerts - {} ({})", request.getAlertType(), request.getSeverity());
        gridService.createAlert(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    /**
     * GET /api/alerts/active
     * Cilj: Vraca sve aktivne (neresene) alarme
     * Koristi: Frontend za notifikacije
     */
    @GetMapping("/alerts/active")
    public ResponseEntity<List<AlertDTO>> getActiveAlerts() {
        log.debug("GET /api/alerts/active");
        List<AlertDTO> alerts = gridService.getActiveAlerts();
        return ResponseEntity.ok(alerts);
    }
    
    /**
     * PUT /api/alerts/{id}/resolve
     * Cilj: Oznacava alarm kao resen
     * Koristi: Frontend (operator klikom na dugme)
     */
    @PutMapping("/alerts/{id}/resolve")
    public ResponseEntity<Void> resolveAlert(@PathVariable Long id) {
        log.info("PUT /api/alerts/{}/resolve", id);
        gridService.resolveAlert(id);
        return ResponseEntity.ok().build();
    }
}

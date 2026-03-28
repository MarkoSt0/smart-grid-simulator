/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.actuator.service;

import rs.ac.bg.fon.marko.grid.common.dto.response.AlertDTO;
import rs.ac.bg.fon.marko.grid.common.dto.response.NodeStateDTO;
import rs.ac.bg.fon.marko.grid.common.dto.response.EdgeStateDTO;
import rs.ac.bg.fon.marko.grid.common.dto.response.TelemetryDTO;
import rs.ac.bg.fon.marko.grid.common.dto.response.EdgeDTO;
import rs.ac.bg.fon.marko.grid.common.dto.response.CommandDTO;
import rs.ac.bg.fon.marko.grid.common.dto.response.NodeDTO;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.marko.grid.common.dto.request.AlertRequest;
import rs.ac.bg.fon.marko.grid.common.dto.request.CommandRequest;
import rs.ac.bg.fon.marko.grid.common.dto.request.TelemetryBatchRequest;
import rs.ac.bg.fon.marko.grid.common.dto.response.FullStateResponse;
import rs.ac.bg.fon.marko.grid.actuator.entity.*;
import rs.ac.bg.fon.marko.grid.actuator.mapper.*;
import rs.ac.bg.fon.marko.grid.actuator.repository.*;

/**
 *
 * @author Marko
 */
@Service
@RequiredArgsConstructor
@Slf4j // Biblioteka Simple Logging Facade for Java
// Svrha ove biblioteke je da da prof logging pomocu metoda .trace .info .debug .warn .error
public class GridServiceImpl implements GridService{
    
    // Repositories
    private final NodeRepository nodeRepository;
    private final EdgeRepository edgeRepository;
    private final TelemetryRepository telemetryRepository;
    private final NodeStateRepository nodeStateRepository;
    private final EdgeStateRepository edgeStateRepository;
    private final CommandRepository commandRepository;
    private final AlertRepository alertRepository;
    // Mappers
    private final NodeMapper nodeMapper;
    private final EdgeMapper edgeMapper;
    private final TelemetryMapper telemetryMapper;
    private final NodeStateMapper nodeStateMapper;
    private final EdgeStateMapper edgeStateMapper;
    private final CommandMapper commandMapper;
    private final AlertMapper alertMapper;
    
    // Topology - deo koji radi sa elementima grafa
    // Cilj: Vracanje svih cvorova
    @Override
    public List<NodeDTO> getAllNodes() {
        log.debug("Fetching all nodes");
        return nodeRepository.findAll()
                .stream()
                .map(nodeMapper::toDto)
                .collect(Collectors.toList());
    }
    
    // Cilj: Vracanje svih ivica
    @Override
    public List<EdgeDTO> getAllEdges() {
        log.debug("Fetching all edges");
        return edgeRepository.findAll()
                .stream()
                .map(edgeMapper::toDto)
                .collect(Collectors.toList());
    }
    
    // Cilj: Vracanje cvora na osnovu ID-a
    @Override
    public NodeDTO getNodeById(String id) {
        log.debug("Fetching node: {}", id);
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Node not found: " + id));
        return nodeMapper.toDto(node);
    }

    // Telemetry - rad sa telemetrijom
    
    /*  
    *Poziva: SIMULATION-SEVRICE
    *Nacin izvrsavanja: Simulation-Service kreira TelemetryBatchRequest
        svake 2 sekunde, salje POST zahtev preko Feign Client-a (gridClient.sendTelemetryBatch),
        trenutni mikroservis prima request i poziva narednu metodu.
    *Cilj: Pamcenje telemetrije koja je prosledjena
    
    -Metoda je transakciona, ako nesto ne uspe radi se rollback
    -Ispisuje se broj telemetrija i vreme citanja
    -Izvlace se sve telemetrije i pretvaraju u listu sa istim timestamp-om
    -Upisuju se telemetrije u bazu, prikazuje se debug log sa informacijom
    */
    @Override
    @Transactional
    public void receiveTelemetryBatch(TelemetryBatchRequest request) {
        log.info("Receiving telemetry batch with {} readings at {}", 
                request.getReadings().size(), request.getTimestamp());
        
        LocalDateTime timestamp = request.getTimestamp();
        
        List<Telemetry> telemetries = request.getReadings().stream()
                .map(reading -> Telemetry.builder()
                        .nodeId(reading.getNodeId())
                        .reportedMw(reading.getReportedMw())
                        .timestamp(timestamp)
                        .metadata("{}")
                        .build())
                .collect(Collectors.toList());
        
        telemetryRepository.saveAll(telemetries);
        
        log.debug("Saved {} telemetry records", telemetries.size());
    }
    
    // Cilj: Vracanje poslednje sacuvane telemetrije svih cvorova
    @Override
    public List<TelemetryDTO> getLatestTelemetry() {
        log.debug("Fetching latest telemetry for all nodes");
        return telemetryRepository.findLatestByNodeId()
                .stream()
                .map(telemetryMapper::toDto)
                .collect(Collectors.toList());
    }
    
    // Cilj: Vracanje telemetrija nekog cvora
    @Override
    public List<TelemetryDTO> getTelemetryForNode(String nodeId) {
        log.debug("Fetching telemetry history for node: {}", nodeId);
        // Za sada vraćamo sve, možeš dodati limit ili time range
        Telemetry latest = telemetryRepository.findFirstByNodeIdOrderByTimestampDesc(nodeId);
        return latest != null ? List.of(telemetryMapper.toDto(latest)) : List.of();
    }
    
    // State
    
    // Cilj: Vracanje svih *trenutnih* stanja cvorova
    @Override
    public List<NodeStateDTO> getAllNodeStates() {
        log.debug("Fetching all node states");
        return nodeStateRepository.findAll()
                .stream()
                .map(nodeStateMapper::toDto)
                .collect(Collectors.toList());
    }
    
    // Cilj: Vracanje svih *trenutnih* stanja ivica
    @Override
    public List<EdgeStateDTO> getAllEdgeStates() {
        log.debug("Fetching all edge states");
        return edgeStateRepository.findAll()
                .stream()
                .map(edgeStateMapper::toDto)
                .collect(Collectors.toList());
    }
    
    // Cilj: Vracanje svih potrebnih informacija o mrezi
    @Override
    public FullStateResponse getFullState() {
        log.debug("Fetching full grid state");
        
        return FullStateResponse.builder()
                .nodes(getAllNodes())
                .edges(getAllEdges())
                .nodeStates(getAllNodeStates())
                .edgeStates(getAllEdgeStates())
                .activeAlerts(getActiveAlerts())
                .build();
    }
    
    // Commands
    
    /*  
    *Poziva: SMART-ORCHESTRATOR (mikroservis)
    *Nacin izvrsavanja: Na svakih 2 sekunde poziva metodu da se balansira graf.
        Vrsi izracunavanje balansiranosti grafa, ako je balans presao praga onda
        kreira CommandRequest i salje preko Feign Client-a
    *Cilj: Pamcenje promene baterije na osnovu prosledjenog zahteva
    
    -Metoda je transakciona, ako nesto ne uspe radi se rollback
    1. Vrsi se validacija da cvor(node) postoji
    2. Vrsi se validacija po tipu komande
    3. Izvrsava se komanda
    4. Komanda se cuva u bazi
    Primer requesta:
     {
        "commandType": "CHARGE_BATTERY",
        "targetNode": "BAT_MAIN",
        "value": 40.0,
        "issuedBy": "smart-orchestrator"
      }
    */
    @Override
    @Transactional
    public void executeCommand(CommandRequest request) {
        log.info("Executing command: {} on {} with value {}", 
                request.getCommandType(), request.getTargetNode(), request.getValue());
        
        // 1
        Node targetNode = nodeRepository.findById(request.getTargetNode())
                .orElseThrow(() -> new RuntimeException("Target node not found: " + request.getTargetNode()));
        
        // 2
        validateCommand(request, targetNode);
        
        // 3
        switch (request.getCommandType()) {
            case "CHARGE_BATTERY":
            case "DISCHARGE_BATTERY":
                updateBatteryState(request, targetNode);
                break;
                
            case "SHED_LOAD":
                updateConsumerLoad(request, targetNode);
                break;
                
            default:
                throw new RuntimeException("Unknown command type: " + request.getCommandType());
        }
        
        // 4
        Command command = Command.builder()
                .commandType(request.getCommandType())
                .targetNode(request.getTargetNode())
                .value(request.getValue())
                .issuedBy(request.getIssuedBy())
                .executedAt(LocalDateTime.now())
                .build();
        
        commandRepository.save(command);
        
        log.info("Command executed successfully");
    }
    
    // Cilj: Provera da li se request odnosi na ispravan tip cvora
    private void validateCommand(CommandRequest request, Node targetNode) {
        switch (request.getCommandType()) {
            case "CHARGE_BATTERY":
            case "DISCHARGE_BATTERY":
                if (!"STORAGE".equals(targetNode.getType())) {
                    throw new RuntimeException("Target node is not a battery: " + targetNode.getId());
                }
                break;
                
            case "SHED_LOAD":
                if (!"CONSUMER".equals(targetNode.getType())) {
                    throw new RuntimeException("Target node is not a consumer: " + targetNode.getId());
                }
                break;
        }
    }
    
    // Cilj: Ubacivanje novih informacija u cvor koji je tip baterija
    private void updateBatteryState(CommandRequest request, Node targetNode) {
        // Nadji trenutno stanje baterije
        NodeState state = nodeStateRepository.findById(targetNode.getId())
                .orElseThrow(() -> new RuntimeException("Node state not found: " + targetNode.getId()));
        
        double currentSoC = state.getCurrentSocPercent() != null ? state.getCurrentSocPercent() : 0.0;
        double energyCapacity = targetNode.getCapacityMwh();
        double powerValue = request.getValue();
        
        // Racunanje novog SoC
        // Formula: *delta*Energy = Power × Time (0.25h za 15min ciklus, prilagodi po potrebi!!!!)
        // Za sada pretpostavljamo da je value vec u MWh
        double deltaEnergy = powerValue * 0.25; // 15 minuta = 0.25h
        double deltaSoC = (deltaEnergy / energyCapacity) * 100;
        
        double newSoC;
        if ("CHARGE_BATTERY".equals(request.getCommandType())) {
            newSoC = Math.min(100.0, currentSoC + deltaSoC);
        } else { // *DISCHARGE*
            newSoC = Math.max(0.0, currentSoC - deltaSoC);
        }
        // Azuriranje vrednosti
        state.setCurrentMw(powerValue);
        state.setCurrentSocPercent(newSoC);
        state.setLastUpdated(LocalDateTime.now());
        
        nodeStateRepository.save(state);
        
        log.debug("Battery {} updated: SoC {} -> {}%", 
                targetNode.getId(), currentSoC, newSoC);
    }
    
    // Cilj: Menja potrosnju
    private void updateConsumerLoad(CommandRequest request, Node targetNode) {
        NodeState state = nodeStateRepository.findById(targetNode.getId())
                .orElseThrow(() -> new RuntimeException("Node state not found: " + targetNode.getId()));
        
        state.setCurrentMw(request.getValue()); // request.value je vec negativan broj, ne treba stavljati -
        state.setLastUpdated(LocalDateTime.now());
        
        nodeStateRepository.save(state);
        
        log.debug("Consumer {} load updated to {} MW", targetNode.getId(), request.getValue());
    }
    
    // Cilj: Dobijanje istorije komandi za odredjeni cvor
    @Override
    public List<CommandDTO> getCommandHistory(String nodeId) {
        log.debug("Fetching command history for node: {}", nodeId);
        
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        
        return commandRepository.findByTargetNodeAndExecutedAtBetweenOrderByExecutedAtDesc(
                        nodeId, weekAgo, LocalDateTime.now())
                .stream()
                .map(commandMapper::toDto)
                .collect(Collectors.toList());
    }
    
    // Alerts
    
    // Cilj: Kreiranje novog Alert-a
    @Override
    @Transactional
    public void createAlert(AlertRequest request) {
        log.warn("Creating alert: {} - {}", request.getAlertType(), request.getSeverity());
        
        Alert alert = Alert.builder()
                .alertType(request.getAlertType())
                .severity(request.getSeverity())
                .description(request.getDescription())
                .resolved(false)
                .createdAt(LocalDateTime.now())
                .build();
        
        alertRepository.save(alert);
    }
    
    // Cilj: Dobijanje svih aktivnih alert-a
    @Override
    public List<AlertDTO> getActiveAlerts() {
        log.debug("Fetching active alerts");
        return alertRepository.findByResolvedFalseOrderByCreatedAtDesc()
                .stream()
                .map(alertMapper::toDto)
                .collect(Collectors.toList());
    }
    
    // Cilj: Odredjeni Alert oznacava kao resen (RESOLVED)
    @Override
    @Transactional
    public void resolveAlert(Long alertId) {
        log.info("Resolving alert: {}", alertId);
        
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found: " + alertId));
        
        alert.setResolved(true);
        alert.setResolvedAt(LocalDateTime.now());
        
        alertRepository.save(alert);
    }
    
}

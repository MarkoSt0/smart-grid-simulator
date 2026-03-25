/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.common.constants;

/**
 * Zajednicke konstante za ceo sistem
 */
public final class GridConstants {
    private GridConstants() {
        // Utility class - ne instancira se
    }
    
    // Thresholds
    public static final double EDGE_WARNING_THRESHOLD = 75.0;    // 75% utilization
    public static final double EDGE_CRITICAL_THRESHOLD = 90.0;   // 90% utilization
    public static final double BATTERY_LOW_THRESHOLD = 10.0;     // 10% SoC
    public static final double BATTERY_HIGH_THRESHOLD = 95.0;    // 95% SoC
    public static final double IMBALANCE_THRESHOLD = 5.0;        // 5 MW disbalans
    
    // Time constants
    public static final int SIMULATION_INTERVAL_MS = 2000;       // 2 sekunde
    public static final int ORCHESTRATOR_INTERVAL_MS = 2000;     // 2 sekunde
    public static final int FRONTEND_REFRESH_MS = 10000;         // 10 sekundi
    
    // Energy calculations
    public static final double TIME_STEP_HOURS = 0.25;           // 15 minuta = 0.25h
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.common.enums;

/**
 * Tipovi alarma
 */
public enum AlertType {
    EDGE_OVERLOAD,      // Kabl preopterecen
    BATTERY_CRITICAL,   // Baterija skoro prazna/puna
    IMBALANCE,          // Veliki disbalans u mrezi
    NODE_FAILURE        // Cvor pao
}

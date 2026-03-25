/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package rs.ac.bg.fon.marko.grid.common.enums;

/**
 * Tipovi komandi koje Orchestrator moze da posalje
 */
public enum CommandType {
    CHARGE_BATTERY,
    DISCHARGE_BATTERY,
    SHED_LOAD,       // Smanji potrosnju
    RESTORE_LOAD     // Vrati potrosnju na normalu
}

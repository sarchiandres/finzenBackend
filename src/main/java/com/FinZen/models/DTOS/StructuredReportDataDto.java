package com.FinZen.models.DTOS;

import java.math.BigDecimal; // Importa BigDecimal para manejar dinero con precisión
import java.time.LocalDate;
import java.util.List;
import java.util.Map; // Para secciones genéricas de clave-valor

import lombok.Data;

@Data
public class StructuredReportDataDto {

    // --- Datos Generales del Usuario/Informe ---
    private String userName;
    private String userEmail; // Si tienes el email
    private LocalDate generationDate;

    // --- Resumen Financiero ---
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal currentBalance;
    private BigDecimal totalInvestmentReturn;

    // --- Secciones de Datos Dinámicos (ej. Transacciones, Categorías) ---
    // Puedes tener una lista de secciones, donde cada sección es un mapa de clave-valor
    // O si las secciones son muy específicas, crea DTOs dedicados para ellas (ej. List<TransactionDto>)
    private List<Map<String, String>> generalInfoSection; // Ej: "Datos del Usuario"
    private List<Map<String, String>> recentTransactionsSection; // "Transacciones Recientes"
    private List<Map<String, String>> expenseCategoriesSection; // "Categorías de Gastos"

    // Si quieres algo más estructurado para transacciones o categorías, puedes definirlos:
    // private List<TransactionDto> transactions;
    // private List<CategorySummaryDto> categorySummaries;


    // Constructor vacío (necesario para frameworks)
    public StructuredReportDataDto() {}

   
}
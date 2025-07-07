package com.FinZen.services;

import com.FinZen.models.Entities.Informe;
import com.FinZen.models.Entities.Usuarios; // Asegúrate de que este es el path correcto a tu entidad de usuario
import com.FinZen.repository.InformeRepository;
import com.FinZen.repository.UsuariosRepository; // Asegúrate de que este es el path correcto a tu repositorio de usuarios
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress; // Importar para CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ---- CLASES DTO PARA ESTRUCTURAR LOS DATOS DEL REPORTE ----

class UserInfo {
    private String name;
    private String documentNumber;
    private String email;

    public UserInfo(String name, String documentNumber, String email) {
        this.name = name;
        this.documentNumber = documentNumber;
        this.email = email;
    }

    public String getName() { return name; }
    public String getDocumentNumber() { return documentNumber; }
    public String getEmail() { return email; }
}

class FinancialSummary {
    private double totalIncome;
    private double totalExpenses;
    private double currentBalance;

    public FinancialSummary(double totalIncome, double totalExpenses, double currentBalance) {
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.currentBalance = currentBalance;
    }

    public double getTotalIncome() { return totalIncome; }
    public double getTotalExpenses() { return totalExpenses; }
    public double getCurrentBalance() { return currentBalance; }
}

class Transaction {
    private String type; // "Gasto" o "Ingreso"
    private String name;
    private double amount;
    private String date;
    private String category;
    private String description;

    public Transaction(String type, String name, double amount, String date, String category, String description) {
        this.type = type;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.description = description;
    }

    public String getType() { return type; }
    public String getName() { return name; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
}

class Budget {
    private String name;
    private Long id;
    private String category;
    private double assignedAmount;
    private List<Transaction> expenses; // Gastos asociados al presupuesto

    public Budget(String name, Long id, String category, double assignedAmount) {
        this.name = name;
        this.id = id;
        this.category = category;
        this.assignedAmount = assignedAmount;
        this.expenses = new ArrayList<>();
    }

    public String getName() { return name; }
    public Long getId() { return id; }
    public String getCategory() { return category; }
    public double getAssignedAmount() { return assignedAmount; }
    public List<Transaction> getExpenses() { return expenses; }
    public void addExpense(Transaction expense) { this.expenses.add(expense); }
}

class Account {
    private String name;
    private Long id;
    private String bank;
    private double totalAmount;
    private double freeAmount;
    private double occupiedAmount;
    private List<Budget> budgets; // Presupuestos asociados a la cuenta
    private List<Transaction> incomes; // Ingresos asociados a la cuenta

    public Account(String name, Long id, String bank, double totalAmount, double freeAmount, double occupiedAmount) {
        this.name = name;
        this.id = id;
        this.bank = bank;
        this.totalAmount = totalAmount;
        this.freeAmount = freeAmount;
        this.occupiedAmount = occupiedAmount;
        this.budgets = new ArrayList<>();
        this.incomes = new ArrayList<>();
    }

    public String getName() { return name; }
    public Long getId() { return id; }
    public String getBank() { return bank; }
    public double getTotalAmount() { return totalAmount; }
    public double getFreeAmount() { return freeAmount; }
    public double getOccupiedAmount() { return occupiedAmount; }
    public List<Budget> getBudgets() { return budgets; }
    public List<Transaction> getIncomes() { return incomes; }
    public void addBudget(Budget budget) { this.budgets.add(budget); }
    public void addIncome(Transaction income) { this.incomes.add(income); }
}

class Card {
    private String name;
    private Long id;
    private String type; // CREDITO/DEBITO
    private String bank;
    private double currentBalance;
    private double creditLimit;
    private List<Budget> budgets; // Presupuestos asociados a la tarjeta
    private List<Transaction> incomes; // Ingresos asociados a la tarjeta

    public Card(String name, Long id, String type, String bank, double currentBalance, double creditLimit) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.bank = bank;
        this.currentBalance = currentBalance;
        this.creditLimit = creditLimit;
        this.budgets = new ArrayList<>();
        this.incomes = new ArrayList<>();
    }

    public String getName() { return name; }
    public Long getId() { return id; }
    public String getType() { return type; }
    public String getBank() { return bank; }
    public double getCurrentBalance() { return currentBalance; }
    public double getCreditLimit() { return creditLimit; }
    public List<Budget> getBudgets() { return budgets; }
    public List<Transaction> getIncomes() { return incomes; }
    public void addBudget(Budget budget) { this.budgets.add(budget); }
    public void addIncome(Transaction income) { this.incomes.add(income); }
}

class Investment {
    private String name;
    private Long id;
    private String type; // ACCIONES, FONDOS, CRYPTO, BONOS, CDT
    private String platform;
    private double initialValue;
    private double currentValue;
    private String startDate;
    private double expectedReturn;
    private List<Budget> budgets; // Presupuestos asociados a la inversión

    public Investment(String name, Long id, String type, String platform, double initialValue, double currentValue, String startDate, double expectedReturn) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.platform = platform;
        this.initialValue = initialValue;
        this.currentValue = currentValue;
        this.startDate = startDate;
        this.expectedReturn = expectedReturn;
        this.budgets = new ArrayList<>();
    }

    public String getName() { return name; }
    public Long getId() { return id; }
    public String getType() { return type; }
    public String getPlatform() { return platform; }
    public double getInitialValue() { return initialValue; }
    public double getCurrentValue() { return currentValue; }
    public String getStartDate() { return startDate; }
    public double getExpectedReturn() { return expectedReturn; }
    public List<Budget> getBudgets() { return budgets; }
    public void addBudget(Budget budget) { this.budgets.add(budget); }
}

// Clase principal para agrupar todos los datos del informe
class FullReportData {
    private UserInfo userInfo;
    private FinancialSummary financialSummary;
    private List<Account> accounts;
    private List<Card> cards;
    private List<Investment> investments;

    public FullReportData() {
        this.accounts = new ArrayList<>();
        this.cards = new ArrayList<>();
        this.investments = new ArrayList<>();
    }

    public UserInfo getUserInfo() { return userInfo; }
    public void setUserInfo(UserInfo userInfo) { this.userInfo = userInfo; }
    public FinancialSummary getFinancialSummary() { return financialSummary; }
    public void setFinancialSummary(FinancialSummary financialSummary) { this.financialSummary = financialSummary; }
    public List<Account> getAccounts() { return accounts; }
    public void addAccount(Account account) { this.accounts.add(account); }
    public List<Card> getCards() { return cards; }
    public void addCard(Card card) { this.cards.add(card); }
    public List<Investment> getInvestments() { return investments; }
    public void addInvestment(Investment investment) { this.investments.add(investment); }
}

// -----------------------------------------------------------

@Service
public class ReportExcelService {

    @Autowired
    private InformeRepository informeRepository;
    
    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public ByteArrayInputStream generateFullReportExcel(Long userId) throws IOException { // Cambiado el nombre del método para reflejar el informe completo
        Optional<Usuarios> userOpt = usuariosRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario con ID " + userId + " no encontrado.");
        }
        Usuarios usuario = userOpt.get();

        String contenidoInformeHtml = null;
        Long informeGeneradoId = null;

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("generar_informe_completo");
            query.registerStoredProcedureParameter("p_id_usuario", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_informe", Long.class, ParameterMode.OUT);
            query.setParameter("p_id_usuario", userId);
            query.execute();
            informeGeneradoId = (Long) query.getOutputParameterValue("p_id_informe");
        } catch (Exception e) {
            System.err.println("Error al llamar al SP 'generar_informe_completo' desde ReportExcelService: " + e.getMessage());
            throw new IOException("No se pudo generar el informe para Excel: " + e.getMessage(), e);
        }

        if (informeGeneradoId == null) {
            throw new IOException("El procedimiento almacenado no devolvió un ID de informe válido.");
        }

        Optional<Informe> informeEntityOpt = informeRepository.findById(informeGeneradoId);
        if (informeEntityOpt.isEmpty()) {
            throw new IOException("Informe con ID " + informeGeneradoId + " no encontrado después de la generación del SP.");
        }
        contenidoInformeHtml = informeEntityOpt.get().getDescripcion();

        if (contenidoInformeHtml == null || contenidoInformeHtml.isEmpty()) {
            throw new IOException("El contenido del informe HTML está vacío para el usuario con ID: " + userId);
        }
        
        // **PARSEO DEL HTML PARA EXTRAER TODOS LOS DATOS ESTRUCTURADOS**
        FullReportData reportData = parseHtmlContentForExcel(contenidoInformeHtml);
        
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Estilos
            CellStyle headerCellStyle = createHeaderStyle(workbook);
            CellStyle cellStyle = createCellStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle subtitleStyle = createSubtitleStyle(workbook);
            CellStyle subItemTitleStyle = createSubItemTitleStyle(workbook);

            // --- Hoja de Resumen General (siempre la primera) ---
            Sheet summarySheet = workbook.createSheet("Resumen General");
            int rowIndex = 0;

            // Título principal
            Row titleRow = summarySheet.createRow(rowIndex++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Informe Financiero Completo - Usuario: " + usuario.getNombre());
            summarySheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            titleCell.setCellStyle(titleStyle);

            // Fecha de generación
            Row dateRow = summarySheet.createRow(rowIndex++);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue("Fecha de Generación: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            summarySheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            dateCell.setCellStyle(dateStyle);

            rowIndex++; // Espacio en blanco

            // Información del Usuario
            if (reportData.getUserInfo() != null) {
                Row userTitleRow = summarySheet.createRow(rowIndex++);
                Cell userTitleCell = userTitleRow.createCell(0);
                userTitleCell.setCellValue("Información del Usuario");
                userTitleCell.setCellStyle(subtitleStyle);
                summarySheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 1));
                
                Row userDataRow1 = summarySheet.createRow(rowIndex++);
                userDataRow1.createCell(0).setCellValue("Nombre:");
                userDataRow1.createCell(1).setCellValue(reportData.getUserInfo().getName());
                userDataRow1.createCell(0).setCellStyle(cellStyle);
                userDataRow1.createCell(1).setCellStyle(cellStyle);

                Row userDataRow2 = summarySheet.createRow(rowIndex++);
                userDataRow2.createCell(0).setCellValue("Documento:");
                userDataRow2.createCell(1).setCellValue(reportData.getUserInfo().getDocumentNumber());
                userDataRow2.createCell(0).setCellStyle(cellStyle);
                userDataRow2.createCell(1).setCellStyle(cellStyle);

                Row userDataRow3 = summarySheet.createRow(rowIndex++);
                userDataRow3.createCell(0).setCellValue("Email:");
                userDataRow3.createCell(1).setCellValue(reportData.getUserInfo().getEmail());
                userDataRow3.createCell(0).setCellStyle(cellStyle);
                userDataRow3.createCell(1).setCellStyle(cellStyle);

                rowIndex++; // Espacio en blanco
            }

            // Resumen Financiero
            if (reportData.getFinancialSummary() != null) {
                Row summaryTitleRow = summarySheet.createRow(rowIndex++);
                Cell summaryTitleCell = summaryTitleRow.createCell(0);
                summaryTitleCell.setCellValue("Resumen Financiero");
                summaryTitleCell.setCellStyle(subtitleStyle);
                summarySheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 1));

                Row financialDataRow1 = summarySheet.createRow(rowIndex++);
                financialDataRow1.createCell(0).setCellValue("Total Ingresos:");
                financialDataRow1.createCell(1).setCellValue(reportData.getFinancialSummary().getTotalIncome());
                financialDataRow1.getCell(0).setCellStyle(cellStyle);
                financialDataRow1.getCell(1).setCellStyle(currencyStyle);

                Row financialDataRow2 = summarySheet.createRow(rowIndex++);
                financialDataRow2.createCell(0).setCellValue("Total Gastos:");
                financialDataRow2.createCell(1).setCellValue(reportData.getFinancialSummary().getTotalExpenses());
                financialDataRow2.getCell(0).setCellStyle(cellStyle);
                financialDataRow2.getCell(1).setCellStyle(currencyStyle);

                Row financialDataRow3 = summarySheet.createRow(rowIndex++);
                financialDataRow3.createCell(0).setCellValue("Balance Actual:");
                financialDataRow3.createCell(1).setCellValue(reportData.getFinancialSummary().getCurrentBalance());
                financialDataRow3.getCell(0).setCellStyle(cellStyle);
                financialDataRow3.getCell(1).setCellStyle(currencyStyle);
            }

            // Auto-ajustar columnas en la hoja de resumen
            for (int i = 0; i < 4; i++) { // Ajusta hasta 4 columnas para el resumen
                summarySheet.autoSizeColumn(i);
            }

            // --- HOJAS DETALLADAS PARA CUENTAS, TARJETAS, INVERSIONES ---

            // Sección de Cuentas
            if (!reportData.getAccounts().isEmpty()) {
                Sheet accountsSheet = workbook.createSheet("Cuentas");
                addAccountsToSheet(accountsSheet, reportData.getAccounts(), headerCellStyle, cellStyle, currencyStyle, subtitleStyle, subItemTitleStyle);
            }

            // Sección de Tarjetas
            if (!reportData.getCards().isEmpty()) {
                Sheet cardsSheet = workbook.createSheet("Tarjetas");
                addCardsToSheet(cardsSheet, reportData.getCards(), headerCellStyle, cellStyle, currencyStyle, subtitleStyle, subItemTitleStyle);
            }

            // Sección de Inversiones
            if (!reportData.getInvestments().isEmpty()) {
                Sheet investmentsSheet = workbook.createSheet("Inversiones");
                addInvestmentsToSheet(investmentsSheet, reportData.getInvestments(), headerCellStyle, cellStyle, currencyStyle, subtitleStyle, subItemTitleStyle);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // --- Métodos de Ayuda para Creación de Estilos ---
    private CellStyle createHeaderStyle(Workbook workbook) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        CellStyle style = workbook.createCellStyle();
        style.setFont(headerFont);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(createCellStyle(workbook));
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("$#,##0.00"));
        return style;
    }

    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setItalic(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createSubtitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.MEDIUM);
        return style;
    }

    private CellStyle createSubItemTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        return style;
    }

    // Método auxiliar para aplicar estilos a todas las celdas de una fila
    private void applyStylesToRow(Row row, CellStyle style, int numberOfCells) {
        for (int i = 0; i < numberOfCells; i++) {
            Cell cell = row.getCell(i);
            if (cell == null) {
                cell = row.createCell(i);
            }
            cell.setCellStyle(style);
        }
    }

    private void autoSizeAllColumns(Sheet sheet, int maxCols) {
        for (int i = 0; i < maxCols; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    // --- Métodos para Añadir Secciones a las Hojas de Excel ---

    private void addAccountsToSheet(Sheet sheet, List<Account> accounts, CellStyle headerStyle, CellStyle cellStyle, CellStyle currencyStyle, CellStyle subtitleStyle, CellStyle subItemTitleStyle) {
        int rowIndex = 0;
        Row sectionTitleRow = sheet.createRow(rowIndex++);
        Cell sectionTitleCell = sectionTitleRow.createCell(0);
        sectionTitleCell.setCellValue("Detalle de Cuentas");
        sectionTitleCell.setCellStyle(subtitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 5));
        rowIndex++; // Espacio

        for (Account account : accounts) {
            Row accountTitleRow = sheet.createRow(rowIndex++);
            Cell accountTitleCell = accountTitleRow.createCell(0);
            accountTitleCell.setCellValue("Cuenta: " + account.getName() + " (ID: " + account.getId() + ")");
            accountTitleCell.setCellStyle(subItemTitleStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 5));
            
            Row accountDetailsRow1 = sheet.createRow(rowIndex++);
            accountDetailsRow1.createCell(0).setCellValue("Banco:");
            accountDetailsRow1.createCell(1).setCellValue(account.getBank());
            accountDetailsRow1.createCell(2).setCellValue("Monto Total:");
            accountDetailsRow1.createCell(3).setCellValue(account.getTotalAmount());
            accountDetailsRow1.getCell(3).setCellStyle(currencyStyle);
            applyStylesToRow(accountDetailsRow1, cellStyle, 4);

            Row accountDetailsRow2 = sheet.createRow(rowIndex++);
            accountDetailsRow2.createCell(0).setCellValue("Monto Libre:");
            accountDetailsRow2.createCell(1).setCellValue(account.getFreeAmount());
            accountDetailsRow2.getCell(1).setCellStyle(currencyStyle);
            accountDetailsRow2.createCell(2).setCellValue("Monto Ocupado:");
            accountDetailsRow2.createCell(3).setCellValue(account.getOccupiedAmount());
            accountDetailsRow2.getCell(3).setCellStyle(currencyStyle);
            applyStylesToRow(accountDetailsRow2, cellStyle, 4);

            rowIndex++; // Espacio

            // Presupuestos de la Cuenta
            if (!account.getBudgets().isEmpty()) {
                Row budgetSubSectionTitleRow = sheet.createRow(rowIndex++);
                Cell budgetSubSectionTitleCell = budgetSubSectionTitleRow.createCell(0);
                budgetSubSectionTitleCell.setCellValue("Presupuestos asociados a " + account.getName());
                budgetSubSectionTitleCell.setCellStyle(subtitleStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 5));
                rowIndex++; // Espacio

                String[] budgetHeaders = {"Nombre Presupuesto", "ID", "Categoría", "Monto Asignado"};
                Row budgetHeaderRow = sheet.createRow(rowIndex++);
                for (int i = 0; i < budgetHeaders.length; i++) {
                    Cell cell = budgetHeaderRow.createCell(i);
                    cell.setCellValue(budgetHeaders[i]);
                    cell.setCellStyle(headerStyle);
                }

                for (Budget budget : account.getBudgets()) {
                    Row budgetRow = sheet.createRow(rowIndex++);
                    budgetRow.createCell(0).setCellValue(budget.getName());
                    budgetRow.createCell(1).setCellValue(budget.getId());
                    budgetRow.createCell(2).setCellValue(budget.getCategory());
                    budgetRow.createCell(3).setCellValue(budget.getAssignedAmount());
                    budgetRow.getCell(3).setCellStyle(currencyStyle);
                    applyStylesToRow(budgetRow, cellStyle, budgetHeaders.length);

                    // Gastos del Presupuesto
                    if (!budget.getExpenses().isEmpty()) {
                        rowIndex++; // Espacio
                        Row expenseSubHeaderRow = sheet.createRow(rowIndex++);
                        expenseSubHeaderRow.createCell(0).setCellValue("Gastos de " + budget.getName());
                        expenseSubHeaderRow.createCell(1).setCellValue("Monto");
                        expenseSubHeaderRow.createCell(2).setCellValue("Fecha");
                        expenseSubHeaderRow.createCell(3).setCellValue("Categoría");
                        expenseSubHeaderRow.createCell(4).setCellValue("Descripción");
                        applyStylesToRow(expenseSubHeaderRow, headerStyle, 5);
                        
                        for (Transaction expense : budget.getExpenses()) {
                            Row expenseRow = sheet.createRow(rowIndex++);
                            expenseRow.createCell(0).setCellValue(expense.getName());
                            expenseRow.createCell(1).setCellValue(expense.getAmount());
                            expenseRow.createCell(2).setCellValue(expense.getDate());
                            expenseRow.createCell(3).setCellValue(expense.getCategory());
                            expenseRow.createCell(4).setCellValue(expense.getDescription());
                            expenseRow.getCell(1).setCellStyle(currencyStyle);
                            applyStylesToRow(expenseRow, cellStyle, 5);
                        }
                        rowIndex++; // Espacio
                    }
                }
                rowIndex++; // Espacio
            }

            // Ingresos de la Cuenta
            if (!account.getIncomes().isEmpty()) {
                Row incomeSubSectionTitleRow = sheet.createRow(rowIndex++);
                Cell incomeSubSectionTitleCell = incomeSubSectionTitleRow.createCell(0);
                incomeSubSectionTitleCell.setCellValue("Ingresos asociados a " + account.getName());
                incomeSubSectionTitleCell.setCellStyle(subtitleStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 5));
                rowIndex++; // Espacio

                String[] incomeHeaders = {"Nombre Ingreso", "Monto", "Fecha", "Categoría", "Descripción"};
                Row incomeHeaderRow = sheet.createRow(rowIndex++);
                for (int i = 0; i < incomeHeaders.length; i++) {
                    Cell cell = incomeHeaderRow.createCell(i);
                    cell.setCellValue(incomeHeaders[i]);
                    cell.setCellStyle(headerStyle);
                }

                for (Transaction income : account.getIncomes()) {
                    Row incomeRow = sheet.createRow(rowIndex++);
                    incomeRow.createCell(0).setCellValue(income.getName());
                    incomeRow.createCell(1).setCellValue(income.getAmount());
                    incomeRow.createCell(2).setCellValue(income.getDate());
                    incomeRow.createCell(3).setCellValue(income.getCategory());
                    incomeRow.createCell(4).setCellValue(income.getDescription());
                    incomeRow.getCell(1).setCellStyle(currencyStyle);
                    applyStylesToRow(incomeRow, cellStyle, incomeHeaders.length);
                }
                rowIndex++; // Espacio
            }
            rowIndex++; // Espacio entre cuentas
        }
        autoSizeAllColumns(sheet, 6); // Ajustar todas las columnas después de agregar todos los datos
    }

    private void addCardsToSheet(Sheet sheet, List<Card> cards, CellStyle headerStyle, CellStyle cellStyle, CellStyle currencyStyle, CellStyle subtitleStyle, CellStyle subItemTitleStyle) {
        int rowIndex = 0;
        Row sectionTitleRow = sheet.createRow(rowIndex++);
        Cell sectionTitleCell = sectionTitleRow.createCell(0);
        sectionTitleCell.setCellValue("Detalle de Tarjetas");
        sectionTitleCell.setCellStyle(subtitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 5));
        rowIndex++; // Espacio

        for (Card card : cards) {
            Row cardTitleRow = sheet.createRow(rowIndex++);
            Cell cardTitleCell = cardTitleRow.createCell(0);
            cardTitleCell.setCellValue("Tarjeta: " + card.getName() + " (ID: " + card.getId() + ")");
            cardTitleCell.setCellStyle(subItemTitleStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 5));

            Row cardDetailsRow1 = sheet.createRow(rowIndex++);
            cardDetailsRow1.createCell(0).setCellValue("Tipo:");
            cardDetailsRow1.createCell(1).setCellValue(card.getType());
            cardDetailsRow1.createCell(2).setCellValue("Banco:");
            cardDetailsRow1.createCell(3).setCellValue(card.getBank());
            applyStylesToRow(cardDetailsRow1, cellStyle, 4);

            Row cardDetailsRow2 = sheet.createRow(rowIndex++);
            cardDetailsRow2.createCell(0).setCellValue("Saldo Actual:");
            cardDetailsRow2.createCell(1).setCellValue(card.getCurrentBalance());
            cardDetailsRow2.getCell(1).setCellStyle(currencyStyle);
            cardDetailsRow2.createCell(2).setCellValue("Límite de Crédito:");
            cardDetailsRow2.createCell(3).setCellValue(card.getCreditLimit());
            cardDetailsRow2.getCell(3).setCellStyle(currencyStyle);
            applyStylesToRow(cardDetailsRow2, cellStyle, 4);

            rowIndex++; // Espacio

            // Presupuestos de la Tarjeta
            if (!card.getBudgets().isEmpty()) {
                Row budgetSubSectionTitleRow = sheet.createRow(rowIndex++);
                Cell budgetSubSectionTitleCell = budgetSubSectionTitleRow.createCell(0);
                budgetSubSectionTitleCell.setCellValue("Presupuestos asociados a " + card.getName());
                budgetSubSectionTitleCell.setCellStyle(subtitleStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 5));
                rowIndex++; // Espacio

                String[] budgetHeaders = {"Nombre Presupuesto", "ID", "Categoría", "Monto Asignado"};
                Row budgetHeaderRow = sheet.createRow(rowIndex++);
                for (int i = 0; i < budgetHeaders.length; i++) {
                    Cell cell = budgetHeaderRow.createCell(i);
                    cell.setCellValue(budgetHeaders[i]);
                    cell.setCellStyle(headerStyle);
                }

                for (Budget budget : card.getBudgets()) {
                    Row budgetRow = sheet.createRow(rowIndex++);
                    budgetRow.createCell(0).setCellValue(budget.getName());
                    budgetRow.createCell(1).setCellValue(budget.getId());
                    budgetRow.createCell(2).setCellValue(budget.getCategory());
                    budgetRow.createCell(3).setCellValue(budget.getAssignedAmount());
                    budgetRow.getCell(3).setCellStyle(currencyStyle);
                    applyStylesToRow(budgetRow, cellStyle, budgetHeaders.length);

                    // Gastos del Presupuesto
                    if (!budget.getExpenses().isEmpty()) {
                        rowIndex++; // Espacio
                        Row expenseSubHeaderRow = sheet.createRow(rowIndex++);
                        expenseSubHeaderRow.createCell(0).setCellValue("Gastos de " + budget.getName());
                        expenseSubHeaderRow.createCell(1).setCellValue("Monto");
                        expenseSubHeaderRow.createCell(2).setCellValue("Fecha");
                        expenseSubHeaderRow.createCell(3).setCellValue("Categoría");
                        expenseSubHeaderRow.createCell(4).setCellValue("Descripción");
                        applyStylesToRow(expenseSubHeaderRow, headerStyle, 5);
                        
                        for (Transaction expense : budget.getExpenses()) {
                            Row expenseRow = sheet.createRow(rowIndex++);
                            expenseRow.createCell(0).setCellValue(expense.getName());
                            expenseRow.createCell(1).setCellValue(expense.getAmount());
                            expenseRow.createCell(2).setCellValue(expense.getDate());
                            expenseRow.createCell(3).setCellValue(expense.getCategory());
                            expenseRow.createCell(4).setCellValue(expense.getDescription());
                            expenseRow.getCell(1).setCellStyle(currencyStyle);
                            applyStylesToRow(expenseRow, cellStyle, 5);
                        }
                        rowIndex++; // Espacio
                    }
                }
                rowIndex++; // Espacio
            }

            // Ingresos de la Tarjeta
            if (!card.getIncomes().isEmpty()) {
                Row incomeSubSectionTitleRow = sheet.createRow(rowIndex++);
                Cell incomeSubSectionTitleCell = incomeSubSectionTitleRow.createCell(0);
                incomeSubSectionTitleCell.setCellValue("Ingresos asociados a " + card.getName());
                incomeSubSectionTitleCell.setCellStyle(subtitleStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 5));
                rowIndex++; // Espacio

                String[] incomeHeaders = {"Nombre Ingreso", "Monto", "Fecha", "Categoría", "Descripción"};
                Row incomeHeaderRow = sheet.createRow(rowIndex++);
                for (int i = 0; i < incomeHeaders.length; i++) {
                    Cell cell = incomeHeaderRow.createCell(i);
                    cell.setCellValue(incomeHeaders[i]);
                    cell.setCellStyle(headerStyle);
                }

                for (Transaction income : card.getIncomes()) {
                    Row incomeRow = sheet.createRow(rowIndex++);
                    incomeRow.createCell(0).setCellValue(income.getName());
                    incomeRow.createCell(1).setCellValue(income.getAmount());
                    incomeRow.createCell(2).setCellValue(income.getDate());
                    incomeRow.createCell(3).setCellValue(income.getCategory());
                    incomeRow.createCell(4).setCellValue(income.getDescription());
                    incomeRow.getCell(1).setCellStyle(currencyStyle);
                    applyStylesToRow(incomeRow, cellStyle, incomeHeaders.length);
                }
                rowIndex++; // Espacio
            }
            rowIndex++; // Espacio entre tarjetas
        }
        autoSizeAllColumns(sheet, 6); // Ajustar todas las columnas después de agregar todos los datos
    }

    private void addInvestmentsToSheet(Sheet sheet, List<Investment> investments, CellStyle headerStyle, CellStyle cellStyle, CellStyle currencyStyle, CellStyle subtitleStyle, CellStyle subItemTitleStyle) {
        int rowIndex = 0;
        Row sectionTitleRow = sheet.createRow(rowIndex++);
        Cell sectionTitleCell = sectionTitleRow.createCell(0);
        sectionTitleCell.setCellValue("Detalle de Inversiones");
        sectionTitleCell.setCellStyle(subtitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 6)); // Ajusta el rango
        rowIndex++; // Espacio

        for (Investment investment : investments) {
            Row investmentTitleRow = sheet.createRow(rowIndex++);
            Cell investmentTitleCell = investmentTitleRow.createCell(0);
            investmentTitleCell.setCellValue("Inversión: " + investment.getName() + " (ID: " + investment.getId() + ")");
            investmentTitleCell.setCellStyle(subItemTitleStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 6));

            Row investmentDetailsRow1 = sheet.createRow(rowIndex++);
            investmentDetailsRow1.createCell(0).setCellValue("Tipo:");
            investmentDetailsRow1.createCell(1).setCellValue(investment.getType());
            investmentDetailsRow1.createCell(2).setCellValue("Plataforma:");
            investmentDetailsRow1.createCell(3).setCellValue(investment.getPlatform());
            applyStylesToRow(investmentDetailsRow1, cellStyle, 4);

            Row investmentDetailsRow2 = sheet.createRow(rowIndex++);
            investmentDetailsRow2.createCell(0).setCellValue("Valor Inicial:");
            investmentDetailsRow2.createCell(1).setCellValue(investment.getInitialValue());
            investmentDetailsRow2.getCell(1).setCellStyle(currencyStyle);
            investmentDetailsRow2.createCell(2).setCellValue("Valor Actual:");
            investmentDetailsRow2.createCell(3).setCellValue(investment.getCurrentValue());
            investmentDetailsRow2.getCell(3).setCellStyle(currencyStyle);
            applyStylesToRow(investmentDetailsRow2, cellStyle, 4);

            Row investmentDetailsRow3 = sheet.createRow(rowIndex++);
            investmentDetailsRow3.createCell(0).setCellValue("Fecha Inicio:");
            investmentDetailsRow3.createCell(1).setCellValue(investment.getStartDate());
            investmentDetailsRow3.createCell(2).setCellValue("Retorno Esperado:");
            investmentDetailsRow3.createCell(3).setCellValue(investment.getExpectedReturn());
            investmentDetailsRow3.getCell(3).setCellStyle(currencyStyle);
            applyStylesToRow(investmentDetailsRow3, cellStyle, 4);

            rowIndex++; // Espacio

            // Presupuestos de la Inversión
            if (!investment.getBudgets().isEmpty()) {
                Row budgetSubSectionTitleRow = sheet.createRow(rowIndex++);
                Cell budgetSubSectionTitleCell = budgetSubSectionTitleRow.createCell(0);
                budgetSubSectionTitleCell.setCellValue("Presupuestos asociados a " + investment.getName());
                budgetSubSectionTitleCell.setCellStyle(subtitleStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 6));
                rowIndex++; // Espacio

                String[] budgetHeaders = {"Nombre Presupuesto", "ID", "Categoría", "Monto Asignado"};
                Row budgetHeaderRow = sheet.createRow(rowIndex++);
                for (int i = 0; i < budgetHeaders.length; i++) {
                    Cell cell = budgetHeaderRow.createCell(i);
                    cell.setCellValue(budgetHeaders[i]);
                    cell.setCellStyle(headerStyle);
                }

                for (Budget budget : investment.getBudgets()) {
                    Row budgetRow = sheet.createRow(rowIndex++);
                    budgetRow.createCell(0).setCellValue(budget.getName());
                    budgetRow.createCell(1).setCellValue(budget.getId());
                    budgetRow.createCell(2).setCellValue(budget.getCategory());
                    budgetRow.createCell(3).setCellValue(budget.getAssignedAmount());
                    budgetRow.getCell(3).setCellStyle(currencyStyle);
                    applyStylesToRow(budgetRow, cellStyle, budgetHeaders.length);

                    // Gastos del Presupuesto
                    if (!budget.getExpenses().isEmpty()) {
                        rowIndex++; // Espacio
                        Row expenseSubHeaderRow = sheet.createRow(rowIndex++);
                        expenseSubHeaderRow.createCell(0).setCellValue("Gastos de " + budget.getName());
                        expenseSubHeaderRow.createCell(1).setCellValue("Monto");
                        expenseSubHeaderRow.createCell(2).setCellValue("Fecha");
                        expenseSubHeaderRow.createCell(3).setCellValue("Categoría");
                        expenseSubHeaderRow.createCell(4).setCellValue("Descripción");
                        applyStylesToRow(expenseSubHeaderRow, headerStyle, 5);
                        
                        for (Transaction expense : budget.getExpenses()) {
                            Row expenseRow = sheet.createRow(rowIndex++);
                            expenseRow.createCell(0).setCellValue(expense.getName());
                            expenseRow.createCell(1).setCellValue(expense.getAmount());
                            expenseRow.createCell(2).setCellValue(expense.getDate());
                            expenseRow.createCell(3).setCellValue(expense.getCategory());
                            expenseRow.createCell(4).setCellValue(expense.getDescription());
                            expenseRow.getCell(1).setCellStyle(currencyStyle);
                            applyStylesToRow(expenseRow, cellStyle, 5);
                        }
                        rowIndex++; // Espacio
                    }
                }
                rowIndex++; // Espacio
            }
            rowIndex++; // Espacio entre inversiones
        }
        autoSizeAllColumns(sheet, 7); // Ajustar todas las columnas después de agregar todos los datos
    }


    // --- MÉTODO CLAVE: PARSEO DE HTML A ESTRUCTURA DE DATOS (FullReportData) ---
    private FullReportData parseHtmlContentForExcel(String htmlContent) {
        FullReportData reportData = new FullReportData();
        Document doc = Jsoup.parse(htmlContent);

        // Parsear Información del Usuario
        Element userInfoSection = doc.selectFirst(".user-info-section");
        if (userInfoSection != null) {
            String name = userInfoSection.selectFirst(".user-name span") != null ? userInfoSection.selectFirst(".user-name span").text().trim() : "N/A";
            String document = userInfoSection.selectFirst(".user-document span") != null ? userInfoSection.selectFirst(".user-document span").text().trim() : "N/A";
            String email = userInfoSection.selectFirst(".user-email span") != null ? userInfoSection.selectFirst(".user-email span").text().trim() : "N/A";
            reportData.setUserInfo(new UserInfo(name, document, email));
        }

        // Parsear Resumen Financiero
        Element financialSummarySection = doc.selectFirst(".financial-summary-section");
        if (financialSummarySection != null) {
            double totalIncome = parseAmount(financialSummarySection.selectFirst(".total-income span"));
            double totalExpenses = parseAmount(financialSummarySection.selectFirst(".total-expenses span"));
            double currentBalance = parseAmount(financialSummarySection.selectFirst(".current-balance span"));
            reportData.setFinancialSummary(new FinancialSummary(totalIncome, totalExpenses, currentBalance));
        }

        // Parsear otras secciones (Cuentas, Tarjetas, Inversiones) con lógica similar...
        return reportData;
    }

    // --- Métodos de Ayuda para Parseo de HTML ---

    // Parsea un valor monetario (ej. "$1,234.56" o "1.234,56") a double
    private double parseAmount(Element element) {
        if (element == null) {
            return 0.0;
        }
        String text = element.text().trim();
        // Elimina símbolos de moneda, comas de miles y convierte comas decimales a puntos
        text = text.replace("$", "").replace("€", "").replace(",", "").trim(); // Elimina símbolos y comas
        // Si el separador decimal es una coma, cámbialo a punto
        if (text.contains(",")) {
            text = text.replace(",", ".");
        }
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0.0; // Devuelve 0.0 si no se puede parsear
        }
    }

    // Extrae el valor de un elemento dado un selector CSS
    private String extractValue(Document doc, String cssSelector) {
        Element element = doc.selectFirst(cssSelector);
        return (element != null) ? element.text().trim() : null;
    }

    // Extrae el ID de una cadena que contiene "(ID: X)"
    private Long parseIdFromText(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        Pattern pattern = Pattern.compile("\\(ID:\\s*(\\d+)\\)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            try {
                return Long.parseLong(matcher.group(1));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
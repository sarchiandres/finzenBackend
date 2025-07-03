import java.math.BigDecimal;

public class Estadisticas {
    private int totalMetas;
    private int metasCompletadas;
    private BigDecimal totalObjetivo;
    private BigDecimal totalAhorrado;
    private double porcentajeCompletado;

    public int getTotalMetas() {
        return totalMetas;
    }

    public void setTotalMetas(int totalMetas) {
        this.totalMetas = totalMetas;
    }

    public int getMetasCompletadas() {
        return metasCompletadas;
    }

    public void setMetasCompletadas(int metasCompletadas) {
        this.metasCompletadas = metasCompletadas;
    }

    public BigDecimal getTotalObjetivo() {
        return totalObjetivo;
    }

    public void setTotalObjetivo(BigDecimal totalObjetivo) {
        this.totalObjetivo = totalObjetivo;
    }

    public BigDecimal getTotalAhorrado() {
        return totalAhorrado;
    }

    public void setTotalAhorrado(BigDecimal totalAhorrado) {
        this.totalAhorrado = totalAhorrado;
    }

    public double getPorcentajeCompletado() {
        return porcentajeCompletado;
    }

    public void setPorcentajeCompletado(double porcentajeCompletado) {
        this.porcentajeCompletado = porcentajeCompletado;
    }
}
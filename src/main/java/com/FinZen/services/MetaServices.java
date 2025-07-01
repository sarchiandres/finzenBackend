package com.FinZen.services;

import com.FinZen.models.DTOS.Estadisticas;
import com.FinZen.models.DTOS.MetaDto;
import com.FinZen.models.Entities.Cuenta;
import com.FinZen.models.Entities.Meta;
import com.FinZen.repository.CuentaRepository;
import com.FinZen.repository.MetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class MetaServices {

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    public Meta createMeta(MetaDto metaDto) {
        Cuenta cuenta = cuentaRepository.findById(metaDto.getIdCuenta())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        if (metaDto.getMontoAhorrado().compareTo(metaDto.getValor()) > 0) {
            throw new IllegalArgumentException("El monto ahorrado inicial no puede exceder el valor objetivo");
        }

        Meta meta = new Meta();
        meta.setTitulo(metaDto.getTitulo());
        meta.setDescripcion(metaDto.getDescripcion());
        meta.setEstado(metaDto.getEstado());
        meta.setValor(metaDto.getValor());
        meta.setMontoAhorrado(metaDto.getMontoAhorrado());
        meta.setFechaInicio(metaDto.getFechaInicio());
        meta.setFechaLimite(metaDto.getFechaLimite());
        meta.setEnProgreso(metaDto.getEstado().equalsIgnoreCase("iniciado"));
        meta.setCuenta(cuenta);
        meta.setIcon(metaDto.getIcon());

        return metaRepository.save(meta);
    }

    public List<Meta> getAllMetas(Long idCuenta) {
        return metaRepository.findByCuentaIdCuenta(idCuenta);
    }

    public void deleteMeta(Long idMeta) {
        Meta meta = metaRepository.findById(idMeta)
                .orElseThrow(() -> new IllegalArgumentException("Meta no encontrada"));
        metaRepository.delete(meta);
    }

    public void updateEstado(Long idMeta, String nuevoEstado) {
        Meta meta = metaRepository.findById(idMeta)
                .orElseThrow(() -> new IllegalArgumentException("Meta no encontrada"));

        if ("terminado".equals(meta.getEstado()) && !"terminado".equals(nuevoEstado)) {
            throw new IllegalArgumentException("No se puede cambiar una meta terminada a otro estado");
        }

        meta.setEstado(nuevoEstado);
        meta.setEnProgreso(nuevoEstado.equalsIgnoreCase("iniciado"));
        metaRepository.save(meta);
    }

    public MetaDto agregarAporte(Long idMeta, BigDecimal montoAporte) {
        Meta meta = metaRepository.findById(idMeta)
                .orElseThrow(() -> new IllegalArgumentException("Meta no encontrada"));

        BigDecimal nuevoTotal = meta.getMontoAhorrado().add(montoAporte);
        if (nuevoTotal.compareTo(meta.getValor()) > 0) {
            throw new IllegalArgumentException("El monto ahorrado no puede exceder el valor objetivo de la meta");
        }

        meta.setMontoAhorrado(nuevoTotal);
        metaRepository.save(meta);

        MetaDto metaDto = new MetaDto();
        metaDto.setIdMeta(meta.getIdMeta());
        metaDto.setTitulo(meta.getTitulo());
        metaDto.setDescripcion(meta.getDescripcion());
        metaDto.setEstado(meta.getEstado());
        metaDto.setValor(meta.getValor());
        metaDto.setMontoAhorrado(meta.getMontoAhorrado());
        metaDto.setIdCuenta(meta.getCuenta().getIdCuenta());
        metaDto.setFechaInicio(meta.getFechaInicio());
        metaDto.setFechaLimite(meta.getFechaLimite());
        metaDto.setEnProgreso(meta.getEnProgreso());
        metaDto.setIcon(meta.getIcon());
        return metaDto;
    }

    public List<Meta> getProximasMetas(Long idCuenta, int dias) {
        LocalDate ahora = LocalDate.now();
        LocalDate limite = ahora.plusDays(dias);
        return metaRepository.findMetasProximas(idCuenta, limite);
    }

    public Estadisticas getEstadisticas(Long idCuenta) {
        List<Meta> metas = metaRepository.findByCuentaIdCuenta(idCuenta);
        Estadisticas stats = new Estadisticas();
        stats.setTotalMetas(metas.size());
        stats.setMetasCompletadas((int) metas.stream()
                .filter(m -> "terminado".equals(m.getEstado()) || m.getMontoAhorrado().compareTo(m.getValor()) >= 0)
                .count());
        stats.setTotalObjetivo(metas.stream()
                .map(Meta::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        stats.setTotalAhorrado(metas.stream()
                .map(Meta::getMontoAhorrado)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        stats.setPorcentajeCompletado(
                stats.getTotalObjetivo().compareTo(BigDecimal.ZERO) > 0 ?
                        stats.getTotalAhorrado().divide(stats.getTotalObjetivo(), 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(BigDecimal.valueOf(100)).doubleValue() : 0.0
        );
        return stats;
    }
}
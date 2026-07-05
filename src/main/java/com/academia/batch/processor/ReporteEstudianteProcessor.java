package com.academia.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.academia.batch.model.Estudiante;
import com.academia.batch.model.EstudianteReporte;

public class ReporteEstudianteProcessor implements ItemProcessor<Estudiante, EstudianteReporte> {

    private static final Logger logger = LoggerFactory.getLogger(ReporteEstudianteProcessor.class);

    @Override
    public EstudianteReporte process(Estudiante estudiante) throws Exception {
        // Crear nuevo reporte
        EstudianteReporte reporte = new EstudianteReporte();

        // Copiar datos del estudiante
        reporte.setNombre(estudiante.getNombre());
        reporte.setGrupo(estudiante.getGrupo());
        reporte.setPromedio(estudiante.getPromedio());

        // Asignar estado basado en el promedio
        if (estudiante.getPromedio() >= 70) {
            reporte.setEstado("APROBADO");
        } else {
            reporte.setEstado("REPROBADO");
        }

        // Registrar log
        logger.info("Step 2 - Reporte: {}", reporte);

        // Devolver el reporte
        return reporte;
    }
}


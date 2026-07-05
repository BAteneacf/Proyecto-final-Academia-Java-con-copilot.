package com.academia.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.academia.batch.model.Estudiante;

public class EstudianteProcessor implements ItemProcessor<Estudiante, Estudiante> {

    private static final Logger logger = LoggerFactory.getLogger(EstudianteProcessor.class);

    @Override
    public Estudiante process(Estudiante estudiante) throws Exception {
        // Calcular el promedio
        double promedio = (estudiante.getNota1() + estudiante.getNota2() + estudiante.getNota3()) / 3;

        // Asignar el promedio al estudiante
        estudiante.setPromedio(promedio);

        // Registrar log
        logger.info("Step 1 - Procesando: {}", estudiante);

        // Devolver el estudiante
        return estudiante;
    }
}


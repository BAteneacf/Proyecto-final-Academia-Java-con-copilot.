package com.academia.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import com.academia.batch.model.Estudiante;
import com.academia.batch.model.EstudianteReporte;
import com.academia.batch.processor.EstudianteProcessor;
import com.academia.batch.processor.ReporteEstudianteProcessor;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class BatchConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MongoTemplate mongoTemplate;

    // ==================== STEP 1: READER ====================
    @Bean
    public FlatFileItemReader<Estudiante> estudianteReader() {
        FlatFileItemReader<Estudiante> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("estudiantes.csv"));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<Estudiante>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("nombre", "grupo", "nota1", "nota2", "nota3");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Estudiante>() {{
                setTargetType(Estudiante.class);
            }});
        }});
        return reader;
    }

    // ==================== STEP 1: PROCESSOR ====================
    @Bean
    public EstudianteProcessor estudianteProcessor() {
        return new EstudianteProcessor();
    }

    // ==================== STEP 1: WRITER ====================
    @Bean
    public JdbcBatchItemWriter<Estudiante> estudianteWriter() {
        JdbcBatchItemWriter<Estudiante> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO estudiantes_procesados (nombre, grupo, nota1, nota2, nota3, promedio) " +
                "VALUES (:nombre, :grupo, :nota1, :nota2, :nota3, :promedio)");
        writer.setItemSqlParameterSourceProvider(new org.springframework.batch.item.database.ItemSqlParameterSourceProvider<Estudiante>() {
            @Override
            public org.springframework.jdbc.core.namedparam.SqlParameterSource createSqlParameterSource(Estudiante item) {
                return new org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource(item);
            }
        });
        return writer;
    }

    // ==================== STEP 1 ====================
    @Bean
    public Step paso1(FlatFileItemReader<Estudiante> estudianteReader,
                      EstudianteProcessor estudianteProcessor,
                      JdbcBatchItemWriter<Estudiante> estudianteWriter) {
        return new StepBuilder("paso1", jobRepository)
                .<Estudiante, Estudiante>chunk(3, transactionManager)
                .reader(estudianteReader)
                .processor(estudianteProcessor)
                .writer(estudianteWriter)
                .build();
    }

    // ==================== STEP 2: READER ====================
    @Bean
    public JdbcCursorItemReader<Estudiante> reporteReader() {
        JdbcCursorItemReader<Estudiante> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT nombre, grupo, promedio FROM estudiantes_procesados");
        reader.setRowMapper(new RowMapper<Estudiante>() {
            @Override
            public Estudiante mapRow(ResultSet rs, int rowNum) throws SQLException {
                Estudiante estudiante = new Estudiante();
                estudiante.setNombre(rs.getString("nombre"));
                estudiante.setGrupo(rs.getString("grupo"));
                estudiante.setPromedio(rs.getDouble("promedio"));
                return estudiante;
            }
        });
        return reader;
    }

    // ==================== STEP 2: PROCESSOR ====================
    @Bean
    public ReporteEstudianteProcessor reporteProcessor() {
        return new ReporteEstudianteProcessor();
    }

    @Bean
    public MongoItemWriter<EstudianteReporte> reporteWriter() {
        MongoItemWriter<EstudianteReporte> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("reportes_estudiantes");
        return writer;
    }

    // ==================== STEP 2 ====================
    @Bean
    public Step paso2(JdbcCursorItemReader<Estudiante> reporteReader,
                      ReporteEstudianteProcessor reporteProcessor,
                      MongoItemWriter<EstudianteReporte> reporteWriter) {
        return new StepBuilder("paso2", jobRepository)
                .<Estudiante, EstudianteReporte>chunk(3, transactionManager)
                .reader(reporteReader)
                .processor(reporteProcessor)
                .writer(reporteWriter)
                .build();
    }

    // ==================== JOB ====================
    @Bean
    public Job procesarCalificacionesJob(Step paso1, Step paso2) {
        return new JobBuilder("procesarCalificacionesJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(paso1)
                .next(paso2)
                .build();
    }
}


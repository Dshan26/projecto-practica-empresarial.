package com.gft.recruitment.test.infrastructure.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("test_questions")
public class TestQuestionEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Transient
    private boolean isNew = true;

    @Column("test_id")
    private UUID testId;

    @Column("enunciado")
    private String enunciado;

    @Column("tipo")
    private String tipo;

    @Column("opciones")
    private String opciones;

    @Column("respuesta_correcta")
    private String respuestaCorrecta;

    @Column("orden")
    private int orden;

    public TestQuestionEntity() {
    }

    @Override
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTestId() { return testId; }
    public void setTestId(UUID testId) { this.testId = testId; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getOpciones() { return opciones; }
    public void setOpciones(String opciones) { this.opciones = opciones; }

    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

    @Override
    @Transient
    public boolean isNew() { return isNew; }
    public void setNew(boolean isNew) { this.isNew = isNew; }
}

package com.gft.recruitment.test.domain.model;

import java.util.List;
import java.util.UUID;

public class TestQuestion {

    private UUID id;
    private UUID testId;
    private String enunciado;
    private QuestionType tipo;
    private List<String> opciones;
    private String respuestaCorrecta;
    private int orden;

    public TestQuestion() {
    }

    public TestQuestion(UUID id, UUID testId, String enunciado, QuestionType tipo,
                        List<String> opciones, String respuestaCorrecta, int orden) {
        this.id = id;
        this.testId = testId;
        this.enunciado = enunciado;
        this.tipo = tipo;
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
        this.orden = orden;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTestId() { return testId; }
    public void setTestId(UUID testId) { this.testId = testId; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public QuestionType getTipo() { return tipo; }
    public void setTipo(QuestionType tipo) { this.tipo = tipo; }

    public List<String> getOpciones() { return opciones; }
    public void setOpciones(List<String> opciones) { this.opciones = opciones; }

    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }
}

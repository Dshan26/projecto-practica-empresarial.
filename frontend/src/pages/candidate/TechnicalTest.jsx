import React, { useState, useEffect } from 'react';
import axiosClient from '../../api/axiosClient';

export default function TechnicalTest() {
  const [jobOffers, setJobOffers] = useState([]);
  const [selectedJobOffer, setSelectedJobOffer] = useState('');
  const [test, setTest] = useState(null);
  const [answers, setAnswers] = useState({});
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loadingTest, setLoadingTest] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [alreadySubmitted, setAlreadySubmitted] = useState(false);
  const [completed, setCompleted] = useState(false);

  useEffect(() => {
    axiosClient.get('/api/job-offers')
      .then((res) => setJobOffers(res.data))
      .catch(() => setError('Error al cargar ofertas'));
  }, []);

  useEffect(() => {
    if (!selectedJobOffer) { setAlreadySubmitted(false); return; }
    axiosClient.get(`/api/tests/check-submission/${selectedJobOffer}`)
      .then((res) => {
        if (res.data && res.data.submitted) {
          setAlreadySubmitted(true);
        } else {
          setAlreadySubmitted(false);
        }
      })
      .catch(() => setAlreadySubmitted(false));
  }, [selectedJobOffer]);

  const loadTest = async () => {
    if (!selectedJobOffer) { setError('Seleccione una oferta laboral'); return; }
    setError(''); setSuccess(''); setTest(null); setAnswers({});
    setLoadingTest(true);
    try {
      const res = await axiosClient.get(`/api/tests/${selectedJobOffer}`);
      setTest(res.data);
      const initial = {};
      (res.data.preguntas || []).forEach((q) => { initial[q.questionId] = ''; });
      setAnswers(initial);
    } catch (err) {
      setError(err.response?.data?.message || 'Error al cargar la prueba técnica');
    } finally {
      setLoadingTest(false);
    }
  };

  const handleAnswerChange = (questionId, value) => {
    setAnswers({ ...answers, [questionId]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(''); setSuccess('');
    const unanswered = Object.values(answers).some((a) => !a.trim());
    if (unanswered) { setError('Debe responder todas las preguntas'); return; }

    const payload = {
      testId: test.testId,
      jobOfferId: selectedJobOffer,
      respuestas: Object.entries(answers).map(([questionId, respuesta]) => ({ questionId, respuesta })),
    };

    setSubmitting(true);
    try {
      await axiosClient.post('/api/tests/submit', payload);
      setSuccess('¡Prueba técnica enviada exitosamente! Tus respuestas están siendo evaluadas.');
      setTest(null);
      setAnswers({});
      setCompleted(true);
      setAlreadySubmitted(true);
    } catch (err) {
      setError(err.response?.data?.message || 'Error al enviar la prueba');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div>
      <div className="page-header"><h2>Prueba Técnica</h2></div>

      {!test && (
        <div className="card">
          {error && <p className="error-msg" style={{ marginBottom: 12 }}>{error}</p>}
          {success && <p className="success-msg" style={{ marginBottom: 12 }}>{success}</p>}

          <div className="form-group">
            <label>Seleccione la Oferta Laboral</label>
            <select value={selectedJobOffer} onChange={(e) => { setSelectedJobOffer(e.target.value); setCompleted(false); }}>
              <option value="">-- Seleccione --</option>
              {jobOffers.map((o) => (
                <option key={o.id} value={o.id}>{o.nombreCargo}</option>
              ))}
            </select>
          </div>

          {alreadySubmitted || completed ? (
            <div style={{ padding: 16, background: '#e8f5e9', borderRadius: 6, marginTop: 12 }}>
              <p style={{ fontWeight: 600, color: '#2e7d32', margin: 0 }}>
                ✅ Ya completaste la prueba técnica para esta oferta
              </p>
              <p style={{ fontSize: 13, color: '#555', margin: '6px 0 0' }}>
                Tus respuestas han sido enviadas y están siendo evaluadas. El reclutador podrá ver tus resultados en el ranking.
              </p>
            </div>
          ) : (
            <button className="btn-primary" onClick={loadTest} disabled={loadingTest || !selectedJobOffer}>
              {loadingTest ? 'Generando prueba (puede tardar unos segundos)...' : 'Iniciar Prueba'}
            </button>
          )}
        </div>
      )}

      {test && (
        <div className="card">
          <h3 style={{ marginBottom: 16 }}>Preguntas de la Prueba ({test.preguntas?.length || 0} preguntas)</h3>
          {error && <p className="error-msg" style={{ marginBottom: 12 }}>{error}</p>}
          <form onSubmit={handleSubmit}>
            {(test.preguntas || []).map((q, idx) => (
              <div key={q.questionId} style={{ marginBottom: 20, paddingBottom: 16, borderBottom: '1px solid #e0e0e0' }}>
                <p style={{ fontWeight: 600, marginBottom: 8 }}>{idx + 1}. {q.enunciado}</p>
                {q.tipo === 'MULTIPLE_CHOICE' && q.opciones ? (
                  <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
                    {q.opciones.map((opt, oi) => (
                      <label key={oi} style={{ display: 'flex', alignItems: 'center', gap: 8, cursor: 'pointer' }}>
                        <input type="radio" name={`q-${q.questionId}`} value={opt}
                          checked={answers[q.questionId] === opt}
                          onChange={() => handleAnswerChange(q.questionId, opt)} />
                        {opt}
                      </label>
                    ))}
                  </div>
                ) : (
                  <textarea
                    value={answers[q.questionId] || ''}
                    onChange={(e) => handleAnswerChange(q.questionId, e.target.value)}
                    placeholder="Escriba su respuesta o código aquí..."
                    style={{ width: '100%', minHeight: 150, padding: 10, border: '1px solid #ccc', borderRadius: 4, fontFamily: 'monospace', fontSize: 13, lineHeight: 1.5 }}
                  />
                )}
              </div>
            ))}
            <div style={{ display: 'flex', gap: 10 }}>
              <button type="submit" className="btn-primary" disabled={submitting}>
                {submitting ? 'Enviando...' : 'Enviar Respuestas'}
              </button>
              <button type="button" className="btn-secondary" onClick={() => { setTest(null); setAnswers({}); }}>
                Cancelar
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
}

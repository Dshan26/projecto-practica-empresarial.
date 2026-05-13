import React, { useState, useEffect } from 'react';
import axiosClient from '../../api/axiosClient';

export default function Ranking() {
  const [jobOffers, setJobOffers] = useState([]);
  const [selectedJobOffer, setSelectedJobOffer] = useState('');
  const [rankings, setRankings] = useState([]);
  const [selectedCandidate, setSelectedCandidate] = useState(null);
  const [candidateDetail, setCandidateDetail] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [loadingDetail, setLoadingDetail] = useState(false);

  useEffect(() => {
    axiosClient.get('/api/job-offers')
      .then((res) => setJobOffers(res.data))
      .catch(() => setError('Error al cargar ofertas'));
  }, []);

  const loadRanking = async (jobOfferId) => {
    if (!jobOfferId) { setRankings([]); return; }
    setError('');
    setSelectedCandidate(null);
    setCandidateDetail(null);
    setLoading(true);
    try {
      const res = await axiosClient.get(`/api/rankings/${jobOfferId}`);
      setRankings(res.data);
    } catch {
      setError('Error al cargar el ranking');
      setRankings([]);
    } finally {
      setLoading(false);
    }
  };

  const handleJobOfferChange = (e) => {
    const val = e.target.value;
    setSelectedJobOffer(val);
    loadRanking(val);
  };

  const viewCandidateDetail = async (candidateId) => {
    setError('');
    setLoadingDetail(true);
    setSelectedCandidate(candidateId);
    setCandidateDetail(null);
    try {
      const res = await axiosClient.get(`/api/rankings/${selectedJobOffer}/candidates/${candidateId}`);
      setCandidateDetail(res.data);
    } catch {
      setError('Error al cargar detalle del candidato');
    } finally {
      setLoadingDetail(false);
    }
  };

  return (
    <div>
      <div className="page-header">
        <h2>Ranking de Candidatos</h2>
      </div>

      <div className="card">
        <div className="form-group">
          <label>Filtrar por Oferta Laboral</label>
          <select value={selectedJobOffer} onChange={handleJobOfferChange}>
            <option value="">-- Seleccione una oferta --</option>
            {jobOffers.map((o) => (
              <option key={o.id} value={o.id}>{o.nombreCargo}</option>
            ))}
          </select>
        </div>
      </div>

      {error && <p className="error-msg" style={{ marginBottom: 12 }}>{error}</p>}

      {loading ? (
        <p>Cargando ranking...</p>
      ) : selectedJobOffer && rankings.length === 0 ? (
        <div className="card"><p>No hay candidatos en el ranking para esta oferta.</p></div>
      ) : rankings.length > 0 && (
        <table>
          <thead>
            <tr>
              <th>#</th>
              <th>Candidato</th>
              <th>CV Score</th>
              <th>Tech Score</th>
              <th>Final Score</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {rankings.map((r, idx) => (
              <tr key={r.candidateId} style={{
                background: selectedCandidate === r.candidateId ? '#e8f0fe' : 'transparent',
              }}>
                <td>{idx + 1}</td>
                <td>{r.candidateName || r.candidateId}</td>
                <td>{r.cvScore}</td>
                <td>{r.techScore}</td>
                <td style={{ fontWeight: 700 }}>{typeof r.finalScore === 'number' ? r.finalScore.toFixed(2) : r.finalScore}</td>
                <td>
                  <button className="link-btn" onClick={() => viewCandidateDetail(r.candidateId)}>
                    Ver detalle
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {loadingDetail && <p style={{ marginTop: 16 }}>Cargando detalle...</p>}

      {candidateDetail && (
        <div className="card" style={{ marginTop: 20 }}>
          <h3 style={{ marginBottom: 14 }}>Detalle del Candidato</h3>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
            <div>
              <h4 style={{ marginBottom: 8, color: '#1a73e8' }}>Puntuaciones</h4>
              <p><strong>CV Score:</strong> {candidateDetail.cvScore}</p>
              <p><strong>Tech Score:</strong> {candidateDetail.techScore}</p>
              <p><strong>Final Score:</strong> {typeof candidateDetail.finalScore === 'number' ? candidateDetail.finalScore.toFixed(2) : candidateDetail.finalScore}</p>
              {candidateDetail.rankingPosition && (
                <p><strong>Posición:</strong> #{candidateDetail.rankingPosition}</p>
              )}
            </div>

            <div>
              <h4 style={{ marginBottom: 8, color: '#1a73e8' }}>Habilidades Identificadas</h4>
              {candidateDetail.habilidades && candidateDetail.habilidades.length > 0 ? (
                <div className="skill-list">
                  {candidateDetail.habilidades.map((h, i) => (
                    <span key={i} className="skill-tag">
                      {typeof h === 'string' ? h : `${h.nombre} (${h.nivel})`}
                    </span>
                  ))}
                </div>
              ) : (
                <p style={{ fontSize: 13, color: '#5f6368' }}>Sin datos de habilidades</p>
              )}
            </div>
          </div>

          {candidateDetail.analysisResult && (
            <div style={{ marginTop: 16 }}>
              <h4 style={{ marginBottom: 8, color: '#1a73e8' }}>Resultado del Análisis IA</h4>
              <p><strong>Experiencia:</strong> {candidateDetail.analysisResult.aniosExperiencia} años</p>
              <p><strong>Seniority Detectado:</strong> {candidateDetail.analysisResult.seniorityDetectado}</p>
              {candidateDetail.analysisResult.resumenProfesional && (
                <p style={{ marginTop: 8 }}><strong>Resumen:</strong> {candidateDetail.analysisResult.resumenProfesional}</p>
              )}
            </div>
          )}

          {candidateDetail.evaluationBreakdown && (
            <div style={{ marginTop: 16 }}>
              <h4 style={{ marginBottom: 8, color: '#1a73e8' }}>Desglose de Evaluación Técnica</h4>
              <p><strong>Total Preguntas:</strong> {candidateDetail.evaluationBreakdown.totalQuestions}</p>
              <p><strong>Respuestas Correctas:</strong> {candidateDetail.evaluationBreakdown.correctAnswers}</p>
              <p><strong>Tech Score:</strong> {candidateDetail.evaluationBreakdown.techScore}</p>
            </div>
          )}

          <button className="btn-secondary" style={{ marginTop: 16 }} onClick={() => { setSelectedCandidate(null); setCandidateDetail(null); }}>
            Cerrar Detalle
          </button>
        </div>
      )}
    </div>
  );
}

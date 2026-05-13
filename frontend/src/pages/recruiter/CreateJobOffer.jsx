import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axiosClient from '../../api/axiosClient';

export default function CreateJobOffer() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();

  const [nombreCargo, setNombreCargo] = useState('');
  const [descripcion, setDescripcion] = useState('');
  const [habilidades, setHabilidades] = useState([]);
  const [newSkill, setNewSkill] = useState('');
  const [experienciaMinimaAnios, setExperienciaMinimaAnios] = useState(0);
  const [seniorityEsperado, setSeniorityEsperado] = useState('JUNIOR');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isEdit) {
      axiosClient.get(`/api/job-offers/${id}`)
        .then((res) => {
          const o = res.data;
          setNombreCargo(o.nombreCargo || '');
          setDescripcion(o.descripcion || '');
          setHabilidades(o.habilidadesRequeridas || []);
          setExperienciaMinimaAnios(o.experienciaMinimaAnios || 0);
          setSeniorityEsperado(o.seniorityEsperado || 'JUNIOR');
        })
        .catch(() => setError('No se pudo cargar la oferta'));
    }
  }, [id, isEdit]);

  const addSkill = () => {
    const trimmed = newSkill.trim();
    if (trimmed && !habilidades.includes(trimmed)) {
      setHabilidades([...habilidades, trimmed]);
      setNewSkill('');
    }
  };

  const removeSkill = (skill) => {
    setHabilidades(habilidades.filter((s) => s !== skill));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!nombreCargo.trim()) { setError('El nombre del cargo es obligatorio'); return; }
    if (habilidades.length === 0) { setError('Debe agregar al menos una habilidad'); return; }

    const payload = {
      nombreCargo,
      descripcion,
      habilidadesRequeridas: habilidades,
      experienciaMinimaAnios: Number(experienciaMinimaAnios),
      seniorityEsperado,
    };

    setLoading(true);
    try {
      if (isEdit) {
        await axiosClient.put(`/api/job-offers/${id}`, payload);
      } else {
        await axiosClient.post('/api/job-offers', payload);
      }
      navigate('/recruiter/job-offers');
    } catch (err) {
      setError(err.response?.data?.message || 'Error al guardar la oferta');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="page-header">
        <h2>{isEdit ? 'Editar Oferta Laboral' : 'Crear Oferta Laboral'}</h2>
      </div>
      <div className="card">
        {error && <p className="error-msg" style={{ marginBottom: 12 }}>{error}</p>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Nombre del Cargo</label>
            <input type="text" value={nombreCargo} onChange={(e) => setNombreCargo(e.target.value)} placeholder="Ej: Desarrollador Java Senior" />
          </div>
          <div className="form-group">
            <label>Descripción</label>
            <textarea value={descripcion} onChange={(e) => setDescripcion(e.target.value)} placeholder="Descripción del cargo..." />
          </div>
          <div className="form-group">
            <label>Habilidades Requeridas</label>
            <div style={{ display: 'flex', gap: 8 }}>
              <input
                type="text"
                value={newSkill}
                onChange={(e) => setNewSkill(e.target.value)}
                placeholder="Ej: Java"
                onKeyDown={(e) => { if (e.key === 'Enter') { e.preventDefault(); addSkill(); } }}
              />
              <button type="button" className="btn-secondary" onClick={addSkill}>Agregar</button>
            </div>
            <div className="skill-list">
              {habilidades.map((s) => (
                <span key={s} className="skill-tag">
                  {s}
                  <button type="button" onClick={() => removeSkill(s)} style={{
                    background: 'none', border: 'none', color: '#1a73e8', cursor: 'pointer',
                    marginLeft: 4, fontWeight: 700, fontSize: 12,
                  }}>×</button>
                </span>
              ))}
            </div>
          </div>
          <div className="form-group">
            <label>Experiencia Mínima (años)</label>
            <input type="number" min="0" value={experienciaMinimaAnios} onChange={(e) => setExperienciaMinimaAnios(e.target.value)} />
          </div>
          <div className="form-group">
            <label>Seniority Esperado</label>
            <select value={seniorityEsperado} onChange={(e) => setSeniorityEsperado(e.target.value)}>
              <option value="JUNIOR">Junior</option>
              <option value="SEMI_SENIOR">Semi Senior</option>
              <option value="SENIOR">Senior</option>
            </select>
          </div>
          <div style={{ display: 'flex', gap: 10, marginTop: 16 }}>
            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Guardando...' : (isEdit ? 'Actualizar' : 'Crear Oferta')}
            </button>
            <button type="button" className="btn-secondary" onClick={() => navigate('/recruiter/job-offers')}>Cancelar</button>
          </div>
        </form>
      </div>
    </div>
  );
}

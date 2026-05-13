import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axiosClient from '../../api/axiosClient';

export default function JobOfferList() {
  const [offers, setOffers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchOffers = async () => {
    setLoading(true);
    try {
      const res = await axiosClient.get('/api/job-offers');
      setOffers(res.data);
    } catch {
      setError('Error al cargar ofertas');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchOffers(); }, []);

  const handleDelete = async (id) => {
    if (!window.confirm('¿Eliminar esta oferta laboral?')) return;
    try {
      await axiosClient.delete(`/api/job-offers/${id}`);
      setOffers(offers.filter((o) => o.id !== id));
    } catch {
      setError('Error al eliminar la oferta');
    }
  };

  const seniorityLabel = (s) => {
    const map = { JUNIOR: 'Junior', SEMI_SENIOR: 'Semi Senior', SENIOR: 'Senior' };
    return map[s] || s;
  };

  if (loading) return <p>Cargando ofertas...</p>;

  return (
    <div>
      <div className="page-header">
        <h2>Ofertas Laborales</h2>
        <Link to="/recruiter/job-offers/new">
          <button className="btn-primary">+ Nueva Oferta</button>
        </Link>
      </div>
      {error && <p className="error-msg">{error}</p>}
      {offers.length === 0 ? (
        <div className="card"><p>No hay ofertas laborales creadas.</p></div>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Cargo</th>
              <th>Seniority</th>
              <th>Exp. Mínima</th>
              <th>Habilidades</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {offers.map((o) => (
              <tr key={o.id}>
                <td>{o.nombreCargo}</td>
                <td>{seniorityLabel(o.seniorityEsperado)}</td>
                <td>{o.experienciaMinimaAnios} años</td>
                <td>
                  <div className="skill-list">
                    {(o.habilidadesRequeridas || []).map((s) => (
                      <span key={s} className="skill-tag">{s}</span>
                    ))}
                  </div>
                </td>
                <td>
                  <div style={{ display: 'flex', gap: 6 }}>
                    <Link to={`/recruiter/job-offers/edit/${o.id}`}>
                      <button className="btn-secondary" style={{ fontSize: 12, padding: '4px 10px' }}>Editar</button>
                    </Link>
                    <button className="btn-danger" style={{ fontSize: 12, padding: '4px 10px' }} onClick={() => handleDelete(o.id)}>
                      Eliminar
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

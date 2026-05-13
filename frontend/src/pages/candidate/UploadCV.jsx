import React, { useState, useEffect } from 'react';
import axiosClient from '../../api/axiosClient';

const MAX_FILE_SIZE = 10 * 1024 * 1024;

export default function UploadCV() {
  const [file, setFile] = useState(null);
  const [jobOffers, setJobOffers] = useState([]);
  const [selectedJobOffer, setSelectedJobOffer] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const [alreadyUploaded, setAlreadyUploaded] = useState(false);
  const [uploadedFileName, setUploadedFileName] = useState('');

  useEffect(() => {
    axiosClient.get('/api/job-offers')
      .then((res) => setJobOffers(res.data))
      .catch(() => setError('Error al cargar ofertas laborales'));
  }, []);

  useEffect(() => {
    if (!selectedJobOffer) { setAlreadyUploaded(false); return; }
    axiosClient.get(`/api/cv/check/${selectedJobOffer}`)
      .then((res) => {
        if (res.data && res.data.id) {
          setAlreadyUploaded(true);
          setUploadedFileName(res.data.fileName || 'CV');
        } else {
          setAlreadyUploaded(false);
        }
      })
      .catch(() => setAlreadyUploaded(false));
  }, [selectedJobOffer]);

  const handleFileChange = (e) => {
    const selected = e.target.files[0];
    setError(''); setSuccess('');
    if (!selected) { setFile(null); return; }
    if (selected.type !== 'application/pdf') {
      setError('Solo se permiten archivos PDF'); setFile(null); e.target.value = ''; return;
    }
    if (selected.size > MAX_FILE_SIZE) {
      setError('El archivo no debe superar los 10 MB'); setFile(null); e.target.value = ''; return;
    }
    setFile(selected);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(''); setSuccess('');
    if (!file) { setError('Debe seleccionar un archivo PDF'); return; }
    if (!selectedJobOffer) { setError('Debe seleccionar una oferta laboral'); return; }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('jobOfferId', selectedJobOffer);

    setLoading(true);
    try {
      await axiosClient.post('/api/cv/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      setSuccess('Hoja de vida cargada exitosamente. El sistema está analizando tu CV.');
      setFile(null);
      setAlreadyUploaded(true);
      setUploadedFileName(file.name);
    } catch (err) {
      setError(err.response?.data?.message || 'Error al cargar la hoja de vida');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="page-header"><h2>Subir Hoja de Vida</h2></div>
      <div className="card">
        {error && <p className="error-msg" style={{ marginBottom: 12 }}>{error}</p>}
        {success && <p className="success-msg" style={{ marginBottom: 12 }}>{success}</p>}

        <div className="form-group">
          <label>Oferta Laboral</label>
          <select value={selectedJobOffer} onChange={(e) => setSelectedJobOffer(e.target.value)}>
            <option value="">-- Seleccione una oferta --</option>
            {jobOffers.map((o) => (
              <option key={o.id} value={o.id}>{o.nombreCargo}</option>
            ))}
          </select>
        </div>

        {alreadyUploaded ? (
          <div style={{ padding: 16, background: '#e8f5e9', borderRadius: 6, marginTop: 12 }}>
            <p style={{ fontWeight: 600, color: '#2e7d32', margin: 0 }}>
              ✅ Ya subiste tu hoja de vida para esta oferta
            </p>
            <p style={{ fontSize: 13, color: '#555', margin: '6px 0 0' }}>
              Archivo: {uploadedFileName}. Ahora puedes ir a la sección de Prueba Técnica.
            </p>
          </div>
        ) : (
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Archivo PDF (máx. 10 MB)</label>
              <input type="file" accept=".pdf,application/pdf" onChange={handleFileChange} />
              {file && <p style={{ fontSize: 13, marginTop: 4, color: '#5f6368' }}>{file.name} ({(file.size / 1024 / 1024).toFixed(2)} MB)</p>}
            </div>
            <button type="submit" className="btn-primary" style={{ marginTop: 10 }} disabled={loading || !selectedJobOffer}>
              {loading ? 'Subiendo...' : 'Subir Hoja de Vida'}
            </button>
          </form>
        )}
      </div>
    </div>
  );
}

import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const RECRUITER_CODE = 'GFT2026';

export default function Register() {
  const [nombre, setNombre] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isRecruiter, setIsRecruiter] = useState(false);
  const [recruiterCode, setRecruiterCode] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    if (!nombre || !email || !password) {
      setError('Todos los campos son obligatorios');
      return;
    }

    let rol = 'CANDIDATE';
    if (isRecruiter) {
      if (recruiterCode !== RECRUITER_CODE) {
        setError('Código de reclutador inválido');
        return;
      }
      rol = 'RECRUITER';
    }

    setLoading(true);
    try {
      await register(nombre, email, password, rol);
      setSuccess('Registro exitoso. Redirigiendo al login...');
      setTimeout(() => navigate('/login'), 1500);
    } catch (err) {
      setError(err.response?.data?.message || 'Error al registrar usuario');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="card auth-form">
        <h2>Registro</h2>
        {error && <p className="error-msg">{error}</p>}
        {success && <p className="success-msg">{success}</p>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Nombre</label>
            <input type="text" value={nombre} onChange={(e) => setNombre(e.target.value)} placeholder="Tu nombre completo" />
          </div>
          <div className="form-group">
            <label>Email</label>
            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="correo@ejemplo.com" />
          </div>
          <div className="form-group">
            <label>Contraseña</label>
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="••••••••" />
          </div>
          <div className="form-group">
            <label style={{ display: 'flex', alignItems: 'center', gap: 8, cursor: 'pointer' }}>
              <input
                type="checkbox"
                checked={isRecruiter}
                onChange={(e) => setIsRecruiter(e.target.checked)}
                style={{ width: 'auto' }}
              />
              Soy reclutador
            </label>
          </div>
          {isRecruiter && (
            <div className="form-group">
              <label>Código de reclutador</label>
              <input
                type="password"
                value={recruiterCode}
                onChange={(e) => setRecruiterCode(e.target.value)}
                placeholder="Ingresa el código proporcionado por la empresa"
              />
            </div>
          )}
          <button type="submit" className="btn-primary" style={{ width: '100%', marginTop: 8 }} disabled={loading}>
            {loading ? 'Registrando...' : 'Registrarse'}
          </button>
        </form>
        <p style={{ textAlign: 'center', marginTop: 14, fontSize: 13 }}>
          ¿Ya tienes cuenta? <Link to="/login">Inicia sesión</Link>
        </p>
      </div>
    </div>
  );
}

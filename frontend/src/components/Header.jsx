import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Header() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header style={{
      background: '#1a73e8',
      color: '#fff',
      padding: '12px 24px',
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
    }}>
      <Link to="/" style={{ color: '#fff', textDecoration: 'none', fontWeight: 700, fontSize: 18 }}>
        GFT Recruitment
      </Link>
      <nav style={{ display: 'flex', gap: 16, alignItems: 'center' }}>
        {user ? (
          <>
            {user.rol === 'RECRUITER' && (
              <>
                <Link to="/recruiter/job-offers" style={{ color: '#fff', textDecoration: 'none' }}>Ofertas</Link>
                <Link to="/recruiter/ranking" style={{ color: '#fff', textDecoration: 'none' }}>Ranking</Link>
              </>
            )}
            {user.rol === 'CANDIDATE' && (
              <>
                <Link to="/candidate/upload-cv" style={{ color: '#fff', textDecoration: 'none' }}>Subir CV</Link>
                <Link to="/candidate/test" style={{ color: '#fff', textDecoration: 'none' }}>Prueba Técnica</Link>
              </>
            )}
            <span style={{ fontSize: 13, opacity: 0.85 }}>{user.email} ({user.rol})</span>
            <button onClick={handleLogout} style={{
              background: 'rgba(255,255,255,0.2)',
              color: '#fff',
              border: '1px solid rgba(255,255,255,0.4)',
              borderRadius: 4,
              padding: '5px 12px',
              cursor: 'pointer',
              fontSize: 13,
            }}>
              Salir
            </button>
          </>
        ) : (
          <>
            <Link to="/login" style={{ color: '#fff', textDecoration: 'none' }}>Login</Link>
            <Link to="/register" style={{ color: '#fff', textDecoration: 'none' }}>Registro</Link>
          </>
        )}
      </nav>
    </header>
  );
}

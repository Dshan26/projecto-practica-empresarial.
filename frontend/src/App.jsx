import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/Login';
import Register from './pages/Register';
import CreateJobOffer from './pages/recruiter/CreateJobOffer';
import JobOfferList from './pages/recruiter/JobOfferList';
import Ranking from './pages/recruiter/Ranking';
import UploadCV from './pages/candidate/UploadCV';
import TechnicalTest from './pages/candidate/TechnicalTest';
import { useAuth } from './context/AuthContext';

function Home() {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (user.rol === 'RECRUITER') return <Navigate to="/recruiter/job-offers" replace />;
  return <Navigate to="/candidate/upload-cv" replace />;
}

export default function App() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <Header />
      <main style={{ flex: 1 }}>
        <div className="container">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            {/* Recruiter routes */}
            <Route path="/recruiter/job-offers" element={
              <ProtectedRoute allowedRoles={['RECRUITER']}>
                <JobOfferList />
              </ProtectedRoute>
            } />
            <Route path="/recruiter/job-offers/new" element={
              <ProtectedRoute allowedRoles={['RECRUITER']}>
                <CreateJobOffer />
              </ProtectedRoute>
            } />
            <Route path="/recruiter/job-offers/edit/:id" element={
              <ProtectedRoute allowedRoles={['RECRUITER']}>
                <CreateJobOffer />
              </ProtectedRoute>
            } />
            <Route path="/recruiter/ranking" element={
              <ProtectedRoute allowedRoles={['RECRUITER']}>
                <Ranking />
              </ProtectedRoute>
            } />

            {/* Candidate routes */}
            <Route path="/candidate/upload-cv" element={
              <ProtectedRoute allowedRoles={['CANDIDATE']}>
                <UploadCV />
              </ProtectedRoute>
            } />
            <Route path="/candidate/test" element={
              <ProtectedRoute allowedRoles={['CANDIDATE']}>
                <TechnicalTest />
              </ProtectedRoute>
            } />

            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </div>
      </main>
      <Footer />
    </div>
  );
}

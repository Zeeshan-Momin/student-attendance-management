import { Navigate, Route, Routes } from 'react-router-dom';
import ProtectedRoute from './components/ProtectedRoute';
import LoginPage from './pages/LoginPage';
import UnauthorizedPage from './pages/UnauthorizedPage';
import AdminDashboardPage from './pages/AdminDashboardPage';
import FacultyDashboardPage from './pages/FacultyDashboardPage';
import StudentDashboardPage from './pages/StudentDashboardPage';

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/unauthorized" element={<UnauthorizedPage />} />
      <Route path="/" element={<Navigate to="/login" replace />} />

      <Route
        path="/admin"
        element={
          <ProtectedRoute requiredRole="ADMIN">
            <AdminDashboardPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/faculty"
        element={
          <ProtectedRoute requiredRole="FACULTY">
            <FacultyDashboardPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/student"
        element={
          <ProtectedRoute requiredRole="STUDENT">
            <StudentDashboardPage />
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}

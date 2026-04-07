import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { Toaster } from 'react-hot-toast'
import { AuthProvider, useAuth } from './context/AuthContext.jsx'

import LoginPage        from './pages/LoginPage.jsx'
import AdminDashboard   from './pages/admin/AdminDashboard.jsx'
import SubjectsPage     from './pages/admin/SubjectsPage.jsx'
import FacultiesPage    from './pages/admin/FacultiesPage.jsx'
import StudentsPage     from './pages/admin/StudentsPage.jsx'
import AssignmentsPage  from './pages/admin/AssignmentsPage.jsx'
import StudentDashboard from './pages/student/StudentDashboard.jsx'
import FacultyDashboard from './pages/faculty/FacultyDashboard.jsx'
import ProfilePage      from './pages/ProfilePage.jsx'

import LandingPage     from './pages/LandingPage.jsx'

function RoleRoute({ role, children }) {
  const { user, loading } = useAuth()
  if (loading) return <FullScreenLoader />
  
  if (!user) return <Navigate to="/login" replace />
  
  if (user.role !== role) {
    const dashboard = user.role === 'ADMIN' ? '/admin' : user.role === 'FACULTY' ? '/faculty' : '/student'
    return <Navigate to={dashboard} replace />
  }
  
  return children
}
 
function AuthGate({ children }) {
  const { user, loading } = useAuth()
  if (loading) return <FullScreenLoader />
  if (!user) return <Navigate to="/login" replace />
  return children
}

function PublicRoute({ children }) {
  const { user, loading } = useAuth()
  if (loading) return <FullScreenLoader />
  if (user) {
    const dashboard = user.role === 'ADMIN' ? '/admin' : user.role === 'FACULTY' ? '/faculty' : '/student'
    return <Navigate to={dashboard} replace />
  }
  return children
}

function FullScreenLoader() {
  return (
    <div style={{
      minHeight: '100vh', display: 'flex', alignItems: 'center',
      justifyContent: 'center', background: 'var(--bg-base)'
    }}>
      <div style={{
        width: 40, height: 40, borderRadius: '50%',
        border: '3px solid var(--border)',
        borderTopColor: 'var(--gold)',
        animation: 'spin 0.8s linear infinite'
      }} />
    </div>
  )
}

function AppRoutes() {
  const { user } = useAuth()
  return (
    <Routes>
      <Route path="/" element={<PublicRoute><LandingPage /></PublicRoute>} />
      <Route path="/login" element={<PublicRoute><LoginPage /></PublicRoute>} />

      {/* Admin Routes */}
      <Route path="/admin" element={<RoleRoute role="ADMIN"><AdminDashboard /></RoleRoute>} />
      <Route path="/admin/subjects" element={<RoleRoute role="ADMIN"><SubjectsPage /></RoleRoute>} />
      <Route path="/admin/faculties" element={<RoleRoute role="ADMIN"><FacultiesPage /></RoleRoute>} />
      <Route path="/admin/students" element={<RoleRoute role="ADMIN"><StudentsPage /></RoleRoute>} />
      <Route path="/admin/assignments" element={<RoleRoute role="ADMIN"><AssignmentsPage /></RoleRoute>} />

      {/* Student Routes */}
      <Route path="/student" element={<RoleRoute role="STUDENT"><StudentDashboard /></RoleRoute>} />

      {/* Faculty Routes */}
      <Route path="/faculty" element={<RoleRoute role="FACULTY"><FacultyDashboard /></RoleRoute>} />

      {/* Profile for anyone logged in */}
      <Route path="/profile" element={
        <AuthGate><ProfilePage /></AuthGate>
      } />

      {/* Redirect for any non-existent route */}
      <Route path="*" element={
        user 
          ? <Navigate to={user.role === 'ADMIN' ? '/admin' : user.role === 'FACULTY' ? '/faculty' : '/student'} replace />
          : <Navigate to="/" replace />
      } />
    </Routes>
  )
}

import { ThemeProvider } from './context/ThemeContext.jsx'

export default function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <BrowserRouter>
          <AppRoutes />
          <Toaster
            position="top-right"
            toastOptions={{
              duration: 3500,
              style: {
                background: 'var(--bg-elevated)',
                color: 'var(--text-primary)',
                border: '1px solid var(--border-bright)',
                fontFamily: 'var(--font-body)',
                fontSize: '0.875rem',
                borderRadius: 'var(--radius-md)',
              },
              success: { iconTheme: { primary: 'var(--mint)', secondary: 'var(--bg-elevated)' } },
              error:   { iconTheme: { primary: 'var(--rose)',  secondary: 'var(--bg-elevated)' } },
            }}
          />
        </BrowserRouter>
      </AuthProvider>
    </ThemeProvider>
  )
}


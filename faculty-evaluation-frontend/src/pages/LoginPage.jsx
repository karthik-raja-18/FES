import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import toast from 'react-hot-toast'
import { login } from '../api/axios.js'
import { useAuth } from '../context/AuthContext.jsx'
import { useTheme } from '../context/ThemeContext.jsx'
import { Button, Input } from '../components/UI.jsx'
import logo from '../assets/logo.png'

const ROLES = [
  {
    id: 'ADMIN', label: 'Admin', icon: '⚙️',
    desc: 'Manage system data, assign faculty, enroll students',
    color: 'var(--violet)',
  },
  {
    id: 'FACULTY', label: 'Faculty', icon: '🎓',
    desc: 'View ratings and anonymous student feedback',
    color: 'var(--sky)',
  },
  {
    id: 'STUDENT', label: 'Student', icon: '📚',
    desc: 'Rate professors and submit evaluation feedback',
    color: 'var(--mint)',
  },
]

export default function LoginPage() {
  const { loginUser } = useAuth()
  const { theme, toggleTheme } = useTheme()
  const navigate = useNavigate()
  const [form, setForm] = useState({ username: '', password: '', role: 'STUDENT' })
  const [loading, setLoading] = useState(false)

  const handleSubmit = async e => {
    e.preventDefault()
    if (!form.username || !form.password) {
      toast.error('Please enter both username and password')
      return
    }
    setLoading(true)
    try {
      const res = await login(form)
      const { token, ...userData } = res.data.data
      loginUser(userData, token)
      toast.success(`Welcome back, ${userData.fullName}!`)
      const routes = { ADMIN: '/admin', FACULTY: '/faculty', STUDENT: '/student' }
      navigate(routes[userData.role] || '/login')
    } catch (err) {
      toast.error(err.response?.data?.message || 'Login failed. Check your credentials.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={{
      minHeight: '100vh', display: 'flex',
      background: 'var(--bg-base)',
      position: 'relative',
    }}>
      {/* Floating Theme Toggle for Login Page */}
      <button 
        onClick={toggleTheme}
        style={{
          position: 'absolute', top: 24, right: 24,
          padding: '10px', borderRadius: '50%',
          background: 'var(--bg-elevated)', border: '1px solid var(--border)',
          color: 'var(--text-secondary)', cursor: 'pointer',
          zIndex: 10, display: 'flex', alignItems: 'center', justifyContent: 'center',
          transition: 'var(--transition)'
        }}
        onMouseEnter={e => e.currentTarget.style.borderColor = 'var(--gold)'}
        onMouseLeave={e => e.currentTarget.style.borderColor = 'var(--border)'}
      >
        {theme === 'dark' ? (
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><circle cx="12" cy="12" r="5"/><path d="M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42"/></svg>
        ) : (
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M21 12.79A9 9 0 1111.21 3 7 7 0 0021 12.79z"/></svg>
        )}
      </button>

      {/* Left Panel */}
      <div style={{
        flex: 1, display: 'none', flexDirection: 'column', justifyContent: 'center',
        padding: '60px 64px',
        background: 'linear-gradient(135deg, var(--bg-surface) 0%, var(--bg-card) 100%)',
        borderRight: '1px solid var(--border)',
        position: 'relative', overflow: 'hidden',
      }} className="login-left">
        {/* Decorative circles */}
        <div style={{
          position: 'absolute', top: -80, left: -80, width: 320, height: 320,
          borderRadius: '50%',
          background: 'radial-gradient(circle, rgba(240,165,0,0.08) 0%, transparent 70%)',
        }} />
        <div style={{
          position: 'absolute', bottom: -60, right: -60, width: 240, height: 240,
          borderRadius: '50%',
          background: 'radial-gradient(circle, rgba(56,189,248,0.06) 0%, transparent 70%)',
        }} />
        <div style={{ position: 'relative', zIndex: 1 }}>
          <Link to="/" style={{
            display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
            width: 56, height: 56, borderRadius: 16,
            background: 'linear-gradient(135deg, var(--gold) 0%, #D4920A 100%)',
            marginBottom: 28, overflow: 'hidden', boxShadow: 'var(--shadow-glow)'
          }}>
            <img src={logo} alt="FES Logo" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
          </Link>
          <h1 style={{
            fontFamily: 'var(--font-display)', fontWeight: 800,
            fontSize: '2.2rem', color: 'var(--text-primary)', lineHeight: 1.2,
            marginBottom: 16,
          }}>Faculty Evaluation<br />System</h1>
          <p style={{ color: 'var(--text-secondary)', fontSize: '1rem', lineHeight: 1.7, marginBottom: 40 }}>
            A transparent, anonymous platform connecting students, faculty, and administrators for meaningful academic feedback.
          </p>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
            {[
              '✦ Anonymous student feedback',
              '✦ Real-time rating analytics',
              '✦ Role-based access control',
            ].map(f => (
              <div key={f} style={{ display: 'flex', alignItems: 'center', gap: 10, color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
                {f}
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Right Panel - Form */}
      <div style={{
        flex: 1, display: 'flex', flexDirection: 'column',
        alignItems: 'center', justifyContent: 'center',
        padding: '40px 24px',
      }}>
        <div style={{
          width: '100%', maxWidth: 440,
          animation: 'slideUp 0.4s cubic-bezier(0.4,0,0.2,1) both',
        }}>
          {/* Logo on mobile/login */}
          <Link to="/" style={{ marginBottom: 32, display: 'flex', alignItems: 'center', gap: 12, width: 'fit-content', textDecoration: 'none' }}>
            <div style={{
              width: 44, height: 44, borderRadius: 12,
              background: 'linear-gradient(135deg, var(--gold) 0%, #D4920A 100%)',
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              boxShadow: 'var(--shadow-glow)', overflow: 'hidden'
            }}>
              <img src={logo} alt="FES Logo" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
            </div>
            <div>
              <div style={{ fontFamily: 'var(--font-display)', fontWeight: 700, color: 'var(--text-primary)', fontSize: '1.1rem' }}>
                EvalPortal
              </div>
              <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>Faculty Evaluation System</div>
            </div>
          </Link>

          <h2 style={{
            fontFamily: 'var(--font-display)', fontWeight: 800,
            fontSize: '1.7rem', color: 'var(--text-primary)', marginBottom: 6,
          }}>Welcome back</h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem', marginBottom: 32 }}>
            Please select your role and enter your credentials.
          </p>

          <form onSubmit={handleSubmit} style={{ display: 'grid', gap: 24 }}>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 12 }}>
               {ROLES.map(role => (
                 <button
                   key={role.id}
                   id={`role-${role.id}`}
                   type="button"
                   onClick={() => setForm(f => ({ ...f, role: role.id }))}
                   style={{
                     padding: '16px 12px', borderRadius: 'var(--radius-md)',
                     border: `1px solid ${form.role === role.id ? role.color : 'var(--border)'}`,
                     background: form.role === role.id ? `${role.color}10` : 'var(--bg-base)',
                     cursor: 'pointer', transition: 'var(--transition)',
                     display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 8
                   }}
                 >
                    <span style={{ fontSize: '1.2rem' }}>{role.icon}</span>
                    <span style={{ fontSize: '0.75rem', fontWeight: 700, color: form.role === role.id ? role.color : 'var(--text-secondary)' }}>{role.label}</span>
                 </button>
               ))}
            </div>

            <div style={{ display: 'grid', gap: 16 }}>
              <div style={{ display: 'grid', gap: 6 }}>
                 <label style={{ fontSize: '0.75rem', fontWeight: 700, color: 'var(--text-secondary)', marginLeft: 4 }}>Username</label>
                 <Input 
                   id="username"
                   placeholder="Enter your username" 
                   value={form.username}
                   onChange={e => setForm(f => ({ ...f, username: e.target.value }))}
                 />
              </div>
              <div style={{ display: 'grid', gap: 6 }}>
                 <label style={{ fontSize: '0.75rem', fontWeight: 700, color: 'var(--text-secondary)', marginLeft: 4 }}>Password</label>
                 <Input 
                   id="password"
                   type="password"
                   placeholder="••••••••" 
                   value={form.password}
                   onChange={e => setForm(f => ({ ...f, password: e.target.value }))}
                 />
              </div>
            </div>

            <Button id="login-btn" type="submit" loading={loading} style={{ width: '100%', height: 48, justifyContent: 'center', marginTop: 8 }}>
              Sign In to Portal
            </Button>
          </form>
        </div>
      </div>
    </div>
  )
}

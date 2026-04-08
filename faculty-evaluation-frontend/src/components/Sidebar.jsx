import { useState } from 'react'
import { NavLink, useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'
import toast from 'react-hot-toast'
import { useTheme } from '../context/ThemeContext.jsx'
import logo from '../assets/logo.png'

export default function Sidebar({ navItems, role, roleBadgeColor }) {
  const { user, logout } = useAuth()
  const { theme, toggleTheme } = useTheme()
  const navigate = useNavigate()
  const [collapsed, setCollapsed] = useState(false)

  const handleLogout = () => {
    logout()
    toast.success('Logged out successfully')
    navigate('/login')
  }

  const roleColors = {
    ADMIN:   { bg: 'rgba(167,139,250,0.15)', text: '#A78BFA', border: 'rgba(167,139,250,0.3)' },
    FACULTY: { bg: 'rgba(56,189,248,0.15)',  text: '#38BDF8', border: 'rgba(56,189,248,0.3)' },
    STUDENT: { bg: 'rgba(52,211,153,0.15)',  text: '#34D399', border: 'rgba(52,211,153,0.3)' },
  }
  const color = roleColors[role] || roleColors.ADMIN

  return (
    <aside style={{
      width: collapsed ? 72 : 240,
      minHeight: '100vh',
      background: 'var(--bg-surface)',
      borderRight: '1px solid var(--border)',
      display: 'flex',
      flexDirection: 'column',
      transition: 'width 0.3s cubic-bezier(0.4,0,0.2,1)',
      position: 'fixed',
      top: 0, left: 0, bottom: 0,
      zIndex: 100,
      overflow: 'hidden',
    }}>
      {/* Logo */}
      <Link to="/" style={{
        padding: collapsed ? '24px 0' : '28px 24px 20px',
        borderBottom: '1px solid var(--border)',
        display: 'flex',
        alignItems: 'center',
        gap: 12,
        overflow: 'hidden',
        justifyContent: collapsed ? 'center' : 'flex-start',
        textDecoration: 'none',
        transition: 'var(--transition)',
      }}
      onMouseEnter={e => e.currentTarget.style.background = 'var(--bg-elevated)'}
      onMouseLeave={e => e.currentTarget.style.background = 'transparent'}
      >
        <div style={{
          width: 36, height: 36, borderRadius: 10,
          background: 'linear-gradient(135deg, var(--gold) 0%, #E08A00 100%)',
          display: 'flex', alignItems: 'center', justifyContent: 'center',
          flexShrink: 0, overflow: 'hidden'
        }}>
          <img src={logo} alt="FES Logo" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
        </div>
        {!collapsed && (
          <div style={{ overflow: 'hidden' }}>
            <div style={{
              fontFamily: 'var(--font-display)', fontWeight: 700,
              fontSize: '0.95rem', color: 'var(--text-primary)',
              whiteSpace: 'nowrap',
            }}>EvalPortal</div>
            <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)', marginTop: 1 }}>
              Faculty Evaluation
            </div>
          </div>
         )}
       </Link>


      {/* Sidebar Search (Admin Only) */}
      {!collapsed && role === 'ADMIN' && (
        <div style={{ padding: '4px 16px 12px' }}>
           <div style={{ position: 'relative' }}>
              <input 
                type="text" 
                placeholder="Quick search..."
                style={{
                  width: '100%', background: 'var(--bg-card)', 
                  border: '1px solid var(--border)', borderRadius: 8,
                  padding: '8px 12px 8px 32px', fontSize: '0.8rem', outline: 'none',
                  color: 'var(--text-primary)', transition: 'var(--transition)'
                }}
                onFocus={e => e.currentTarget.style.borderColor = 'var(--gold)'}
                onBlur={e => e.currentTarget.style.borderColor = 'var(--border)'}
              />
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" style={{ position: 'absolute', left: 10, top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }}>
                <circle cx="11" cy="11" r="8"/><path d="M21 21l-4.35-4.35"/>
              </svg>
           </div>
        </div>
      )}

      {/* User Badge */}
      {!collapsed && (
        <div style={{
          margin: '16px 16px 8px',
          padding: '12px 14px',
          borderRadius: 'var(--radius-md)',
          background: color.bg,
          border: `1px solid ${color.border}`,
        }}>
          <div style={{
            fontSize: '0.65rem', fontWeight: 600, textTransform: 'uppercase',
            letterSpacing: '0.1em', color: color.text, marginBottom: 4,
          }}>
            {role}
          </div>
          <div style={{
            fontSize: '0.875rem', fontWeight: 600, color: 'var(--text-primary)',
            whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis',
          }}>{user?.fullName}</div>
          <div style={{
            fontSize: '0.75rem', color: 'var(--text-secondary)',
            whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis',
          }}>@{user?.username}</div>
          {user?.department && (
            <div style={{
              fontSize: '0.7rem', color: color.text, fontWeight: 500,
              marginTop: 6, display: 'flex', alignItems: 'center', gap: 4
            }}>
               <svg width="10" height="10" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5"><path d="M3 21h18M3 7v14M21 7v14M3 7l9-4 9 4M9 21V11h6v10"/></svg>
               {user?.department}
            </div>
          )}
        </div>
      )}

      {/* Nav Items */}
      <nav style={{ flex: 1, padding: '8px 12px', overflowY: 'auto' }}>
        {navItems.map(item => (
          <NavLink key={item.to} to={item.to} end={item.end}
            style={({ isActive }) => ({
              display: 'flex', alignItems: 'center',
              gap: 12, padding: collapsed ? '12px 0' : '10px 14px',
              borderRadius: 'var(--radius-sm)', marginBottom: 4,
              color: isActive ? color.text : 'var(--text-secondary)',
              background: isActive ? color.bg : 'transparent',
              border: `1px solid ${isActive ? color.border : 'transparent'}`,
              fontWeight: isActive ? 600 : 400,
              fontSize: '0.875rem',
              transition: 'var(--transition)',
              justifyContent: collapsed ? 'center' : 'flex-start',
              whiteSpace: 'nowrap',
              overflow: 'hidden',
            })}
          >
            <span style={{ flexShrink: 0, display: 'flex' }}>{item.icon}</span>
            {!collapsed && item.label}
          </NavLink>
        ))}
      </nav>

      {/* Collapse + Theme + Logout */}
      <div style={{ borderTop: '1px solid var(--border)', padding: '12px' }}>
        <button onClick={() => setCollapsed(!collapsed)} style={{
          width: '100%', padding: collapsed ? '10px 0' : '10px 14px',
          display: 'flex', alignItems: 'center', justifyContent: collapsed ? 'center' : 'flex-start',
          gap: 10, borderRadius: 'var(--radius-sm)',
          background: 'transparent', color: 'var(--text-muted)',
          fontSize: '0.8rem', fontWeight: 500, marginBottom: 4,
          transition: 'var(--transition)',
        }}
          onMouseEnter={e => e.currentTarget.style.color = 'var(--text-secondary)'}
          onMouseLeave={e => e.currentTarget.style.color = 'var(--text-muted)'}
        >
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            {collapsed
              ? <path d="M9 18l6-6-6-6" />
              : <path d="M15 18l-6-6 6-6" />}
          </svg>
          {!collapsed && 'Collapse'}
        </button>

        <button onClick={toggleTheme} style={{
          width: '100%', padding: collapsed ? '10px 0' : '10px 14px',
          display: 'flex', alignItems: 'center', justifyContent: collapsed ? 'center' : 'flex-start',
          gap: 10, borderRadius: 'var(--radius-sm)',
          background: 'transparent', color: 'var(--text-secondary)',
          fontSize: '0.8rem', fontWeight: 600, marginBottom: 4,
          transition: 'var(--transition)',
        }}
          onMouseEnter={e => e.currentTarget.style.background = 'var(--bg-elevated)'}
          onMouseLeave={e => e.currentTarget.style.background = 'transparent'}
        >
          {theme === 'dark' ? (
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><circle cx="12" cy="12" r="5"/><path d="M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42"/></svg>
          ) : (
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M21 12.79A9 9 0 1111.21 3 7 7 0 0021 12.79z"/></svg>
          )}
          {!collapsed && (theme === 'dark' ? 'Light Mode' : 'Dark Mode')}
        </button>

        <button id="logout-btn" onClick={handleLogout} style={{
          width: '100%', padding: collapsed ? '10px 0' : '10px 14px',
          display: 'flex', alignItems: 'center', justifyContent: collapsed ? 'center' : 'flex-start',
          gap: 10, borderRadius: 'var(--radius-sm)',
          background: 'transparent', color: 'var(--text-muted)',
          fontSize: '0.875rem', fontWeight: 500,
          transition: 'var(--transition)',
        }}
          onMouseEnter={e => { e.currentTarget.style.color = 'var(--rose)'; e.currentTarget.style.background = 'rgba(248,113,113,0.1)' }}
          onMouseLeave={e => { e.currentTarget.style.color = 'var(--text-muted)'; e.currentTarget.style.background = 'transparent' }}
        >
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4M16 17l5-5-5-5M21 12H9" />
          </svg>
          {!collapsed && 'Log Out'}
        </button>
      </div>
    </aside>
  )
}


// =============================================
// Shared UI Primitives
// =============================================

// ----- Modal -----
export function Modal({ open, onClose, title, children, width = 520 }) {
  if (!open) return null
  return (
    <div onClick={onClose} style={{
      position: 'fixed', inset: 0, zIndex: 1000,
      background: 'rgba(0,0,0,0.5)', backdropFilter: 'blur(8px)',
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      padding: 16,
      animation: 'fadeIn 0.2s ease both',
    }}>
      <div onClick={e => e.stopPropagation()} style={{
        width: '100%', maxWidth: width, maxHeight: '90vh',
        background: 'var(--bg-card)', borderRadius: 'var(--radius-xl)',
        border: '1px solid var(--border-bright)',
        boxShadow: 'var(--shadow-modal)',
        overflow: 'hidden', display: 'flex', flexDirection: 'column',
        animation: 'slideUp 0.25s cubic-bezier(0.4,0,0.2,1) both',
      }}>
        {/* Header */}
        <div style={{
          padding: '20px 24px', borderBottom: '1px solid var(--border)',
          display: 'flex', alignItems: 'center', justifyContent: 'space-between',
          flexShrink: 0,
        }}>
          <h3 style={{
            fontFamily: 'var(--font-display)', fontWeight: 700,
            fontSize: '1.05rem', color: 'var(--text-primary)',
          }}>{title}</h3>
          <button onClick={onClose} style={{
            width: 32, height: 32, borderRadius: 8,
            background: 'var(--bg-elevated)', border: '1px solid var(--border)',
            color: 'var(--text-muted)', display: 'flex',
            alignItems: 'center', justifyContent: 'center',
            transition: 'var(--transition)', cursor: 'pointer',
          }}
            onMouseEnter={e => e.currentTarget.style.color = 'var(--text-primary)'}
            onMouseLeave={e => e.currentTarget.style.color = 'var(--text-muted)'}
          >
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
              <path d="M18 6L6 18M6 6l12 12" />
            </svg>
          </button>
        </div>
        {/* Body */}
        <div style={{ padding: '24px', overflowY: 'auto', flex: 1 }}>
          {children}
        </div>
      </div>
    </div>
  )
}

// ----- FormField -----
export function FormField({ label, children, error }) {
  return (
    <div style={{ marginBottom: 16 }}>
      <label style={{
        display: 'block', fontSize: '0.78rem', fontWeight: 600,
        color: 'var(--text-secondary)', marginBottom: 6,
        textTransform: 'uppercase', letterSpacing: '0.06em',
      }}>{label}</label>
      {children}
      {error && <div style={{ fontSize: '0.75rem', color: 'var(--rose)', marginTop: 4 }}>{error}</div>}
    </div>
  )
}

// ----- Input -----
export function Input({ style, ...props }) {
  return (
    <input
      style={{
        width: '100%', padding: '10px 14px',
        background: 'var(--bg-elevated)', border: '1px solid var(--border-bright)',
        borderRadius: 'var(--radius-sm)', color: 'var(--text-primary)',
        fontSize: '0.875rem', transition: 'var(--transition)',
        ...style,
      }}
      onFocus={e => e.target.style.borderColor = 'var(--gold)'}
      onBlur={e => e.target.style.borderColor = 'var(--border-bright)'}
      {...props}
    />
  )
}

// ----- Select -----
export function Select({ style, children, ...props }) {
  return (
    <select
      style={{
        width: '100%', padding: '10px 14px',
        background: 'var(--bg-elevated)', border: '1px solid var(--border-bright)',
        borderRadius: 'var(--radius-sm)', color: 'var(--text-primary)',
        fontSize: '0.875rem', cursor: 'pointer', transition: 'var(--transition)',
        ...style,
      }}
      onFocus={e => e.target.style.borderColor = 'var(--gold)'}
      onBlur={e => e.target.style.borderColor = 'var(--border-bright)'}
      {...props}
    >
      {children}
    </select>
  )
}

// ----- Textarea -----
export function Textarea({ style, id, ...props }) {
  return (
    <textarea
      id={id}
      style={{
        width: '100%', padding: '10px 14px',
        background: 'var(--bg-elevated)', border: '1px solid var(--border-bright)',
        borderRadius: 'var(--radius-sm)', color: 'var(--text-primary)',
        fontSize: '0.875rem', resize: 'vertical', minHeight: 90,
        transition: 'var(--transition)', ...style,
      }}
      onFocus={e => e.target.style.borderColor = 'var(--gold)'}
      onBlur={e => e.target.style.borderColor = 'var(--border-bright)'}
      {...props}
    />
  )
}

// ----- Button -----
export function Button({ variant = 'primary', loading, style, children, id, ...props }) {
  const variants = {
    primary: {
      background: 'linear-gradient(135deg, var(--gold) 0%, #D4920A 100%)',
      color: '#080E1A', fontWeight: 700,
    },
    secondary: {
      background: 'var(--bg-elevated)', color: 'var(--text-primary)',
      border: '1px solid var(--border-bright)',
    },
    danger: {
      background: 'var(--rose-dim)', color: 'var(--rose)',
      border: '1px solid var(--rose-dim)',
    },
    ghost: {
      background: 'transparent', color: 'var(--text-secondary)',
    },
  }
  return (
    <button
      id={id}
      disabled={loading || props.disabled}
      style={{
        padding: '10px 20px', borderRadius: 'var(--radius-sm)',
        fontSize: '0.875rem', fontWeight: 600,
        display: 'inline-flex', alignItems: 'center', gap: 8,
        transition: 'var(--transition)',
        opacity: (loading || props.disabled) ? 0.6 : 1,
        cursor: (loading || props.disabled) ? 'not-allowed' : 'pointer',
        ...variants[variant],
        ...style,
      }}
      {...props}
    >
      {loading && <Spinner size={14} />}
      {children}
    </button>
  )
}

// ----- StatCard -----
export function StatCard({ label, value, icon, accent = 'var(--gold)', delay = 0, id, ...props }) {
  return (
    <div 
      id={id}
      {...props}
      style={{
      background: 'var(--bg-card)', borderRadius: 'var(--radius-lg)',
      border: '1px solid var(--border)', padding: '24px',
      display: 'flex', flexDirection: 'column', gap: 12,
      animation: 'slideUp 0.4s cubic-bezier(0.4,0,0.2,1) both',
      animationDelay: `${delay}s`,
      position: 'relative', overflow: 'hidden',
    }}>
      <div style={{
        position: 'absolute', top: 0, right: 0, width: 80, height: 80,
        background: `radial-gradient(circle at top right, ${accent}18 0%, transparent 70%)`,
      }} />
      <div style={{
        width: 42, height: 42, borderRadius: 10,
        background: 'var(--bg-elevated)',
        border: '1px solid var(--border)',
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        color: accent,
      }}>{icon}</div>
      <div>
        <div style={{
          fontSize: '2rem', fontWeight: 800,
          fontFamily: 'var(--font-display)', color: 'var(--text-primary)',
          lineHeight: 1,
        }}>{value ?? '—'}</div>
        <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginTop: 6, fontWeight: 500 }}>
          {label}
        </div>
      </div>
    </div>
  )
}

// ----- Badge -----
export function Badge({ text, color = 'gold' }) {
  const colors = {
    gold:   { bg: 'var(--gold-dim)',   text: 'var(--gold)',   border: 'var(--gold-dim)' },
    sky:    { bg: 'var(--sky-dim)',    text: 'var(--sky)',    border: 'var(--sky-dim)' },
    mint:   { bg: 'var(--mint-dim)',   text: 'var(--mint)',   border: 'var(--mint-dim)' },
    rose:   { bg: 'var(--rose-dim)',   text: 'var(--rose)',   border: 'var(--rose-dim)' },
    violet: { bg: 'var(--violet-dim)', text: 'var(--violet)', border: 'var(--violet-dim)' },
    muted:  { bg: 'var(--bg-elevated)',     text: 'var(--text-muted)', border: 'var(--border)' },
  }
  const c = colors[color] || colors.gold
  return (
    <span style={{
      padding: '2px 10px', borderRadius: 100,
      fontSize: '0.72rem', fontWeight: 600,
      textTransform: 'uppercase', letterSpacing: '0.06em',
      background: c.bg, color: c.text,
      border: `1px solid ${c.border}`,
      display: 'inline-block',
    }}>{text}</span>
  )
}

// ----- Spinner -----
export function Spinner({ size = 20, color = 'var(--gold)' }) {
  return (
    <span style={{
      display: 'inline-block',
      width: size, height: size,
      borderRadius: '50%',
      border: `2px solid ${color}30`,
      borderTopColor: color,
      animation: 'spin 0.7s linear infinite',
      flexShrink: 0,
    }} />
  )
}

// ----- PageHeader -----
export function PageHeader({ title, subtitle, action }) {
  return (
    <div style={{
      display: 'flex', alignItems: 'flex-end',
      justifyContent: 'space-between', marginBottom: 28,
      flexWrap: 'wrap', gap: 16,
    }}>
      <div>
        <h1 style={{
          fontFamily: 'var(--font-display)', fontWeight: 800,
          fontSize: '1.6rem', color: 'var(--text-primary)', lineHeight: 1.2,
        }}>{title}</h1>
        {subtitle && <p style={{ color: 'var(--text-muted)', marginTop: 6, fontSize: '0.875rem' }}>
          {subtitle}
        </p>}
      </div>
      {action && <div>{action}</div>}
    </div>
  )
}

// ----- DataTable -----
export function DataTable({ columns, rows, emptyText = 'No records found' }) {
  return (
    <div style={{
      background: 'var(--bg-card)', borderRadius: 'var(--radius-lg)',
      border: '1px solid var(--border)', overflow: 'hidden',
    }}>
      <div style={{ overflowX: 'auto' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ background: 'var(--bg-surface)' }}>
              {columns.map(col => (
                <th key={col.key} style={{
                  padding: '12px 16px', textAlign: 'left',
                  fontSize: '0.72rem', fontWeight: 700,
                  textTransform: 'uppercase', letterSpacing: '0.08em',
                  color: 'var(--text-muted)',
                  borderBottom: '1px solid var(--border)',
                  whiteSpace: 'nowrap',
                }}>{col.label}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {rows.length === 0 ? (
              <tr>
                <td colSpan={columns.length} style={{
                  padding: '40px 16px', textAlign: 'center',
                  color: 'var(--text-muted)', fontSize: '0.875rem',
                }}>{emptyText}</td>
              </tr>
            ) : rows.map((row, i) => (
              <tr key={i} style={{
                borderBottom: i < rows.length - 1 ? '1px solid var(--border)' : 'none',
                transition: 'background 0.15s',
              }}
                onMouseEnter={e => e.currentTarget.style.background = 'var(--bg-elevated)'}
                onMouseLeave={e => e.currentTarget.style.background = 'transparent'}
              >
                {columns.map(col => (
                  <td key={col.key} style={{
                    padding: '13px 16px', fontSize: '0.875rem',
                    color: 'var(--text-primary)', whiteSpace: col.wrap ? 'normal' : 'nowrap',
                  }}>
                    {col.render ? col.render(row[col.key], row) : row[col.key]}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}

// ----- StarDisplay -----
export function StarDisplay({ rating, max = 5, size = 16 }) {
  return (
    <span style={{ display: 'inline-flex', gap: 2 }}>
      {Array.from({ length: max }).map((_, i) => (
        <svg key={i} width={size} height={size} viewBox="0 0 24 24"
          fill={i < Math.round(rating) ? 'var(--gold)' : 'none'}
          stroke={i < Math.round(rating) ? 'var(--gold)' : 'var(--text-muted)'}
          strokeWidth="1.5"
        >
          <polygon points="12,2 15.09,8.26 22,9.27 17,14.14 18.18,21.02 12,17.77 5.82,21.02 7,14.14 2,9.27 8.91,8.26" />
        </svg>
      ))}
    </span>
  )
}

// ----- EmptyState -----
export function EmptyState({ icon, title, subtitle }) {
  return (
    <div style={{
      display: 'flex', flexDirection: 'column', alignItems: 'center',
      justifyContent: 'center', padding: '60px 24px', gap: 12,
      color: 'var(--text-muted)',
    }}>
      <div style={{ opacity: 0.4, marginBottom: 8 }}>{icon}</div>
      <div style={{ fontSize: '1rem', fontWeight: 600, color: 'var(--text-secondary)' }}>{title}</div>
      {subtitle && <div style={{ fontSize: '0.875rem', color: 'var(--text-muted)', textAlign: 'center' }}>{subtitle}</div>}
    </div>
  )
}

// ----- LoadingPage -----
export function LoadingPage() {
  return (
    <div style={{
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      minHeight: '50vh', flexDirection: 'column', gap: 16,
    }}>
      <Spinner size={32} />
      <span style={{ color: 'var(--text-muted)', fontSize: '0.875rem' }}>Loading…</span>
    </div>
  )
}

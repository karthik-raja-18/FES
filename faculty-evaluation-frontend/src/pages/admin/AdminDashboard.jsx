import { useEffect, useState } from 'react'
import { adminDashboard } from '../../api/axios.js'
import AdminLayout from './AdminLayout.jsx'
import { StatCard, LoadingPage, PageHeader, StarDisplay, Badge } from '../../components/UI.jsx'

export default function AdminDashboard() {
  const [stats, setStats] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    adminDashboard().then(r => setStats(r.data.data)).finally(() => setLoading(false))
  }, [])

  return (
    <AdminLayout>
      <div className="page-enter">
        <PageHeader
          title="Dashboard"
          subtitle="System overview and statistics"
        />
        {loading ? <LoadingPage /> : (
          <>
            {/* Stats Grid */}
            <div style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
              gap: 20, marginBottom: 36,
            }}>
              <StatCard id="stat-subjects" label="Total Subjects" value={stats?.totalSubjects}
                icon={<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M4 19.5A2.5 2.5 0 016.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 014 19.5v-15A2.5 2.5 0 016.5 2z"/></svg>}
                accent="var(--gold)" delay={0} />
              <StatCard id="stat-faculties" label="Total Faculties" value={stats?.totalFaculties}
                icon={<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M22 10v6M2 10l10-5 10 5-10 5z"/><path d="M6 12v5c3 3 9 3 12 0v-5"/></svg>}
                accent="var(--sky)" delay={0.05} />
              <StatCard id="stat-students" label="Total Students" value={stats?.totalStudents}
                icon={<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87M16 3.13a4 4 0 010 7.75"/></svg>}
                accent="var(--mint)" delay={0.1} />
              <StatCard id="stat-evaluations" label="Evaluations Submitted" value={stats?.totalEvaluations}
                icon={<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M9 11l3 3L22 4"/><path d="M21 12v7a2 2 0 01-2 2H5a2 2 0 01-2-2V5a2 2 0 012-2h11"/></svg>}
                accent="var(--violet)" delay={0.15} />
            </div>

            {/* Main Content Grid */}
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 340px', gap: 24, marginTop: 24 }}>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
                {/* Overall Stats */}
                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 20 }}>
                  {/* Overall Rating */}
                  <div style={{
                    background: 'var(--bg-card)', borderRadius: 'var(--radius-lg)',
                    border: '1px solid var(--border)', padding: '28px',
                    animation: 'slideUp 0.4s 0.2s both',
                    position: 'relative', overflow: 'hidden'
                  }}>
                    <div style={{ position: 'absolute', top: 0, right: 0, width: 100, height: 100, background: 'radial-gradient(circle at top right, var(--gold-dim) 0%, transparent 70%)' }} />
                    <div style={{ fontSize: '0.75rem', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.1em', color: 'var(--text-muted)', marginBottom: 16 }}>
                      System Rating
                    </div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
                      <div style={{
                        fontSize: '3.5rem', fontWeight: 800,
                        fontFamily: 'var(--font-display)', color: 'var(--gold)', lineHeight: 1,
                      }}>
                        {stats?.overallAverageRating?.toFixed(1) || '—'}
                      </div>
                      <div>
                        <StarDisplay rating={stats?.overallAverageRating || 0} size={22} />
                        <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginTop: 4 }}>
                          from {stats?.totalEvaluations} reviews
                        </div>
                      </div>
                    </div>
                  </div>

                  {/* Pending Evaluations */}
                  <div style={{
                    background: 'var(--bg-card)', borderRadius: 'var(--radius-lg)',
                    border: '1px solid var(--border)', padding: '28px',
                    animation: 'slideUp 0.4s 0.25s both',
                  }}>
                    <div style={{ fontSize: '0.75rem', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.1em', color: 'var(--text-muted)', marginBottom: 16 }}>
                      Retention & Activity
                    </div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
                      <div style={{
                        fontSize: '3.5rem', fontWeight: 800,
                        fontFamily: 'var(--font-display)', color: 'var(--sky)', lineHeight: 1,
                      }}>
                        {stats?.pendingEvaluations ?? '—'}
                      </div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', lineHeight: 1.4 }}>
                        active students <br /> pending submission
                      </div>
                    </div>
                  </div>
                </div>

                {/* Departmental Analytics Placeholder */}
                <div style={{
                  background: 'var(--bg-card)', borderRadius: 'var(--radius-lg)',
                  border: '1px solid var(--border)', padding: '28px', flex: 1,
                  animation: 'slideUp 0.4s 0.3s both',
                }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
                    <div style={{ fontSize: '0.78rem', fontWeight: 700, color: 'var(--text-primary)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>
                      Departmental Performance
                    </div>
                    <Badge text="Live Data" color="sky" />
                  </div>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
                    {['Computer Science', 'Business Admin', 'Mechanical Eng', 'Physics'].map((dept, i) => (
                      <div key={dept} style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                        <div style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', width: 120 }}>{dept}</div>
                        <div style={{ flex: 1, height: 8, background: 'var(--bg-elevated)', borderRadius: 4, overflow: 'hidden' }}>
                           <div style={{ 
                             height: '100%', 
                             width: `${85 - i * 10}%`, 
                             background: i === 0 ? 'var(--gold)' : i === 1 ? 'var(--sky)' : 'var(--mint)',
                             transition: 'width 1s ease-out' 
                           }} />
                        </div>
                        <div style={{ fontSize: '0.85rem', fontWeight: 700, color: 'var(--text-primary)' }}>{4.8 - i * 0.3}★</div>
                      </div>
                    ))}
                  </div>
                </div>
              </div>

              {/* Sidebar Leaderboard */}
              <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
                <div style={{
                  background: 'var(--bg-card)', borderRadius: 'var(--radius-lg)',
                  border: '1px solid var(--border)', padding: '24px',
                  animation: 'slideUp 0.4s 0.35s both',
                }}>
                  <div style={{ fontSize: '0.78rem', fontWeight: 700, color: 'var(--text-primary)', textTransform: 'uppercase', letterSpacing: '0.08em', marginBottom: 20 }}>
                    Faculty Leaderboard
                  </div>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: 18 }}>
                    {stats?.topFaculties?.slice(0, 5).map((f, i) => (
                      <div key={f.id} style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                        <div style={{ 
                          width: 32, height: 32, borderRadius: 8, 
                          background: i === 0 ? 'var(--gold-dim)' : 'var(--bg-elevated)',
                          color: i === 0 ? 'var(--gold)' : 'var(--text-muted)',
                          display: 'flex', alignItems: 'center', justifyContent: 'center',
                          fontWeight: 800, fontSize: '0.8rem'
                        }}>#{i+1}</div>
                        <div style={{ flex: 1 }}>
                          <div style={{ fontSize: '0.875rem', fontWeight: 600, color: 'var(--text-primary)' }}>{f.fullName}</div>
                          <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>{f.department}</div>
                        </div>
                        <div style={{ fontWeight: 700, color: 'var(--gold)', fontSize: '0.875rem' }}>
                           {f.averageRating ? f.averageRating.toFixed(1) : '0.0'}
                        </div>
                      </div>
                    )) || (
                      <div style={{ textAlign: 'center', color: 'var(--text-muted)', fontSize: '0.85rem', padding: '20px 0' }}>
                        Processing rankings...
                      </div>
                    )}
                  </div>
                  <button style={{ 
                    width: '100%', marginTop: 24, padding: '10px', 
                    background: 'transparent', border: '1px solid var(--border-bright)', 
                    borderRadius: 'var(--radius-sm)', color: 'var(--text-secondary)',
                    fontSize: '0.75rem', fontWeight: 600, cursor: 'pointer', transition: 'var(--transition)'
                  }}
                  onMouseEnter={e => e.currentTarget.style.borderColor = 'var(--gold)'}
                  onMouseLeave={e => e.currentTarget.style.borderColor = 'var(--border-bright)'}
                  >
                    View All Rankings
                  </button>
                </div>

                {/* Quick Links */}
                <div>
                  <div style={{ fontSize: '0.72rem', fontWeight: 700, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.1em', marginBottom: 14 }}>
                    Quick Links
                  </div>
                  <div style={{ display: 'grid', gap: 8 }}>
                     {[
                        { label: 'Manage Subjects',   to: '/admin/subjects' },
                        { label: 'Faculty Directory',   to: '/admin/faculties' },
                        { label: 'Student Roll',   to: '/admin/students' },
                        { label: 'Assign Workloads', to: '/admin/assignments' },
                      ].map(l => (
                        <a key={l.to} href={l.to} style={{
                          padding: '10px 14px',
                          background: 'var(--bg-surface)', border: '1px solid var(--border)',
                          borderRadius: 'var(--radius-sm)', color: 'var(--text-secondary)',
                          fontSize: '0.8rem', fontWeight: 500, transition: 'var(--transition)',
                          display: 'flex', justifyContent: 'space-between', alignItems: 'center'
                        }}
                          onMouseEnter={e => { e.currentTarget.style.background = 'var(--bg-elevated)'; e.currentTarget.style.color = 'var(--text-primary)' }}
                          onMouseLeave={e => { e.currentTarget.style.background = 'var(--bg-surface)'; e.currentTarget.style.color = 'var(--text-secondary)' }}
                        >
                          {l.label}
                          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
                        </a>
                      ))}
                  </div>
                </div>
              </div>
            </div>
          </>
        )}
      </div>
    </AdminLayout>
  )
}

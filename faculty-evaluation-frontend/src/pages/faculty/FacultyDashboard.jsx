import { useState, useEffect } from 'react'
import { RadialBarChart, RadialBar, BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Cell } from 'recharts'
import Sidebar from '../../components/Sidebar.jsx'
import { getFacultyDashboard } from '../../api/axios.js'
import { Badge, StarDisplay, LoadingPage, EmptyState } from '../../components/UI.jsx'
import { useAuth } from '../../context/AuthContext.jsx'

const NAV = [
  {
    to: '/faculty', end: true, label: 'My Dashboard',
    icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/></svg>,
  },
  {
    to: '/profile', label: 'My Profile',
    icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>,
  },
]

function FeedbackCard({ item, index }) {
  const starColors = ['', '#EF4444', '#F97316', '#EAB308', '#22C55E', '#10B981']
  return (
    <div 
      className="feedback-item"
      id={`feedback-item-${index}`}
      style={{
      background: 'var(--bg-elevated)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--border)', padding: '16px 18px',
      animation: `slideUp 0.3s ${index * 0.04}s both`,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 8, gap: 12 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <StarDisplay rating={item.rating} size={14} />
          <span style={{ fontSize: '0.82rem', fontWeight: 700, color: starColors[item.rating] }}>
            {item.rating}/5
          </span>
        </div>
        <div style={{ display: 'flex', gap: 8, flexShrink: 0 }}>
          <Badge text={item.subjectCode} color="sky" />
          <span style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>
            {new Date(item.submittedAt).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' })}
          </span>
        </div>
      </div>
      {item.feedback && (
        <p style={{ fontSize: '0.875rem', color: 'var(--text-secondary)', lineHeight: 1.6, fontStyle: 'italic' }}>
          "{item.feedback}"
        </p>
      )}
    </div>
  )
}

export default function FacultyDashboard() {
  const { user } = useAuth()
  const [data, setData]     = useState(null)
  const [loading, setLoading] = useState(true)
  const [activeTab, setActiveTab] = useState('overview')

  useEffect(() => {
    getFacultyDashboard().then(r => setData(r.data.data)).finally(() => setLoading(false))
  }, [])

  if (loading) return (
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      <Sidebar navItems={NAV} role="FACULTY" />
      <main style={{ marginLeft: 240, flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <LoadingPage />
      </main>
    </div>
  )

  const ratingDist = data?.ratingDistribution
    ? Object.entries(data.ratingDistribution).map(([star, count]) => ({ star: `${star}★`, count, fill: ['#EF4444','#F97316','#EAB308','#22C55E','#10B981'][+star - 1] }))
    : []

  const tabStyle = active => ({
    padding: '8px 20px', borderRadius: 'var(--radius-sm)', border: 'none',
    fontWeight: 600, fontSize: '0.85rem', cursor: 'pointer',
    background: active ? 'rgba(56,189,248,0.15)' : 'transparent',
    color: active ? 'var(--sky)' : 'var(--text-muted)',
    borderBottom: active ? '2px solid var(--sky)' : '2px solid transparent',
    transition: 'var(--transition)',
  })

  return (
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      <Sidebar navItems={NAV} role="FACULTY" />
      <main style={{ marginLeft: 240, flex: 1, padding: '32px 36px', minHeight: '100vh' }}>
        <div className="page-enter">
          {/* Header */}
          <div style={{ marginBottom: 32 }}>
            <h1 style={{ fontFamily: 'var(--font-display)', fontWeight: 800, fontSize: '1.6rem', color: 'var(--text-primary)' }}>
              Performance Dashboard
            </h1>
            <p style={{ color: 'var(--text-muted)', marginTop: 6, fontSize: '0.875rem' }}>
              {data?.department} · {data?.designation}
            </p>
          </div>

          {/* Performance Sections */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 340px', gap: 24, marginBottom: 32 }}>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
              {/* Stats Row */}
              <div style={{ display: 'grid', gridTemplateColumns: 'auto 1fr', gap: 20 }}>
                {/* Big rating card */}
                <div style={{
                  background: 'var(--bg-card)', borderRadius: 'var(--radius-xl)',
                  border: '1px solid var(--border-bright)', padding: '28px 36px',
                  display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
                  minWidth: 200, animation: 'slideUp 0.4s both',
                  background: 'linear-gradient(135deg, var(--bg-card) 0%, rgba(240,165,0,0.06) 100%)',
                }}>
                  <div style={{ fontSize: '0.72rem', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.1em', color: 'var(--text-muted)', marginBottom: 8 }}>
                    Overall Rating
                  </div>
                  <div id="faculty-overall-rating" style={{
                    fontSize: '4.5rem', fontWeight: 800, fontFamily: 'var(--font-display)',
                    color: 'var(--gold)', lineHeight: 1,
                    textShadow: '0 0 40px rgba(240,165,0,0.3)',
                  }}>
                    {data?.overallAverageRating?.toFixed(1) || '—'}
                  </div>
                  <div style={{ marginTop: 10 }}>
                    <StarDisplay rating={data?.overallAverageRating || 0} size={20} />
                  </div>
                  <div id="faculty-total-evals" style={{ fontSize: '0.78rem', color: 'var(--text-muted)', marginTop: 8 }}>
                    from {data?.totalEvaluations} reviews
                  </div>
                </div>

                {/* Rating Distribution */}
                <div style={{
                  background: 'var(--bg-card)', borderRadius: 'var(--radius-xl)',
                  border: '1px solid var(--border)', padding: '24px',
                  animation: 'slideUp 0.4s 0.05s both',
                }}>
                  <div style={{ fontSize: '0.72rem', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.1em', color: 'var(--text-muted)', marginBottom: 16 }}>
                    Rating Distribution
                  </div>
                  {data?.totalEvaluations > 0 ? (
                    <ResponsiveContainer width="100%" height={140}>
                      <BarChart data={ratingDist} barSize={32}>
                        <XAxis dataKey="star" axisLine={false} tickLine={false} tick={{ fontSize: 11, fill: 'var(--text-muted)' }} />
                        <YAxis hide />
                        <Tooltip
                          contentStyle={{ background: 'var(--bg-elevated)', border: '1px solid var(--border)', borderRadius: 8, fontSize: 12 }}
                          cursor={{ fill: 'rgba(255,255,255,0.04)' }}
                        />
                        <Bar dataKey="count" radius={[4,4,0,0]}>
                          {ratingDist.map((entry, i) => <Cell key={i} fill={entry.fill} />)}
                        </Bar>
                      </BarChart>
                    </ResponsiveContainer>
                  ) : <EmptyState title="No data" />}
                </div>
              </div>

              {/* Subject Performance Grid */}
              <div style={{
                background: 'var(--bg-card)', borderRadius: 'var(--radius-xl)',
                border: '1px solid var(--border)', padding: '24px',
                animation: 'slideUp 0.4s 0.1s both',
              }}>
                <div style={{ fontSize: '0.72rem', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.1em', color: 'var(--text-muted)', marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
                  Subject Breakdown
                  <span style={{ color: 'var(--sky)', textTransform: 'none' }}>Active Semester</span>
                </div>
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: 16 }}>
                  {data?.subjectSummaries?.length > 0 ? data.subjectSummaries.map(s => (
                    <div key={s.subjectId} style={{
                      padding: '16px', background: 'var(--bg-surface)',
                      borderRadius: 'var(--radius-md)', border: '1px solid var(--border)',
                      display: 'flex', flexDirection: 'column', gap: 10
                    }}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                        <div style={{ fontWeight: 700, fontSize: '0.95rem', color: 'var(--text-primary)' }}>{s.subjectCode}</div>
                        <Badge text={`${s.averageRating?.toFixed(1)}★`} color="gold" />
                      </div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{s.subjectName}</div>
                      <div style={{ height: 4, background: 'var(--bg-elevated)', borderRadius: 2 }}>
                        <div style={{ height: '100%', width: `${(s.averageRating / 5) * 100}%`, background: 'var(--gold)', borderRadius: 2 }} />
                      </div>
                      <div id={`subject-eval-count-${s.subjectCode}`} style={{ fontSize: '0.7rem', color: 'var(--text-muted)' }}>{s.evaluationCount} evaluations submitted</div>
                    </div>
                  )) : <div style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>No subjects assigned</div>}
                </div>
              </div>
            </div>

            {/* Sidebar Insights */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
              {/* Achievement Card */}
              <div style={{
                background: 'var(--bg-card)', borderRadius: 'var(--radius-xl)',
                border: '1px solid var(--border)', padding: '24px',
                animation: 'slideUp 0.4s 0.2s both',
                textAlign: 'center'
              }}>
                <div style={{ 
                  width: 60, height: 60, borderRadius: '50%', background: 'var(--gold-dim)',
                  margin: '0 auto 16px', display: 'flex', alignItems: 'center', justifyContent: 'center',
                  color: 'var(--gold)', border: '1px solid var(--gold-glow)'
                }}>
                  <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M6 9H4.5a2.5 2.5 0 010-5H6M18 9h1.5a2.5 2.5 0 000-5H18M4 22h16M10 14.66V17M14 14.66V17M18 20H6M15.34 9A5.24 5.24 0 0012 3a5.24 5.24 0 00-3.34 6 5.5 5.5 0 001.33 10.11 c.41.08.67.43.67.89V20h2.66v-1c0-.46.26-.81.67-.89A5.5 5.5 0 0015.34 9z"/></svg>
                </div>
                <h4 style={{ fontSize: '0.9rem', fontWeight: 700, marginBottom: 4 }}>Gold Contributor</h4>
                <p style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Top 5% Faculty by Engagement</p>
              </div>
            </div>
          </div>

          {/* Tabs */}
          <div style={{ display: 'flex', gap: 4, borderBottom: '1px solid var(--border)', marginBottom: 24 }}>
            {[
              { key: 'overview', label: 'All Feedback' },
              ...(data?.subjectSummaries?.map(s => ({ key: s.subjectCode, label: s.subjectName, summary: s })) || []),
            ].map(t => (
              <button key={t.key} style={tabStyle(activeTab === t.key)} onClick={() => setActiveTab(t.key)}>
                {t.label}
              </button>
            ))}
          </div>

          {/* Feedback list */}
          {activeTab === 'overview' ? (
            data?.recentFeedbacks?.length > 0 ? (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
                <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)', marginBottom: 4, fontWeight: 600 }}>
                  {data.recentFeedbacks.length} written feedback comment{data.recentFeedbacks.length !== 1 ? 's' : ''}
                </div>
                {data.recentFeedbacks.map((fb, i) => <FeedbackCard key={fb.id} item={fb} index={i} />)}
              </div>
            ) : (
              <EmptyState
                icon={<svg width="56" height="56" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1"><path d="M21 15a2 2 0 01-2 2H7l-4 4V5a2 2 0 012-2h14a2 2 0 012 2z"/></svg>}
                title="No written feedback yet"
                subtitle="Students can optionally leave comments when they evaluate you"
              />
            )
          ) : (
            (() => {
              const summary = data?.subjectSummaries?.find(s => s.subjectCode === activeTab)
              return summary?.feedbacks?.length > 0 ? (
                <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginBottom: 4 }}>
                    <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)', fontWeight: 600 }}>
                      {summary.evaluationCount} evaluations submitted
                    </div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
                      <StarDisplay rating={summary.averageRating} size={13} />
                      <span style={{ color: 'var(--gold)', fontWeight: 700, fontSize: '0.9rem' }}>{summary.averageRating?.toFixed(1)}</span>
                      <span style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>avg</span>
                    </div>
                  </div>
                  {summary.feedbacks.map((fb, i) => <FeedbackCard key={fb.id} item={fb} index={i} />)}
                </div>
              ) : (
                <EmptyState
                  icon={<svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1"><path d="M21 15a2 2 0 01-2 2H7l-4 4V5a2 2 0 012-2h14a2 2 0 012 2z"/></svg>}
                  title="No written feedback for this subject"
                  subtitle={summary ? `${summary.evaluationCount} student${summary.evaluationCount !== 1 ? 's' : ''} rated without comments` : 'No evaluations yet'}
                />
              )
            })()
          )}
        </div>
      </main>
    </div>
  )
}

import { useState, useEffect } from 'react'
import toast from 'react-hot-toast'
import Sidebar from '../../components/Sidebar.jsx'
import { getMySubjects, submitEvaluation } from '../../api/axios.js'
import { Modal, Button, Textarea, Badge, LoadingPage, EmptyState } from '../../components/UI.jsx'
import StarRating from '../../components/StarRating.jsx'
import { useAuth } from '../../context/AuthContext.jsx'

const NAV = [
  {
    to: '/student', end: true, label: 'My Subjects',
    icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M4 19.5A2.5 2.5 0 016.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 014 19.5v-15A2.5 2.5 0 016.5 2z"/></svg>,
  },
  {
    to: '/profile', label: 'My Profile',
    icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>,
  },
]

export default function StudentDashboard() {
  const { user } = useAuth()
  const [subjects, setSubjects] = useState([])
  const [loading, setLoading]   = useState(true)
  const [modal, setModal]       = useState(false)
  const [active, setActive]     = useState(null)
  const [rating, setRating]     = useState(0)
  const [feedback, setFeedback] = useState('')
  const [submitting, setSubmitting] = useState(false)

  const load = () => getMySubjects().then(r => setSubjects(r.data.data)).finally(() => setLoading(false))
  useEffect(() => { load() }, [])

  const openEval = subj => {
    setActive(subj); setRating(0); setFeedback(''); setModal(true)
  }

  const handleSubmit = async () => {
    if (!rating) { toast.error('Please select a star rating'); return }
    setSubmitting(true)
    try {
      await submitEvaluation({ subjectId: active.subjectId, facultyId: active.facultyId, rating, feedback })
      toast.success('Evaluation submitted anonymously!')
      setModal(false)
      load()
    } catch (err) { toast.error(err.response?.data?.message || 'Error submitting') }
    finally { setSubmitting(false) }
  }

  const pending  = subjects.filter(s => !s.alreadyEvaluated && s.facultyId)
  const done     = subjects.filter(s => s.alreadyEvaluated)
  const noFaculty = subjects.filter(s => !s.facultyId)

  return (
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      <Sidebar navItems={NAV} role="STUDENT" />
      <main style={{ marginLeft: 240, flex: 1, padding: '32px 36px', minHeight: '100vh' }}>
        <div className="page-enter">
          {/* Header */}
          <div style={{ marginBottom: 32 }}>
            <h1 style={{ fontFamily: 'var(--font-display)', fontWeight: 800, fontSize: '1.6rem', color: 'var(--text-primary)' }}>
              My Enrolled Subjects
            </h1>
            <p style={{ color: 'var(--text-muted)', marginTop: 6, fontSize: '0.875rem' }}>
              Welcome, {user?.fullName}. Submit your anonymous feedback for each subject.
            </p>
          </div>

          {/* Main Layout Grid */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 320px', gap: 24 }}>
            <div>
              {/* Summary bar */}
              {!loading && subjects.length > 0 && (
                <div style={{
                  display: 'flex', gap: 12, marginBottom: 28, flexWrap: 'wrap',
                }}>
                  {[
                    { label: 'Total Enrolled', val: subjects.length, color: 'var(--gold)' },
                    { label: 'Pending Feedback', val: pending.length, color: 'var(--rose)' },
                    { label: 'Submitted', val: done.length, color: 'var(--mint)' },
                  ].map(s => (
                    <div key={s.label} style={{
                      padding: '10px 20px', background: 'var(--bg-card)',
                      borderRadius: 'var(--radius-md)', border: '1px solid var(--border)',
                      display: 'flex', gap: 10, alignItems: 'center',
                    }}>
                      <span style={{ fontWeight: 800, fontSize: '1.2rem', fontFamily: 'var(--font-display)', color: s.color }}>{s.val}</span>
                      <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{s.label}</span>
                    </div>
                  ))}
                </div>
              )}
        
              {/* List */}
               {loading ? <LoadingPage /> : subjects.length === 0 ? (
                <EmptyState
                  icon={<svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1"><path d="M4 19.5A2.5 2.5 0 016.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 014 19.5v-15A2.5 2.5 0 016.5 2z"/></svg>}
                  title="No subjects enrolled"
                  subtitle="Contact your admin to enroll you in subjects"
                />
              ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
                  {subjects.map((s, i) => (
                    // ... rest of item remains (removed for brevity in chunk but will be kept in full replacement)
                    <div key={s.enrollmentId} style={{
                      background: 'var(--bg-card)', borderRadius: 'var(--radius-lg)',
                      border: `1px solid ${s.alreadyEvaluated ? 'rgba(52,211,153,0.15)' : !s.facultyId ? 'var(--border)' : 'var(--border-bright)'}`,
                      padding: '22px 26px',
                      display: 'flex', alignItems: 'center', gap: 20, flexWrap: 'wrap',
                      animation: `slideUp 0.35s ${i * 0.05}s both`,
                      position: 'relative', overflow: 'hidden',
                    }}>
                      {/* Side accent */}
                      <div style={{
                        position: 'absolute', left: 0, top: 0, bottom: 0, width: 3,
                        background: s.alreadyEvaluated ? 'var(--mint)' : !s.facultyId ? 'var(--text-muted)' : 'var(--gold)',
                        borderRadius: '3px 0 0 3px',
                      }} />
    
                      {/* Subject code badge */}
                      <div style={{
                        width: 54, height: 54, borderRadius: 12,
                        background: 'rgba(240,165,0,0.08)', border: '1px solid rgba(240,165,0,0.25)',
                        display: 'flex', alignItems: 'center', justifyContent: 'center',
                        fontFamily: 'var(--font-mono)', fontSize: '0.7rem', color: 'var(--gold)',
                        fontWeight: 700, textAlign: 'center', lineHeight: 1.2, flexShrink: 0,
                      }}>{s.subjectCode}</div>
    
                      {/* Subject info */}
                      <div style={{ flex: 1, minWidth: 200 }}>
                        <div style={{ fontWeight: 700, fontSize: '1rem', color: 'var(--text-primary)', marginBottom: 2 }}>{s.subjectName}</div>
                        {s.description && <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginBottom: 6 }}>{s.description}</div>}
                      </div>
    
                      {/* Faculty info */}
                      <div style={{ minWidth: 150 }}>
                        {s.facultyId ? (
                          <div style={{ fontWeight: 600, color: 'var(--sky)', fontSize: '0.9rem' }}>{s.facultyName}</div>
                        ) : (
                          <span style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>No instructor</span>
                        )}
                      </div>
    
                      {/* Eval button */}
                      <div style={{ flexShrink: 0 }}>
                        {s.alreadyEvaluated ? (
                          <div id={`status-done-${s.subjectCode}`} style={{
                            display: 'flex', alignItems: 'center', gap: 6,
                            padding: '6px 14px', borderRadius: 100,
                            background: 'rgba(52,211,153,0.1)', color: 'var(--mint)', 
                            fontSize: '0.75rem', fontWeight: 700,
                          }}>
                            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="3"><path d="M20 6L9 17l-5-5"/></svg>
                            Complete
                          </div>
                        ) : s.facultyId ? (
                          <Button id={`evaluate-btn-${s.subjectCode}`} onClick={() => openEval(s)} style={{ padding: '8px 16px' }}>
                            ⭐ Evaluate
                          </Button>
                        ) : null}
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>

            {/* Sidebar Status */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
                {/* Confidentiality Shield */}
                <div style={{
                  background: 'var(--bg-card)', borderRadius: 'var(--radius-xl)',
                  border: '1px solid var(--border-bright)', padding: '24px',
                  position: 'relative', overflow: 'hidden',
                  background: 'linear-gradient(to bottom right, var(--bg-card), rgba(52,211,153,0.03))'
                }}>
                   <div style={{ 
                     width: 44, height: 44, borderRadius: 12, background: 'rgba(52,211,153,0.15)',
                     display: 'flex', alignItems: 'center', justifyContent: 'center',
                     color: 'var(--mint)', marginBottom: 16
                   }}>
                     <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
                   </div>
                   <h3 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: 8 }}>Confidentiality Guaranteed</h3>
                   <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', lineHeight: 1.6 }}>
                     Your feedback is encrypted and decoupled from your identity. Faculty only see aggregated scores and anonymized comments.
                   </p>
                </div>

                {/* Progress Card */}
                <div style={{
                  background: 'var(--bg-card)', borderRadius: 'var(--radius-xl)',
                  border: '1px solid var(--border)', padding: '24px'
                }}>
                   <div style={{ fontSize: '0.72rem', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.1em', color: 'var(--text-muted)', marginBottom: 20 }}>
                     Submission Progress
                   </div>
                   <div style={{ position: 'relative', height: 160, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                      <svg width="120" height="120" viewBox="0 0 100 100">
                        <circle cx="50" cy="50" r="40" fill="none" stroke="var(--bg-elevated)" strokeWidth="8" />
                        <circle cx="50" cy="50" r="40" fill="none" stroke={done.length === subjects.length ? 'var(--mint)' : 'var(--gold)'} 
                          strokeWidth="8" strokeDasharray={`${(done.length / (subjects.length || 1)) * 251.2} 251.2`} 
                          strokeLinecap="round" transform="rotate(-90 50 50)" style={{ transition: 'stroke-dasharray 1s ease' }} 
                        />
                        <text x="50" y="55" textAnchor="middle" fontSize="14" fontWeight="800" fill="var(--text-primary)" fontFamily="var(--font-display)">
                           {Math.round((done.length / (subjects.length || 1)) * 100)}%
                        </text>
                      </svg>
                   </div>
                   <div style={{ textAlign: 'center', marginTop: 16 }}>
                      <div style={{ fontSize: '0.875rem', fontWeight: 600 }}>{done.length} of {subjects.length} Completed</div>
                      <p style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginTop: 4 }}>
                         {pending.length} evaluations remaining
                      </p>
                   </div>
                </div>
            </div>
          </div>
        </div>
      </main>

      {/* Evaluation Modal */}
      <Modal open={modal} onClose={() => setModal(false)} title="Submit Evaluation" width={500}>
        {active && (
          <div>
            {/* Anonymous notice */}
            <div style={{
              padding: '10px 14px', borderRadius: 'var(--radius-sm)',
              background: 'rgba(56,189,248,0.08)', border: '1px solid rgba(56,189,248,0.2)',
              color: 'var(--sky)', fontSize: '0.8rem', marginBottom: 22,
              display: 'flex', alignItems: 'center', gap: 8,
            }}>
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><circle cx="12" cy="12" r="10"/><path d="M12 8v4M12 16h.01"/></svg>
              Your response is completely anonymous. Your identity will not be shared.
            </div>

            {/* Subject and faculty info */}
            <div style={{
              padding: '14px 16px', background: 'var(--bg-elevated)', borderRadius: 'var(--radius-md)',
              marginBottom: 22, border: '1px solid var(--border)',
            }}>
              <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginBottom: 4 }}>Evaluating</div>
              <div style={{ fontWeight: 700, fontSize: '1rem', color: 'var(--text-primary)' }}>{active.subjectName}</div>
              <div style={{ fontSize: '0.85rem', color: 'var(--sky)', marginTop: 2 }}>{active.facultyName}</div>
            </div>

            {/* Star Rating */}
            <div style={{ marginBottom: 22 }}>
              <div style={{ fontSize: '0.78rem', fontWeight: 700, color: 'var(--text-secondary)', marginBottom: 12, textTransform: 'uppercase', letterSpacing: '0.08em' }}>
                Your Rating *
              </div>
              <StarRating value={rating} onChange={setRating} size={36} />
            </div>

            {/* Feedback */}
            <div style={{ marginBottom: 24 }}>
              <div style={{ fontSize: '0.78rem', fontWeight: 700, color: 'var(--text-secondary)', marginBottom: 6, textTransform: 'uppercase', letterSpacing: '0.08em' }}>
                Written Feedback (Optional)
              </div>
              <Textarea
                id="eval-feedback"
                placeholder="Share your thoughts about the teaching quality, course content, engagement..."
                value={feedback}
                onChange={e => setFeedback(e.target.value)}
                style={{ minHeight: 110 }}
                maxLength={1000}
              />
              <div style={{ textAlign: 'right', fontSize: '0.72rem', color: 'var(--text-muted)', marginTop: 4 }}>
                {feedback.length}/1000
              </div>
            </div>

            <div style={{ display: 'flex', gap: 12, justifyContent: 'flex-end' }}>
              <Button id="cancel-eval-btn" variant="secondary" onClick={() => setModal(false)}>Cancel</Button>
              <Button id="submit-eval-btn" onClick={handleSubmit} loading={submitting} disabled={!rating}>
                Submit Anonymously
              </Button>
            </div>
          </div>
        )}
      </Modal>
    </div>
  )
}

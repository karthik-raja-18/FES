import { useState, useEffect } from 'react'
import toast from 'react-hot-toast'
import AdminLayout from './AdminLayout.jsx'
import {
  getFaculties, getStudents, getSubjects,
  assignFaculty, unassignFaculty, enrollStudent, unenrollStudent,
} from '../../api/axios.js'
import { Select, Button, PageHeader, LoadingPage, Badge } from '../../components/UI.jsx'

export default function AssignmentsPage() {
  const [faculties, setFaculties] = useState([])
  const [students,  setStudents]  = useState([])
  const [subjects,  setSubjects]  = useState([])
  const [loading,   setLoading]   = useState(true)

  const [fsForm, setFsForm] = useState({ facultyId: '', subjectId: '' })
  const [seForm, setSeForm] = useState({ studentId: '', subjectId: '' })
  const [fsLoading, setFsLoading] = useState(false)
  const [seLoading, setSeLoading] = useState(false)
  const [tab, setTab] = useState('assign')

  useEffect(() => {
    Promise.all([
      getFaculties().then(r => setFaculties(r.data.data)),
      getStudents().then(r => setStudents(r.data.data)),
      getSubjects().then(r => setSubjects(r.data.data)),
    ]).finally(() => setLoading(false))
  }, [])

  const refreshSubjects = () => getSubjects().then(r => setSubjects(r.data.data))

  const handleAssign = async e => {
    e.preventDefault()
    if (!fsForm.facultyId || !fsForm.subjectId) { toast.error('Select faculty and subject'); return }
    setFsLoading(true)
    try { await assignFaculty({ facultyId: +fsForm.facultyId, subjectId: +fsForm.subjectId }); toast.success('Faculty assigned!'); setFsForm({ facultyId: '', subjectId: '' }); refreshSubjects() }
    catch (err) { toast.error(err.response?.data?.message || 'Error') }
    finally { setFsLoading(false) }
  }

  const handleUnassign = async e => {
    e.preventDefault()
    if (!fsForm.facultyId || !fsForm.subjectId) { toast.error('Select faculty and subject'); return }
    if (!confirm('Unassign this faculty from the subject?')) return
    setFsLoading(true)
    try { await unassignFaculty({ facultyId: +fsForm.facultyId, subjectId: +fsForm.subjectId }); toast.success('Faculty unassigned'); setFsForm({ facultyId: '', subjectId: '' }); refreshSubjects() }
    catch (err) { toast.error(err.response?.data?.message || 'Error') }
    finally { setFsLoading(false) }
  }

  const handleEnroll = async e => {
    e.preventDefault()
    if (!seForm.studentId || !seForm.subjectId) { toast.error('Select student and subject'); return }
    setSeLoading(true)
    try { await enrollStudent({ studentId: +seForm.studentId, subjectId: +seForm.subjectId }); toast.success('Student enrolled!'); setSeForm({ studentId: '', subjectId: '' }) }
    catch (err) { toast.error(err.response?.data?.message || 'Error') }
    finally { setSeLoading(false) }
  }

  const handleUnenroll = async e => {
    e.preventDefault()
    if (!seForm.studentId || !seForm.subjectId) { toast.error('Select student and subject'); return }
    if (!confirm('Unenroll student from this subject?')) return
    setSeLoading(true)
    try { await unenrollStudent({ studentId: +seForm.studentId, subjectId: +seForm.subjectId }); toast.success('Student unenrolled'); setSeForm({ studentId: '', subjectId: '' }) }
    catch (err) { toast.error(err.response?.data?.message || 'Error') }
    finally { setSeLoading(false) }
  }

  const tabStyle = active => ({
    padding: '10px 24px', borderRadius: 'var(--radius-sm)',
    fontWeight: 600, fontSize: '0.875rem', cursor: 'pointer',
    border: 'none', transition: 'var(--transition)',
    background: active ? 'var(--gold)' : 'var(--bg-elevated)',
    color: active ? '#080E1A' : 'var(--text-secondary)',
  })

  if (loading) return <AdminLayout><LoadingPage /></AdminLayout>

  return (
    <AdminLayout>
      <div className="page-enter">
        <PageHeader title="Assignments & Enrollments" subtitle="Link faculties to subjects and enroll students" />

        {/* Tabs */}
        <div style={{ display: 'flex', gap: 8, marginBottom: 28 }}>
          <button id="tab-assign" style={tabStyle(tab === 'assign')} onClick={() => setTab('assign')}>
            🎓 Assign Faculty to Subject
          </button>
          <button id="tab-enroll" style={tabStyle(tab === 'enroll')} onClick={() => setTab('enroll')}>
            📚 Enroll Student in Subject
          </button>
          <button id="tab-overview" style={tabStyle(tab === 'overview')} onClick={() => setTab('overview')}>
            📋 Subject Overview
          </button>
        </div>

        {/* Faculty-Subject Assignment */}
        {tab === 'assign' && (
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24 }}>
            <div style={{
              background: 'var(--bg-card)', borderRadius: 'var(--radius-lg)',
              border: '1px solid var(--border)', padding: '28px',
              animation: 'slideUp 0.35s both',
            }}>
              <h3 style={{ fontFamily: 'var(--font-display)', fontWeight: 700, fontSize: '1rem', marginBottom: 6, color: 'var(--text-primary)' }}>
                Assign Faculty
              </h3>
              <p style={{ color: 'var(--text-muted)', fontSize: '0.8rem', marginBottom: 24 }}>
                Link a faculty member to teach a specific subject
              </p>
              <form>
                <div style={{ marginBottom: 16 }}>
                  <label style={{ display: 'block', fontSize: '0.72rem', fontWeight: 700, color: 'var(--text-secondary)', marginBottom: 6, textTransform: 'uppercase', letterSpacing: '0.08em' }}>Faculty Member</label>
                  <Select id="assign-faculty-select" value={fsForm.facultyId} onChange={e => setFsForm(p => ({...p, facultyId: e.target.value}))}>
                    <option value="">— Select Faculty —</option>
                    {faculties.filter(f => f.active).map(f => (
                      <option key={f.id} value={f.id}>{f.fullName} ({f.department})</option>
                    ))}
                  </Select>
                </div>
                <div style={{ marginBottom: 24 }}>
                  <label style={{ display: 'block', fontSize: '0.72rem', fontWeight: 700, color: 'var(--text-secondary)', marginBottom: 6, textTransform: 'uppercase', letterSpacing: '0.08em' }}>Subject</label>
                  <Select id="assign-subject-select" value={fsForm.subjectId} onChange={e => setFsForm(p => ({...p, subjectId: e.target.value}))}>
                    <option value="">— Select Subject —</option>
                    {subjects.filter(s => s.active).map(s => (
                      <option key={s.id} value={s.id}>{s.name} ({s.subjectCode})</option>
                    ))}
                  </Select>
                </div>
                <div style={{ display: 'flex', gap: 10 }}>
                  <Button id="assign-btn" style={{ flex: 1, justifyContent: 'center' }} loading={fsLoading} onClick={handleAssign} type="button">
                    Assign
                  </Button>
                  <Button id="unassign-btn" variant="danger" style={{ flex: 1, justifyContent: 'center' }} loading={fsLoading} onClick={handleUnassign} type="button">
                    Unassign
                  </Button>
                </div>
              </form>
            </div>

            {/* Preview panel */}
            <div style={{
              background: 'var(--bg-card)', borderRadius: 'var(--radius-lg)',
              border: '1px solid var(--border)', padding: '28px',
            }}>
              <h3 style={{ fontFamily: 'var(--font-display)', fontWeight: 700, fontSize: '1rem', marginBottom: 20, color: 'var(--text-primary)' }}>
                Current Assignments
              </h3>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 10, maxHeight: 360, overflowY: 'auto' }}>
                {subjects.filter(s => s.active && s.facultyName).map(s => (
                  <div key={s.id} style={{
                    padding: '12px 14px', background: 'var(--bg-elevated)',
                    borderRadius: 'var(--radius-sm)', border: '1px solid var(--border)',
                    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
                  }}>
                    <div>
                      <div style={{ fontWeight: 600, fontSize: '0.875rem' }}>{s.name}</div>
                      <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{s.subjectCode}</div>
                    </div>
                    <Badge text={s.facultyName} color="sky" />
                  </div>
                ))}
                {subjects.filter(s => s.active && s.facultyName).length === 0 && (
                  <div style={{ color: 'var(--text-muted)', fontSize: '0.875rem', textAlign: 'center', padding: '24px 0' }}>
                    No faculty assigned yet
                  </div>
                )}
              </div>
            </div>
          </div>
        )}

        {/* Student Enrollment */}
        {tab === 'enroll' && (
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24 }}>
            <div style={{
              background: 'var(--bg-card)', borderRadius: 'var(--radius-lg)',
              border: '1px solid var(--border)', padding: '28px',
              animation: 'slideUp 0.35s both',
            }}>
              <h3 style={{ fontFamily: 'var(--font-display)', fontWeight: 700, fontSize: '1rem', marginBottom: 6 }}>Enroll Student</h3>
              <p style={{ color: 'var(--text-muted)', fontSize: '0.8rem', marginBottom: 24 }}>
                Register a student in a subject for evaluation
              </p>
              <form>
                <div style={{ marginBottom: 16 }}>
                  <label style={{ display: 'block', fontSize: '0.72rem', fontWeight: 700, color: 'var(--text-secondary)', marginBottom: 6, textTransform: 'uppercase', letterSpacing: '0.08em' }}>Student</label>
                  <Select id="enroll-student-select" value={seForm.studentId} onChange={e => setSeForm(p => ({...p, studentId: e.target.value}))}>
                    <option value="">— Select Student —</option>
                    {students.filter(s => s.active).map(s => (
                      <option key={s.id} value={s.id}>{s.fullName} ({s.rollNumber || s.username})</option>
                    ))}
                  </Select>
                </div>
                <div style={{ marginBottom: 24 }}>
                  <label style={{ display: 'block', fontSize: '0.72rem', fontWeight: 700, color: 'var(--text-secondary)', marginBottom: 6, textTransform: 'uppercase', letterSpacing: '0.08em' }}>Subject</label>
                  <Select id="enroll-subject-select" value={seForm.subjectId} onChange={e => setSeForm(p => ({...p, subjectId: e.target.value}))}>
                    <option value="">— Select Subject —</option>
                    {subjects.filter(s => s.active).map(s => (
                      <option key={s.id} value={s.id}>{s.name} ({s.subjectCode})</option>
                    ))}
                  </Select>
                </div>
                <div style={{ display: 'flex', gap: 10 }}>
                  <Button id="enroll-btn" style={{ flex: 1, justifyContent: 'center' }} loading={seLoading} onClick={handleEnroll} type="button">
                    Enroll
                  </Button>
                  <Button id="unenroll-btn" variant="danger" style={{ flex: 1, justifyContent: 'center' }} loading={seLoading} onClick={handleUnenroll} type="button">
                    Unenroll
                  </Button>
                </div>
              </form>
            </div>

            <div style={{
              background: 'var(--bg-card)', borderRadius: 'var(--radius-lg)',
              border: '1px solid var(--border)', padding: '28px',
            }}>
              <h3 style={{ fontFamily: 'var(--font-display)', fontWeight: 700, fontSize: '1rem', marginBottom: 20 }}>Subject Enrollment Stats</h3>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 10, maxHeight: 360, overflowY: 'auto' }}>
                {subjects.filter(s => s.active).map(s => (
                  <div key={s.id} style={{
                    padding: '12px 14px', background: 'var(--bg-elevated)',
                    borderRadius: 'var(--radius-sm)', border: '1px solid var(--border)',
                    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
                  }}>
                    <div>
                      <div style={{ fontWeight: 600, fontSize: '0.875rem' }}>{s.name}</div>
                      <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{s.subjectCode}</div>
                    </div>
                    <Badge text={`${s.enrolledStudents} students`} color={s.enrolledStudents > 0 ? 'mint' : 'muted'} />
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {/* Overview */}
        {tab === 'overview' && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 12, animation: 'slideUp 0.35s both' }}>
            {subjects.filter(s => s.active).map(s => (
              <div key={s.id} style={{
                background: 'var(--bg-card)', borderRadius: 'var(--radius-md)',
                border: '1px solid var(--border)', padding: '18px 22px',
                display: 'flex', alignItems: 'center', gap: 16, flexWrap: 'wrap',
              }}>
                <div style={{
                  width: 44, height: 44, borderRadius: 10,
                  background: 'rgba(240,165,0,0.1)', border: '1px solid rgba(240,165,0,0.3)',
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  fontFamily: 'var(--font-mono)', fontSize: '0.72rem', color: 'var(--gold)', fontWeight: 600,
                  flexShrink: 0,
                }}>{s.subjectCode}</div>
                <div style={{ flex: 1, minWidth: 150 }}>
                  <div style={{ fontWeight: 700, fontSize: '0.95rem' }}>{s.name}</div>
                  {s.semester && <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{s.semester} · {s.academicYear}</div>}
                </div>
                <Badge text={s.facultyName || 'No Faculty'} color={s.facultyName ? 'sky' : 'rose'} />
                <Badge text={`${s.enrolledStudents} enrolled`} color={s.enrolledStudents > 0 ? 'mint' : 'muted'} />
              </div>
            ))}
          </div>
        )}
      </div>
    </AdminLayout>
  )
}

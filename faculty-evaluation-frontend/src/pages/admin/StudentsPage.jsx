import { useState, useEffect } from 'react'
import toast from 'react-hot-toast'
import AdminLayout from './AdminLayout.jsx'
import { getStudents, createStudent, updateStudent, deleteStudent } from '../../api/axios.js'
import { Modal, FormField, Input, Button, DataTable, Badge, PageHeader, LoadingPage } from '../../components/UI.jsx'

const empty = { username:'', password:'', fullName:'', email:'', batch:'', program:'', department:'', phoneNumber:'' }

export default function StudentsPage() {
  const [students, setStudents] = useState([])
  const [loading, setLoading]   = useState(true)
  const [modal, setModal]       = useState(false)
  const [editing, setEditing]   = useState(null)
  const [form, setForm]         = useState(empty)
  const [saving, setSaving]     = useState(false)

  const load = () => getStudents().then(r => setStudents(r.data.data)).finally(() => setLoading(false))
  useEffect(() => { load() }, [])

  const openCreate = () => { setEditing(null); setForm(empty); setModal(true) }
  const openEdit   = s => {
    setEditing(s)
    setForm({ ...empty, fullName: s.fullName, email: s.email, batch: s.batch || '', program: s.program || '', department: s.department || '', phoneNumber: s.phoneNumber || '' })
    setModal(true)
  }
  const close = () => setModal(false)
  const f = k => e => setForm(p => ({ ...p, [k]: e.target.value }))

  const handleSave = async e => {
    e.preventDefault()
    if (!editing && (!form.username || !form.password)) { toast.error('Username and password required'); return }
    if (!form.fullName) { toast.error('Full name is required'); return }
    setSaving(true)
    try {
      if (editing) { await updateStudent(editing.id, form); toast.success('Student updated') }
      else { await createStudent(form); toast.success('Student created') }
      close(); load()
    } catch (err) { toast.error(err.response?.data?.message || 'Error') }
    finally { setSaving(false) }
  }

  const handleDelete = async id => {
    if (!confirm('Deactivate this student?')) return
    try { await deleteStudent(id); toast.success('Student deactivated'); load() }
    catch (err) { toast.error(err.response?.data?.message || 'Error') }
  }

  const columns = [
    { key: 'fullName', label: 'Student', render: (v, r) => (
      <div>
        <div style={{ fontWeight: 600 }}>{v}</div>
        <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>@{r.username}</div>
      </div>
    )},
    { key: 'rollNumber', label: 'Roll No', render: v => v ? <span style={{ fontFamily: 'var(--font-mono)', fontSize: '0.82rem', color: 'var(--mint)' }}>{v}</span> : '—' },
    { key: 'department', label: 'Dept', render: v => v ? <Badge text={v} color="sky" /> : '—' },
    { key: 'program', label: 'Program', render: v => v || '—' },
    { key: 'batch', label: 'Batch', render: v => v ? <Badge text={v} color="gold" /> : '—' },
    { key: 'email', label: 'Email', render: v => <span style={{ color: 'var(--text-secondary)', fontSize: '0.82rem' }}>{v}</span> },
    { key: 'enrolledSubjects', label: 'Enrolled', render: v => <Badge text={`${v} subjects`} color={v > 0 ? 'sky' : 'muted'} /> },
    { key: 'active', label: 'Status', render: v => <Badge text={v ? 'Active' : 'Inactive'} color={v ? 'mint' : 'rose'} /> },
    { key: '_a', label: 'Actions', render: (_, r) => (
      <div style={{ display: 'flex', gap: 8 }}>
        <Button id={`edit-student-${r.username}`} variant="secondary" style={{ padding: '5px 12px', fontSize: '0.78rem' }} onClick={() => openEdit(r)}>Edit</Button>
        <Button id={`remove-student-${r.username}`} variant="danger" style={{ padding: '5px 12px', fontSize: '0.78rem' }} onClick={() => handleDelete(r.id)}>Remove</Button>
      </div>
    )},
  ]

  return (
    <AdminLayout>
      <div className="page-enter">
        <PageHeader title="Students" subtitle={`${students.length} registered students`}
          action={<Button id="add-student-btn" onClick={openCreate}>+ Add Student</Button>} />
        {loading ? <LoadingPage /> : (
          <DataTable columns={columns} rows={students} emptyText="No students yet." />
        )}
      </div>

      <Modal open={modal} onClose={close} title={editing ? 'Edit Student' : 'Add New Student'} width={560}>
        <form onSubmit={handleSave}>
          {!editing && (
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0 16px' }}>
              <FormField label="Username *"><Input id="student-username" placeholder="student_alice" value={form.username} onChange={f('username')} /></FormField>
              <FormField label="Password *"><Input id="student-password" type="password" placeholder="Set password" value={form.password} onChange={f('password')} /></FormField>
            </div>
          )}
          {editing && (
            <FormField label="New Password (leave blank to keep)">
              <Input id="student-password-edit" type="password" placeholder="Enter new password" value={form.password} onChange={f('password')} />
            </FormField>
          )}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0 16px' }}>
            <FormField label="Full Name *"><Input id="student-name" placeholder="Alice Johnson" value={form.fullName} onChange={f('fullName')} /></FormField>
            <FormField label="Email"><Input id="student-email" type="email" placeholder="alice@uni.edu" value={form.email} onChange={f('email')} /></FormField>
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0 16px' }}>
            <FormField label="Batch"><Input id="student-batch" placeholder="2024" value={form.batch} onChange={f('batch')} /></FormField>
            <FormField label="Program"><Input id="student-program" placeholder="B.Tech CSE" value={form.program} onChange={f('program')} /></FormField>
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0 16px' }}>
            <FormField label="Department"><Input id="student-dept" placeholder="Computer Science" value={form.department} onChange={f('department')} /></FormField>
            <FormField label="Phone"><Input id="student-phone" placeholder="+91 98765 43210" value={form.phoneNumber} onChange={f('phoneNumber')} /></FormField>
          </div>
          <div style={{ display: 'flex', gap: 12, justifyContent: 'flex-end', marginTop: 8 }}>
            <Button id="cancel-student-btn" variant="secondary" type="button" onClick={close}>Cancel</Button>
            <Button id="save-student-btn" type="submit" loading={saving}>{editing ? 'Update' : 'Create'} Student</Button>
          </div>
        </form>
      </Modal>
    </AdminLayout>
  )
}

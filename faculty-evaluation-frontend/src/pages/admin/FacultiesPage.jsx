import { useState, useEffect } from 'react'
import toast from 'react-hot-toast'
import AdminLayout from './AdminLayout.jsx'
import { getFaculties, createFaculty, updateFaculty, deleteFaculty } from '../../api/axios.js'
import { Modal, FormField, Input, Button, DataTable, Badge, PageHeader, LoadingPage, StarDisplay } from '../../components/UI.jsx'

const empty = { username:'', password:'', fullName:'', email:'', department:'', designation:'', phoneNumber:'' }

export default function FacultiesPage() {
  const [faculties, setFaculties] = useState([])
  const [loading, setLoading]     = useState(true)
  const [modal, setModal]         = useState(false)
  const [editing, setEditing]     = useState(null)
  const [form, setForm]           = useState(empty)
  const [saving, setSaving]       = useState(false)

  const load = () => getFaculties().then(r => setFaculties(r.data.data)).finally(() => setLoading(false))
  useEffect(() => { load() }, [])

  const openCreate = () => { setEditing(null); setForm(empty); setModal(true) }
  const openEdit   = s => { setEditing(s); setForm({ ...empty, fullName: s.fullName, email: s.email, department: s.department, designation: s.designation || '', phoneNumber: s.phoneNumber || '' }); setModal(true) }
  const close      = () => setModal(false)
  const f = k => e => setForm(p => ({ ...p, [k]: e.target.value }))

  const handleSave = async e => {
    e.preventDefault()
    if (!editing && (!form.username || !form.password)) { toast.error('Username and password are required'); return }
    if (!form.fullName || !form.department) { toast.error('Full name and department are required'); return }
    setSaving(true)
    try {
      if (editing) { await updateFaculty(editing.id, form); toast.success('Faculty updated') }
      else { await createFaculty(form); toast.success('Faculty created') }
      close(); load()
    } catch (err) { toast.error(err.response?.data?.message || 'Error') }
    finally { setSaving(false) }
  }

  const handleDelete = async id => {
    if (!confirm('Deactivate this faculty member?')) return
    try { await deleteFaculty(id); toast.success('Faculty deactivated'); load() }
    catch (err) { toast.error(err.response?.data?.message || 'Error') }
  }

  const columns = [
    { key: 'fullName', label: 'Name', render: (v, r) => (
      <div>
        <div style={{ fontWeight: 600 }}>{v}</div>
        <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>@{r.username}</div>
      </div>
    )},
    { key: 'department', label: 'Department', render: v => <Badge text={v} color="sky" /> },
    { key: 'designation', label: 'Designation', render: v => v || '—' },
    { key: 'employeeId', label: 'Emp ID', render: v => v ? <span style={{ fontFamily: 'var(--font-mono)', fontSize: '0.8rem' }}>{v}</span> : '—' },
    { key: 'averageRating', label: 'Avg Rating', render: v => v ? (
      <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
        <StarDisplay rating={v} size={13} />
        <span style={{ color: 'var(--gold)', fontSize: '0.82rem', fontWeight: 600 }}>{v.toFixed(1)}</span>
      </div>
    ) : <span style={{ color: 'var(--text-muted)' }}>No ratings</span> },
    { key: 'totalEvaluations', label: 'Reviews', render: v => <Badge text={v} color="violet" /> },
    { key: 'active', label: 'Status', render: v => <Badge text={v ? 'Active' : 'Inactive'} color={v ? 'mint' : 'rose'} /> },
    { key: '_a', label: 'Actions', render: (_, r) => (
      <div style={{ display: 'flex', gap: 8 }}>
        <Button id={`edit-faculty-${r.username}`} variant="secondary" style={{ padding: '5px 12px', fontSize: '0.78rem' }} onClick={() => openEdit(r)}>Edit</Button>
        <Button id={`delete-faculty-${r.username}`} variant="danger" style={{ padding: '5px 12px', fontSize: '0.78rem' }} onClick={() => handleDelete(r.id)}>Delete</Button>
      </div>
    )},
  ]

  return (
    <AdminLayout>
      <div className="page-enter">
        <PageHeader title="Faculties" subtitle={`${faculties.length} faculty members`}
          action={<Button id="add-faculty-btn" onClick={openCreate}>+ Add Faculty</Button>} />
        {loading ? <LoadingPage /> : (
          <DataTable columns={columns} rows={faculties} emptyText="No faculty members yet." />
        )}
      </div>

      <Modal open={modal} onClose={close} title={editing ? 'Edit Faculty' : 'Add New Faculty'} width={580}>
        <form onSubmit={handleSave}>
          {!editing && (
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0 16px' }}>
              <FormField label="Username *"><Input id="faculty-username" placeholder="dr.johnson" value={form.username} onChange={f('username')} /></FormField>
              <FormField label="Password *"><Input id="faculty-password" type="password" placeholder="Set password" value={form.password} onChange={f('password')} /></FormField>
            </div>
          )}
          {editing && (
            <FormField label="New Password (leave blank to keep)">
              <Input id="faculty-password-edit" type="password" placeholder="Enter new password" value={form.password} onChange={f('password')} />
            </FormField>
          )}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0 16px' }}>
            <FormField label="Full Name *"><Input id="faculty-name" placeholder="Dr. Jane Johnson" value={form.fullName} onChange={f('fullName')} /></FormField>
            <FormField label="Email *"><Input id="faculty-email" type="email" placeholder="j.johnson@uni.edu" value={form.email} onChange={f('email')} /></FormField>
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0 16px' }}>
            <FormField label="Department *"><Input id="faculty-dept" placeholder="Computer Science" value={form.department} onChange={f('department')} /></FormField>
            <FormField label="Designation"><Input id="faculty-designation" placeholder="Associate Professor" value={form.designation} onChange={f('designation')} /></FormField>
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0 16px' }}>
            <FormField label="Phone"><Input id="faculty-phone" placeholder="+91 98765 43210" value={form.phoneNumber} onChange={f('phoneNumber')} /></FormField>
          </div>
          <div style={{ display: 'flex', gap: 12, justifyContent: 'flex-end', marginTop: 8 }}>
            <Button id="cancel-faculty-btn" variant="secondary" type="button" onClick={close}>Cancel</Button>
            <Button id="save-faculty-btn" type="submit" loading={saving}>{editing ? 'Update' : 'Create'} Faculty</Button>
          </div>
        </form>
      </Modal>
    </AdminLayout>
  )
}

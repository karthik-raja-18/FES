import { useState, useEffect } from 'react'
import toast from 'react-hot-toast'
import AdminLayout from './AdminLayout.jsx'
import { getSubjects, createSubject, updateSubject, deleteSubject } from '../../api/axios.js'
import { Modal, FormField, Input, Textarea, Button, DataTable, Badge, PageHeader, LoadingPage } from '../../components/UI.jsx'

const empty = { name: '', subjectCode: '', description: '', semester: '', academicYear: '' }

export default function SubjectsPage() {
  const [subjects, setSubjects]   = useState([])
  const [loading, setLoading]     = useState(true)
  const [modal, setModal]         = useState(false)
  const [editing, setEditing]     = useState(null)
  const [form, setForm]           = useState(empty)
  const [saving, setSaving]       = useState(false)

  const load = () => getSubjects().then(r => setSubjects(r.data.data)).finally(() => setLoading(false))
  useEffect(() => { load() }, [])

  const openCreate = () => { setEditing(null); setForm(empty); setModal(true) }
  const openEdit   = (s) => { setEditing(s); setForm({ name: s.name, subjectCode: s.subjectCode, description: s.description || '', semester: s.semester || '', academicYear: s.academicYear || '' }); setModal(true) }
  const close      = () => setModal(false)

  const handleSave = async e => {
    e.preventDefault()
    if (!form.name || !form.subjectCode) { toast.error('Name and code are required'); return }
    setSaving(true)
    try {
      if (editing) {
        await updateSubject(editing.id, form)
        toast.success('Subject updated')
      } else {
        await createSubject(form)
        toast.success('Subject created')
      }
      close(); load()
    } catch (err) {
      toast.error(err.response?.data?.message || 'Error saving subject')
    } finally { setSaving(false) }
  }

  const handleDelete = async (id) => {
    if (!confirm('Deactivate this subject?')) return
    try { await deleteSubject(id); toast.success('Subject deactivated'); load() }
    catch (err) { toast.error(err.response?.data?.message || 'Error') }
  }

  const f = k => e => setForm(p => ({ ...p, [k]: e.target.value }))

  const columns = [
    { key: 'subjectCode', label: 'Code', render: v => <span style={{ fontFamily: 'var(--font-mono)', fontSize: '0.82rem', color: 'var(--gold)' }}>{v}</span> },
    { key: 'name', label: 'Subject Name' },
    { key: 'semester', label: 'Semester', render: v => v || '—' },
    { key: 'academicYear', label: 'Year', render: v => v || '—' },
    { key: 'facultyName', label: 'Assigned Faculty', render: v => v ? <span style={{ color: 'var(--sky)' }}>{v}</span> : <span style={{ color: 'var(--text-muted)' }}>Unassigned</span> },
    { key: 'enrolledStudents', label: 'Students', render: v => <Badge text={v} color={v > 0 ? 'mint' : 'muted'} /> },
    { key: 'active', label: 'Status', render: v => <Badge text={v ? 'Active' : 'Inactive'} color={v ? 'mint' : 'rose'} /> },
    { key: '_actions', label: 'Actions', render: (_, row) => (
      <div style={{ display: 'flex', gap: 8 }}>
        <Button id={`edit-subject-${row.subjectCode}`} variant="secondary" style={{ padding: '5px 12px', fontSize: '0.78rem' }} onClick={() => openEdit(row)}>Edit</Button>
        <Button id={`delete-subject-${row.subjectCode}`} variant="danger" style={{ padding: '5px 12px', fontSize: '0.78rem' }} onClick={() => handleDelete(row.id)}>Delete</Button>
      </div>
    )},
  ]

  return (
    <AdminLayout>
      <div className="page-enter">
        <PageHeader
          title="Subjects"
          subtitle={`${subjects.length} subjects in the system`}
          action={<Button id="add-subject-btn" onClick={openCreate}>+ Add Subject</Button>}
        />
        {loading ? <LoadingPage /> : (
          <DataTable columns={columns} rows={subjects} emptyText="No subjects yet. Create one to get started." />
        )}
      </div>

      <Modal open={modal} onClose={close} title={editing ? 'Edit Subject' : 'Add New Subject'}>
        <form onSubmit={handleSave}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0 16px' }}>
            <FormField label="Subject Name *">
              <Input id="subject-name" placeholder="e.g. Data Structures" value={form.name} onChange={f('name')} />
            </FormField>
            <FormField label="Subject Code *">
              <Input id="subject-code" placeholder="e.g. CS301" value={form.subjectCode} onChange={f('subjectCode')} disabled={!!editing} />
            </FormField>
          </div>
          <FormField label="Description">
            <Textarea id="subject-description" placeholder="Brief description of the subject..." value={form.description} onChange={f('description')} />
          </FormField>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0 16px' }}>
            <FormField label="Semester">
              <Input id="subject-semester" placeholder="e.g. Semester 3" value={form.semester} onChange={f('semester')} />
            </FormField>
            <FormField label="Academic Year">
              <Input id="subject-year" placeholder="e.g. 2024-25" value={form.academicYear} onChange={f('academicYear')} />
            </FormField>
          </div>
          <div style={{ display: 'flex', gap: 12, justifyContent: 'flex-end', marginTop: 8 }}>
            <Button id="cancel-subject-btn" variant="secondary" type="button" onClick={close}>Cancel</Button>
            <Button id="save-subject-btn" type="submit" loading={saving}>{editing ? 'Update' : 'Create'} Subject</Button>
          </div>
        </form>
      </Modal>
    </AdminLayout>
  )
}

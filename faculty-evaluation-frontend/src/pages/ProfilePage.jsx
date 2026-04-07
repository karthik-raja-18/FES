import { useState } from 'react'
import { useAuth } from '../context/AuthContext.jsx'
import Sidebar from '../components/Sidebar.jsx'
import { Button, Input, PageHeader, Badge } from '../components/UI.jsx'
import { updateProfile } from '../api/axios.js'
import toast from 'react-hot-toast'

const SIDEBARS = {
  ADMIN: [
    { to: '/admin', end: true, label: 'Dashboard', icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/></svg> },
    { to: '/admin/subjects', label: 'Subjects', icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M4 19.5A2.5 2.5 0 016.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 014 19.5v-15A2.5 2.5 0 016.5 2z"/></svg> },
    { to: '/admin/faculties', label: 'Faculties', icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M22 10v6M2 10l10-5 10 5-10 5z"/><path d="M6 12v5c3 3 9 3 12 0v-5"/></svg> },
    { to: '/admin/students', label: 'Students', icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87M16 3.13a4 4 0 010 7.75"/></svg> },
    { to: '/admin/assignments', label: 'Assignments', icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M17 3a2.828 2.828 0 114 4L7.5 20.5 2 22l1.5-5.5L17 3z"/></svg> },
    { to: '/profile', label: 'My Profile', icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/><circle cx="12" cy="7" r="4"/></svg> },
  ],
  STUDENT: [
    { to: '/student', end: true, label: 'My Subjects', icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M4 19.5A2.5 2.5 0 016.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 014 19.5v-15A2.5 2.5 0 016.5 2z"/></svg> },
    { to: '/profile', label: 'My Profile', icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/><circle cx="12" cy="7" r="4"/></svg> },
  ],
  FACULTY: [
    { to: '/faculty', end: true, label: 'My Dashboard', icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/></svg> },
    { to: '/profile', label: 'My Profile', icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/><circle cx="12" cy="7" r="4"/></svg> },
  ],
}

export default function ProfilePage() {
  const { user, updateUser } = useAuth()
  const [passwords, setPasswords] = useState({ current: '', new: '', confirm: '' })
  const [updatingPass, setUpdatingPass] = useState(false)

  const handlePasswordChange = async e => {
    e.preventDefault()
    if (passwords.new !== passwords.confirm) {
        toast.error('Passwords do not match')
        return
    }
    setUpdatingPass(true)
    try {
      await updateProfile({ password: passwords.new }) // leveraging same endpoint
      toast.success('Password updated successfully')
      setPasswords({ current: '', new: '', confirm: '' })
    } catch (err) {
      toast.error('Could not update password')
    } finally {
        setUpdatingPass(false)
    }
  }

  const getRoleBadgeColor = () => {
    if (user?.role === 'ADMIN') return 'violet'
    if (user?.role === 'FACULTY') return 'sky'
    return 'mint'
  }

  return (
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      <Sidebar navItems={SIDEBARS[user?.role] || []} role={user?.role} />
      <main style={{ marginLeft: 240, flex: 1, padding: '32px 36px' }}>
        <div className="page-enter">
          <PageHeader title="My Profile" subtitle="Manage your personal information and security" />
          
          <div style={{ display: 'grid', gridTemplateColumns: '1.2fr 1fr', gap: 24 }}>
            {/* User Info Card */}
            <div style={{
              background: 'var(--bg-card)', borderRadius: 'var(--radius-lg)',
              border: '1px solid var(--border)', padding: '28px'
            }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 32 }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: 20 }}>
                  <div style={{
                    width: 80, height: 80, borderRadius: '50%',
                    background: 'linear-gradient(135deg, var(--gold) 0%, #D4920A 100%)',
                    display: 'flex', alignItems: 'center', justifyContent: 'center',
                    fontSize: '2rem', fontWeight: 800, color: '#080E1A'
                  }}>{user?.fullName?.[0]}</div>
                  <div>
                    <h2 style={{ fontSize: '1.4rem', color: 'var(--text-primary)', marginBottom: 4 }}>{user?.fullName}</h2>
                    <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
                      <Badge text={user?.role} color={getRoleBadgeColor()} />
                      <span style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>ID: #{user?.userId}</span>
                    </div>
                  </div>
                </div>
              </div>

              <div style={{ display: 'grid', gap: 16 }}>
                <InfoRow label="Username" value={`@${user?.username}`} />
                <InfoRow label="Full Name" value={user?.fullName} />
                <InfoRow label="Department" value={user?.department || 'N/A'} />
                <InfoRow label="Email" value={user?.email || '—'} />
                <InfoRow label="Phone Number" value={user?.phoneNumber || '—'} />
                <InfoRow label="Role" value={user?.role} />
                <InfoRow label="Status" value="Active Account" color="var(--mint)" />
              </div>
            </div>

            {/* Security Card */}
            <div style={{
              background: 'var(--bg-card)', borderRadius: 'var(--radius-lg)',
              border: '1px solid var(--border)', padding: '28px'
            }}>
                <h3 style={{ fontSize: '1.1rem', marginBottom: 20 }}>Security Settings</h3>
                <form onSubmit={handlePasswordChange} style={{ display: 'grid', gap: 20 }}>
                    <div>
                        <label style={{ fontSize: '0.75rem', fontWeight: 700, color: 'var(--text-secondary)', display: 'block', marginBottom: 6 }}>New Password</label>
                        <Input 
                            type="password" 
                            placeholder="Min. 8 characters" 
                            value={passwords.new}
                            onChange={e => setPasswords(p => ({ ...p, new: e.target.value }))}
                        />
                    </div>
                    <div>
                        <label style={{ fontSize: '0.75rem', fontWeight: 700, color: 'var(--text-secondary)', display: 'block', marginBottom: 6 }}>Confirm New Password</label>
                        <Input 
                            type="password" 
                            placeholder="Re-enter password" 
                            value={passwords.confirm}
                            onChange={e => setPasswords(p => ({ ...p, confirm: e.target.value }))}
                        />
                    </div>
                    <Button type="submit" loading={updatingPass} style={{ width: '100%', justifyContent: 'center', marginTop: 8 }}>
                        Update Security Settings
                    </Button>
                </form>
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}

function InfoRow({ label, value, color }) {
    return (
        <div style={{ display: 'flex', justifyContent: 'space-between', padding: '12px 0', borderBottom: '1px solid var(--border)' }}>
            <span style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>{label}</span>
            <span style={{ fontSize: '0.9rem', color: color || 'var(--text-primary)', fontWeight: 600 }}>{value}</span>
        </div>
    )
}

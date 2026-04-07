import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
})

// Attach JWT token to every request
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

// Handle auth errors globally
api.interceptors.response.use(
  res => res,
  err => {
    if (err.response?.status === 401) {
      localStorage.clear()
      window.location.href = '/login'
    }
    return Promise.reject(err)
  }
)

// ---- Auth ----
export const login = (data) => api.post('/auth/login', data)
export const updateProfile = (data) => api.put('/auth/profile', data)

// ---- Admin ----
export const adminDashboard    = ()        => api.get('/admin/dashboard')
export const getSubjects       = ()        => api.get('/admin/subjects')
export const createSubject     = (data)    => api.post('/admin/subjects', data)
export const updateSubject     = (id,data) => api.put(`/admin/subjects/${id}`, data)
export const deleteSubject     = (id)      => api.delete(`/admin/subjects/${id}`)

export const getFaculties      = ()        => api.get('/admin/faculties')
export const createFaculty     = (data)    => api.post('/admin/faculties', data)
export const updateFaculty     = (id,data) => api.put(`/admin/faculties/${id}`, data)
export const deleteFaculty     = (id)      => api.delete(`/admin/faculties/${id}`)

export const getStudents       = ()        => api.get('/admin/students')
export const createStudent     = (data)    => api.post('/admin/students', data)
export const updateStudent     = (id,data) => api.put(`/admin/students/${id}`, data)
export const deleteStudent     = (id)      => api.delete(`/admin/students/${id}`)

export const assignFaculty     = (data)    => api.post('/admin/assign-faculty', data)
export const unassignFaculty   = (data)    => api.post('/admin/unassign-faculty', data)
export const enrollStudent     = (data)    => api.post('/admin/enroll-student', data)
export const unenrollStudent   = (data)    => api.post('/admin/unenroll-student', data)

// ---- Student ----
export const getMySubjects     = ()        => api.get('/student/subjects')
export const submitEvaluation  = (data)    => api.post('/student/evaluate', data)

// ---- Faculty ----
export const getFacultyDashboard = ()      => api.get('/faculty/dashboard')

export default api

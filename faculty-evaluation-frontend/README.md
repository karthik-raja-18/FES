# Faculty Evaluation System — Frontend

React + Vite frontend for the Faculty Evaluation System.

## Tech Stack
- **React 18** + React Router v6
- **Vite** dev server with backend proxy
- **Axios** for API calls with JWT interceptors
- **Recharts** for analytics charts
- **react-hot-toast** for notifications
- Custom design system (CSS variables, no Tailwind/MUI)

## Setup

### Prerequisites
- Node.js 18+
- Backend running on http://localhost:8080

### Install & Run
```bash
npm install
npm run dev
```
App runs on **http://localhost:3000**

## Default Credentials

| Role    | Username    | Password     |
|---------|-------------|--------------|
| Admin   | admin       | admin123     |
| Faculty | dr.smith    | faculty123   |
| Faculty | prof.jones  | faculty123   |
| Student | student1    | student123   |
| Student | student2    | student123   |

## Pages by Role

### Admin (`/admin`)
- **Dashboard** — System-wide stats and overview
- **Subjects** — Create, edit, delete subjects
- **Faculties** — Manage faculty accounts
- **Students** — Manage student accounts  
- **Assignments** — Assign faculty to subjects, enroll students

### Student (`/student`)
- View all enrolled subjects with faculty info
- Submit anonymous star rating + written feedback

### Faculty (`/faculty`)
- Overall average rating and total evaluations
- Rating distribution bar chart
- Per-subject breakdown with ratings
- Anonymous feedback comments from students

## Production Build
```bash
npm run build
```
Update `vite.config.js` proxy target for your production backend URL.

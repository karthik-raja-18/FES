import { Link } from 'react-router-dom'
import { Button } from '../components/UI'
import { useTheme } from '../context/ThemeContext'
import logo from '../assets/logo.png'

export default function LandingPage() {
  const { theme, toggleTheme } = useTheme()
  return (
    <div style={{
      minHeight: '100vh',
      background: 'var(--bg-base)',
      color: 'var(--text-primary)',
      overflowX: 'hidden',
      position: 'relative',
      transition: 'background 0.3s, color 0.3s'
    }}>
      {/* Decorative Background Elements */}
      <div style={{
        position: 'absolute', top: '-10%', left: '-10%', width: '40%', height: '40%',
        background: 'radial-gradient(circle, var(--gold-glow) 0%, transparent 70%)',
        zIndex: 0, pointerEvents: 'none', filter: 'blur(80px)'
      }} />
      <div style={{
        position: 'absolute', bottom: '10%', right: '-5%', width: '30%', height: '30%',
        background: 'radial-gradient(circle, var(--sky-dim) 0%, transparent 70%)',
        zIndex: 0, pointerEvents: 'none', filter: 'blur(100px)'
      }} />

      {/* Header */}
      <header style={{
        position: 'sticky', top: 0, zIndex: 100,
        background: 'var(--bg-surface)',
        opacity: 0.9,
        backdropFilter: 'blur(12px)',
        borderBottom: '1px solid var(--border)',
        padding: '0 5%'
      }}>
        <div style={{
          maxWidth: 1200, margin: '0 auto', height: 80,
          display: 'flex', alignItems: 'center', justifyContent: 'space-between'
        }}>
          <Link to="/" style={{ display: 'flex', alignItems: 'center', gap: 12, textDecoration: 'none' }}>
            <div style={{
              width: 36, height: 36, borderRadius: 8,
              background: 'linear-gradient(135deg, var(--gold) 0%, #D4920A 100%)',
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              boxShadow: 'var(--shadow-glow)', overflow: 'hidden'
            }}>
              <img src={logo} alt="FES Logo" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
            </div>
            <span style={{
              fontFamily: 'var(--font-display)', fontWeight: 800,
              fontSize: '1.4rem', letterSpacing: '-0.02em',
              color: 'var(--text-primary)'
            }}>FacultyEval</span>
          </Link>

          <nav style={{ display: 'flex', alignItems: 'center', gap: 32 }}>
            {['Features', 'About', 'Contact'].map(item => (
              <a key={item} href={`#${item.toLowerCase()}`} style={{
                fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)',
                transition: 'var(--transition)', cursor: 'pointer'
              }}
              onMouseEnter={e => e.currentTarget.style.color = 'var(--text-primary)'}
              onMouseLeave={e => e.currentTarget.style.color = 'var(--text-secondary)'}
              >{item}</a>
            ))}

            {/* Theme Toggle */}
            <button 
              onClick={toggleTheme}
              style={{
                width: 36, height: 36, borderRadius: '50%',
                background: 'var(--bg-elevated)', border: '1px solid var(--border)',
                color: 'var(--text-secondary)', cursor: 'pointer',
                display: 'flex', alignItems: 'center', justifyContent: 'center',
                transition: 'var(--transition)'
              }}
              onMouseEnter={e => e.currentTarget.style.borderColor = 'var(--gold)'}
              onMouseLeave={e => e.currentTarget.style.borderColor = 'var(--border)'}
            >
              {theme === 'dark' ? (
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><circle cx="12" cy="12" r="5"/><path d="M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42"/></svg>
              ) : (
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M21 12.79A9 9 0 1111.21 3 7 7 0 0021 12.79z"/></svg>
              )}
            </button>

            <Link to="/login">
              <Button variant="secondary" style={{ padding: '8px 20px', fontSize: '0.8rem' }}>
                Login
              </Button>
            </Link>
          </nav>
        </div>
      </header>

      {/* Hero Section */}
      <section style={{
        maxWidth: 1200, margin: '0 auto', padding: '120px 5% 100px',
        textAlign: 'center', position: 'relative', zIndex: 1
      }}>
        <div className="page-enter" style={{ maxWidth: 800, margin: '0 auto' }}>
          <div style={{
            display: 'inline-flex', alignItems: 'center', gap: 8,
            padding: '6px 14px', borderRadius: 100,
            background: 'var(--gold-dim)', border: '1px solid var(--gold-dim)',
            color: 'var(--gold)', fontSize: '0.75rem', fontWeight: 700,
            textTransform: 'uppercase', letterSpacing: '0.1em', marginBottom: 24
          }}>
            <span style={{ width: 6, height: 6, borderRadius: '50%', background: 'var(--gold)', animation: 'pulse-ring 2s infinite' }} />
            Academic Excellence Reimagined
          </div>
          
          <h1 style={{
            fontFamily: 'var(--font-display)', fontWeight: 800,
            fontSize: 'clamp(2.5rem, 8vw, 4.5rem)', lineHeight: 1.1,
            marginBottom: 24, letterSpacing: '-0.03em',
            color: 'var(--text-primary)'
          }}>
            Precision Insights for <br /> Higher Education.
          </h1>
          
          <p style={{
            fontSize: '1.15rem', color: 'var(--text-secondary)',
            lineHeight: 1.7, maxWidth: 600, margin: '0 auto 40px',
            animation: 'fadeIn 0.8s ease backwards', animationDelay: '0.3s'
          }}>
            A sophisticated evaluation ecosystem designed to empower faculty, 
            inspire students, and drive institutional growth through data-driven transparency.
          </p>


          <div style={{
            display: 'flex', gap: 16, justifyContent: 'center',
            animation: 'slideUp 0.6s ease backwards', animationDelay: '0.5s'
          }}>
            <Link to="/login">
              <Button style={{ padding: '14px 32px', fontSize: '1rem', borderRadius: 'var(--radius-md)' }}>
                Get Started
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
                  <path d="M5 12h14M12 5l7 7-7 7" />
                </svg>
              </Button>
            </Link>
            <Button variant="secondary" style={{ padding: '14px 32px', fontSize: '1rem', borderRadius: 'var(--radius-md)' }}>
              View Demo
            </Button>
          </div>
        </div>

        {/* Hero Visual Mockup Removed */}
      </section>

      {/* System Preview Mockup */}
      <section style={{ maxWidth: 1200, margin: '-40px auto 100px', padding: '0 5%', position: 'relative', zIndex: 1 }}>
        <div style={{
           background: 'var(--bg-card)', borderRadius: 'var(--radius-xl)',
           border: '1px solid var(--border-bright)', padding: '12px',
           boxShadow: theme === 'dark' ? '0 40px 100px rgba(0,0,0,0.6), var(--shadow-glow)' : '0 20px 60px rgba(0,0,0,0.1)',
           animation: 'fadeIn 1s ease 0.6s both', transform: 'perspective(1000px) rotateX(2deg)'
        }}>
           <div style={{ background: 'var(--bg-base)', borderRadius: 'var(--radius-lg)', overflow: 'hidden', border: '1px solid var(--border)' }}>
              {/* Fake Toolbar */}
              <div style={{ height: 48, background: 'var(--bg-surface)', borderBottom: '1px solid var(--border)', display: 'flex', alignItems: 'center', padding: '0 20px', gap: 8 }}>
                <div style={{ width: 10, height: 10, borderRadius: '50%', background: '#ff5f56' }} />
                <div style={{ width: 10, height: 10, borderRadius: '50%', background: '#ffbd2e' }} />
                <div style={{ width: 10, height: 10, borderRadius: '50%', background: '#27c93f' }} />
                <div style={{ flex: 1, height: 24, background: 'var(--bg-base)', borderRadius: 4, margin: '0 40px', opacity: 0.5 }} />
              </div>
              {/* Fake Dashboard Content */}
              <div style={{ padding: 40, display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 24 }}>
                <div style={{ height: 140, background: 'var(--bg-elevated)', borderRadius: 12, border: '1px solid var(--border)', position: 'relative', overflow: 'hidden' }}>
                  <div style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: 4, background: 'var(--gold)' }} />
                </div>
                <div style={{ height: 140, background: 'var(--bg-elevated)', borderRadius: 12, border: '1px solid var(--border)', position: 'relative', overflow: 'hidden' }}>
                   <div style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: 4, background: 'var(--sky)' }} />
                </div>
                <div style={{ height: 140, background: 'var(--bg-elevated)', borderRadius: 12, border: '1px solid var(--border)', position: 'relative', overflow: 'hidden' }}>
                   <div style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: 4, background: 'var(--mint)' }} />
                </div>
                <div style={{ gridColumn: 'span 2', height: 240, background: 'var(--bg-elevated)', borderRadius: 12, border: '1px solid var(--border)' }} />
                <div style={{ height: 240, background: 'var(--bg-elevated)', borderRadius: 12, border: '1px solid var(--border)' }} />
              </div>
           </div>
        </div>
      </section>

      {/* Features Section */}
      <section id="features" style={{
        maxWidth: 1200, margin: '0 auto', padding: '60px 5% 100px',
        position: 'relative', zIndex: 1
      }}>
        <div style={{ textAlign: 'center', marginBottom: 60 }}>
          <h2 style={{ fontFamily: 'var(--font-display)', fontSize: '2.8rem', fontWeight: 800, marginBottom: 16, letterSpacing: '-0.02em' }}>
            Unrivaled Intelligence.
          </h2>
          <p style={{ color: 'var(--text-secondary)', maxWidth: 600, margin: '0 auto', fontSize: '1.1rem' }}>
            The evaluation ecosystem was built with modern standards to provide the most reliable experience.
          </p>
        </div>

        <div style={{
          display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: 32
        }}>
          <FeatureCard 
            icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M22 12h-4l-3 9L9 3l-3 9H2" /></svg>}
            title="Real-time Analytics"
            desc="Instant visualization of faculty performance across multiple domains with live tracking."
            color="var(--gold)"
          />
          <FeatureCard 
            icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" /></svg>}
            title="Encrypted Anonymity"
            desc="Proprietary identity masking ensuring student feedback remains 100% confidential."
            color="var(--sky)"
          />
          <FeatureCard 
            icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z" /></svg>}
            title="AI Performance Insights"
            desc="Smart reporting that identifies pedagogical strengths and key improvement vectors."
            color="var(--mint)"
          />
        </div>

        {/* Workflow Steps */}
        <div style={{ marginTop: 100, display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 40, position: 'relative' }}>
          {/* Connector Line */}
          <div style={{ position: 'absolute', top: 40, left: '10%', right: '10%', height: 2, background: 'linear-gradient(to right, transparent, var(--border), transparent)', zIndex: 0 }} />
          
          {[
            { step: '01', title: 'Enrollment', desc: 'Admins assign faculty to subjects and enroll students.' },
            { step: '02', title: 'Evaluation', desc: 'Students provide anonymous, honest feedback via mobile or web.' },
            { step: '03', title: 'Improvement', desc: 'Faculty receive structured insights to refine teaching quality.' },
          ].map((item, i) => (
            <div key={item.step} style={{ textAlign: 'center', position: 'relative', zIndex: 1 }}>
               <div style={{ 
                 width: 80, height: 80, borderRadius: '50%', background: 'var(--bg-card)', 
                 border: '1px solid var(--border)', margin: '0 auto 24px',
                 display: 'flex', alignItems: 'center', justifyContent: 'center',
                 fontSize: '1.2rem', fontWeight: 800, color: i === 1 ? 'var(--sky)' : 'var(--gold)',
                 boxShadow: i === 1 ? (theme === 'dark' ? '0 0 30px rgba(56,189,248,0.1)' : '0 0 20px rgba(56,189,248,0.05)') : 'none'
               }}>{item.step}</div>
               <h4 style={{ marginBottom: 12, fontWeight: 700 }}>{item.title}</h4>
               <p style={{ color: 'var(--text-muted)', fontSize: '0.875rem', lineHeight: 1.6, maxWidth: 240, margin: '0 auto' }}>{item.desc}</p>
            </div>
          ))}
        </div>
      </section>

      {/* About Section */}
      <section id="about" style={{
        maxWidth: 1200, margin: '0 auto', padding: '100px 5%',
        position: 'relative', zIndex: 1, borderTop: '1px solid var(--border)'
      }}>
        <div style={{ display: 'grid', gridTemplateColumns: 'minmax(300px, 1fr) minmax(300px, 1fr)', gap: 60, alignItems: 'center', flexWrap: 'wrap' }}>
          <div>
            <h2 style={{ fontFamily: 'var(--font-display)', fontSize: '2.4rem', fontWeight: 800, marginBottom: 24 }}>
              Designed for <br /><span style={{ color: 'var(--gold)' }}>Institutional Growth.</span>
            </h2>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 20 }}>
              <p style={{ color: 'var(--text-secondary)', fontSize: '1.05rem', lineHeight: 1.7 }}>
                The Faculty Evaluation System (FES) was born out of a need for transparency and constructive feedback in higher education. Our platform bridge the gap between student expectations and faculty performance.
              </p>
              <p style={{ color: 'var(--text-secondary)', fontSize: '1.05rem', lineHeight: 1.7 }}>
                By leveraging data-driven insights, we help institutions maintain the highest standards of academic excellence while fostering a culture of continuous improvement.
              </p>
              <div style={{ display: 'flex', gap: 40, marginTop: 10 }}>
                <div>
                  <div style={{ fontSize: '1.5rem', fontWeight: 800, color: 'var(--text-primary)' }}>98%</div>
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', textTransform: 'uppercase' }}>User Satisfaction</div>
                </div>
                <div>
                  <div style={{ fontSize: '1.5rem', fontWeight: 800, color: 'var(--text-primary)' }}>50+</div>
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', textTransform: 'uppercase' }}>Departments</div>
                </div>
              </div>
            </div>
          </div>
          <div style={{
            background: 'var(--bg-surface)', borderRadius: 'var(--radius-xl)',
            padding: 40, border: '1px solid var(--border-bright)', position: 'relative'
          }}>
             <div style={{ position: 'absolute', top: -20, right: -20, padding: '10px 20px', background: 'var(--gold)', color: 'white', fontWeight: 700, borderRadius: 8, transform: 'rotate(5deg)' }}>
               Mission First
             </div>
             <p style={{ fontStyle: 'italic', fontSize: '1.1rem', color: 'var(--text-primary)', marginBottom: 20 }}>
               "FES has completely transformed how we handle feedback. The anonymity ensures honest responses, while the analytics give us a clear path forward."
             </p>
             <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                <div style={{ width: 40, height: 40, borderRadius: '50%', background: 'var(--bg-elevated)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--gold)', fontWeight: 800 }}>SJ</div>
                <div>
                  <div style={{ fontWeight: 700, fontSize: '0.9rem' }}>Dr. Sarah Jenkins</div>
                  <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Dean of Academic Affairs</div>
                </div>
             </div>
          </div>
        </div>
      </section>

      {/* Contact Section */}
      <section id="contact" style={{
        maxWidth: 1200, margin: '0 auto', padding: '100px 5%',
        position: 'relative', zIndex: 1, borderTop: '1px solid var(--border)'
      }}>
        <div style={{ textAlign: 'center', marginBottom: 60 }}>
          <h2 style={{ fontFamily: 'var(--font-display)', fontSize: '2.4rem', fontWeight: 800, marginBottom: 16 }}>
            Connect With Us.
          </h2>
          <p style={{ color: 'var(--text-secondary)', maxWidth: 600, margin: '0 auto' }}>
            Ready to implement FES in your institution? Or have some questions? We're here to help.
          </p>
        </div>

        <div style={{
          display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: 32
        }}>
          <div style={{ background: 'var(--bg-card)', padding: 32, borderRadius: 'var(--radius-lg)', border: '1px solid var(--border)' }}>
            <div style={{ color: 'var(--sky)', marginBottom: 16 }}>
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z" /><polyline points="22,6 12,13 2,6" /></svg>
            </div>
            <h4 style={{ marginBottom: 8, color: 'var(--text-primary)' }}>Email Support</h4>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>support@facultyeval.edu</p>
          </div>
          <div style={{ background: 'var(--bg-card)', padding: 32, borderRadius: 'var(--radius-lg)', border: '1px solid var(--border)' }}>
            <div style={{ color: 'var(--mint)', marginBottom: 16 }}>
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z" /></svg>
            </div>
            <h4 style={{ marginBottom: 8, color: 'var(--text-primary)' }}>Direct Line</h4>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>+1 (555) 000-1234</p>
          </div>
          <div style={{ background: 'var(--bg-card)', padding: 32, borderRadius: 'var(--radius-lg)', border: '1px solid var(--border)' }}>
            <div style={{ color: 'var(--violet)', marginBottom: 16 }}>
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" /><circle cx="12" cy="10" r="3" /></svg>
            </div>
            <h4 style={{ marginBottom: 8, color: 'var(--text-primary)' }}>Mailing Address</h4>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>123 Academic Way, Tech City</p>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer style={{
        borderTop: '1px solid var(--border)', padding: '60px 5% 40px', background: 'var(--bg-surface)'
      }}>
        <div style={{ maxWidth: 1200, margin: '0 auto', display: 'flex', flexWrap: 'wrap', justifyContent: 'space-between', gap: 40 }}>
          <div style={{ flex: '1 1 300px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginBottom: 20 }}>
              <div style={{ width: 30, height: 30, borderRadius: 6, background: 'var(--gold)', fontWeight: 800, color: 'white', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '1rem' }}>F</div>
              <span style={{ fontFamily: 'var(--font-display)', fontWeight: 800, fontSize: '1.1rem' }}>FacultyEval</span>
            </div>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', lineHeight: 1.6, maxWidth: 300 }}>
              The gold standard in academic evaluation systems. Built for the next generation of educational leadership.
            </p>
          </div>
          
          <div style={{ display: 'flex', gap: 80 }}>
            <div>
              <h4 style={{ fontSize: '0.85rem', color: 'var(--text-primary)', marginBottom: 20, textTransform: 'uppercase', letterSpacing: '0.1em' }}>Product</h4>
              <ul style={{ listStyle: 'none', display: 'flex', flexDirection: 'column', gap: 12, fontSize: '0.85rem', color: 'var(--text-muted)' }}>
                <li>Features</li>
                <li>Security</li>
                <li>Enterprise</li>
              </ul>
            </div>
            <div>
              <h4 style={{ fontSize: '0.85rem', color: 'var(--text-primary)', marginBottom: 20, textTransform: 'uppercase', letterSpacing: '0.1em' }}>Institution</h4>
              <ul style={{ listStyle: 'none', display: 'flex', flexDirection: 'column', gap: 12, fontSize: '0.85rem', color: 'var(--text-muted)' }}>
                <li>About Us</li>
                <li>Documentation</li>
                <li>Contact</li>
              </ul>
            </div>
          </div>
        </div>
        <div style={{ maxWidth: 1200, margin: '40px auto 0', paddingTop: 30, borderTop: '1px solid var(--border)', textAlign: 'center', color: 'var(--text-muted)', fontSize: '0.8rem' }}>
          © {new Date().getFullYear()} Faculty Evaluation System. All rights reserved.
        </div>
      </footer>
    </div>
  )
}

function FeatureCard({ icon, title, desc, color }) {
  // Use theme-aware color mapping
  const dimColor = color === 'var(--gold)' ? 'var(--gold-dim)' : 
                   color === 'var(--sky)' ? 'var(--sky-dim)' : 
                   color === 'var(--mint)' ? 'var(--mint-dim)' : `${color}15`;
  
  return (
    <div style={{
      background: 'var(--bg-card)', padding: '40px 32px', borderRadius: 'var(--radius-xl)',
      border: '1px solid var(--border)', transition: 'var(--transition)',
      position: 'relative', overflow: 'hidden'
    }}
    onMouseEnter={e => {
      e.currentTarget.style.borderColor = 'var(--border-bright)'
      e.currentTarget.style.transform = 'translateY(-4px)'
    }}
    onMouseLeave={e => {
      e.currentTarget.style.borderColor = 'var(--border)'
      e.currentTarget.style.transform = 'translateY(0)'
    }}
    >
      <div style={{
        width: 48, height: 48, borderRadius: 12, background: dimColor,
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        color: color, marginBottom: 24, border: `1px solid ${dimColor}`
      }}>
        {icon}
      </div>
      <h3 style={{ fontFamily: 'var(--font-display)', fontSize: '1.25rem', fontWeight: 700, marginBottom: 12 }}>
        {title}
      </h3>
      <p style={{ color: 'var(--text-secondary)', fontSize: '0.95rem', lineHeight: 1.6 }}>
        {desc}
      </p>
    </div>
  )
}


import { useState } from 'react'

const labels = ['', 'Poor', 'Fair', 'Good', 'Very Good', 'Excellent']

export default function StarRating({ value, onChange, size = 40 }) {
  const [hovered, setHovered] = useState(0)
  const display = hovered || value

  return (
    <div>
      <div style={{ display: 'flex', gap: 8, marginBottom: 10 }}>
        {[1,2,3,4,5].map(star => (
          <button
            key={star}
            id={`star-${star}`}
            type="button"
            onClick={() => onChange(star)}
            onMouseEnter={() => setHovered(star)}
            onMouseLeave={() => setHovered(0)}
            style={{
              background: 'none', border: 'none', padding: 4,
              cursor: 'pointer', transform: display >= star ? 'scale(1.1)' : 'scale(1)',
              transition: 'transform 0.15s ease',
            }}
          >
            <svg width={size} height={size} viewBox="0 0 24 24"
              fill={display >= star ? 'var(--gold)' : 'none'}
              stroke={display >= star ? 'var(--gold)' : 'var(--border-bright)'}
              strokeWidth="1.5"
              style={{ filter: display >= star ? 'drop-shadow(0 0 8px rgba(240,165,0,0.5))' : 'none', transition: 'all 0.15s ease' }}
            >
              <polygon points="12,2 15.09,8.26 22,9.27 17,14.14 18.18,21.02 12,17.77 5.82,21.02 7,14.14 2,9.27 8.91,8.26" />
            </svg>
          </button>
        ))}
      </div>
      <div style={{
        height: 24, fontSize: '0.95rem', fontWeight: 600,
        color: display ? 'var(--gold)' : 'var(--text-muted)',
        letterSpacing: '0.02em', transition: 'color 0.15s',
      }}>
        {display ? labels[display] : 'Select a rating'}
      </div>
    </div>
  )
}

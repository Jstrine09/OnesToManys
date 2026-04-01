const ENDPOINTS = [
  {
    group: '🌍 Leagues',
    items: [
      { method: 'GET',    url: '/api/leagues' },
      { method: 'GET',    url: '/api/leagues/{id}' },
      { method: 'POST',   url: '/api/leagues' },
      { method: 'PUT',    url: '/api/leagues/{id}' },
      { method: 'DELETE', url: '/api/leagues/{id}' },
    ]
  },
  {
    group: '🏟️ Clubs',
    items: [
      { method: 'GET',    url: '/api/leagues/{leagueId}/clubs' },
      { method: 'GET',    url: '/api/leagues/{leagueId}/clubs/{id}' },
      { method: 'POST',   url: '/api/leagues/{leagueId}/clubs' },
      { method: 'PUT',    url: '/api/leagues/{leagueId}/clubs/{id}' },
      { method: 'DELETE', url: '/api/leagues/{leagueId}/clubs/{id}' },
    ]
  },
  {
    group: '👟 Players',
    items: [
      { method: 'GET',    url: '/api/leagues/{leagueId}/clubs/{clubId}/players' },
      { method: 'GET',    url: '/api/leagues/{leagueId}/clubs/{clubId}/players/{id}' },
      { method: 'POST',   url: '/api/leagues/{leagueId}/clubs/{clubId}/players' },
      { method: 'PUT',    url: '/api/leagues/{leagueId}/clubs/{clubId}/players/{id}' },
      { method: 'DELETE', url: '/api/leagues/{leagueId}/clubs/{clubId}/players/{id}' },
    ]
  },
  {
    group: '💾 Data Export / Import',
    items: [
      { method: 'GET',  url: '/api/export/json' },
      { method: 'GET',  url: '/api/export/sql' },
      { method: 'POST', url: '/api/import/json' },
    ]
  }
]

function formatUrl(url) {
  const parts = url.split(/(\{[^}]+\})/)
  return parts.map((part, i) =>
    part.startsWith('{')
      ? <span key={i} className="param">{part}</span>
      : <span key={i}>{part}</span>
  )
}

function ApiDrawer({ open, onClose }) {
  return (
    <>
      <div className={`drawer-overlay ${open ? 'open' : ''}`} onClick={onClose} />
      <div className={`drawer ${open ? 'open' : ''}`}>
        <div className="drawer-header">
          <h3>⚡ API Endpoints</h3>
          <button className="drawer-close" onClick={onClose}>✕</button>
        </div>
        <div className="drawer-body">
          {ENDPOINTS.map(group => (
            <div key={group.group} className="endpoint-group">
              <h4>{group.group}</h4>
              {group.items.map((item, i) => (
                <div key={i} className="endpoint-row">
                  <span className={`method ${item.method}`}>{item.method}</span>
                  <span className="endpoint-url">{formatUrl(item.url)}</span>
                </div>
              ))}
            </div>
          ))}
        </div>
      </div>
    </>
  )
}

export default ApiDrawer

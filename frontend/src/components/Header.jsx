function Header({ leagues, clubs, players, onApiClick }) {
  return (
    <header className="header">
      <div className="logo">
        <span className="logo-icon">⚽</span>
        <div className="logo-text">
          <h1>Soccer App</h1>
          <p>League · Club · Player Explorer</p>
        </div>
      </div>

      <div className="header-right">
        <div className="stats-bar">
          <div className="stat">
            <div className="stat-num">{leagues.length || '—'}</div>
            <div className="stat-label">Leagues</div>
          </div>
          <div className="stat">
            <div className="stat-num">{clubs.length || '—'}</div>
            <div className="stat-label">Clubs</div>
          </div>
          <div className="stat">
            <div className="stat-num">{players.length || '—'}</div>
            <div className="stat-label">Players</div>
          </div>
        </div>
        <button className="api-btn" onClick={onApiClick}>⚙ API Docs</button>
      </div>
    </header>
  )
}

export default Header

import Spinner from './Spinner'

const FLAGS = {
  england: '🏴󠁧󠁢󠁥󠁮󠁧󠁿', spain: '🇪🇸', germany: '🇩🇪',
  france: '🇫🇷', italy: '🇮🇹', portugal: '🇵🇹',
  netherlands: '🇳🇱', brazil: '🇧🇷', argentina: '🇦🇷'
}

function flag(country) {
  return FLAGS[(country || '').toLowerCase()] || '🌍'
}

function LeagueList({ leagues, loading, selected, onSelect }) {
  return (
    <div className="column">
      <div className="col-header">
        <h2>Leagues</h2>
        <p>Select a league to explore</p>
      </div>
      <div className="col-body">
        {loading && <Spinner message="Loading leagues..." />}

        {!loading && leagues.length === 0 && (
          <div className="empty">
            <div className="icon">🌍</div>
            <div>No leagues found</div>
          </div>
        )}

        {!loading && leagues.map(league => (
          <div
            key={league.id}
            className={`card ${selected?.id === league.id ? 'active' : ''}`}
            onClick={() => onSelect(league)}
          >
            <span className="league-flag">{flag(league.country)}</span>
            <div className="card-title">{league.name}</div>
            <div className="card-sub">{league.country || '—'}</div>
            {league.foundedYear && (
              <div className="tags">
                <span className="tag">Est. {league.foundedYear}</span>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  )
}

export default LeagueList

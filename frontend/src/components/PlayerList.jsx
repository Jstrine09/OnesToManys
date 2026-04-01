import Spinner from './Spinner'

function posClass(position) {
  if (!position) return ''
  const p = position.toLowerCase()
  if (p.includes('goalkeeper')) return 'pos-GK'
  if (p.includes('back') || p.includes('defender')) return 'pos-DEF'
  if (p.includes('mid')) return 'pos-MID'
  if (p.includes('forward') || p.includes('wing') || p.includes('striker')) return 'pos-FWD'
  return 'pos-MID'
}

function posLabel(position) {
  if (!position) return ''
  const p = position.toLowerCase()
  if (p.includes('goalkeeper')) return 'GK'
  if (p.includes('centre-back') || p.includes('center-back')) return 'CB'
  if (p.includes('left-back')) return 'LB'
  if (p.includes('right-back')) return 'RB'
  if (p.includes('defensive mid')) return 'DM'
  if (p.includes('attacking mid')) return 'AM'
  if (p.includes('midfield')) return 'MID'
  if (p.includes('left wing')) return 'LW'
  if (p.includes('right wing')) return 'RW'
  if (p.includes('striker') || p.includes('centre-forward')) return 'ST'
  if (p.includes('forward')) return 'FW'
  if (p.includes('winger')) return 'W'
  if (p.includes('back')) return 'DEF'
  return position.slice(0, 3).toUpperCase()
}

function PlayerCard({ player }) {
  return (
    <div className="card player-card">
      <div className="player-top">
        <div className="jersey">
          {player.jerseyNumber != null ? player.jerseyNumber : '?'}
        </div>
        <div>
          {player.position && (
            <span className={`position-badge ${posClass(player.position)}`}>
              {posLabel(player.position)}
            </span>
          )}
          <div className="card-title">{player.name}</div>
        </div>
      </div>
      <div className="tags">
        {player.nationality && <span className="tag green">🌍 {player.nationality}</span>}
        {player.age         && <span className="tag">Age {player.age}</span>}
      </div>
    </div>
  )
}

function PlayerList({ players, loading, selectedClub }) {
  return (
    <div className="column">
      <div className="col-header">
        <h2>Players</h2>
        <p>{selectedClub ? selectedClub.name : 'Choose a club first'}</p>
      </div>
      <div className="col-body">
        {!selectedClub && (
          <div className="empty">
            <div className="icon">👟</div>
            <div>Select a club to see its squad</div>
          </div>
        )}

        {selectedClub && loading && <Spinner message="Loading players..." />}

        {selectedClub && !loading && players.length === 0 && (
          <div className="empty">
            <div className="icon">👟</div>
            <div>No players in this club</div>
          </div>
        )}

        {!loading && players.map(player => (
          <PlayerCard key={player.id} player={player} />
        ))}
      </div>
    </div>
  )
}

export default PlayerList

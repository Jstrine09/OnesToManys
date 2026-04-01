import Spinner from './Spinner'

function ClubList({ clubs, loading, selected, selectedLeague, onSelect }) {
  return (
    <div className="column">
      <div className="col-header">
        <h2>Clubs</h2>
        <p>{selectedLeague ? selectedLeague.name : 'Choose a league first'}</p>
      </div>
      <div className="col-body">
        {!selectedLeague && (
          <div className="empty">
            <div className="icon">🏟️</div>
            <div>Select a league to see its clubs</div>
          </div>
        )}

        {selectedLeague && loading && <Spinner message="Loading clubs..." />}

        {selectedLeague && !loading && clubs.length === 0 && (
          <div className="empty">
            <div className="icon">🏟️</div>
            <div>No clubs in this league</div>
          </div>
        )}

        {!loading && clubs.map(club => (
          <div
            key={club.id}
            className={`card ${selected?.id === club.id ? 'active' : ''}`}
            onClick={() => onSelect(club)}
          >
            <div className="card-title">{club.name}</div>
            <div className="tags">
              {club.stadiumName && <span className="tag green">🏟 {club.stadiumName}</span>}
              {club.coachName   && <span className="tag">👔 {club.coachName}</span>}
              {club.foundedYear && <span className="tag">📅 {club.foundedYear}</span>}
              {club.city        && <span className="tag">📍 {club.city}</span>}
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}

export default ClubList

import { useState, useEffect } from 'react'
import Header from './components/Header'
import LeagueList from './components/LeagueList'
import ClubList from './components/ClubList'
import PlayerList from './components/PlayerList'
import ApiDrawer from './components/ApiDrawer'
import './App.css'

function App() {
  const [leagues, setLeagues]               = useState([])
  const [clubs, setClubs]                   = useState([])
  const [players, setPlayers]               = useState([])
  const [selectedLeague, setSelectedLeague] = useState(null)
  const [selectedClub, setSelectedClub]     = useState(null)
  const [loadingLeagues, setLoadingLeagues] = useState(true)
  const [loadingClubs, setLoadingClubs]     = useState(false)
  const [loadingPlayers, setLoadingPlayers] = useState(false)
  const [drawerOpen, setDrawerOpen]         = useState(false)

  // Load leagues on mount
  useEffect(() => {
    fetch('/api/leagues')
      .then(r => r.json())
      .then(data => { setLeagues(data); setLoadingLeagues(false) })
      .catch(() => setLoadingLeagues(false))
  }, [])

  // Load clubs when league changes
  useEffect(() => {
    if (!selectedLeague) return
    setLoadingClubs(true)
    setClubs([])
    setPlayers([])
    setSelectedClub(null)
    fetch(`/api/leagues/${selectedLeague.id}/clubs`)
      .then(r => r.json())
      .then(data => { setClubs(data); setLoadingClubs(false) })
      .catch(() => setLoadingClubs(false))
  }, [selectedLeague])

  // Load players when club changes
  useEffect(() => {
    if (!selectedClub || !selectedLeague) return
    setLoadingPlayers(true)
    setPlayers([])
    fetch(`/api/leagues/${selectedLeague.id}/clubs/${selectedClub.id}/players`)
      .then(r => r.json())
      .then(data => { setPlayers(data); setLoadingPlayers(false) })
      .catch(() => setLoadingPlayers(false))
  }, [selectedClub])

  return (
    <div className="app">
      <Header
        leagues={leagues}
        clubs={clubs}
        players={players}
        onApiClick={() => setDrawerOpen(true)}
      />

      <div className="columns">
        <LeagueList
          leagues={leagues}
          loading={loadingLeagues}
          selected={selectedLeague}
          onSelect={setSelectedLeague}
        />
        <ClubList
          clubs={clubs}
          loading={loadingClubs}
          selected={selectedClub}
          selectedLeague={selectedLeague}
          onSelect={setSelectedClub}
        />
        <PlayerList
          players={players}
          loading={loadingPlayers}
          selectedClub={selectedClub}
        />
      </div>

      <ApiDrawer open={drawerOpen} onClose={() => setDrawerOpen(false)} />
    </div>
  )
}

export default App

function Spinner({ message = 'Loading...' }) {
  return (
    <div className="loading">
      <div className="spinner" />
      {message}
    </div>
  )
}

export default Spinner

import { useEffect, useState } from "react";
import { apiRequest } from "../services/api";

export default function BorrowingsPage() {
  const [borrowings, setBorrowings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadBorrowings() {
    try {
      setLoading(true);
      setError("");
      const data = await apiRequest("/borrowings");
      setBorrowings(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message || "Unable to load borrowings");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadBorrowings();
  }, []);

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <p className="page-eyebrow">CIRCULATION</p>
          <h1>Borrowings</h1>
          <p>Issued, overdue and returned books.</p>
        </div>

        <button className="secondary-button" onClick={loadBorrowings}>
          Refresh
        </button>
      </div>

      {loading && <div className="state-card">Loading borrowings...</div>}

      {error && (
        <div className="state-card error-state">
          <strong>Could not load borrowings</strong>
          <p>{error}</p>
        </div>
      )}

      {!loading && !error && borrowings.length === 0 && (
        <div className="state-card">
          <h3>No borrowing records</h3>
        </div>
      )}

      {!loading && !error && borrowings.length > 0 && (
        <div className="data-list">
          {borrowings.map((item) => (
            <div className="data-card" key={item.id}>
              <div>
                <span className="book-id">BORROWING #{item.id}</span>
                <h3>{item.bookCopy?.book?.title || "Unknown book"}</h3>
                <p>
                  {item.user?.firstName} {item.user?.lastName}
                </p>
              </div>

              <div className="data-meta">
                <span>Status</span>
                <strong>{item.status}</strong>

                <span>Due</span>
                <strong>
                  {item.dueAt
                    ? new Date(item.dueAt).toLocaleString()
                    : "N/A"}
                </strong>

                <span>Returned</span>
                <strong>
                  {item.returnedAt
                    ? new Date(item.returnedAt).toLocaleString()
                    : "Not returned"}
                </strong>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

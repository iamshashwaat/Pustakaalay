import { useEffect, useState } from "react";
import { apiRequest } from "../services/api";
import { useAuth } from "../context/AuthContext";

export default function FinesPage() {
  const { user } = useAuth();

  const [fines, setFines] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [processingId, setProcessingId] = useState(null);

  async function loadFines() {
    try {
      setLoading(true);
      setError("");

      const data = await apiRequest("/fines");
      setFines(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message || "Unable to load fines");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadFines();
  }, []);

  async function performAction(id, action) {
    try {
      setProcessingId(id);
      setError("");
      setSuccess("");

      await apiRequest(`/fines/${id}/${action}`, {
        method: "POST",
      });

      setSuccess(
        action === "paid"
          ? `Fine #${id} marked as paid.`
          : `Fine #${id} waived.`
      );

      await loadFines();
    } catch (err) {
      setError(err.message || "Unable to update fine");
    } finally {
      setProcessingId(null);
    }
  }

  function markPaid(id) {
    if (window.confirm(`Mark fine #${id} as paid?`)) {
      performAction(id, "paid");
    }
  }

  function waiveFine(id) {
    if (window.confirm(`Waive fine #${id}?`)) {
      performAction(id, "waive");
    }
  }

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <p className="page-eyebrow">OVERDUE MANAGEMENT</p>
          <h1>Fines</h1>
          <p>Overdue charges and payment status.</p>
        </div>

        <button
          className="secondary-button"
          onClick={loadFines}
        >
          Refresh
        </button>
      </div>

      {success && (
        <div className="success-message">
          {success}
        </div>
      )}

      {loading && (
        <div className="state-card">
          Loading fines...
        </div>
      )}

      {error && (
        <div className="state-card error-state">
          <strong>Could not load fines</strong>
          <p>{error}</p>
        </div>
      )}

      {!loading && !error && fines.length === 0 && (
        <div className="state-card">
          <h3>No fines found</h3>
        </div>
      )}

      {!loading && !error && fines.length > 0 && (
        <div className="data-list">
          {fines.map((fine) => (
            <div className="data-card" key={fine.id}>
              <div>
                <span className="book-id">
                  FINE #{fine.id}
                </span>

                <h3>
                  ₹{Number(fine.amount).toFixed(2)}
                </h3>

                <p>
                  {fine.reason || "Library fine"}
                </p>
              </div>

              <div className="data-meta">
                <span>Status</span>
                <strong>{fine.status}</strong>

                <span>User ID</span>
                <strong>{fine.userId}</strong>

                <span>Borrowing ID</span>
                <strong>{fine.borrowingId}</strong>

                <span>Issued</span>
                <strong>
                  {fine.issuedAt
                    ? new Date(fine.issuedAt).toLocaleString()
                    : "N/A"}
                </strong>
              </div>

              {user?.role === "ADMIN" &&
                fine.status === "PENDING" && (
                  <div className="fine-actions">
                    <button
                      className="primary-button"
                      disabled={processingId === fine.id}
                      onClick={() => markPaid(fine.id)}
                    >
                      Mark Paid
                    </button>

                    <button
                      className="secondary-button"
                      disabled={processingId === fine.id}
                      onClick={() => waiveFine(fine.id)}
                    >
                      Waive
                    </button>
                  </div>
                )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

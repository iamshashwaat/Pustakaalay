import { useEffect, useMemo, useState } from "react";
import { apiRequest } from "../services/api";
import { useAuth } from "../context/AuthContext";

export default function BorrowingsPage() {
  const { user } = useAuth();

  const [borrowings, setBorrowings] = useState([]);
  const [users, setUsers] = useState([]);
  const [copies, setCopies] = useState([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [showIssueForm, setShowIssueForm] = useState(false);
  const [saving, setSaving] = useState(false);

  const [form, setForm] = useState({
    userId: "",
    bookCopyId: "",
    dueAt: "",
  });

  const availableCopies = useMemo(
    () => copies.filter((copy) => copy.status === "AVAILABLE"),
    [copies]
  );

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

  async function loadAdminData() {
    if (user?.role !== "ADMIN") {
      return;
    }

    try {
      const [usersData, copiesData] = await Promise.all([
        apiRequest("/users"),
        apiRequest("/book-copies"),
      ]);

      setUsers(Array.isArray(usersData) ? usersData : []);
      setCopies(Array.isArray(copiesData) ? copiesData : []);
    } catch (err) {
      setError(err.message || "Unable to load issue-book data");
    }
  }

  useEffect(() => {
    loadBorrowings();

    if (user?.role === "ADMIN") {
      loadAdminData();
    }
  }, [user]);

  function handleChange(event) {
    const { name, value } = event.target;

    setForm((current) => ({
      ...current,
      [name]: value,
    }));
  }

  async function handleIssue(event) {
    event.preventDefault();

    try {
      setSaving(true);
      setError("");
      setSuccess("");

      await apiRequest("/borrowings/issue", {
        method: "POST",
        body: JSON.stringify({
          userId: Number(form.userId),
          bookCopyId: Number(form.bookCopyId),
          dueAt: form.dueAt,
        }),
      });

      setSuccess("Book issued successfully.");

      setForm({
        userId: "",
        bookCopyId: "",
        dueAt: "",
      });

      setShowIssueForm(false);

      await Promise.all([
        loadBorrowings(),
        loadAdminData(),
      ]);
    } catch (err) {
      setError(err.message || "Unable to issue book");
    } finally {
      setSaving(false);
    }
  }

  async function handleReturn(id) {
    const confirmed = window.confirm(
      `Return borrowing #${id}?`
    );

    if (!confirmed) {
      return;
    }

    try {
      setError("");
      setSuccess("");

      await apiRequest(`/borrowings/${id}/return`, {
        method: "POST",
      });

      setSuccess(`Borrowing #${id} returned successfully.`);

      await Promise.all([
        loadBorrowings(),
        loadAdminData(),
      ]);
    } catch (err) {
      setError(err.message || "Unable to return book");
    }
  }

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <p className="page-eyebrow">CIRCULATION</p>
          <h1>Borrowings</h1>
          <p>Issued, overdue and returned books.</p>
        </div>

        <div className="page-actions">
          {user?.role === "ADMIN" && (
            <button
              className="primary-button"
              onClick={() =>
                setShowIssueForm((current) => !current)
              }
            >
              + Issue Book
            </button>
          )}

          <button
            className="secondary-button"
            onClick={loadBorrowings}
          >
            Refresh
          </button>
        </div>
      </div>

      {success && (
        <div className="success-message">
          {success}
        </div>
      )}

      {showIssueForm && user?.role === "ADMIN" && (
        <div className="form-card">
          <div className="form-card-header">
            <div>
              <p className="page-eyebrow">ADMIN</p>
              <h2>Issue Book</h2>
            </div>

            <button
              className="close-button"
              onClick={() => setShowIssueForm(false)}
            >
              ×
            </button>
          </div>

          <form className="book-form" onSubmit={handleIssue}>
            <div className="form-grid">
              <div>
                <label>Member *</label>
                <select
                  name="userId"
                  value={form.userId}
                  onChange={handleChange}
                  required
                >
                  <option value="">Select user</option>

                  {users
                    .filter((item) => item.status === "ACTIVE")
                    .map((item) => (
                      <option key={item.id} value={item.id}>
                        {item.firstName} {item.lastName} ({item.email})
                      </option>
                    ))}
                </select>
              </div>

              <div>
                <label>Available Copy *</label>
                <select
                  name="bookCopyId"
                  value={form.bookCopyId}
                  onChange={handleChange}
                  required
                >
                  <option value="">Select copy</option>

                  {availableCopies.map((copy) => (
                    <option key={copy.id} value={copy.id}>
                      {copy.book?.title || "Unknown book"} · {copy.barcode}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label>Due Date *</label>
                <input
                  type="datetime-local"
                  name="dueAt"
                  value={form.dueAt}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>

            {availableCopies.length === 0 && (
              <div className="state-card">
                No available physical copies found.
              </div>
            )}

            <div className="form-actions">
              <button
                type="button"
                className="secondary-button"
                onClick={() => setShowIssueForm(false)}
              >
                Cancel
              </button>

              <button
                type="submit"
                className="primary-button"
                disabled={saving || availableCopies.length === 0}
              >
                {saving ? "Issuing..." : "Issue Book"}
              </button>
            </div>
          </form>
        </div>
      )}

      {loading && (
        <div className="state-card">
          Loading borrowings...
        </div>
      )}

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
                <span className="book-id">
                  BORROWING #{item.id}
                </span>

                <h3>
                  {item.bookCopy?.book?.title || "Unknown book"}
                </h3>

                <p>
                  {item.user?.firstName} {item.user?.lastName}
                </p>

                <p>
                  Copy: {item.bookCopy?.barcode || "N/A"}
                </p>
              </div>

              <div className="data-meta">
                <span>Status</span>
                <strong>{item.status}</strong>

                <span>Borrowed</span>
                <strong>
                  {item.borrowedAt
                    ? new Date(item.borrowedAt).toLocaleString()
                    : "N/A"}
                </strong>

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

              {user?.role === "ADMIN" &&
                (item.status === "BORROWED" ||
                  item.status === "OVERDUE") && (
                  <div className="row-action">
                    <button
                      className="primary-button"
                      onClick={() => handleReturn(item.id)}
                    >
                      Return Book
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

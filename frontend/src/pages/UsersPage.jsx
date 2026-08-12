import { Navigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { apiRequest } from "../services/api";
import { useAuth } from "../context/AuthContext";

export default function UsersPage() {
  const { user } = useAuth();

  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadUsers() {
    try {
      setLoading(true);
      setError("");
      const data = await apiRequest("/users");
      setUsers(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message || "Unable to load users");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    if (user?.role === "ADMIN") {
      loadUsers();
    }
  }, [user]);

  if (user?.role !== "ADMIN") {
    return <Navigate to="/dashboard" replace />;
  }

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <p className="page-eyebrow">ADMINISTRATION</p>
          <h1>Users</h1>
          <p>Registered library members and administrators.</p>
        </div>

        <button className="secondary-button" onClick={loadUsers}>
          Refresh
        </button>
      </div>

      {loading && <div className="state-card">Loading users...</div>}

      {error && (
        <div className="state-card error-state">
          <strong>Could not load users</strong>
          <p>{error}</p>
        </div>
      )}

      {!loading && !error && (
        <div className="data-list">
          {users.map((item) => (
            <div className="data-card" key={item.id}>
              <div>
                <span className="book-id">USER #{item.id}</span>
                <h3>
                  {item.firstName} {item.lastName}
                </h3>
                <p>{item.email}</p>
              </div>

              <div className="data-meta">
                <span>Role</span>
                <strong>{item.roleName}</strong>

                <span>Membership</span>
                <strong>{item.membershipNumber || "N/A"}</strong>

                <span>Status</span>
                <strong>{item.status}</strong>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

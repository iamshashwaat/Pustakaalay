import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function DashboardPage() {
  const { user } = useAuth();

  return (
    <div className="page">
      <section className="welcome-card">
        <p className="eyebrow">PUSTAKAALAY</p>

        <h1>Welcome to your library.</h1>

        <p>
          Signed in as {user?.email} with {user?.role} access.
        </p>
      </section>

      <section className="dashboard-grid">
        <Link to="/books" className="dashboard-card">
          <span>📚</span>
          <h3>Books</h3>
          <p>Browse the library catalogue.</p>
        </Link>

        <Link to="/borrowings" className="dashboard-card">
          <span>🔄</span>
          <h3>Borrowings</h3>
          <p>View issued and returned books.</p>
        </Link>

        <Link to="/fines" className="dashboard-card">
          <span>₹</span>
          <h3>Fines</h3>
          <p>View overdue fines and payment status.</p>
        </Link>

        {user?.role === "ADMIN" && (
          <Link to="/users" className="dashboard-card">
            <span>👥</span>
            <h3>Users</h3>
            <p>View registered library members.</p>
          </Link>
        )}
      </section>
    </div>
  );
}

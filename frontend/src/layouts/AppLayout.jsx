import { NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function AppLayout() {
  const { user, logout } = useAuth();

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="sidebar-brand">
          <div className="sidebar-logo">P</div>
          <div>
            <strong>Pustakaalay</strong>
            <span>Smart Library</span>
          </div>
        </div>

        <nav className="sidebar-nav">
          <NavLink to="/dashboard">Dashboard</NavLink>
          <NavLink to="/books">Books</NavLink>
          <NavLink to="/borrowings">Borrowings</NavLink>
          <NavLink to="/fines">Fines</NavLink>

          {user?.role === "ADMIN" && (
            <NavLink to="/users">Users</NavLink>
          )}
        </nav>

        <div className="sidebar-user">
          <div>
            <strong>{user?.email}</strong>
            <span>{user?.role}</span>
          </div>

          <button onClick={logout}>Logout</button>
        </div>
      </aside>

      <main className="app-content">
        <Outlet />
      </main>
    </div>
  );
}

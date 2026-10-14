import { NavLink } from "react-router-dom";
import { Bot, Files, LayoutDashboard, Settings2, UsersRound, X } from "lucide-react";
import Brand from "../ui/Brand";

const links = [
  { to: "/dashboard", label: "Overview", icon: LayoutDashboard },
  { to: "/ask", label: "Ask AI", icon: Bot },
  { to: "/documents", label: "Knowledge library", icon: Files },
  { to: "/team", label: "Team & invitations", icon: UsersRound },
  { to: "/settings", label: "Settings", icon: Settings2 },
];

export default function Sidebar({ open, onClose }: { open: boolean; onClose: () => void }) {
  return (
    <>
      {open && <button className="mobile-scrim" aria-label="Close navigation" onClick={onClose} />}
      <aside className={`sidebar ${open ? "sidebar-open" : ""}`}>
        <div className="sidebar-top">
          <Brand />
          <button className="icon-button sidebar-close" onClick={onClose} aria-label="Close menu"><X size={18} /></button>
        </div>
        <div className="workspace-switcher">
          <div className="workspace-avatar">K</div>
          <div><strong>Workspace</strong><span>Organization knowledge</span></div>
          <span className="status-dot" />
        </div>
        <p className="nav-label">WORKSPACE</p>
        <nav className="main-nav">
          {links.map(({ to, label, icon: Icon }) => (
            <NavLink key={to} to={to} onClick={onClose} className={({ isActive }) => `nav-link ${isActive ? "active" : ""}`}>
              <Icon size={18} strokeWidth={1.8} /><span>{label}</span>
              {to === "/ask" && <span className="nav-pill">AI</span>}
            </NavLink>
          ))}
        </nav>
        <div className="sidebar-bottom">
          <div className="sidebar-tip">
            <div className="tip-icon"><Bot size={18} /></div>
            <strong>Your knowledge, amplified.</strong>
            <p>Ask questions across the information your team shares.</p>
            <NavLink to="/ask" onClick={onClose}>Try AI assistant <span>↗</span></NavLink>
          </div>
          <div className="sidebar-foot"><span className="status-dot" /> All systems are configured locally</div>
        </div>
      </aside>
    </>
  );
}

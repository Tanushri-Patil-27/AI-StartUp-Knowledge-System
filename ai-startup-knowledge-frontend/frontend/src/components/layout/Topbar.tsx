import { Bell, Menu, Search } from "lucide-react";
import { useAuth } from "../../context/AuthContext";

export default function Topbar({ onMenu }: { onMenu: () => void }) {
  const { user } = useAuth();
  const initials = (user?.name || user?.email || "U").split(" ").map((part) => part[0]).join("").slice(0, 2).toUpperCase();
  return (
    <header className="topbar">
      <button className="icon-button mobile-menu" onClick={onMenu} aria-label="Open navigation"><Menu size={20} /></button>
      <div className="breadcrumb"><span>Workspace</span><span className="crumb-slash">/</span><strong>Knowledge hub</strong></div>
      <div className="topbar-right">
        <div className="search-shortcut"><Search size={15} /><span>Search your workspace</span><kbd>⌘ K</kbd></div>
        <button className="icon-button notification-button" aria-label="Notifications" title="Notifications are not connected yet"><Bell size={18} /><i /></button>
        <div className="topbar-divider" />
        <div className="topbar-user"><div className="avatar">{initials}</div><div className="topbar-user-copy"><strong>{user?.name || "Workspace user"}</strong><span>{user?.role || "Member"}</span></div></div>
      </div>
    </header>
  );
}

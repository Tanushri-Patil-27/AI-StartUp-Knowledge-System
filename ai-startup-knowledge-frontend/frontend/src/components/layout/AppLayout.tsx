import { useState } from "react";
import { Outlet } from "react-router-dom";
import Sidebar from "./Sidebar";
import Topbar from "./Topbar";

export default function AppLayout() {
  const [menuOpen, setMenuOpen] = useState(false);
  return (
    <div className="app-shell">
      <Sidebar open={menuOpen} onClose={() => setMenuOpen(false)} />
      <div className="app-main">
        <Topbar onMenu={() => setMenuOpen(true)} />
        <main className="page-content"><Outlet /></main>
        <footer className="app-footer"><span>Knowbase</span><span>Private knowledge. Clear answers.</span><span>Built for teams that think ahead.</span></footer>
      </div>
    </div>
  );
}

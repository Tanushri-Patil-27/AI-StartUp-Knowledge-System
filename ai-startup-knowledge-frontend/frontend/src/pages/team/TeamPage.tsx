import { useState, type FormEvent } from "react";
import { AlertCircle, CheckCircle2, Mail, ShieldCheck, UserPlus, UsersRound } from "lucide-react";
import PageHeader from "../../components/ui/PageHeader";
import { useAuth } from "../../context/AuthContext";
import { api, getErrorMessage } from "../../lib/api";

const roles = ["CEO", "MANAGER", "EMPLOYEE"];

export default function TeamPage() {
  const { user } = useAuth();
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("EMPLOYEE");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const isAdmin = user?.role === "ADMIN";

  async function invite(event: FormEvent) {
    event.preventDefault(); setMessage(""); setError(""); setLoading(true);
    try {
      const { data } = await api.post("/invitations", { email: email.trim(), role });
      setMessage(data?.message || `Invitation request submitted for ${email.trim()}. Check the User Service response for delivery status.`);
      setEmail("");
    } catch (err) { setError(getErrorMessage(err, "Invitation failed. Confirm that the current user is an ADMIN and that the invitation endpoint is routed through the Gateway.")); }
    finally { setLoading(false); }
  }

  return <div className="page-enter"><PageHeader eyebrow="WORKSPACE ACCESS" title="Good work is shared work." description="Manage invitations and understand how access is organized across your workspace." /><section className="team-summary-grid"><div className="surface-card team-info-card"><div className="team-info-icon"><UsersRound size={20} /></div><p className="eyebrow">YOUR WORKSPACE</p><h3>One team. Shared context.</h3><p>People invited to your organization can access permitted resources according to their assigned role.</p><div className="team-stat-line"><span>Organization ID</span><strong>#{user?.organizationId ?? "—"}</strong></div><div className="team-stat-line"><span>Your role</span><strong>{user?.role || "Member"}</strong></div></div><div className="surface-card role-info-card"><div className="card-heading"><div><h3>Role overview</h3><p>Application roles supported by the backend</p></div><ShieldCheck size={20} className="muted-icon" /></div>{[{ role: "ADMIN", detail: "Organization configuration and invitations" }, { role: "CEO", detail: "Leadership and organization knowledge" }, { role: "MANAGER", detail: "Management and team knowledge" }, { role: "EMPLOYEE", detail: "Everyday access to permitted knowledge" }].map((item) => <div className="role-row" key={item.role}><span className={`role-dot role-${item.role.toLowerCase()}`} /><div><strong>{item.role}</strong><small>{item.detail}</small></div>{item.role === user?.role && <span className="you-label">YOU</span>}</div>)}</div></section><section className="surface-card invite-card"><div className="card-heading"><div><h3>Invite a teammate</h3><p>Send an invitation to join your organization.</p></div><span className="invite-icon"><UserPlus size={19} /></span></div>{!isAdmin && <div className="alert alert-info"><ShieldCheck size={17} /> Invitations are restricted to the ADMIN role by your backend. You're signed in as {user?.role || "a member"}.</div>}{message && <div className="alert alert-success"><CheckCircle2 size={17} />{message}</div>}{error && <div className="alert alert-error"><AlertCircle size={17} />{error}</div>}<form className="invite-form" onSubmit={(e) => void invite(e)}><label className="field-label">Teammate's work email<span className="input-wrap"><Mail size={17} /><input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="colleague@company.com" required disabled={!isAdmin} /></span></label><label className="field-label">Assign role<select className="standard-select" value={role} onChange={(e) => setRole(e.target.value)} disabled={!isAdmin}>{roles.map((item) => <option key={item} value={item}>{item}</option>)}</select></label><button className="button button-primary" disabled={!isAdmin || loading || !email.trim()}>{loading ? "Sending invitation..." : "Send invitation"} <UserPlus size={16} /></button></form><p className="form-footnote">The User Service should generate the invitation token and deliver the invitation email. Never assign roles based on client-provided registration fields.</p></section></div>;
}

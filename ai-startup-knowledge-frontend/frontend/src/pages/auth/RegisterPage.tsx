import { useState, type FormEvent } from "react";
import { ArrowLeft, ArrowRight, KeyRound, Mail, UserRound } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import Brand from "../../components/ui/Brand";
import { api, getErrorMessage } from "../../lib/api";

export default function RegisterPage() {
  const navigate = useNavigate();
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [invitationToken, setInvitationToken] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setError(""); setSuccess(""); setLoading(true);
    try {
      await api.post("/auth/register", { name: name.trim(), email: email.trim(), password, invitationToken: invitationToken.trim() });
      setSuccess("Account created. You can now sign in.");
      window.setTimeout(() => navigate("/login"), 900);
    } catch (err) {
      setError(getErrorMessage(err, "Registration failed. Check that your invitation token is valid and has not expired."));
    } finally { setLoading(false); }
  }

  return (
    <div className="auth-page auth-page-register">
      <section className="auth-visual">
        <div className="auth-visual-top"><Brand /></div>
        <div className="auth-visual-content"><div className="visual-badge"><span className="status-dot" /> A MORE CONNECTED WORKSPACE</div><h1>Make shared knowledge your <em>advantage.</em></h1><p>Join your organization and turn scattered information into shared understanding.</p><div className="register-note"><KeyRound size={18} /><span>Accounts are created using a single-use invitation from your organization administrator.</span></div></div>
        <div className="auth-visual-foot"><span>Built for curious teams.</span><span>02 — Join your workspace</span></div>
      </section>
      <section className="auth-form-side">
        <div className="auth-mobile-brand"><Brand /></div>
        <div className="auth-form-wrap">
          <div className="auth-kicker">YOUR INVITATION</div><h2>Create your account</h2><p className="muted">Use the details and invitation token provided by your admin.</p>
          <form onSubmit={handleSubmit} className="form-stack">
            <label className="field-label">Full name<span className="input-wrap"><UserRound size={17} /><input value={name} onChange={(e) => setName(e.target.value)} autoComplete="name" placeholder="Your name" required /></span></label>
            <label className="field-label">Work email<span className="input-wrap"><Mail size={17} /><input type="email" value={email} onChange={(e) => setEmail(e.target.value)} autoComplete="email" placeholder="you@company.com" required /></span></label>
            <label className="field-label">Password<span className="input-wrap"><KeyRound size={17} /><input type="password" value={password} onChange={(e) => setPassword(e.target.value)} autoComplete="new-password" minLength={8} placeholder="At least 8 characters" required /></span></label>
            <label className="field-label">Invitation token<span className="input-wrap"><KeyRound size={17} /><input value={invitationToken} onChange={(e) => setInvitationToken(e.target.value)} placeholder="Paste your invitation token" required /></span></label>
            {error && <div className="alert alert-error" role="alert">{error}</div>}{success && <div className="alert alert-success" role="status">{success}</div>}
            <button className="button button-primary button-full" disabled={loading}>{loading ? "Creating account..." : "Create account"} {!loading && <ArrowRight size={17} />}</button>
          </form>
          <div className="auth-switch"><Link to="/login"><ArrowLeft size={14} /> Back to sign in</Link></div>
        </div>
        <div className="auth-bottom">© {new Date().getFullYear()} Knowbase <span>Invitation-only registration</span></div>
      </section>
    </div>
  );
}

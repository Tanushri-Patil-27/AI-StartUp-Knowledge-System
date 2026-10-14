import { useState, type FormEvent } from "react";
import { ArrowRight, Eye, EyeOff, LockKeyhole, Mail } from "lucide-react";
import { Link, Navigate, useLocation, useNavigate } from "react-router-dom";
import Brand from "../../components/ui/Brand";
import { useAuth } from "../../context/AuthContext";
import { getErrorMessage } from "../../lib/api";

export default function LoginPage() {
  const { login, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  if (isAuthenticated) return <Navigate to="/dashboard" replace />;

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setError("");
    setLoading(true);
    try {
      await login({ email: email.trim(), password });
      const from = (location.state as { from?: { pathname?: string } } | null)?.from?.pathname;
      navigate(from || "/dashboard", { replace: true });
    } catch (err) {
      setError(getErrorMessage(err, "Unable to sign in. Check your credentials and API Gateway."));
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="auth-page">
      <section className="auth-visual">
        <div className="auth-visual-top"><Brand /></div>
        <div className="auth-visual-content">
          <div className="visual-badge"><span className="status-dot" /> YOUR TEAM'S KNOWLEDGE, IN ONE PLACE</div>
          <h1>Good ideas deserve to be <em>found.</em></h1>
          <p>Bring your team's documents together. Ask better questions. Move forward with confidence.</p>
          <div className="orbit-card">
            <div className="orbit-icon">✳</div>
            <div><span>KNOWLEDGE ASSISTANT</span><strong>Answers grounded in your documents.</strong></div>
            <div className="orbit-pulse" />
          </div>
        </div>
        <div className="auth-visual-foot"><span>Built for curious teams.</span><span>01 — Knowledge, connected</span></div>
      </section>
      <section className="auth-form-side">
        <div className="auth-mobile-brand"><Brand /></div>
        <div className="auth-form-wrap">
          <div className="auth-kicker">WELCOME BACK</div>
          <h2>Sign in to your workspace</h2>
          <p className="muted">Your team's collective knowledge is waiting.</p>
          <form onSubmit={handleSubmit} className="form-stack">
            <label className="field-label">Work email
              <span className="input-wrap"><Mail size={17} /><input type="email" autoComplete="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="you@company.com" required /></span>
            </label>
            <label className="field-label">Password
              <span className="input-wrap"><LockKeyhole size={17} /><input type={showPassword ? "text" : "password"} autoComplete="current-password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Enter your password" required /><button type="button" className="input-action" onClick={() => setShowPassword((v) => !v)} aria-label={showPassword ? "Hide password" : "Show password"}>{showPassword ? <EyeOff size={17} /> : <Eye size={17} />}</button></span>
            </label>
            {error && <div className="alert alert-error" role="alert">{error}</div>}
            <button className="button button-primary button-full" disabled={loading}>{loading ? "Signing you in..." : "Sign in"} {!loading && <ArrowRight size={17} />}</button>
          </form>
          <div className="auth-switch">Have an invitation? <Link to="/register">Create your account <ArrowRight size={14} /></Link></div>
          <div className="auth-secure"><LockKeyhole size={14} /> Protected with secure, token-based authentication</div>
        </div>
        <div className="auth-bottom">© {new Date().getFullYear()} Knowbase <span>Privacy-first by design</span></div>
      </section>
    </div>
  );
}

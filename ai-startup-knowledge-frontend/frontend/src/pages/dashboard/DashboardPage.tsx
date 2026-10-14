import { ArrowDownRight, ArrowRight, ArrowUpRight, Bot, FileText, Plus, Sparkles, UploadCloud, UsersRound } from "lucide-react";
import { Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import PageHeader from "../../components/ui/PageHeader";
import { useDocuments } from "../../hooks/useDocuments";
import Spinner from "../../components/ui/Spinner";

const suggestions = [
  { icon: "◈", title: "Find a decision", question: "What important decisions are documented in our knowledge base?" },
  { icon: "⌘", title: "Understand a project", question: "Summarize the key project information available in our documents." },
  { icon: "↗", title: "Get up to speed", question: "What should a new team member know from our documentation?" },
];

export default function DashboardPage() {
  const { user } = useAuth();
  const { documents, loading } = useDocuments();
  const firstName = user?.name?.split(" ")[0] || "there";
  return (
    <div className="page-enter">
      <PageHeader eyebrow="YOUR WORKSPACE" title={`Good to have you here, ${firstName}.`} description="Here's a snapshot of your team's shared knowledge." action={<Link to="/documents" className="button button-secondary"><Plus size={16} /> Add knowledge</Link>} />
      <section className="hero-banner">
        <div className="hero-banner-content"><div className="hero-kicker"><Sparkles size={14} /> KNOWLEDGE, MEET INTELLIGENCE</div><h2>Answers are closer<br />than you think.</h2><p>Ask a question. Find the signal in your team's documents. Get back to what matters.</p><Link to="/ask" className="button button-light">Ask your knowledge base <ArrowRight size={16} /></Link></div>
        <div className="hero-art" aria-hidden="true"><div className="hero-ring ring-one" /><div className="hero-ring ring-two" /><div className="hero-orb"><Bot size={45} strokeWidth={1.4} /></div><div className="floating-chip chip-top"><span className="chip-dot" /> Context found</div><div className="floating-chip chip-bottom"><Sparkles size={14} /> Grounded answers</div></div>
      </section>
      <div className="section-heading"><div><h2>Your knowledge at a glance</h2><p>Live data from your workspace</p></div><span className="live-label"><i /> LIVE OVERVIEW</span></div>
      <section className="stats-grid">
        <div className="stat-card"><div className="stat-top"><span className="stat-icon purple"><FileText size={19} /></span><span className="stat-trend neutral">Workspace</span></div><div className="stat-number">{loading ? "—" : documents.length}</div><div className="stat-label">Documents available</div><div className="stat-foot"><span>Stored knowledge records</span><ArrowUpRight size={14} /></div></div>
        <div className="stat-card"><div className="stat-top"><span className="stat-icon mint"><Bot size={19} /></span><span className="stat-trend positive"><ArrowUpRight size={13} /> Ready to ask</span></div><div className="stat-number">AI</div><div className="stat-label">Knowledge assistant</div><div className="stat-foot"><span>RAG-powered question answering</span><Link to="/ask">Explore <ArrowRight size={13} /></Link></div></div>
        <div className="stat-card"><div className="stat-top"><span className="stat-icon amber"><UsersRound size={19} /></span><span className="stat-trend neutral">Access</span></div><div className="stat-number">{user?.role || "Member"}</div><div className="stat-label">Your workspace role</div><div className="stat-foot"><span>Organization-scoped access</span><ArrowDownRight size={14} /></div></div>
      </section>
      <section className="dashboard-lower">
        <div className="surface-card suggestion-panel"><div className="card-heading"><div><h3>Start with a good question</h3><p>Try one of these prompts with your knowledge base.</p></div><Link to="/ask" className="text-link">Open assistant <ArrowRight size={14} /></Link></div><div className="suggestion-list">{suggestions.map((item) => <Link key={item.title} to="/ask" state={{ suggestedQuestion: item.question }} className="suggestion-row"><span className="suggestion-symbol">{item.icon}</span><span><strong>{item.title}</strong><small>{item.question}</small></span><ArrowRight size={16} className="suggestion-arrow" /></Link>)}</div></div>
        <div className="surface-card recent-panel"><div className="card-heading"><div><h3>Recently added</h3><p>Your latest shared documents</p></div><Link to="/documents" className="text-link">View library <ArrowRight size={14} /></Link></div>{loading ? <Spinner label="Loading documents..." /> : documents.length ? <div className="recent-list">{documents.slice(0, 4).map((doc) => <Link key={doc.id} to={`/documents/${doc.id}`} className="recent-row"><span className="file-icon"><FileText size={17} /></span><span className="recent-file"><strong>{doc.fileName}</strong><small>{doc.documentType || "Document"} · {doc.fileType || "File"}</small></span><ArrowRight size={15} /></Link>)}</div> : <div className="empty-inline"><div className="empty-icon"><UploadCloud size={20} /></div><strong>Your knowledge library starts here</strong><p>Upload a document to make team knowledge easier to find.</p><Link to="/documents" className="button button-secondary button-sm">Upload a document <ArrowRight size={14} /></Link></div>}</div>
      </section>
    </div>
  );
}

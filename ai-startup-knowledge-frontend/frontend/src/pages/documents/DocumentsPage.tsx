import { useRef, useState, type ChangeEvent, type FormEvent } from "react";
import { AlertCircle, CheckCircle2, File, FileText, Filter, Plus, Search, Trash2, UploadCloud, X } from "lucide-react";
import { Link } from "react-router-dom";
import PageHeader from "../../components/ui/PageHeader";
import Spinner from "../../components/ui/Spinner";
import { useDocuments } from "../../hooks/useDocuments";
import { api, getErrorMessage } from "../../lib/api";
import type { DocumentRecord } from "../../types";

const types = ["GENERAL", "MEETING_NOTES", "DECISION", "POLICY", "PROJECT_DOCUMENT", "TECHNICAL_DOCUMENT", "BUSINESS_DOCUMENT", "REQUIREMENT", "REPORT"];

function formatSize(size?: number) {
  if (size == null) return "—";
  if (size < 1024) return `${size} B`;
  if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} KB`;
  return `${(size / (1024 * 1024)).toFixed(1)} MB`;
}

export default function DocumentsPage() {
  const { documents, loading, error: loadError, refresh } = useDocuments();
  const fileInput = useRef<HTMLInputElement>(null);
  const [query, setQuery] = useState("");
  const [typeFilter, setTypeFilter] = useState("ALL");
  const [showUpload, setShowUpload] = useState(false);
  const [file, setFile] = useState<File | null>(null);
  const [documentType, setDocumentType] = useState("GENERAL");
  const [description, setDescription] = useState("");
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [deletingId, setDeletingId] = useState("");

  const filtered = documents.filter((doc) => {
    const matchesQuery = `${doc.fileName} ${doc.description || ""} ${doc.documentType || ""}`.toLowerCase().includes(query.toLowerCase());
    return matchesQuery && (typeFilter === "ALL" || doc.documentType === typeFilter);
  });

  function onFileChange(event: ChangeEvent<HTMLInputElement>) {
    setFile(event.target.files?.[0] || null);
    setError("");
  }

  async function handleUpload(event: FormEvent) {
    event.preventDefault();
    if (!file) { setError("Choose a file to upload."); return; }
    setUploading(true); setError(""); setMessage("");
    try {
      const form = new FormData();
      form.append("file", file);
      form.append("documentType", documentType);
      if (description.trim()) form.append("description", description.trim());
      await api.post("/api/documents/upload", form);
      setMessage("Document uploaded successfully.");
      setFile(null); setDescription(""); setShowUpload(false);
      if (fileInput.current) fileInput.current.value = "";
      await refresh();
    } catch (err) {
      setError(getErrorMessage(err, "Upload failed. Check S3 permissions and the Document Service logs."));
    } finally { setUploading(false); }
  }

  async function handleDelete(doc: DocumentRecord) {
    if (!window.confirm(`Delete "${doc.fileName}" metadata? Make sure your backend also handles the stored S3 object.`)) return;
    setDeletingId(doc.id); setError(""); setMessage("");
    try {
      await api.delete(`/api/documents/${doc.id}`);
      setMessage("Document deleted.");
      await refresh();
    } catch (err) { setError(getErrorMessage(err, "Could not delete this document.")); }
    finally { setDeletingId(""); }
  }

  return (
    <div className="page-enter">
      <PageHeader eyebrow="KNOWLEDGE LIBRARY" title="Your team's knowledge." description="A shared home for the documents, decisions, and context your team relies on." action={<button className="button button-primary" onClick={() => setShowUpload((v) => !v)}><Plus size={16} /> Upload document</button>} />
      <div className="library-toolbar"><div className="library-count"><span className="count-number">{documents.length}</span><span>documents in your workspace</span></div><div className="library-filters"><label className="search-input"><Search size={16} /><input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Search documents..." /></label><label className="select-filter"><Filter size={15} /><select value={typeFilter} onChange={(e) => setTypeFilter(e.target.value)}><option value="ALL">All types</option>{types.map((type) => <option key={type} value={type}>{type.replaceAll("_", " ")}</option>)}</select></label></div></div>
      {message && <div className="alert alert-success"><CheckCircle2 size={17} />{message}<button onClick={() => setMessage("")} aria-label="Dismiss"><X size={15} /></button></div>}
      {(error || loadError) && <div className="alert alert-error"><AlertCircle size={17} />{error || loadError}<button onClick={() => setError("")} aria-label="Dismiss"><X size={15} /></button></div>}
      {showUpload && <section className="surface-card upload-panel"><div className="card-heading"><div><h3>Add to your knowledge library</h3><p>Upload a file and give your teammates useful context.</p></div><button className="icon-button" onClick={() => setShowUpload(false)} aria-label="Close upload"><X size={18} /></button></div><form onSubmit={handleUpload} className="upload-form"><button type="button" className={`drop-zone ${file ? "drop-zone-selected" : ""}`} onClick={() => fileInput.current?.click()} onDragOver={(e) => e.preventDefault()} onDrop={(e) => { e.preventDefault(); const dropped = e.dataTransfer.files?.[0]; if (dropped) setFile(dropped); }}><input ref={fileInput} type="file" hidden onChange={onFileChange} /><span className="drop-icon"><UploadCloud size={22} /></span><strong>{file ? file.name : "Choose a file or drag it here"}</strong><small>{file ? formatSize(file.size) : "Use the file types supported by your Document Service."}</small></button><div className="upload-fields"><label className="field-label">Document type<select className="standard-select" value={documentType} onChange={(e) => setDocumentType(e.target.value)}>{types.map((type) => <option key={type} value={type}>{type.replaceAll("_", " ")}</option>)}</select></label><label className="field-label">Description <span className="optional-label">Optional</span><textarea value={description} onChange={(e) => setDescription(e.target.value)} placeholder="What is this document about?" rows={3} /></label></div><div className="upload-actions"><button type="button" className="button button-secondary" onClick={() => setShowUpload(false)}>Cancel</button><button className="button button-primary" disabled={uploading || !file}>{uploading ? "Uploading..." : "Upload document"} <UploadCloud size={16} /></button></div></form></section>}
      <section className="surface-card document-table-card"><div className="table-heading"><div><h3>All documents</h3><p>Browse the knowledge shared with your organization.</p></div><span className="table-result-count">{filtered.length} results</span></div>{loading ? <Spinner label="Loading your documents..." /> : filtered.length ? <div className="document-list"><div className="document-list-head"><span>DOCUMENT</span><span>TYPE</span><span>SIZE</span><span>ADDED</span><span>ACTIONS</span></div>{filtered.map((doc) => <div className="document-row" key={doc.id}><Link to={`/documents/${doc.id}`} className="document-name-cell"><span className="file-icon"><FileText size={18} /></span><span><strong>{doc.fileName}</strong><small>{doc.description || doc.fileType || "Shared document"}</small></span></Link><span><span className="type-badge">{(doc.documentType || "GENERAL").replaceAll("_", " ")}</span></span><span className="muted table-size">{formatSize(doc.fileSize)}</span><span className="muted table-date">{doc.createdAt ? new Date(doc.createdAt).toLocaleDateString() : "—"}</span><span className="row-actions"><Link to={`/documents/${doc.id}`} className="table-action" title="View details"><File size={16} /></Link><button className="table-action danger-action" title="Delete document" disabled={deletingId === doc.id} onClick={() => void handleDelete(doc)}><Trash2 size={16} /></button></span></div>)}</div> : <div className="empty-state"><div className="empty-icon"><FileText size={24} /></div><h3>{query || typeFilter !== "ALL" ? "No matching documents" : "Your library is ready for its first document"}</h3><p>{query || typeFilter !== "ALL" ? "Try a different search or filter." : "Add a document to start building a searchable source of team knowledge."}</p>{!query && typeFilter === "ALL" && <button className="button button-primary" onClick={() => setShowUpload(true)}><Plus size={16} /> Upload your first document</button>}</div>}</section>
    </div>
  );
}

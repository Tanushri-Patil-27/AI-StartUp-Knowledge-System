import { ArrowLeft, ExternalLink, FileText, HardDrive, UserRound } from "lucide-react";
import { Link, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import PageHeader from "../../components/ui/PageHeader";
import Spinner from "../../components/ui/Spinner";
import { api, getErrorMessage } from "../../lib/api";
import type { DocumentRecord } from "../../types";

export default function DocumentDetailsPage() {
  const { documentId = "" } = useParams();
  const [document, setDocument] = useState<DocumentRecord | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  useEffect(() => {
    let active = true;
    api.get(`/api/documents/${encodeURIComponent(documentId)}`)
      .then(({ data }) => { if (active) setDocument({ ...data, id: String(data.id ?? data.documentId ?? documentId), fileName: String(data.fileName ?? "Untitled document") }); })
      .catch((err) => { if (active) setError(getErrorMessage(err, "Unable to load this document.")); })
      .finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, [documentId]);

  return <div className="page-enter"><Link to="/documents" className="back-link"><ArrowLeft size={15} /> Back to library</Link><PageHeader eyebrow="DOCUMENT DETAILS" title={document?.fileName || "Document details"} description="Metadata associated with this knowledge source." />{loading ? <Spinner label="Loading document..." /> : error ? <div className="alert alert-error">{error}</div> : document && <section className="surface-card details-card"><div className="details-file"><span className="details-file-icon"><FileText size={25} /></span><div><h3>{document.fileName}</h3><p>{document.description || "No description has been added."}</p></div><span className="type-badge">{(document.documentType || "GENERAL").replaceAll("_", " ")}</span></div><div className="details-grid"><div className="detail-item"><span>Document ID</span><strong>{document.id}</strong></div><div className="detail-item"><span>File type</span><strong>{document.fileType || "Not available"}</strong></div><div className="detail-item"><span>File size</span><strong>{document.fileSize != null ? `${(document.fileSize / 1024).toFixed(1)} KB` : "Not available"}</strong></div><div className="detail-item"><span>Organization</span><strong>{document.organizationId ?? "Not available"}</strong></div><div className="detail-item"><span>Uploaded by user</span><strong><UserRound size={14} /> {document.uploadedByUserId ?? "Not available"}</strong></div><div className="detail-item"><span>Created</span><strong>{document.createdAt ? new Date(document.createdAt).toLocaleString() : "Not available"}</strong></div></div>{document.s3Url && <a className="button button-secondary details-link" href={document.s3Url} target="_blank" rel="noreferrer"><HardDrive size={16} /> Open stored file <ExternalLink size={15} /></a>}</section>}</div>;
}

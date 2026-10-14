import { useCallback, useEffect, useState } from "react";
import { api } from "../lib/api";
import { useAuth } from "../context/AuthContext";
import type { DocumentRecord } from "../types";

function normalizeDocument(item: Record<string, unknown>): DocumentRecord {
  const id = String(item.id ?? item.documentId ?? "");
  return {
    id,
    documentId: item.documentId ? String(item.documentId) : id,
    organizationId: item.organizationId as number | undefined,
    uploadedByUserId: item.uploadedByUserId as number | undefined,
    fileName: String(item.fileName ?? item.name ?? "Untitled document"),
    fileType: item.fileType ? String(item.fileType) : undefined,
    fileSize: typeof item.fileSize === "number" ? item.fileSize : undefined,
    documentType: item.documentType ? String(item.documentType) : undefined,
    description: item.description ? String(item.description) : undefined,
    s3Url: item.s3Url ? String(item.s3Url) : undefined,
    createdAt: item.createdAt ? String(item.createdAt) : undefined,
    updatedAt: item.updatedAt ? String(item.updatedAt) : undefined,
  };
}

export function useDocuments() {
  const { user } = useAuth();
  const [documents, setDocuments] = useState<DocumentRecord[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const refresh = useCallback(async () => {
    if (!user?.organizationId) { setDocuments([]); setLoading(false); return; }
    setLoading(true); setError("");
    try {
      const { data } = await api.get(`/api/documents/organization/${user.organizationId}`);
      const rows = Array.isArray(data) ? data : Array.isArray(data?.content) ? data.content : [];
      setDocuments(rows.map((row: Record<string, unknown>) => normalizeDocument(row)));
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unable to load documents.");
    } finally { setLoading(false); }
  }, [user?.organizationId]);

  useEffect(() => { void refresh(); }, [refresh]);
  return { documents, loading, error, refresh, setDocuments };
}

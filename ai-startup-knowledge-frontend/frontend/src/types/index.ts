export type AppRole = "ADMIN" | "CEO" | "MANAGER" | "EMPLOYEE" | string;

export interface AuthUser {
  userId: number;
  name: string;
  email: string;
  role: AppRole;
  organizationId: number;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  userId: number;
  name: string;
  email: string;
  role: AppRole;
  organizationId: number;
}

export interface DocumentRecord {
  id: string;
  documentId?: string;
  organizationId?: number;
  uploadedByUserId?: number;
  fileName: string;
  fileType?: string;
  fileSize?: number;
  documentType?: string;
  description?: string;
  s3Url?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface AskQuestionResponse {
  answer: string;
  question: string;
  organizationId: number;
  sources?: Array<{
    documentId?: string;
    fileName?: string;
    sourceFileName?: string;
    [key: string]: unknown;
  }>;
}

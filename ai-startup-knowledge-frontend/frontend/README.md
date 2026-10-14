# Knowbase — AI Startup Knowledge System Frontend

A React + TypeScript frontend for the AI Startup Knowledge System backend.

## Stack
- React + TypeScript + Vite
- React Router
- Axios
- Tailwind CSS
- Lucide icons
- React Markdown

## Run locally

```powershell
cd frontend
Copy-Item .env.example .env
npm install
npm run dev
```

Open http://localhost:5173.

Set `VITE_API_BASE_URL` to the API Gateway base URL. The default is `http://localhost:8080`.

## Backend integration notes
The frontend assumes:
- `POST /auth/login`
- `POST /auth/register` (invitation-token based)
- `POST /invitations`
- `POST /api/documents/upload`
- `GET /api/documents/organization/{organizationId}`
- `GET /api/documents/{documentId}`
- `DELETE /api/documents/{documentId}`
- `POST /api/ai/ask` with `{ "question": "..." }`

Confirm exact route predicates and response DTOs in your backend before production use. The documents list has a safe normalizer for common response shapes. If a service endpoint is not routed by the Gateway yet, route it there or change the API client.

## Security
- Do not commit `.env`.
- Tokens are held in session storage by default and cleared on logout.
- The backend must enforce authentication and organization-scoped authorization; frontend route guards are only a UX layer.

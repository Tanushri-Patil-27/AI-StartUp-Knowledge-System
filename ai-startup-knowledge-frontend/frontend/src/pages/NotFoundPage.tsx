import { ArrowLeft, Compass } from "lucide-react";
import { Link } from "react-router-dom";
import Brand from "../components/ui/Brand";

export default function NotFoundPage() {
  return <div className="not-found-page"><Brand /><div className="not-found-content"><div className="not-found-icon"><Compass size={30} /></div><p className="eyebrow">404 — PAGE NOT FOUND</p><h1>This page isn't in the map.</h1><p>The page may have moved, or the link may be incorrect.</p><Link to="/dashboard" className="button button-primary"><ArrowLeft size={16} /> Back to your workspace</Link></div></div>;
}

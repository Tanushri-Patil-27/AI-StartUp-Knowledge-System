import { BrainCircuit } from "lucide-react";

export default function Brand({ compact = false }: { compact?: boolean }) {
  return (
    <div className="brand">
      <div className="brand-mark"><BrainCircuit size={22} strokeWidth={2.1} /></div>
      {!compact && <div className="brand-copy"><strong>knowbase</strong><span>Startup knowledge, connected</span></div>}
    </div>
  );
}

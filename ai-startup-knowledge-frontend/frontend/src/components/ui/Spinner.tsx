export default function Spinner({ label = "Loading..." }: { label?: string }) {
  return <div className="loading-state"><span className="spinner" />{label}</div>;
}

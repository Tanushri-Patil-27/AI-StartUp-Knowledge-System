import { useEffect, useRef, useState, type FormEvent } from "react";
import { ArrowDown, ArrowRight, Bot, Check, Copy, FileText, RotateCcw, Send, Sparkles, UserRound } from "lucide-react";
import { useLocation } from "react-router-dom";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import PageHeader from "../../components/ui/PageHeader";
import { api, getErrorMessage } from "../../lib/api";
import { useAuth } from "../../context/AuthContext";
import type { AskQuestionResponse } from "../../types";

interface Message {
  id: string;
  role: "assistant" | "user";
  content: string;
  sources?: AskQuestionResponse["sources"];
  timestamp: Date;
}

const prompts = [
  { title: "Get the big picture", text: "Summarize the most important information in our organization's knowledge base." },
  { title: "Find a decision", text: "What decisions are documented in our organization’s knowledge base?" },
  { title: "Understand a process", text: "Explain the key processes described in our technical documents." },
];

export default function AskAiPage() {
  const location = useLocation();
  const { user } = useAuth();
  const [question, setQuestion] = useState("");
  const [messages, setMessages] = useState<Message[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [copiedId, setCopiedId] = useState("");
  const endRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const suggested = (location.state as { suggestedQuestion?: string } | null)?.suggestedQuestion;
    if (suggested) setQuestion(suggested);
  }, [location.state]);

  useEffect(() => { endRef.current?.scrollIntoView({ behavior: "smooth", block: "end" }); }, [messages, loading]);

  async function submitQuestion(event?: FormEvent, supplied?: string) {
    event?.preventDefault();
    const text = (supplied ?? question).trim();
    if (!text || loading) return;
    setError("");
    const userMessage: Message = { id: crypto.randomUUID(), role: "user", content: text, timestamp: new Date() };
    setMessages((current) => [...current, userMessage]);
    setQuestion("");
    setLoading(true);
    try {
      const { data } = await api.post<AskQuestionResponse>("/api/ai/ask", { question: text });
      setMessages((current) => [...current, {
        id: crypto.randomUUID(),
        role: "assistant",
        content: data.answer || "The assistant returned an empty answer.",
        sources: data.sources || [],
        timestamp: new Date(),
      }]);
    } catch (err) {
      setError(getErrorMessage(err, "The AI request failed. Check the RAG Service, Gemini key, Qdrant, and Gateway logs."));
    } finally { setLoading(false); }
  }

  async function copyAnswer(message: Message) {
    try { await navigator.clipboard.writeText(message.content); setCopiedId(message.id); window.setTimeout(() => setCopiedId(""), 1400); } catch { setError("Clipboard access was blocked by your browser."); }
  }

  function resetChat() { setMessages([]); setQuestion(""); setError(""); }

  return (
    <div className="page-enter ask-page">
      <PageHeader eyebrow="AI KNOWLEDGE ASSISTANT" title="Ask your knowledge." description="Get answers grounded in your organization's indexed documents." action={messages.length > 0 ? <button className="button button-secondary" onClick={resetChat}><RotateCcw size={15} /> New conversation</button> : undefined} />
      <div className="ai-workspace">
        <div className="ai-context-bar"><span className="ai-status-icon"><Sparkles size={16} /></span><div><strong>Organization knowledge assistant</strong><small>Searching knowledge for organization #{user?.organizationId ?? "—"}</small></div><span className="ai-online"><i /> AI ready</span></div>
        <div className={`chat-area ${messages.length === 0 ? "chat-area-empty" : ""}`}>
          {messages.length === 0 ? <div className="chat-welcome"><div className="welcome-orb"><Bot size={34} /></div><p className="eyebrow">YOUR KNOWLEDGE, IN CONVERSATION</p><h2>What would you like<br />to understand?</h2><p className="welcome-description">Ask a question about your organization's documents. I'll search for relevant context before answering.</p><div className="prompt-grid">{prompts.map((prompt, index) => <button key={prompt.title} className="prompt-card" onClick={() => { setQuestion(prompt.text); }}><span className={`prompt-index prompt-index-${index + 1}`}>0{index + 1}</span><strong>{prompt.title}</strong><span>{prompt.text}</span><ArrowRight size={15} /></button>)}</div></div> : <div className="message-list">{messages.map((message) => <article key={message.id} className={`message message-${message.role}`}><div className={`message-avatar ${message.role === "assistant" ? "message-avatar-ai" : ""}`}>{message.role === "assistant" ? <Bot size={18} /> : <UserRound size={17} />}</div><div className="message-main"><div className="message-meta"><strong>{message.role === "assistant" ? "Knowbase AI" : "You"}</strong><time>{message.timestamp.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })}</time></div>{message.role === "assistant" ? <div className="markdown-answer"><ReactMarkdown remarkPlugins={[remarkGfm]}>{message.content}</ReactMarkdown></div> : <p className="user-message-content">{message.content}</p>}{message.role === "assistant" && <div className="answer-actions"><button onClick={() => void copyAnswer(message)}>{copiedId === message.id ? <Check size={14} /> : <Copy size={14} />}{copiedId === message.id ? "Copied" : "Copy answer"}</button>{message.sources && message.sources.length > 0 && <span className="source-count"><FileText size={14} /> {message.sources.length} sources</span>}</div>}{message.role === "assistant" && message.sources && message.sources.length > 0 && <div className="answer-sources"><span className="sources-title">SOURCES</span>{message.sources.map((source, index) => <div key={`${source.documentId || source.fileName || index}`} className="source-chip"><FileText size={14} /><span>{source.fileName || source.sourceFileName || source.documentId || "Source document"}</span></div>)}</div>}</div></article>)}{loading && <div className="message message-assistant"><div className="message-avatar message-avatar-ai"><Bot size={18} /></div><div className="message-main"><div className="message-meta"><strong>Knowbase AI</strong><span>Searching your knowledge</span></div><div className="thinking-state"><span /><span /><span /><small>Finding relevant context and preparing an answer...</small></div></div></div>}<div ref={endRef} /></div> : <div className="chat-empty-spacer" />}
        </div>
        {error && <div className="alert alert-error ai-error">{error}<button className="text-link" onClick={() => void submitQuestion(undefined, messages.filter((m) => m.role === "user").at(-1)?.content || "")}>Try again</button></div>}
        <form className="chat-composer" onSubmit={(event) => void submitQuestion(event)}><div className="composer-input-wrap"><textarea value={question} onChange={(e) => setQuestion(e.target.value)} placeholder="Ask anything about your organization's knowledge..." rows={2} maxLength={2000} onKeyDown={(e) => { if (e.key === "Enter" && !e.shiftKey) { e.preventDefault(); void submitQuestion(); } }} /><div className="composer-bottom"><span><Sparkles size={13} /> Answers are based on retrieved context</span><span>{question.length}/2000</span></div></div><button className="send-button" disabled={!question.trim() || loading} aria-label="Send question"><Send size={18} /></button></form>
        <p className="chat-disclaimer"><ArrowDown size={12} /> AI-generated answers can be incomplete. Verify important information against the source documents.</p>
      </div>
    </div>
  );
}

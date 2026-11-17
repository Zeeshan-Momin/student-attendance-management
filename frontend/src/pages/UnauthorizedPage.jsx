import { Link } from 'react-router-dom';

export default function UnauthorizedPage() {
  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-100 p-6">
      <div className="card max-w-md p-8 text-center">
        <h1 className="mb-3 text-3xl font-bold text-slate-800">Access denied</h1>
        <p className="mb-6 text-slate-600">You do not have permission to view this page.</p>
        <Link to="/login" className="btn-primary inline-block">Return to Login</Link>
      </div>
    </div>
  );
}

import { useAuth } from '../context/AuthContext';
import { BookText, CalendarCheck2, TrendingUp } from 'lucide-react';

export default function StudentDashboardPage() {
  const { user, logout } = useAuth();

  return (
    <div className="min-h-screen bg-slate-100 p-6">
      <div className="mx-auto max-w-6xl">
        <div className="mb-6 flex items-center justify-between rounded-2xl bg-white p-4 shadow-card">
          <div>
            <p className="text-sm uppercase tracking-[0.2em] text-slate-500">Student portal</p>
            <h1 className="text-2xl font-bold text-slate-800">Welcome, {user?.username || 'Student'}</h1>
          </div>
          <button onClick={logout} className="btn-secondary">Logout</button>
        </div>

        <div className="grid gap-4 md:grid-cols-3">
          <div className="card p-5">
            <div className="mb-3 flex items-center gap-3">
              <div className="rounded-xl bg-blue-100 p-3 text-blue-700"><TrendingUp size={20} /></div>
              <div>
                <p className="text-sm text-slate-500">Attendance %</p>
                <h3 className="text-xl font-bold">92%</h3>
              </div>
            </div>
          </div>
          <div className="card p-5">
            <div className="mb-3 flex items-center gap-3">
              <div className="rounded-xl bg-emerald-100 p-3 text-emerald-700"><CalendarCheck2 size={20} /></div>
              <div>
                <p className="text-sm text-slate-500">Present days</p>
                <h3 className="text-xl font-bold">48</h3>
              </div>
            </div>
          </div>
          <div className="card p-5">
            <div className="mb-3 flex items-center gap-3">
              <div className="rounded-xl bg-violet-100 p-3 text-violet-700"><BookText size={20} /></div>
              <div>
                <p className="text-sm text-slate-500">Classes attended</p>
                <h3 className="text-xl font-bold">8</h3>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

import { BarChart3, BookOpen, GraduationCap, Users } from 'lucide-react';
import { Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import { useAuth } from '../context/AuthContext';

const data = [
  { name: 'Jan', present: 68, absent: 15 },
  { name: 'Feb', present: 72, absent: 12 },
  { name: 'Mar', present: 80, absent: 10 },
  { name: 'Apr', present: 84, absent: 9 },
  { name: 'May', present: 89, absent: 7 }
];

export default function AdminDashboardPage() {
  const { user, logout } = useAuth();

  const stats = [
    { label: 'Total Students', value: '1,240', icon: GraduationCap },
    { label: 'Total Faculty', value: '88', icon: Users },
    { label: 'Total Classes', value: '56', icon: BookOpen },
    { label: 'Attendance Rate', value: '89%', icon: BarChart3 }
  ];

  return (
    <div className="min-h-screen bg-slate-100 p-6">
      <div className="mx-auto max-w-7xl">
        <div className="mb-6 flex items-center justify-between rounded-2xl bg-white p-4 shadow-card">
          <div>
            <p className="text-sm uppercase tracking-[0.2em] text-slate-500">Admin dashboard</p>
            <h1 className="text-2xl font-bold text-slate-800">Welcome, {user?.username || 'Administrator'}</h1>
          </div>
          <button onClick={logout} className="btn-secondary">Logout</button>
        </div>

        <div className="mb-6 grid gap-4 md:grid-cols-4">
          {stats.map(({ label, value, icon: Icon }) => (
            <div key={label} className="card p-5">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-slate-500">{label}</p>
                  <h2 className="mt-2 text-2xl font-bold text-slate-800">{value}</h2>
                </div>
                <div className="rounded-xl bg-blue-100 p-3 text-blue-700">
                  <Icon size={22} />
                </div>
              </div>
            </div>
          ))}
        </div>

        <div className="card p-5">
          <h3 className="mb-4 text-lg font-semibold text-slate-800">Monthly attendance trend</h3>
          <div style={{ width: '100%', height: 260 }}>
            <ResponsiveContainer>
              <BarChart data={data}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Bar dataKey="present" fill="#2563eb" radius={[6, 6, 0, 0]} />
                <Bar dataKey="absent" fill="#94a3b8" radius={[6, 6, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>
    </div>
  );
}

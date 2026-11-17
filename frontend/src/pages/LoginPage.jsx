import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { GraduationCap, LockKeyhole } from 'lucide-react';
import { toast } from 'react-toastify';
import { useAuth } from '../context/AuthContext';

export default function LoginPage() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [form, setForm] = useState({ username: 'admin', password: 'Admin@123' });
  const [loading, setLoading] = useState(false);

  const handleChange = (event) => {
    setForm((prev) => ({ ...prev, [event.target.name]: event.target.value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);

    try {
      await login(form);
      const user = JSON.parse(localStorage.getItem('user') || '{}');
      const role = user.role || 'ADMIN';
      if (role === 'ADMIN') navigate('/admin');
      else if (role === 'FACULTY') navigate('/faculty');
      else navigate('/student');
    } catch (error) {
      toast.error(error?.response?.data?.message || 'Login failed. Please check your credentials.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-blue-100 via-slate-50 to-slate-200 p-6">
      <div className="grid w-full max-w-5xl overflow-hidden rounded-3xl bg-white shadow-card lg:grid-cols-2">
        <div className="flex flex-col justify-center bg-blue-700 p-10 text-white">
          <div className="mb-8 flex items-center gap-3">
            <div className="rounded-xl bg-white/10 p-3">
              <GraduationCap size={28} />
            </div>
            <div>
              <p className="text-xs uppercase tracking-[0.2em] text-blue-100">Campus portal</p>
              <h1 className="text-3xl font-bold">AttendEase</h1>
            </div>
          </div>
          <h2 className="mb-4 text-4xl font-bold">Student Attendance Management</h2>
          <p className="max-w-md text-blue-100">
            Track attendance, monitor class performance, and keep academic records accurate in real time.
          </p>
          <div className="mt-8 space-y-3 text-sm text-blue-50">
            <div>• Role-based access for admins, faculty and students.</div>
            <div>• Secure JWT authentication and protected routes.</div>
            <div>• Attendance reporting and dashboard insights.</div>
          </div>
        </div>

        <div className="p-8 md:p-12">
          <div className="mb-8 flex items-center gap-3 text-slate-700">
            <div className="rounded-xl bg-blue-100 p-3 text-blue-700">
              <LockKeyhole size={20} />
            </div>
            <div>
              <p className="text-xs uppercase tracking-[0.2em] text-slate-500">Welcome back</p>
              <h3 className="text-2xl font-semibold">Sign in</h3>
            </div>
          </div>

          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">Username</label>
              <input className="input" name="username" value={form.username} onChange={handleChange} placeholder="admin" required />
            </div>
            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">Password</label>
              <input className="input" type="password" name="password" value={form.password} onChange={handleChange} placeholder="••••••••" required />
            </div>
            <button type="submit" disabled={loading} className="btn-primary w-full disabled:opacity-60">
              {loading ? 'Signing in...' : 'Login'}
            </button>
          </form>

          <div className="mt-6 rounded-xl border border-slate-200 bg-slate-50 p-4 text-sm text-slate-600">
            <p className="font-medium text-slate-700">Demo accounts</p>
            <ul className="mt-2 space-y-1">
              <li>Admin: admin / Admin@123</li>
              <li>Faculty: faculty1 / Faculty@123</li>
              <li>Student: student1 / Student@123</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
}

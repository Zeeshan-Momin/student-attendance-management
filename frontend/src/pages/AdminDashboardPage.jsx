import { useEffect, useMemo, useState } from 'react';
import { BarChart3, BookOpen, GraduationCap, Users } from 'lucide-react';
import { Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import api from '../services/api';
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
  const [students, setStudents] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [departmentFilter, setDepartmentFilter] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchStudents = async () => {
      try {
        setLoading(true);
        const response = await api.get('/students');
        setStudents(response.data || []);
        setError('');
      } catch (err) {
        setError('Unable to load student records right now.');
      } finally {
        setLoading(false);
      }
    };

    fetchStudents();
  }, []);

  const departments = useMemo(
    () => [...new Set(students.map((student) => student.department).filter(Boolean))].sort(),
    [students]
  );

  const filteredStudents = useMemo(() => {
    const normalizedQuery = searchTerm.trim().toLowerCase();

    return students.filter((student) => {
      const searchableText = [
        student.fullName,
        student.email,
        student.rollNumber,
        student.department
      ]
        .filter(Boolean)
        .join(' ')
        .toLowerCase();

      const matchesSearch = !normalizedQuery || searchableText.includes(normalizedQuery);
      const matchesDepartment = !departmentFilter || student.department === departmentFilter;
      const matchesStatus = !statusFilter || (student.accountStatus || '').toLowerCase() === statusFilter.toLowerCase();

      return matchesSearch && matchesDepartment && matchesStatus;
    });
  }, [students, searchTerm, departmentFilter, statusFilter]);

  const clearFilters = () => {
    setSearchTerm('');
    setDepartmentFilter('');
    setStatusFilter('');
  };

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

        <div className="mt-6 card p-5">
          <div className="mb-4 flex flex-col gap-2 md:flex-row md:items-center md:justify-between">
            <div>
              <p className="text-sm uppercase tracking-[0.2em] text-slate-500">Student management</p>
              <h3 className="text-lg font-semibold text-slate-800">Students</h3>
            </div>
            <p className="text-sm text-slate-500">{filteredStudents.length} of {students.length} students</p>
          </div>

          <div className="mb-4 grid gap-3 md:grid-cols-[minmax(0,2fr)_180px_180px_auto]">
            <input
              type="text"
              value={searchTerm}
              onChange={(event) => setSearchTerm(event.target.value)}
              className="input"
              placeholder="Search by name, email, roll number, or department"
              aria-label="Search students"
            />

            <select
              value={departmentFilter}
              onChange={(event) => setDepartmentFilter(event.target.value)}
              className="input"
              aria-label="Filter students by department"
            >
              <option value="">All departments</option>
              {departments.map((department) => (
                <option key={department} value={department}>{department}</option>
              ))}
            </select>

            <select
              value={statusFilter}
              onChange={(event) => setStatusFilter(event.target.value)}
              className="input"
              aria-label="Filter students by account status"
            >
              <option value="">All statuses</option>
              <option value="ACTIVE">Active</option>
              <option value="INACTIVE">Inactive</option>
              <option value="SUSPENDED">Suspended</option>
            </select>

            <button type="button" onClick={clearFilters} className="btn-secondary">
              Clear filters
            </button>
          </div>

          {loading ? (
            <div className="rounded-lg border border-slate-200 bg-slate-50 p-4 text-sm text-slate-600">
              Loading student records...
            </div>
          ) : error ? (
            <div className="rounded-lg border border-red-200 bg-red-50 p-4 text-sm text-red-700">
              {error}
            </div>
          ) : filteredStudents.length === 0 ? (
            <div className="rounded-lg border border-dashed border-slate-300 bg-slate-50 p-6 text-center text-sm text-slate-600">
              No students match the current search and filters.
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full border-separate border-spacing-0 text-left text-sm text-slate-700">
                <thead>
                  <tr>
                    <th className="border-b border-slate-200 bg-slate-50 px-3 py-2 font-semibold">Name</th>
                    <th className="border-b border-slate-200 bg-slate-50 px-3 py-2 font-semibold">Roll No.</th>
                    <th className="border-b border-slate-200 bg-slate-50 px-3 py-2 font-semibold">Email</th>
                    <th className="border-b border-slate-200 bg-slate-50 px-3 py-2 font-semibold">Department</th>
                    <th className="border-b border-slate-200 bg-slate-50 px-3 py-2 font-semibold">Status</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredStudents.map((student) => (
                    <tr key={student.id} className="odd:bg-white even:bg-slate-50">
                      <td className="border-b border-slate-200 px-3 py-3 font-medium text-slate-800">{student.fullName}</td>
                      <td className="border-b border-slate-200 px-3 py-3">{student.rollNumber}</td>
                      <td className="border-b border-slate-200 px-3 py-3">{student.email}</td>
                      <td className="border-b border-slate-200 px-3 py-3">{student.department || '—'}</td>
                      <td className="border-b border-slate-200 px-3 py-3">
                        <span className="inline-flex rounded-full px-2.5 py-1 text-xs font-medium
                          {student.accountStatus === 'ACTIVE' ? 'bg-emerald-100 text-emerald-700' :
                            student.accountStatus === 'SUSPENDED' ? 'bg-amber-100 text-amber-700' :
                            'bg-slate-200 text-slate-700'}">
                          {student.accountStatus || 'ACTIVE'}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

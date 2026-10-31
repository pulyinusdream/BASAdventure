import { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, Link, useNavigate, useParams } from 'react-router-dom';
import { supabase } from './supabase';
import { UserProgress, GameRecord, ExamRecord } from './types';
import { 
  Users, 
  BookOpen, 
  Star, 
  BarChart3, 
  LogOut, 
  ChevronRight, 
  Search,
  RefreshCw,
  Trophy,
  History
} from 'lucide-react';

// --- Components ---

const StatCard = ({ title, value, icon: Icon, color }: any) => (
  <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex items-center gap-4">
    <div className={`p-3 rounded-xl ${color}`}>
      <Icon className="w-6 h-6 text-white" />
    </div>
    <div>
      <p className="text-sm text-gray-500 font-medium">{title}</p>
      <h3 className="text-2xl font-bold text-gray-900">{value}</h3>
    </div>
  </div>
);

const StudentAvatar = ({ student, className = "w-12 h-12", fontSize = "text-2xl" }: { student: any; className?: string; fontSize?: string }) => {
  const [error, setError] = useState(false);
  const userProgress = student?.user_progress || student;
  const image = userProgress?.profile_image;
  const avatar = userProgress?.selected_avatar || '👤';

  if (image && !error && (image.startsWith('data:') || image.startsWith('http'))) {
    return (
      <div className={`${className} bg-gray-50 rounded-xl flex items-center justify-center overflow-hidden border border-gray-100 shadow-inner`}>
        <img 
          src={image} 
          alt="Profile" 
          className="w-full h-full object-cover"
          onError={() => setError(true)}
        />
      </div>
    );
  }

  return (
    <div className={`${className} bg-gray-50 rounded-xl flex items-center justify-center ${fontSize} border border-gray-100 shadow-inner`}>
      {avatar}
    </div>
  );
};

// --- Pages ---

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    if (username === 'teacher' && password === 'monitoring') {
      localStorage.setItem('isTeacherAuthenticated', 'true');
      window.location.reload(); // Refresh to update auth state
    } else {
      setError('Maling username o password!');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#f8fafc] p-0">
      <div className="flex w-full max-w-5xl h-[600px] bg-white rounded-[2rem] shadow-2xl overflow-hidden border border-gray-100">
        {/* Left Side: Illustration */}
        <div className="hidden lg:flex lg:w-1/2 bg-gradient-to-br from-indigo-600 to-purple-700 p-12 flex-col justify-between relative overflow-hidden">
          <div className="absolute top-[-10%] right-[-10%] w-64 h-64 bg-white/10 rounded-full blur-3xl"></div>
          <div className="absolute bottom-[-10%] left-[-10%] w-64 h-64 bg-white/10 rounded-full blur-3xl"></div>
          
          <div className="relative z-10">
            <div className="w-12 h-12 bg-white/20 backdrop-blur-md rounded-xl flex items-center justify-center mb-6">
              <BarChart3 className="text-white w-7 h-7" />
            </div>
            <h2 className="text-4xl font-black text-white leading-tight">Monitor Student<br/>Success in Real-time.</h2>
            <p className="text-indigo-100 mt-4 text-lg">Access detailed insights and tracking for your class performance.</p>
          </div>
          
          <div className="relative z-10 text-white/60 text-sm font-medium">
            BAS Adventure © 2024 Teacher Portal
          </div>
        </div>

        {/* Right Side: Login Form */}
        <div className="w-full lg:w-1/2 p-12 flex flex-col justify-center">
          <div className="mb-8">
            <h1 className="text-3xl font-black text-gray-900 mb-2">Welcome Back</h1>
            <p className="text-gray-500">Enter your credentials to access the dashboard</p>
          </div>
          
          <form onSubmit={handleLogin} className="space-y-5">
            <div>
              <label className="block text-sm font-bold text-gray-700 mb-2">Username</label>
              <input 
                type="text" 
                className="w-full px-5 py-3.5 rounded-2xl border border-gray-200 focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 outline-none transition-all text-gray-900 bg-gray-50"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="teacher"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-bold text-gray-700 mb-2">Password</label>
              <input 
                type="password" 
                className="w-full px-5 py-3.5 rounded-2xl border border-gray-200 focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 outline-none transition-all text-gray-900 bg-gray-50"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                required
              />
            </div>
            
            {error && (
              <div className="bg-red-50 text-red-600 p-3 rounded-xl text-sm font-bold text-center border border-red-100 animate-shake">
                {error}
              </div>
            )}
            
            <button 
              type="submit"
              className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-black py-4 rounded-2xl shadow-xl shadow-indigo-200 transition-all active:scale-[0.98] mt-4"
            >
              Sign In
            </button>
          </form>
          
          <div className="mt-8 pt-8 border-t border-gray-100 text-center">
            <p className="text-sm text-gray-400">Difficulty logging in? Contact IT Support</p>
          </div>
        </div>
      </div>
    </div>
  );
};

const DashboardLayout = ({ children }: { children: React.ReactNode }) => {
  const navigate = useNavigate();
  const location = window.location.pathname;

  const NavItem = ({ icon: Icon, label, path, active }: any) => (
    <Link 
      to={path} 
      className={`flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 group ${
        active 
        ? 'bg-indigo-50 text-indigo-700' 
        : 'text-gray-500 hover:bg-gray-50 hover:text-gray-900'
      }`}
    >
      <Icon className={`w-5 h-5 ${active ? 'text-indigo-600' : 'text-gray-400 group-hover:text-gray-600'}`} />
      <span className="font-bold">{label}</span>
    </Link>
  );

  return (
    <div className="flex min-h-screen bg-[#f8fafc]">
      {/* Sidebar */}
      <aside className="w-72 bg-white border-r border-gray-200 flex flex-col fixed inset-y-0 z-20">
        <div className="p-8">
          <div className="flex items-center gap-3 mb-10">
            <div className="w-10 h-10 bg-indigo-600 rounded-2xl flex items-center justify-center shadow-lg shadow-indigo-200">
              <BarChart3 className="text-white w-6 h-6" />
            </div>
            <span className="text-xl font-black tracking-tight text-gray-900">BAS Adventure</span>
          </div>
          
          <nav className="space-y-1">
            <NavItem icon={Users} label="Dashboard" path="/dashboard" active={location === '/dashboard'} />
            <NavItem icon={BarChart3} label="Needs Help" path="/needs-help" active={location === '/needs-help'} />
            <NavItem icon={Star} label="Star Rewards" path="/rewards" active={location === '/rewards'} />
            <NavItem icon={History} label="Global Logs" path="/logs" active={location === '/logs'} />
          </nav>
        </div>
        
        <div className="mt-auto p-6 border-t border-gray-100">
          <button 
            onClick={() => { localStorage.removeItem('isTeacherAuthenticated'); window.location.href = '/'; }}
            className="flex items-center gap-3 w-full px-4 py-3 rounded-xl text-gray-500 hover:bg-red-50 hover:text-red-600 transition-all font-bold group"
          >
            <LogOut className="w-5 h-5 text-gray-400 group-hover:text-red-500" />
            Logout
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 ml-72 p-10 w-full">
        {children}
      </main>
    </div>
  );
};

const NeedsHelp = () => {
  const [strugglingStudents, setStrugglingStudents] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStruggling = async () => {
      setLoading(true);
      // Fetch exams with low scores (less than 50%)
      const { data: exams } = await supabase
        .from('exam_records')
        .select('*, user_progress(first_name, surname, selected_avatar, grade_level, profile_image)')
        .order('timestamp', { ascending: false });

      if (exams) {
        // Filter for scores below 50%
        const lowScores = exams.filter(e => e.score < (e.total_items / 2));
        setStrugglingStudents(lowScores);
      }
      setLoading(false);
    };
    fetchStruggling();
  }, []);

  return (
    <DashboardLayout>
      <header className="mb-10">
        <h1 className="text-4xl font-black text-gray-900 tracking-tight">Needs Help</h1>
        <p className="text-gray-500 mt-1 font-medium text-lg">Students who failed their recent exams (Score &lt; 50%).</p>
      </header>

      <div className="grid grid-cols-1 gap-6">
        {loading ? (
          <div className="p-20 text-center"><RefreshCw className="w-8 h-8 text-indigo-600 animate-spin mx-auto" /></div>
        ) : strugglingStudents.length === 0 ? (
          <div className="bg-emerald-50 border border-emerald-100 p-12 rounded-[2.5rem] text-center">
            <Trophy className="w-12 h-12 text-emerald-500 mx-auto mb-4" />
            <h3 className="text-xl font-black text-emerald-900">Great Job!</h3>
            <p className="text-emerald-600 font-medium">No students currently have failing scores.</p>
          </div>
        ) : strugglingStudents.map((record, i) => (
          <div key={i} className="bg-white p-8 rounded-[2.5rem] border border-red-100 shadow-sm flex items-center justify-between hover:shadow-md transition-all">
            <div className="flex items-center gap-6">
              <StudentAvatar student={record} className="w-16 h-16" fontSize="text-4xl" />
              <div>
                <p className="text-xl font-black text-gray-900">
                  {record.user_progress?.surname}, {record.user_progress?.first_name}
                </p>
                <div className="flex items-center gap-2 mt-1">
                  <span className="px-3 py-0.5 bg-gray-100 text-gray-500 text-[10px] font-black rounded-full uppercase">{record.user_progress?.grade_level}</span>
                  <span className="text-red-500 font-bold text-sm">Failed: {record.exam_name}</span>
                </div>
              </div>
            </div>
            <div className="flex items-center gap-8">
              <div className="text-right">
                <p className="text-3xl font-black text-red-600">{record.score} / {record.total_items}</p>
                <p className="text-xs font-black text-red-400 tracking-widest">FAILING SCORE</p>
              </div>
              <Link 
                to={`/student/${record.student_id}`}
                className="px-6 py-3 bg-indigo-600 text-white font-black rounded-2xl shadow-lg shadow-indigo-100 hover:bg-indigo-700 transition-all active:scale-95"
              >
                VIEW PROFILE
              </Link>
            </div>
          </div>
        ))}
      </div>
    </DashboardLayout>
  );
};

const StarRewards = () => {
  const [students, setStudents] = useState<UserProgress[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchLeaderboard = async () => {
      setLoading(true);
      const { data } = await supabase.from('user_progress').select('*').order('stars', { ascending: false });
      if (data) setStudents(data);
      setLoading(false);
    };
    fetchLeaderboard();
  }, []);

  return (
    <DashboardLayout>
      <header className="mb-10">
        <h1 className="text-4xl font-black text-gray-900 tracking-tight">Star Rewards</h1>
        <p className="text-gray-500 mt-1 font-medium text-lg">Student leaderboard and performance rankings.</p>
      </header>

      <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden max-w-4xl">
        <div className="p-8 border-b border-gray-50 bg-amber-50/50 flex items-center gap-4">
          <Trophy className="w-8 h-8 text-amber-500" />
          <h2 className="text-xl font-black text-gray-900">Star Leaderboard</h2>
        </div>
        <div className="divide-y divide-gray-50">
          {loading ? (
            <div className="p-20 text-center"><RefreshCw className="w-8 h-8 text-indigo-600 animate-spin mx-auto" /></div>
          ) : students.map((student, index) => (
            <div key={student.student_id} className="p-6 flex items-center justify-between hover:bg-gray-50 transition-colors">
              <div className="flex items-center gap-6">
                <div className={`w-10 h-10 rounded-xl flex items-center justify-center font-black text-lg ${
                  index === 0 ? 'bg-amber-100 text-amber-700' : 
                  index === 1 ? 'bg-gray-100 text-gray-700' : 
                  index === 2 ? 'bg-orange-100 text-orange-700' : 'text-gray-300'
                }`}>
                  {index + 1}
                </div>
                <StudentAvatar student={student} />
                <div>
                  <p className="font-black text-gray-900">{student.surname}, {student.first_name}</p>
                  <p className="text-xs font-bold text-gray-400">Level {student.unlocked_level}</p>
                </div>
              </div>
              <div className="flex items-center gap-2 bg-amber-50 px-4 py-2 rounded-2xl border border-amber-100">
                <Star className="w-5 h-5 text-amber-500 fill-amber-500" />
                <span className="text-xl font-black text-amber-700">{student.stars}</span>
              </div>
            </div>
          ))}
        </div>
      </div>
    </DashboardLayout>
  );
};

const GlobalLogs = () => {
  const [logs, setLogs] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchLogs();
  }, []);

  const fetchLogs = async () => {
    setLoading(true);
    const [games, exams] = await Promise.all([
      supabase.from('game_records').select('*, user_progress(first_name, surname, selected_avatar, profile_image)').order('played_at', { ascending: false }).limit(30),
      supabase.from('exam_records').select('*, user_progress(first_name, surname, selected_avatar, profile_image)').order('timestamp', { ascending: false }).limit(30)
    ]);

    const combined = [
      ...(games.data || []).map(g => ({ ...g, type: 'game', time: g.played_at })),
      ...(exams.data || []).map(e => ({ ...e, type: 'exam', time: new Date(e.timestamp).toISOString() }))
    ].sort((a, b) => new Date(b.time).getTime() - new Date(a.time).getTime()).slice(0, 50);

    setLogs(combined);
    setLoading(false);
  };

  return (
    <DashboardLayout>
      <header className="mb-10 flex items-center justify-between">
        <div>
          <h1 className="text-4xl font-black text-gray-900 tracking-tight">Global Logs</h1>
          <p className="text-gray-500 mt-1 font-medium text-lg">Real-time activity feed from all students.</p>
        </div>
        <button onClick={fetchLogs} className="p-4 bg-white border border-gray-200 rounded-2xl hover:bg-gray-50 shadow-sm transition-all active:scale-95">
          <RefreshCw className={`w-6 h-6 text-indigo-600 ${loading ? 'animate-spin' : ''}`} />
        </button>
      </header>

      <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
        <div className="divide-y divide-gray-50">
          {loading ? (
            <div className="p-20 text-center"><RefreshCw className="w-8 h-8 text-indigo-600 animate-spin mx-auto" /></div>
          ) : logs.length === 0 ? (
            <div className="p-20 text-center text-gray-400 font-bold italic">No activity logs found</div>
          ) : logs.map((log, i) => (
            <div key={i} className="p-8 flex items-center justify-between hover:bg-gray-50/50 transition-colors">
              <div className="flex items-center gap-6">
                <StudentAvatar student={log} className="w-14 h-14" fontSize="text-3xl" />
                <div>
                  <p className="text-lg font-black text-gray-900">
                    <span className="text-indigo-600">{log.user_progress?.first_name} {log.user_progress?.surname}</span>
                    <span className="text-gray-400 font-medium mx-2">completed a</span>
                    <span className={log.type === 'game' ? 'text-blue-600' : 'text-emerald-600'}>{log.type === 'game' ? 'Game' : 'Exam'}</span>
                  </p>
                  <p className="text-sm font-bold text-gray-400 mt-1 uppercase tracking-tight">
                    {log.game_name || log.exam_name} • {new Date(log.time).toLocaleString()}
                  </p>
                </div>
              </div>
              <div className="text-right">
                <div className="inline-flex items-center gap-3 bg-gray-50 px-4 py-2 rounded-2xl border border-gray-100">
                  <span className="text-xl font-black text-gray-900">{log.score}/{log.total_items}</span>
                  {log.type === 'game' && (
                    <div className="flex items-center gap-1 text-amber-500 font-black">
                      <Star className="w-4 h-4 fill-amber-500" /> +{log.stars_earned}
                    </div>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </DashboardLayout>
  );
};

const Dashboard = () => {
  const [students, setStudents] = useState<UserProgress[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [selectedGrade, setSelectedGrade] = useState('All');
  const navigate = useNavigate();

  const gradeLevels = ['All', 'Kinder', 'Grade 1', 'Grade 2', 'Grade 3', 'Grade 4', 'Grade 5', 'Grade 6'];

  useEffect(() => {
    fetchStudents();
  }, []);

  const fetchStudents = async () => {
    setLoading(true);
    const { data, error } = await supabase
      .from('user_progress')
      .select('*')
      .order('surname', { ascending: true });
    
    if (data) setStudents(data);
    setLoading(false);
  };

  const filteredStudents = students.filter(s => {
    const matchesSearch = `${s.first_name} ${s.surname}`.toLowerCase().includes(search.toLowerCase());
    const matchesGrade = selectedGrade === 'All' || s.grade_level === selectedGrade;
    return matchesSearch && matchesGrade;
  });

  return (
    <DashboardLayout>
      <header className="mb-10 flex flex-col md:flex-row md:items-center justify-between gap-6">
        <div>
          <h1 className="text-4xl font-black text-gray-900 tracking-tight">Overview</h1>
          <p className="text-gray-500 mt-1 font-medium text-lg">Monitor student performance and class trends across all grades.</p>
        </div>
        
        <div className="flex items-center gap-3">
          <div className="relative group">
            <Search className="w-5 h-5 text-gray-400 absolute left-4 top-1/2 -translate-y-1/2 group-focus-within:text-indigo-500 transition-colors" />
            <input 
              type="text" 
              placeholder="Search students..." 
              className="pl-12 pr-6 py-3.5 bg-white border border-gray-200 rounded-2xl focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 outline-none w-full md:w-80 shadow-sm transition-all"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>
          <button 
            onClick={fetchStudents}
            className="p-3.5 bg-white border border-gray-200 rounded-2xl hover:bg-gray-50 transition-all shadow-sm active:scale-95"
          >
            <RefreshCw className={`w-5 h-5 text-gray-500 ${loading ? 'animate-spin' : ''}`} />
          </button>
        </div>
      </header>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8 mb-10">
        <StatCard title="Total Students" value={students.length} icon={Users} color="bg-blue-600" />
        <StatCard title="Average Stars" value={students.length ? Math.round(students.reduce((a,b) => a + b.stars, 0) / students.length) : 0} icon={Star} color="bg-amber-500" />
        <StatCard title="Highest Level" value={students.length ? Math.max(...students.map(s => s.unlocked_level)) : 1} icon={Trophy} color="bg-indigo-600" />
        <StatCard title="Target Levels" value="Kinder - Grade 6" icon={BookOpen} color="bg-emerald-600" />
      </div>

      {/* Grade Level Selection */}
      <div className="mb-10 bg-white p-6 rounded-[2rem] border border-gray-100 shadow-sm">
        <h3 className="text-sm font-black text-gray-400 uppercase tracking-widest mb-4">Select Grade Level</h3>
        <div className="flex flex-wrap gap-2">
          {gradeLevels.map(grade => (
            <button
              key={grade}
              onClick={() => setSelectedGrade(grade)}
              className={`px-6 py-3 rounded-2xl font-bold transition-all duration-200 ${
                selectedGrade === grade
                ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-200 scale-105'
                : 'bg-gray-50 text-gray-500 hover:bg-gray-100'
              }`}
            >
              {grade}
            </button>
          ))}
        </div>
      </div>

      {/* Students Table */}
      <div className="bg-white rounded-[2rem] shadow-sm border border-gray-100 overflow-hidden">
        <div className="p-8 border-b border-gray-50 flex items-center justify-between">
          <h3 className="text-xl font-black text-gray-900">
            {selectedGrade === 'All' ? 'All Students' : `${selectedGrade} Students`} 
            <span className="ml-3 text-sm text-gray-400 font-bold">({filteredStudents.length})</span>
          </h3>
          <div className="flex gap-2">
            <span className="px-3 py-1 bg-indigo-50 text-indigo-700 text-xs font-bold rounded-full">LIVE DATA</span>
          </div>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="bg-gray-50/50 border-b border-gray-100">
                <th className="px-8 py-5 text-xs font-black text-gray-400 uppercase tracking-[0.2em]">Student Name</th>
                <th className="px-8 py-5 text-xs font-black text-gray-400 uppercase tracking-[0.2em]">Grade Level</th>
                <th className="px-8 py-5 text-xs font-black text-gray-400 uppercase tracking-[0.2em]">Learning Progress</th>
                <th className="px-8 py-5 text-xs font-black text-gray-400 uppercase tracking-[0.2em]">Performance Stats</th>
                <th className="px-8 py-5"></th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-50">
              {loading ? (
                Array.from({ length: 5 }).map((_, i) => (
                  <tr key={i} className="animate-pulse">
                    <td colSpan={5} className="px-8 py-6 h-20">
                      <div className="h-10 bg-gray-100 rounded-xl w-full"></div>
                    </td>
                  </tr>
                ))
              ) : filteredStudents.length === 0 ? (
                <tr>
                  <td colSpan={5} className="px-8 py-20 text-center">
                    <div className="flex flex-col items-center gap-3">
                      <Search className="w-12 h-12 text-gray-200" />
                      <p className="text-gray-400 font-bold italic">No students found for this selection</p>
                    </div>
                  </td>
                </tr>
              ) : filteredStudents.map((student) => (
                <tr 
                  key={student.student_id} 
                  className="group hover:bg-gray-50/80 cursor-pointer transition-all"
                  onClick={() => navigate(`/student/${student.student_id}`)}
                >
                  <td className="px-8 py-6">
                    <div className="flex items-center gap-4">
                      <StudentAvatar student={student} className="w-14 h-14" fontSize="text-3xl" />
                      <div>
                        <p className="font-black text-gray-900 group-hover:text-indigo-700 transition-colors">{student.surname}, {student.first_name}</p>
                        <p className="text-xs font-bold text-gray-400 mt-0.5">ID: {student.student_id.slice(0,8).toUpperCase()}</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-8 py-6">
                    <span className="inline-flex items-center px-4 py-1.5 rounded-xl text-xs font-black bg-blue-50 text-blue-700 border border-blue-100">
                      {student.grade_level.toUpperCase()}
                    </span>
                  </td>
                  <td className="px-8 py-6">
                    <div className="flex flex-col gap-2">
                      <div className="flex justify-between items-end">
                        <p className="text-xs font-black text-gray-900 uppercase">Level {student.unlocked_level}</p>
                        <p className="text-[10px] font-bold text-gray-400">{Math.min(100, Math.round((student.unlocked_level / 10) * 100))}%</p>
                      </div>
                      <div className="w-40 h-2 bg-gray-100 rounded-full overflow-hidden shadow-inner">
                        <div className="h-full bg-indigo-500 rounded-full shadow-sm shadow-indigo-200" style={{ width: `${(student.unlocked_level / 10) * 100}%` }}></div>
                      </div>
                    </div>
                  </td>
                  <td className="px-8 py-6">
                    <div className="flex items-center gap-6">
                      <div className="flex items-center gap-2">
                        <div className="w-8 h-8 bg-amber-50 rounded-lg flex items-center justify-center">
                          <Star className="w-4 h-4 text-amber-500 fill-amber-500" />
                        </div>
                        <span className="font-black text-gray-900">{student.stars}</span>
                      </div>
                      <div className="flex items-center gap-2">
                        <div className="w-8 h-8 bg-purple-50 rounded-lg flex items-center justify-center">
                          <BookOpen className="w-4 h-4 text-purple-600" />
                        </div>
                        <span className="font-black text-gray-900">{student.mastered_letters?.split(',').filter(Boolean).length || 0}</span>
                      </div>
                    </div>
                  </td>
                  <td className="px-8 py-6 text-right">
                    <div className="inline-flex items-center justify-center w-10 h-10 rounded-xl bg-gray-50 group-hover:bg-indigo-600 group-hover:text-white transition-all text-gray-300">
                      <ChevronRight className="w-5 h-5" />
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </DashboardLayout>
  );
};

const StudentDetail = () => {
  const { id } = useParams();
  const [student, setStudent] = useState<UserProgress | null>(null);
  const [games, setGames] = useState<GameRecord[]>([]);
  const [exams, setExams] = useState<ExamRecord[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchDetail();
  }, [id]);

  const fetchDetail = async () => {
    setLoading(true);
    const [studentRes, gamesRes, examsRes] = await Promise.all([
      supabase.from('user_progress').select('*').eq('student_id', id).single(),
      supabase.from('game_records').select('*').eq('student_id', id).order('played_at', { ascending: false }),
      supabase.from('exam_records').select('*').eq('student_id', id).order('timestamp', { ascending: false })
    ]);

    if (studentRes.data) setStudent(studentRes.data);
    if (gamesRes.data) setGames(gamesRes.data);
    if (examsRes.data) setExams(examsRes.data);
    setLoading(false);
  };

  if (loading) return (
    <div className="min-h-screen flex items-center justify-center bg-[#f8fafc]">
      <RefreshCw className="w-10 h-10 text-indigo-600 animate-spin" />
    </div>
  );

  return (
    <DashboardLayout>
      <button 
        onClick={() => navigate('/dashboard')} 
        className="flex items-center gap-2 text-gray-500 hover:text-indigo-600 font-black text-sm mb-8 transition-colors group"
      >
        <ChevronRight className="w-5 h-5 rotate-180 group-hover:-translate-x-1 transition-transform" />
        BACK TO OVERVIEW
      </button>

      {/* Profile Header */}
      <div className="bg-indigo-600 rounded-[3rem] p-12 text-white flex flex-col md:flex-row items-center gap-12 mb-12 shadow-2xl relative overflow-hidden">
        <div className="absolute top-[-20%] right-[-10%] opacity-10 rotate-12">
          <Trophy className="w-96 h-96" />
        </div>
        
        <StudentAvatar student={student} className="w-40 h-40" fontSize="text-7xl" />
        
        <div className="text-center md:text-left relative z-10">
          <div className="flex flex-wrap justify-center md:justify-start gap-3 mb-4">
            <span className="bg-white/20 px-5 py-2 rounded-2xl text-xs font-black tracking-widest backdrop-blur-md border border-white/10">{student?.grade_level.toUpperCase()}</span>
            <span className="bg-amber-400 px-5 py-2 rounded-2xl text-xs font-black tracking-widest text-indigo-950 shadow-xl flex items-center gap-2">
              <Star className="w-4 h-4 fill-indigo-950" /> {student?.stars} TOTAL STARS
            </span>
          </div>
          <h2 className="text-6xl font-black mb-2 tracking-tight">{student?.surname}, {student?.first_name}</h2>
          <p className="text-indigo-100 text-lg font-medium opacity-80">Currently at Learning Level {student?.unlocked_level} • Active Student</p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-10">
        {/* Stats Column */}
        <div className="space-y-10">
          <div className="bg-white p-8 rounded-[2.5rem] shadow-sm border border-gray-100">
            <h3 className="text-xl font-black text-gray-900 mb-6 flex items-center gap-3">
              <div className="w-10 h-10 bg-indigo-50 rounded-xl flex items-center justify-center">
                <BarChart3 className="w-6 h-6 text-indigo-600" />
              </div>
              Mastered Letters
            </h3>
            <div className="grid grid-cols-5 gap-3">
              {student?.mastered_letters?.split(',').filter(Boolean).map(letter => (
                <div key={letter} className="aspect-square bg-indigo-50 text-indigo-700 font-black text-lg rounded-2xl flex items-center justify-center border border-indigo-100 shadow-sm transition-transform hover:scale-110">
                  {letter}
                </div>
              )) || (
                <div className="col-span-5 py-8 text-center bg-gray-50 rounded-3xl border border-dashed border-gray-200">
                  <p className="text-gray-400 text-sm font-bold italic">No letters mastered yet</p>
                </div>
              )}
            </div>
          </div>

          <div className="bg-white p-8 rounded-[2.5rem] shadow-sm border border-gray-100">
            <h3 className="text-xl font-black text-gray-900 mb-6 flex items-center gap-3">
              <div className="w-10 h-10 bg-emerald-50 rounded-xl flex items-center justify-center">
                <Trophy className="w-6 h-6 text-emerald-600" />
              </div>
              Exam Results
            </h3>
            <div className="space-y-4">
              {exams.map(exam => (
                <div key={exam.id} className="bg-gray-50 p-5 rounded-2xl flex justify-between items-center border border-gray-100">
                  <div>
                    <p className="text-xs font-black text-gray-400 tracking-wider uppercase mb-1">{exam.exam_name}</p>
                    <p className="font-black text-gray-900">{exam.score} / {exam.total_items} Points</p>
                  </div>
                  <span className={`text-[10px] font-black px-4 py-2 rounded-xl border ${
                    exam.score >= exam.total_items / 2 
                    ? 'bg-emerald-100 text-emerald-700 border-emerald-200' 
                    : 'bg-red-100 text-red-700 border-red-200'
                  }`}>
                    {exam.score >= exam.total_items / 2 ? 'PASSED' : 'FAILED'}
                  </span>
                </div>
              ))}
              {exams.length === 0 && <p className="text-gray-400 text-sm font-bold text-center py-6 italic">No exam history recorded</p>}
            </div>
          </div>
        </div>

        {/* Activity Column */}
        <div className="lg:col-span-2">
          <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
            <div className="p-8 border-b border-gray-50 flex items-center justify-between bg-white sticky top-0 z-10">
              <h3 className="text-xl font-black text-gray-900 flex items-center gap-3">
                <div className="w-10 h-10 bg-blue-50 rounded-xl flex items-center justify-center">
                  <History className="w-6 h-6 text-blue-600" />
                </div>
                Recent Learning Activities
              </h3>
              <span className="text-xs font-black text-gray-400 uppercase tracking-widest">{games.length} RECORDS</span>
            </div>
            <div className="divide-y divide-gray-50">
              {games.map(game => (
                <div key={game.id} className="p-8 flex items-center justify-between hover:bg-gray-50 transition-colors">
                  <div className="flex items-center gap-6">
                    <div className="w-14 h-14 bg-white rounded-2xl flex items-center justify-center shadow-sm border border-gray-100">
                      <BookOpen className="w-7 h-7 text-indigo-500" />
                    </div>
                    <div>
                      <p className="text-xl font-black text-gray-900">{game.game_name}</p>
                      <p className="text-sm font-bold text-gray-400 mt-1 uppercase tracking-tight">Played on {new Date(game.played_at).toLocaleDateString(undefined, { weekday: 'long', month: 'short', day: 'numeric' })}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-8">
                    <div className="text-right">
                      <p className="text-2xl font-black text-indigo-600">{game.score}/{game.total_items}</p>
                      <p className="text-xs font-bold text-gray-400 uppercase tracking-tighter">SCORE</p>
                    </div>
                    <div className="w-px h-10 bg-gray-100"></div>
                    <div className="text-right">
                      <p className="text-2xl font-black text-amber-500 flex items-center justify-end gap-1">
                        <Star className="w-6 h-6 fill-amber-500" /> +{game.stars_earned}
                      </p>
                      <p className="text-xs font-bold text-gray-400 uppercase tracking-tighter">EARNED</p>
                    </div>
                  </div>
                </div>
              ))}
              {games.length === 0 && <div className="p-20 text-center flex flex-col items-center gap-4">
                <div className="w-16 h-16 bg-gray-50 rounded-full flex items-center justify-center">
                  <History className="w-8 h-8 text-gray-200" />
                </div>
                <p className="text-gray-400 font-bold italic">No student activity logged yet</p>
              </div>}
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

// --- App ---

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    setIsAuthenticated(localStorage.getItem('isTeacherAuthenticated') === 'true');
  }, []);

  return (
    <Router>
      <Routes>
        <Route path="/" element={isAuthenticated ? <Navigate to="/dashboard" /> : <Login />} />
        <Route 
          path="/dashboard" 
          element={isAuthenticated ? <Dashboard /> : <Navigate to="/" />} 
        />
        <Route 
          path="/student/:id" 
          element={isAuthenticated ? <StudentDetail /> : <Navigate to="/" />} 
        />
        <Route path="/needs-help" element={isAuthenticated ? <NeedsHelp /> : <Navigate to="/" />} />
        <Route path="/rewards" element={isAuthenticated ? <StarRewards /> : <Navigate to="/" />} />
        <Route path="/logs" element={isAuthenticated ? <GlobalLogs /> : <Navigate to="/" />} />
      </Routes>
    </Router>
  );
}

export default App;

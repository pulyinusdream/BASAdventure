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
      navigate('/dashboard');
    } else {
      setError('Maling username o password!');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-indigo-600 to-purple-700 p-4">
      <div className="bg-white w-full max-w-md p-8 rounded-3xl shadow-2xl">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-black text-gray-900 mb-2">👩‍🏫 Teacher Portal</h1>
          <p className="text-gray-500">BAS Adventure Monitoring Dashboard</p>
        </div>
        
        <form onSubmit={handleLogin} className="space-y-6">
          <div>
            <label className="block text-sm font-bold text-gray-700 mb-2">Username</label>
            <input 
              type="text" 
              className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
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
              className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              required
            />
          </div>
          
          {error && <p className="text-red-500 text-sm text-center font-medium">{error}</p>}
          
          <button 
            type="submit"
            className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-4 rounded-xl shadow-lg transition-all active:scale-95"
          >
            Sign In
          </button>
        </form>
      </div>
    </div>
  );
};

const Dashboard = () => {
  const [students, setStudents] = useState<UserProgress[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const navigate = useNavigate();

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

  const filteredStudents = students.filter(s => 
    `${s.first_name} ${s.surname}`.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Sidebar/Nav */}
      <nav className="bg-white border-b border-gray-200 sticky top-0 z-10">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16 items-center">
            <div className="flex items-center gap-2">
              <div className="w-10 h-10 bg-indigo-600 rounded-lg flex items-center justify-center">
                <BarChart3 className="text-white w-6 h-6" />
              </div>
              <span className="text-xl font-black text-gray-900 hidden sm:block">BAS ADVENTURE</span>
            </div>
            
            <div className="flex items-center gap-4">
              <button 
                onClick={() => { localStorage.removeItem('isTeacherAuthenticated'); navigate('/'); }}
                className="flex items-center gap-2 text-gray-500 hover:text-red-600 font-bold text-sm"
              >
                <LogOut className="w-4 h-4" />
                Logout
              </button>
            </div>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <header className="mb-8 flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <h1 className="text-3xl font-black text-gray-900">Student Progress</h1>
            <p className="text-gray-500">Monitor individual performance and class trends</p>
          </div>
          <div className="flex items-center gap-2">
            <div className="relative">
              <Search className="w-5 h-5 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
              <input 
                type="text" 
                placeholder="Search students..." 
                className="pl-10 pr-4 py-2 bg-white border border-gray-200 rounded-xl focus:ring-2 focus:ring-indigo-500 outline-none w-full md:w-64"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
            </div>
            <button 
              onClick={fetchStudents}
              className="p-2 bg-white border border-gray-200 rounded-xl hover:bg-gray-50 transition-colors"
            >
              <RefreshCw className={`w-5 h-5 text-gray-500 ${loading ? 'animate-spin' : ''}`} />
            </button>
          </div>
        </header>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <StatCard title="Total Students" value={students.length} icon={Users} color="bg-blue-500" />
          <StatCard title="Average Stars" value={students.length ? Math.round(students.reduce((a,b) => a + b.stars, 0) / students.length) : 0} icon={Star} color="bg-yellow-500" />
          <StatCard title="Highest Level" value={students.length ? Math.max(...students.map(s => s.unlocked_level)) : 1} icon={Trophy} color="bg-purple-500" />
          <StatCard title="Target Levels" value="Kinder - Grade 6" icon={BookOpen} color="bg-green-500" />
        </div>

        {/* Students Table */}
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left">
              <thead>
                <tr className="bg-gray-50 border-b border-gray-100">
                  <th className="px-6 py-4 text-sm font-bold text-gray-600 uppercase tracking-wider">Student</th>
                  <th className="px-6 py-4 text-sm font-bold text-gray-600 uppercase tracking-wider">Grade</th>
                  <th className="px-6 py-4 text-sm font-bold text-gray-600 uppercase tracking-wider">Progress</th>
                  <th className="px-6 py-4 text-sm font-bold text-gray-600 uppercase tracking-wider">Performance</th>
                  <th className="px-6 py-4 text-sm font-bold text-gray-600 uppercase tracking-wider"></th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {loading ? (
                  Array.from({ length: 5 }).map((_, i) => (
                    <tr key={i} className="animate-pulse">
                      <td colSpan={5} className="px-6 py-4 h-16 bg-gray-50/50"></td>
                    </tr>
                  ))
                ) : filteredStudents.length === 0 ? (
                  <tr>
                    <td colSpan={5} className="px-6 py-12 text-center text-gray-500 italic">No students found</td>
                  </tr>
                ) : filteredStudents.map((student) => (
                  <tr 
                    key={student.student_id} 
                    className="hover:bg-indigo-50/30 cursor-pointer transition-colors"
                    onClick={() => navigate(`/student/${student.student_id}`)}
                  >
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-3">
                        <span className="text-2xl">{student.selected_avatar || '👤'}</span>
                        <div>
                          <p className="font-bold text-gray-900">{student.surname}, {student.first_name}</p>
                          <p className="text-xs text-gray-500">ID: {student.student_id.slice(0,8)}...</p>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                        Grade {student.grade_level}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex flex-col gap-1">
                        <p className="text-sm font-bold text-gray-700">Level {student.unlocked_level}</p>
                        <div className="w-32 h-1.5 bg-gray-100 rounded-full overflow-hidden">
                          <div className="h-full bg-indigo-500" style={{ width: `${(student.unlocked_level / 10) * 100}%` }}></div>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-4">
                        <div className="flex items-center gap-1">
                          <Star className="w-4 h-4 text-yellow-500 fill-yellow-500" />
                          <span className="font-bold">{student.stars}</span>
                        </div>
                        <div className="flex items-center gap-1">
                          <BookOpen className="w-4 h-4 text-purple-500" />
                          <span className="font-bold">{student.mastered_letters?.split(',').filter(Boolean).length || 0}</span>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-right">
                      <ChevronRight className="w-5 h-5 text-gray-300" />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </main>
    </div>
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
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <RefreshCw className="w-8 h-8 text-indigo-600 animate-spin" />
    </div>
  );

  return (
    <div className="min-h-screen bg-gray-50 pb-12">
      <nav className="bg-white border-b border-gray-200 px-4 h-16 flex items-center gap-4">
        <button onClick={() => navigate('/dashboard')} className="p-2 hover:bg-gray-100 rounded-lg">
          <ChevronRight className="w-5 h-5 rotate-180 text-gray-500" />
        </button>
        <h1 className="text-xl font-black text-gray-900">Student Profile</h1>
      </nav>

      <main className="max-w-5xl mx-auto px-4 mt-8">
        {/* Profile Card */}
        <div className="bg-indigo-600 rounded-3xl p-8 text-white flex flex-col md:flex-row items-center gap-8 mb-8 shadow-xl relative overflow-hidden">
          <div className="absolute top-0 right-0 p-8 opacity-10">
            <Trophy className="w-48 h-48" />
          </div>
          <div className="w-32 h-32 bg-white rounded-3xl flex items-center justify-center text-6xl relative z-10 shadow-inner">
            {student?.selected_avatar || '👤'}
          </div>
          <div className="text-center md:text-left relative z-10">
            <h2 className="text-4xl font-black mb-1">{student?.surname}, {student?.first_name}</h2>
            <div className="flex flex-wrap justify-center md:justify-start gap-3 mt-4">
              <span className="bg-white/20 px-4 py-1.5 rounded-full text-sm font-bold backdrop-blur-sm">Grade {student?.grade_level}</span>
              <span className="bg-white/20 px-4 py-1.5 rounded-full text-sm font-bold backdrop-blur-sm">Level {student?.unlocked_level}</span>
              <span className="bg-yellow-400 px-4 py-1.5 rounded-full text-sm font-bold text-indigo-900 shadow-lg flex items-center gap-1">
                <Star className="w-4 h-4 fill-indigo-900" /> {student?.stars} Stars
              </span>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Left Column: Stats & Mastery */}
          <div className="space-y-8">
            <div className="bg-white p-6 rounded-3xl shadow-sm border border-gray-100">
              <h3 className="text-lg font-black text-gray-900 mb-4 flex items-center gap-2">
                <BarChart3 className="w-5 h-5 text-indigo-600" />
                Mastered Letters
              </h3>
              <div className="flex flex-wrap gap-2">
                {student?.mastered_letters?.split(',').filter(Boolean).map(letter => (
                  <span key={letter} className="w-10 h-10 bg-indigo-50 text-indigo-600 font-bold rounded-xl flex items-center justify-center border border-indigo-100">
                    {letter}
                  </span>
                )) || <p className="text-gray-400 text-sm">No letters mastered yet.</p>}
              </div>
            </div>

            <div className="bg-white p-6 rounded-3xl shadow-sm border border-gray-100">
              <h3 className="text-lg font-black text-gray-900 mb-4 flex items-center gap-2">
                <Trophy className="w-5 h-5 text-yellow-500" />
                Exams Summary
              </h3>
              <div className="space-y-4">
                {exams.map(exam => (
                  <div key={exam.id} className="flex justify-between items-center">
                    <span className="text-sm font-bold text-gray-700">{exam.exam_name}</span>
                    <span className={`text-xs font-bold px-2 py-1 rounded ${exam.score >= exam.total_items / 2 ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                      {exam.score}/{exam.total_items}
                    </span>
                  </div>
                ))}
                {exams.length === 0 && <p className="text-gray-400 text-sm italic">No exams taken</p>}
              </div>
            </div>
          </div>

          {/* Right Column: History */}
          <div className="lg:col-span-2 space-y-8">
            <div className="bg-white rounded-3xl shadow-sm border border-gray-100 overflow-hidden">
              <div className="p-6 border-b border-gray-100 flex items-center justify-between">
                <h3 className="text-lg font-black text-gray-900 flex items-center gap-2">
                  <History className="w-5 h-5 text-blue-600" />
                  Recent Activities
                </h3>
              </div>
              <div className="divide-y divide-gray-50">
                {games.map(game => (
                  <div key={game.id} className="p-6 flex items-center justify-between hover:bg-gray-50">
                    <div className="flex items-center gap-4">
                      <div className="w-10 h-10 bg-blue-100 rounded-xl flex items-center justify-center">
                        <BookOpen className="w-5 h-5 text-blue-600" />
                      </div>
                      <div>
                        <p className="font-bold text-gray-900">{game.game_name}</p>
                        <p className="text-xs text-gray-500">{new Date(game.played_at).toLocaleDateString()}</p>
                      </div>
                    </div>
                    <div className="text-right">
                      <p className="font-black text-indigo-600">{game.score}/{game.total_items}</p>
                      <p className="text-[10px] font-bold text-yellow-600 flex items-center justify-end gap-0.5">
                        <Star className="w-3 h-3 fill-yellow-600" /> +{game.stars_earned}
                      </p>
                    </div>
                  </div>
                ))}
                {games.length === 0 && <div className="p-12 text-center text-gray-400 italic">No activity yet</div>}
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
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
      </Routes>
    </Router>
  );
}

export default App;

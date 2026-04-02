import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Eye, EyeOff } from 'lucide-react';
import { login } from '../services/api';

function Login() {
  const [username, setUsername] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [error, setError] = useState<string>('');
  const [showPassword, setShowPassword] = useState<boolean>(false);
  const navigate = useNavigate();

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      //called login() from service/api
      const response = await login({ username, password });
      //store token
      localStorage.setItem('token', response.data.token);
      //login success login to dashboard

      localStorage.setItem('userId', response.data.id);
      localStorage.setItem('station', response.data.station);
      localStorage.setItem('username', response.data.username);

      const role = response.data.role;
      localStorage.setItem('role', role);

      if (role === 'ADMIN') {
        navigate('/admin');
      } else if (role === 'SUPERVISOR' || role === 'SAFETY_OFFICER') {
        navigate('/dashboard');
      } else {
        navigate('/dashboard');
      }
    } catch (error: any) {
      setError(error.response?.data || 'Invalid username or password');
    }
  };
  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      {/* header */}
      <header style={{ backgroundColor: '#9a1a2f' }} className="p-4">
        <h1 className="text-white text-2xl font-bold text-center">
          RAMP-SECURE
        </h1>
        <p className="text-white text-center text-sm">
          MaintAir Aviation Services
        </p>
      </header>
      {/* form card */}
      <div className="flex flex-1 items-center justify-center">
        <form
          onSubmit={submit}
          className="bg-white p-8 rounded shadow-md w-full max-w-md"
        >
          <h2 className="text-black text-xl font-semibold mb-6">Sign In</h2>
          <input
            value={username}
            type="text"
            placeholder="Enter your username"
            onChange={(e) => setUsername(e.target.value)}
            className="w-full border border-gray-300 rounded p-3 mb-4 text-black"
          />
          <div className="relative mb-4">
            <input
              value={password}
              type={showPassword ? 'text' : 'password'}
              placeholder="Enter your password"
              onChange={(e) => setPassword(e.target.value)}
              className="w-full border border-gray-300 rounded p-3 text-black pr-10"
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-3 top-3 text-gray-500"
            >
              {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
            </button>
          </div>

          <button
            type="submit"
            style={{ backgroundColor: '#9a1a2f' }}
            className="w-full text-white py-3 rounded font-semibold"
          >
            Sign In
          </button>
          {error && (
            <div className="bg-red-50 border border-red-400 text-red-700 px-4 py-3 rounded mt-3">
              <p className="font-semibold text-sm">{error}</p>
            </div>
          )}
        </form>
      </div>
    </div>
  );
}

export default Login;

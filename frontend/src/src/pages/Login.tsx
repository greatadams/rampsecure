import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/api';

function Login() {
  const [username, setUsername] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [error, setError] = useState<string>('');
  const navigate = useNavigate();

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setError('');
      //called login() from service/api
      const response = await login({ username, password });
      //store token
      localStorage.setItem('token', response.data.token);
      //login success login to dashboard

      localStorage.setItem('userId', response.data.id);
      localStorage.setItem('station', response.data.station);
      localStorage.setItem('username', response.data.username);
      localStorage.setItem('role', response.data.role);
      navigate('/dashboard');
    } catch (error) {
      setError('Invalid username or password');
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
          <input
            value={password}
            type="password"
            placeholder="Enter your password"
            onChange={(e) => setPassword(e.target.value)}
            className="w-full border border-gray-300 rounded p-3 mb-4 text-black"
          />
          <button
            type="submit"
            style={{ backgroundColor: '#9a1a2f' }}
            className="w-full text-white py-3 rounded font-semibold"
          >
            Sign In
          </button>
          {error && <p className="text-red-600 mt-3 text-sm">{error}</p>}
        </form>
      </div>
    </div>
  );
}

export default Login;

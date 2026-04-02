import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import type { UserResponse } from '../types/types.ts';
import { register, updateUser, getAllUsers, deleteUser } from '../services/api';
import { Eye, EyeOff } from 'lucide-react';

function AdminDashboard() {
  const [existingUser, setExistingUser] = useState<UserResponse[]>([]);
  const [createUserForm, setCreateUserForm] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>('');
  const [userToDelete, setUserToDelete] = useState<string | null>(null);
  const [editUser, setEditUser] = useState<string | null>(null);
  const [firstName, setFirstName] = useState<string>('');
  const [lastName, setLastName] = useState<string>('');
  const [username, setUsername] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [phoneNumber, setPhoneNumber] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [confirmPassword, setConfirmPassword] = useState<string>('');
  const [showPassword, setShowPassword] = useState<boolean>(false);
  const [role, setRole] = useState<string>('');
  const [station, setStation] = useState<string>('');

  const navigate = useNavigate();

  const displayUsers = async () => {
    try {
      setLoading(true);
      setError('');

      const response = await getAllUsers();
      setExistingUser(response.data);
    } catch (error) {
      setError('No users in the system');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    displayUsers();
  }, []);

  const handleLogout = () => {
    localStorage.clear();
    navigate('/login');
  };

  const handleDeleteUser = async (id: string) => {
    try {
      setError('');
      await deleteUser(id);
      await displayUsers(); //refresh list
    } catch (error) {
      setError(
        error.response?.data || 'Unable to remove this user at the moment',
      );
    }
  };

  const handleCreateUser = async () => {
    try {
      setLoading(true);
      setError('');
      await register({
        username,
        firstName,
        lastName,
        email,
        phoneNumber,
        password,
        confirmPassword,
        role,
        station,
      });
      setCreateUserForm(false);
      await displayUsers();
    } catch (error: any) {
      if (error.response?.status === 409) {
        setError('Username or email already exists');
      } else {
        setError('Failed to create user');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateUser = async () => {
    try {
      setError('');
      await updateUser(editUser!, { role: role, station: station });
      setEditUser(null);
      await displayUsers();
    } catch (error) {
      setError('Failed to update user');
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      {/* header */}
      <header
        style={{ backgroundColor: '#9a1a2f' }}
        className="p-4 flex justify-between items-center"
      >
        <div>
          <h1 className="text-white text-2xl font-bold">RAMPSECURE</h1>
          <p className="text-white text-sm">
            MaintAir Aviation Services — Admin Dashboard
          </p>
        </div>
        <div className="text-right">
          <p className="text-white text-sm">
            Logged in as:{' '}
            <span className="font-semibold">
              {localStorage.getItem('username')}
            </span>
          </p>
          <p className="text-white text-sm">
            Role:{' '}
            <span className="font-semibold">
              {localStorage.getItem('role')}
            </span>
          </p>
          <button
            onClick={handleLogout}
            className="mt-1 text-xs border border-white text-white px-3 py-1 rounded hover:bg-white hover:text-red-800 transition"
          >
            Logout
          </button>
          <button
            onClick={() => navigate('/dashboard')}
            className="mt-1 text-xs border border-white text-white px-3 py-1 rounded"
          >
            Back to Dashboard
          </button>
        </div>
      </header>

      {/* main */}
      {/* create new user */}
      <main className="p-6">
        {/* create new user */}
        <button
          onClick={() => setCreateUserForm(!createUserForm)}
          style={{ backgroundColor: '#9a1a2f' }}
          className="text-white px-4 py-2 rounded font-semibold mb-6"
        >
          {createUserForm ? 'Cancel' : 'Create New User'}
        </button>

        {/* Create User Form - shows when createUserForm is true */}
        {createUserForm && (
          <div className="bg-white rounded shadow p-6 mb-6">
            <h2 className="text-black text-lg font-bold mb-4">
              Register New User
            </h2>
            {/* form fields go here */}
            <form id="myForm" className="grid grid-cols-2 gap-4">
              <input
                value={username} //displays what's currently in the state
                onChange={(e) => setUsername(e.target.value)} //every keystroke updates the state with what the user typed
                type="text"
                placeholder="Enter Username"
                className="w-full border border-gray-300 rounded p-3 text-black"
              />
              <input
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
                type="text"
                placeholder="Enter firstName"
                className="w-full border border-gray-300 rounded p-3 text-black"
              />
              <input
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
                type="text"
                placeholder="Enter lastName"
                className="w-full border border-gray-300 rounded p-3 text-black"
              />
              <input
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                type="email"
                placeholder="Enter email"
                className="w-full border border-gray-300 rounded p-3 text-black"
              />
              <input
                value={phoneNumber}
                onChange={(e) => setPhoneNumber(e.target.value)}
                type="text"
                placeholder="Enter phoneNumber"
                className="w-full border border-gray-300 rounded p-3 text-black"
              />

              <div className="relative mb-4">
                <input
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  type={showPassword ? 'text' : 'password'}
                  placeholder="Enter  password"
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

              <div className="relative mb-4">
                <input
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  type={showPassword ? 'text' : 'password'}
                  placeholder="Enter confirm password"
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

              <select
                value={role}
                onChange={(e) => setRole(e.target.value)}
                className="w-full border border-gray-300 rounded p-3 mb-4 text-black"
              >
                <option value="">Select Role</option>
                <option value="OPERATOR">Operator</option>
                <option value="SUPERVISOR">Supervisor</option>
                <option value="SAFETY_OFFICER">Safety Officer</option>
              </select>
              <select
                value={station}
                onChange={(e) => setStation(e.target.value)}
                className="w-full border border-gray-300 rounded p-3 text-black"
              >
                <option value="">Select Station</option>
                <option value="YBR">Brandon (YBR)</option>
                <option value="YQT">Thunder Bay (YQT)</option>
                <option value="YFC">Fredericton (YFC)</option>
                <option value="YTS">Timmins (YTS)</option>
                <option value="YQG">Windsor (YQG)</option>
                <option value="YSJ">Saint John (YSJ)</option>
              </select>

              <button
                type="button"
                onClick={handleCreateUser}
                style={{ backgroundColor: '#9a1a2f' }}
                className="col-span-2 text-white py-3 rounded font-semibold mt-2"
              >
                Submit
              </button>
            </form>
          </div>
        )}
        {/* Users Table */}
        {loading && <p className="text-gray-500">Loading users...</p>}
        {error && <p className="text-red-600">{error}</p>}

        <div className="bg-white rounded shadow overflow-hidden">
          <table className="w-full">
            <thead style={{ backgroundColor: '#9a1a2f' }}>
              <tr>
                <th className="text-white p-3 text-left">Username</th>
                <th className="text-white p-3 text-left">Name</th>
                <th className="text-white p-3 text-left">Email</th>
                <th className="text-white p-3 text-left">Role</th>
                <th className="text-white p-3 text-left">Station</th>
                <th className="text-white p-3 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              {existingUser.map((user) => (
                <tr key={user.id} className="border-b border-gray-200">
                  <td className="p-3 text-black">{user.username}</td>
                  <td className="p-3 text-black">
                    {user.firstName} {user.lastName}
                  </td>
                  <td className="p-3 text-black">{user.email}</td>
                  <td className="p-3 text-black">{user.role}</td>
                  <td className="p-3 text-black">{user.station}</td>
                  <td className="p-3 flex gap-2">
                    <button
                      onClick={() => {
                        setEditUser(user.id);
                        setRole(user.role);
                        setStation(user.station);
                      }}
                      className="bg-blue-500 text-white px-3 py-1 rounded text-sm"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleDeleteUser(user.id)}
                      className="bg-red-500 text-white px-3 py-1 rounded text-sm"
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </main>
      {/* Edit User Modal */}
      {editUser && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-md mx-4">
            <h3 className="text-black text-lg font-bold mb-4">Edit User</h3>

            <label className="block text-gray-700 text-sm font-semibold mb-1">
              Role
            </label>
            <select
              value={role}
              onChange={(e) => setRole(e.target.value)}
              className="w-full border border-gray-300 rounded p-3 mb-4 text-black"
            >
              <option value="OPERATOR">Operator</option>
              <option value="SUPERVISOR">Supervisor</option>
              <option value="SAFETY_OFFICER">Safety Officer</option>
            </select>

            <label className="block text-gray-700 text-sm font-semibold mb-1">
              Station
            </label>
            <select
              value={station}
              onChange={(e) => setStation(e.target.value)}
              className="w-full border border-gray-300 rounded p-3 mb-4 text-black"
            >
              <option value="YBR">Brandon (YBR)</option>
              <option value="YQT">Thunder Bay (YQT)</option>
              <option value="YFC">Fredericton (YFC)</option>
              <option value="YTS">Timmins (YTS)</option>
              <option value="YQG">Windsor (YQG)</option>
              <option value="YSJ">Saint John (YSJ)</option>
            </select>

            <div className="flex gap-3">
              <button
                onClick={handleUpdateUser}
                style={{ backgroundColor: '#9a1a2f' }}
                className="flex-1 text-white py-3 rounded font-semibold"
              >
                Save Changes
              </button>
              <button
                onClick={() => setEditUser(null)}
                className="flex-1 border border-gray-300 text-gray-600 py-3 rounded font-semibold"
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default AdminDashboard;

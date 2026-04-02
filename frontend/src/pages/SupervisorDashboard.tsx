import { useEffect, useState } from 'react';
import type { Equipment } from '../types/types';
import { getAllEquipment, updateEquipmentStatus } from '../services/api';
import { useNavigate } from 'react-router-dom';

function SupervisorDashboard() {
  const [error, setError] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(true);
  const [equipment, setEquipment] = useState<Equipment[]>([]);
  const [status, setStatus] = useState<string>('ALL');

  const navigate = useNavigate();

  const displayAllEquipment = async () => {
    try {
      setLoading(true);
      setError('');

      const response = await getAllEquipment();

      setEquipment(response.data);
    } catch (error: any) {
      setError('No Equipment');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    displayAllEquipment();
  }, []);

  const filteredTasks = equipment.filter((e) =>
    status === 'ALL' ? true : e.status === status,
  );

  const handleClearMaintenance = async (id: string) => {
    try {
      await updateEquipmentStatus(id, 'AVAILABLE');
      await displayAllEquipment(); // refresh the list
    } catch (error: any) {
      setError('Failed to clear maintenance');
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      <header
        style={{ backgroundColor: '#9a1a2f' }}
        className="p-4 flex justify-between items-center"
      >
        <div>
          <h1 className="text-white text-2xl font-bold">RAMPSECURE</h1>
          <p className="text-white text-sm">
            MaintAir Aviation Services — Supervisor Dashboard
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
            onClick={() => navigate('/dashboard')}
            className="mt-1 text-xs border border-white text-white px-3 py-1 rounded hover:bg-white hover:text-red-800 transition"
          >
            Back to Dashboard
          </button>
        </div>
      </header>

      <main className="p-6">
        <div className="flex gap-2 mb-6">
          {['ALL', 'AVAILABLE', 'IN_USE', 'MAINTENANCE'].map((s) => (
            <button
              key={s}
              onClick={() => setStatus(s)}
              style={status === s ? { backgroundColor: '#9a1a2f' } : {}}
              className={`px-4 py-2 rounded font-semibold text-sm ${
                status === s
                  ? 'text-white'
                  : 'bg-white text-gray-600 border border-gray-300'
              }`}
            >
              {s}
            </button>
          ))}
        </div>

        {loading && <p className="text-gray-500">Loading equipment...</p>}
        {error && <p className="text-red-600 mb-4">{error}</p>}
      </main>

      {/* Equipment Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {filteredTasks.map((item) => (
          <div key={item.id} className="bg-white rounded shadow p-4">
            <div className="flex justify-between items-center mb-2">
              <h3 className="font-bold text-black">{item.equipmentCode}</h3>
              <span
                className={`text-xs font-semibold px-2 py-1 rounded ${
                  item.status === 'AVAILABLE'
                    ? 'bg-green-100 text-green-700'
                    : item.status === 'IN_USE'
                      ? 'bg-yellow-100 text-yellow-700'
                      : 'bg-red-100 text-red-700'
                }`}
              >
                {item.status}
              </span>
            </div>

            <p className="text-gray-600 text-sm">{item.equipmentType}</p>
            <p className="text-gray-600 text-sm">{item.model}</p>
            <p className="text-gray-500 text-xs mt-1">
              Station: {item.station}
            </p>
            <p className="text-gray-500 text-xs">Location: {item.location}</p>

            {item.status === 'IN_USE' && (
              <div className="mt-2 p-2 bg-yellow-50 rounded border border-yellow-200">
                <p className="text-sm font-semibold text-gray-700">
                  Operator: {item.currentOperator?.username}
                </p>
                <p className="text-xs text-gray-500">
                  Since:{' '}
                  {item.lastCheckoutAt
                    ? new Date(item.lastCheckoutAt).toLocaleString()
                    : 'N/A'}
                </p>
              </div>
            )}
            {item.status === 'MAINTENANCE' && (
              <button
                onClick={() => {
                  handleClearMaintenance(item.id);
                }}
                className="mt-3 w-full bg-green-600 text-white py-2 rounded text-sm font-semibold"
              >
                Clear Maintenance
              </button>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}
export default SupervisorDashboard;

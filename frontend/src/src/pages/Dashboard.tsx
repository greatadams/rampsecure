import { useEffect, useState } from 'react';
import type { Equipment } from '../types/types';
import { useNavigate } from 'react-router-dom';
import { getEquipmentByStation, checkout, checkin } from '../services/api';

function Dashboard() {
  const [equipment, setEquipment] = useState<Equipment[]>([]);
  const [error, setError] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(true);
  const [checkinEquipmentId, setCheckinEquipmentId] = useState<string | null>(
    null,
  );
  const [condition, setCondition] = useState<string>('GOOD');
  const [notes, setNotes] = useState<string>('');
  const navigate = useNavigate();

  const station = localStorage.getItem('station') || '';
  const username = localStorage.getItem('username') || '';
  const userId = localStorage.getItem('userId') || '';

  const displayEquipment = async () => {
    try {
      setLoading(true);
      setError('');

      if (!station) {
        setError('No station found. Please log in again.');
        setEquipment([]);
        return;
      }

      const response = await getEquipmentByStation(station);
      console.log('equipment data:', response.data);
      setEquipment(response.data);
    } catch (error) {
      setError('No equipment available');
      setEquipment([]);
    } finally {
      setLoading(false);
    }
  };

  const handleCheckout = async (equipmentId: string) => {
    try {
      setError('');
      await checkout({ equipmentId });
      await displayEquipment();
    } catch (error) {
      setError('Checkout failed');
    }
  };

  const handleCheckin = async (equipmentId: string) => {
    try {
      setError('');
      await checkin({ equipmentId, condition, notes });
      setCheckinEquipmentId(null);
      setCondition('GOOD');
      setNotes('');
      await displayEquipment();
    } catch (error) {
      setError('Checkin failed');
    }
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate('/login');
  };
  useEffect(() => {
    displayEquipment();
  }, []);

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      {/* Header */}
      <header
        style={{ backgroundColor: '#9a1a2f' }}
        className="p-4 flex justify-between items-center"
      >
        <div>
          <h1 className="text-white text-2xl font-bold">RAMPSECURE</h1>
          <p className="text-white text-sm">MaintAir Aviation Services</p>
        </div>

        <div className="text-right">
          <p className="text-white text-sm">
            Logged in as: <span className="font-semibold">{username}</span>
          </p>
          <p className="text-white text-sm">
            Station: <span className="font-semibold">{station}</span>
          </p>
          <button
            onClick={handleLogout}
            className="mt-1 text-xs border border-white text-white px-3 py-1 rounded hover:bg-white hover:text-red-800 transition"
          >
            Logout
          </button>
        </div>
      </header>

      {/* Main content */}
      <main className="p-6">
        <h2 className="text-black text-xl font-semibold mb-4">
          Equipment at {station}
        </h2>

        {loading && <p className="text-gray-500">Loading equipment...</p>}
        {error && <p className="text-red-600 mb-4">{error}</p>}

        {!loading && !error && equipment.length === 0 && (
          <p className="text-gray-500">No equipment found for this station.</p>
        )}

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {equipment.map((item) => (
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

              <p className="text-gray-600 text-sm">{item.type}</p>
              <p className="text-gray-600 text-sm">{item.model}</p>

              {item.status === 'IN_USE' &&
                item.currentOperatorId?.toString() === userId && (
                  <button
                    onClick={() => setCheckinEquipmentId(item.id)}
                    style={{ backgroundColor: '#9a1a2f' }}
                    className="mt-3 w-full text-white py-2 rounded text-sm font-semibold"
                  >
                    Check In
                  </button>
                )}

              {item.status === 'AVAILABLE' && (
                <button
                  onClick={() => handleCheckout(item.id)}
                  style={{ backgroundColor: '#9a1a2f' }}
                  className="mt-3 w-full text-white py-2 rounded text-sm font-semibold"
                >
                  Check Out
                </button>
              )}
            </div>
          ))}
        </div>
      </main>

      {/* Checkin Modal */}
      {checkinEquipmentId && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-md mx-4">
            <h3 className="text-black text-lg font-bold mb-4">
              Check In Equipment
            </h3>

            <label className="block text-gray-700 text-sm font-semibold mb-1">
              Condition
            </label>
            <select
              value={condition}
              onChange={(e) => setCondition(e.target.value)}
              className="w-full border border-gray-300 rounded p-3 mb-4 text-black"
            >
              <option value="GOOD">Good</option>
              <option value="DAMAGED">Damaged</option>
              <option value="NEEDS_MAINTENANCE">Needs Maintenance</option>
            </select>

            <label className="block text-gray-700 text-sm font-semibold mb-1">
              Notes
            </label>
            <textarea
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              placeholder="Describe any issues or observations..."
              rows={3}
              className="w-full border border-gray-300 rounded p-3 mb-4 text-black text-sm"
            />

            <div className="flex gap-3">
              <button
                onClick={() => handleCheckin(checkinEquipmentId)}
                style={{ backgroundColor: '#9a1a2f' }}
                className="flex-1 text-white py-3 rounded font-semibold"
              >
                Confirm Check In
              </button>

              <button
                onClick={() => {
                  setCheckinEquipmentId(null);
                  setCondition('GOOD');
                  setNotes('');
                }}
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

export default Dashboard;

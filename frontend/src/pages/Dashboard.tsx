import { useEffect, useState } from 'react';
import type { Equipment } from '../types/types';
import { getEquipmentByStation } from '../services/api';

function Dashboard() {
  const [equipment, setEquipment] = useState<Equipment[]>([]);
  const [error, setError] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(true);

  const station = localStorage.getItem('station') || '';
  const username = localStorage.getItem('username') || '';

  const displayEquipment = async () => {
    try {
      setLoading(true);
      setError('');

      if (!station) {
        setError('No station found. Please log in again.');
        return;
      }
      const response = await getEquipmentByStation(station);
      setEquipment(response.data);
    } catch (error) {
      setError('No equipments available');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    displayEquipment();
  }, []);

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      {/* header */}
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
        </div>
      </header>

      {/* Main content*/}
      <div>
        <h2 className="text-black text-xl font-semibold mb-4">
          Equipment at {station}
        </h2>

        {loading && <p className="text-gray-500">Loading equipment...</p>}

        {error && <p className="text-red-600">{error}</p>}

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
              {item.status === 'AVAILABLE' && (
                <button
                  style={{ backgroundColor: '#9a1a2f' }}
                  className="mt-3 w-full text-white py-2 rounded text-sm font-semibold"
                >
                  Checkout
                </button>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Dashboard;

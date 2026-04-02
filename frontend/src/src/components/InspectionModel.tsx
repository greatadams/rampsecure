import { useEffect, useState } from 'react';
import type { ChecklistItem } from '../types/types';
import {
  getChecklist,
  sendToMaintenance,
  startInspection,
  submitInspection,
} from '../services/api';

type InspectionModalProps = {
  equipmentId: string; // which equipment
  equipmentCode: string; // to display in the header
  isOpen: boolean; // show or hide
  onClose: () => void; // called when cancelled
  onInspectionPassed: (equipmentId: string) => void; // called when passed, triggers checkout
};

export function InspectionModel({
  equipmentId,
  equipmentCode,
  isOpen,
  onClose,
  onInspectionPassed,
}: InspectionModalProps) {
  const [checklistItems, setCheckListItems] = useState<ChecklistItem[]>([]);
  const [notes, setNote] = useState<Record<string, string>>({});
  const [reportId, setReportId] = useState<string | null>(null);
  const [result, setResult] = useState<Record<string, string>>({});
  const [error, setError] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [showMaintenanceButton, setShowMaintenanceButton] =
    useState<boolean>(false);

  const displayChecklist = async () => {
    try {
      setLoading(true);
      setError('');
      //call the getchecklist
      const response = await getChecklist(equipmentId);
      //store/save the response of data gotten from the checklist
      setCheckListItems(response.data);
    } catch (error) {
      setError('checklist for this equipment not  available');
      setCheckListItems([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSentToMaintenance = async () => {
    try {
      await sendToMaintenance(equipmentId);
      onClose();
    } catch (error) {
      setError('Failed to send equipment to maintenance');
    }
  };

  const handleSubmit = async () => {
    try {
      setError('');

      // only start inspection if we don't have a reportId yet
      let currentReportId = reportId;
      if (!currentReportId) {
        const start = await startInspection({ equipmentId });
        currentReportId = start.data.reportId;
        setReportId(currentReportId);

        // if report already submitted, handle based on existing status
        if (start.data.checklistStatus === 'FAILED') {
          setError(
            'Critical items failed. Equipment must be sent to maintenance.',
          );
          setShowMaintenanceButton(true);
          return; // stop here, don't submit again
        }
      }

      // for INCOMPLETE or PASSED - always submit new results
      const results = Object.entries(result).map(([itemId, resultStatus]) => ({
        checkListItemId: itemId,
        resultStatus: resultStatus,
        notes: notes[itemId] || '',
      }));

      const submit = await submitInspection({
        reportId: currentReportId,
        results,
      });

      if (submit.data.hasCriticalFailure) {
        setError(
          'Critical items failed. Checkout blocked.Equipment must be sent to maintenance',
        );
        setShowMaintenanceButton(true);
      } else {
        onInspectionPassed(equipmentId);
      }
    } catch (error) {
      setError('Error,cannot submit at the moment');
    }
  };

  useEffect(() => {
    if (!isOpen) return; // don't fetch if modal is closed
    setReportId(null); // reset reportId when modal opens
    setResult({}); // reset results
    setNote({}); // reset notes
    displayChecklist();
  }, [equipmentId, isOpen]);

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-2xl mx-4 max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div
          style={{ backgroundColor: '#9a1a2f' }}
          className="p-4 rounded-t-lg"
        >
          <h2 className="text-white text-xl font-bold">
            Pre-Use Safety Inspection
          </h2>
          <p className="text-white text-sm">Equipment: {equipmentCode}</p>
        </div>
        <div className="p-6">
          {loading && <p className="text-gray-500">Loading check list...</p>}
          {error && <p className="text-red-600 mb-4">{error}</p>}

          {showMaintenanceButton && (
            <button
              onClick={handleSentToMaintenance}
              style={{ backgroundColor: '#9a1a2f' }}
              className="w-full text-white py-3 rounded font-semibold mt-3"
            >
              Send to Maintenance
            </button>
          )}

          {/* Checklist Items */}
          <div className="space-y-4">
            {checklistItems.map((item) => (
              <div
                key={item.id}
                className="border border-gray-200 rounded-lg p-4"
              >
                <div className="flex justify-between items-start mb-2">
                  <div>
                    <h3 className="font-bold text-black">{item.itemName}</h3>
                    <p className="text-gray-500 text-sm">{item.description}</p>
                  </div>
                  {item.isCritical && (
                    <span className="text-xs font-bold px-2 py-1 rounded bg-red-100 text-red-700">
                      CRITICAL
                    </span>
                  )}
                </div>

                {/* Pass/Fail/NA buttons */}
                <div className="flex gap-2 mt-3">
                  <button
                    onClick={() => setResult({ ...result, [item.id]: 'PASS' })}
                    className={`flex-1 py-2 rounded font-semibold text-sm ${
                      result[item.id] === 'PASS'
                        ? 'bg-green-500 text-white'
                        : 'bg-gray-100 text-gray-600'
                    }`}
                  >
                    Pass
                  </button>
                  <button
                    onClick={() => setResult({ ...result, [item.id]: 'FAIL' })}
                    className={`flex-1 py-2 rounded font-semibold text-sm ${
                      result[item.id] === 'FAIL'
                        ? 'bg-red-500 text-white'
                        : 'bg-gray-100 text-gray-600'
                    }`}
                  >
                    Fail
                  </button>
                  <button
                    onClick={() => setResult({ ...result, [item.id]: 'NA' })}
                    className={`flex-1 py-2 rounded font-semibold text-sm ${
                      result[item.id] === 'NA'
                        ? 'bg-gray-500 text-white'
                        : 'bg-gray-100 text-gray-600'
                    }`}
                  >
                    NA
                  </button>
                </div>

                {/* Notes field for failed items */}
                {result[item.id] === 'FAIL' && (
                  <textarea
                    value={notes[item.id] || ''}
                    onChange={(e) =>
                      setNote({ ...notes, [item.id]: e.target.value })
                    }
                    placeholder="Describe the issue..."
                    rows={2}
                    className="w-full mt-2 border border-gray-300 rounded p-2 text-sm text-black"
                  />
                )}
              </div>
            ))}
          </div>
          {/* Submit and Cancel */}
          <div className="flex gap-3 mt-6">
            <button
              onClick={handleSubmit}
              style={{ backgroundColor: '#9a1a2f' }}
              className="flex-1 text-white py-3 rounded font-semibold"
            >
              Submit Inspection
            </button>
            <button
              onClick={onClose}
              className="flex-1 border border-gray-300 text-gray-600 py-3 rounded font-semibold"
            >
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

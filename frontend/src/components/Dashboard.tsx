import React, { useEffect, useState } from 'react';
import { getCases, updateCaseStatus } from '../services/api';
import type { IncidentCase } from '../services/api';

export const Dashboard: React.FC = () => {
  const [cases, setCases] = useState<IncidentCase[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [errorMessage, setErrorMessage] = useState<string>('');

  const fetchCases = async () => {
    setLoading(true);
    setErrorMessage('');
    try {
      // The API is the source of truth. Never substitute demo records when it
      // fails or returns an empty list.
      const data = await getCases();
      setCases(Array.isArray(data) ? data : []);
    } catch (error) {
      console.error('Failed to fetch incident cases from the backend.', error);
      setCases([]);
      setErrorMessage('Could not load live incident cases. Check the API connection and try again.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void fetchCases();
  }, []);

  const handleStatusChange = async (caseId: string, newStatus: string) => {
    setErrorMessage('');
    try {
      await updateCaseStatus(caseId, newStatus);
      await fetchCases();
    } catch (error) {
      console.error('Failed to update case status.', error);
      setErrorMessage('Could not update this incident case. The displayed status was not changed.');
    }
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 p-6">
      <header className="mb-8 border-b border-slate-800 pb-4 flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold tracking-tight text-emerald-400">Casewire SOC</h1>
          <p className="text-sm text-slate-400 mt-1">Incident Case Management & Triage Dashboard</p>
        </div>
        <button
          onClick={() => void fetchCases()}
          disabled={loading}
          className="bg-emerald-600 hover:bg-emerald-500 disabled:opacity-60 text-white px-4 py-2 rounded text-sm font-medium transition"
        >
          {loading ? 'Refreshing...' : 'Refresh Cases'}
        </button>
      </header>
      {errorMessage && (
        <div role="alert" className="mb-5 rounded border border-red-800 bg-red-950 p-4 text-red-200">
          {errorMessage}
        </div>
      )}
      <div className="bg-slate-900 border border-slate-800 rounded-lg shadow-xl overflow-hidden">
        <div className="p-4 border-b border-slate-800 font-semibold text-slate-300">Active Incident Cases</div>
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse text-sm">
            <thead>
              <tr className="bg-slate-950 text-slate-400 border-b border-slate-800">
                <th className="p-4">Title</th>
                <th className="p-4">Severity</th>
                <th className="p-4">Status</th>
                <th className="p-4">Owner</th>
                <th className="p-4">Created At</th>
                <th className="p-4">Actions</th>
              </tr>
            </thead>
            <tbody>
              {cases.map((c) => (
                <tr key={c.id} className="border-b border-slate-800 hover:bg-slate-850 transition">
                  <td className="p-4 font-medium text-slate-200">{c.title}</td>
                  <td className="p-4">
                    <span className={`px-2 py-1 rounded text-xs font-semibold ${
                      c.severity === 'CRITICAL' ? 'bg-red-950 text-red-400 border border-red-800' :
                      c.severity === 'HIGH' ? 'bg-orange-950 text-orange-400 border border-orange-800' :
                      'bg-yellow-950 text-yellow-400 border border-yellow-800'
                    }`}>
                      {c.severity}
                    </span>
                  </td>
                  <td className="p-4">
                    <span className="px-2 py-1 rounded text-xs font-medium bg-slate-800 text-slate-300 border border-slate-700">
                      {c.status}
                    </span>
                  </td>
                  <td className="p-4 text-slate-400">{c.owner || 'Unassigned'}</td>
                  <td className="p-4 text-slate-500">{new Date(c.createdAt).toLocaleString()}</td>
                  <td className="p-4 flex gap-2">
                    <button
                      onClick={() => void handleStatusChange(c.id, 'IN_PROGRESS')}
                      className="bg-blue-600 hover:bg-blue-500 text-white px-2.5 py-1 rounded text-xs transition"
                    >
                      In Progress
                    </button>
                    <button
                      onClick={() => void handleStatusChange(c.id, 'RESOLVED')}
                      className="bg-emerald-700 hover:bg-emerald-600 text-white px-2.5 py-1 rounded text-xs transition"
                    >
                      Resolve
                    </button>
                  </td>
                </tr>
              ))}
              {!loading && cases.length === 0 && (
                <tr>
                  <td colSpan={6} className="p-6 text-center text-slate-500">
                    {errorMessage ? 'Live case data is unavailable.' : 'No live incident cases found.'}
                  </td>
                </tr>
              )}
              {loading && cases.length === 0 && (
                <tr>
                  <td colSpan={6} className="p-6 text-center text-slate-500">Loading live incident cases…</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

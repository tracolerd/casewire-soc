import axios from 'axios';
const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json'
  }
});
export interface IncidentCase {
  id: string;
  title: string;
  status: 'OPEN' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED';
  severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  owner?: string;
  alertGroupId: string;
  createdAt: string;
}
export const getCases = async () => {
  const response = await api.get<IncidentCase[]>('/cases');
  return response.data;
};
export const updateCaseStatus = async (caseId: string, status: string, owner?: string) => {
  const response = await api.patch<IncidentCase>(`/cases/${caseId}`, { status, owner });
  return response.data;
};
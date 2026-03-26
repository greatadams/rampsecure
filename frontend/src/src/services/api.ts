import type { CheckoutRequest, CheckoutResponse, CheckingRequest, CheckingResponse, LoginRequest, LoginResponse, Equipment,ChecklistItem ,InspectionReportResponse ,StartInspectionRequest,SubmitInspectionRequest} from "../types/types";
//communication layer with the backend

import axios from "axios";

//sets up a reusable connection to the backend 
const api = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json'
    }
})

//grabs and attach token from localstorage 
api.interceptors.request.use((config) => {
    const token =localStorage.getItem('token')
    if(token){
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})
//a response interceptor that catches 401 errors and redirects to login
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            localStorage.clear()
            window.location.href = '/login'
        }
        return Promise.reject(error)
    }
)
export const login = (data: LoginRequest) => 
    api.post<LoginResponse>('/api/auth/login', data)

export const checkin = (data: CheckingRequest) => 
    api.post<CheckingResponse>('/api/equipment/checkin', data)


export const checkout = (data: CheckoutRequest) => 
    api.post<CheckoutResponse>('/api/equipment/checkout', data)

export const getEquipmentByStation = (station: string) =>
    api.get<Equipment[]>(`/api/equipment/allEquipmentByStation?station=${station}`)

export const startInspection = (data:StartInspectionRequest) => 
    api.post<InspectionReportResponse>(`/api/inspection/start`,data)

export const submitInspection = (data:SubmitInspectionRequest) =>
    api.post<InspectionReportResponse>(`/api/inspection/submit`,data)

export const getChecklist = (equipmentId: string) =>
    api.get<ChecklistItem[]>(`/api/inspection/checklist/${equipmentId}`)



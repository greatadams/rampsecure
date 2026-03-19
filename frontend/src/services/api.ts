import type { CheckoutRequest, CheckoutResponse, CheckingRequest, CheckingResponse, LoginRequest, LoginResponse, Equipment } from "../types/types";
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


export const login = (data: LoginRequest) => 
    api.post<LoginResponse>('/api/auth/login', data)

export const checking = (data: CheckingRequest) => 
    api.post<CheckingResponse>('/api/equipment/checking', data)


export const checkout = (data: CheckoutRequest) => 
    api.post<CheckoutResponse>('/api/equipment/checkout', data)

export const getEquipmentByStation = (station: string) =>
    api.get<Equipment[]>(`/api/equipment/allEquipmentByStation?station=${station}`)

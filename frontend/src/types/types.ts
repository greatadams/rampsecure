export type LoginRequest ={
    username:string,
    password:string,
    
}

export type LoginResponse = {
    token:string,
    username:string,
    role:string,
    station:string,
    expiryDate:Date
}

export type EquipmentStatus = 
 'AVAILABLE' |'IN_USE' |'MAINTENANCE';

export type Equipment = {
    id:string,
    equipmentCode:string,
    type:string,
    model:string,
    status:EquipmentStatus,
    station:string,
    location:string,
}


export type CheckoutRequest = {
    equipmentId:string,
}

export type CheckoutResponse = {
    transactionId: string
    equipmentCode:string
    checkoutAt:Date,
    status:EquipmentStatus

}

export type CheckingRequest = {
    equipmentId:string,
    condition: string,
    notes:string
}

export type CheckingResponse = {
    transactionId:string,
    equipmentCode:string,
    checkingAt:Date, 
    durationMinutes:number,
    condition:string
}

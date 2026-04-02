export type LoginRequest ={
    username:string,
    password:string,
    
}

export type LoginResponse = {
    id:string,
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
    equipmentType: string
    type:string,
    model:string,
    status:EquipmentStatus,
    station:string,
    location:string,
    currentOperatorId: {
    id: string
    username: string
    firstName: string
    lastName: string | null,
    }
     lastCheckoutAt: string | null,
  

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


export type ChecklistItem = {
    id: string,
    itemName: string,
    description: string,
    isCritical: boolean,
    template: string
}

export type StartInspectionRequest = {
    equipmentId:string
}

export type SubmitInspectionRequest ={
    reportId:string,
    results:ItemResultRequest[]
}

export type ItemResultRequest = {
    checkListItemId: string,
    resultStatus: string,
    notes:string,
}

export type InspectionReportResponse  ={
     reportId:string,
     checklistStatus:string,
     equipmentCode:string,
     inspectedAt:Date,
     expireAt:Date,
     hasCriticalFailure:boolean
}

export type RegisterRequest = {
    username: string
    firstName: string
    lastName: string
    email: string
    phoneNumber: string
    password: string
    confirmPassword: string
    role: string
    station: string
}


export type UserResponse = {
    id:string
    firstName: string
    lastName: string
    username: string  
    email: string
    station:string
    role:string
}

export type UpdateUserRequest = {
    role: string
    station: string
}
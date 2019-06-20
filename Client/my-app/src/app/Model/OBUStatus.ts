export class OBUStatus{
    id: number
    obuId: number
    statusDate: Date
    latitude: number
    longitude: number
    speed: number
    usableStorage: number
    freeStorage: number
    criticalAlarms: number
    majorAlarms: number
    warningAlarms: number
    temperature: number
    networkInterfaces

    //O que vem da API
    date: String
    location
    storage
    alarms
}
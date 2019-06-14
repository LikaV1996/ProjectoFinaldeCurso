export class OBUStatus{
    id: number
    obuId: number
    statusDate: Date
    latitude: number
    longitude: number
    speed: number
    properties//location_properties
    usableStorage: number
    freeStorage: number
    criticalAlarms: number
    majorAlarms: number
    warningAlarms: number
    temperature: number
    networkInterfaces

    type: String
    coordinates
}
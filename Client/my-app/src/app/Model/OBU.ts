export class OBU{
    id: number
    hardwareId: number
    obuState: string
    currentConfigId: number
    currentTestPlanId: number
    factoryConfig: number
    obuName: string
    obuPassword: string
    //properties: string
    sims
    uploadRequest: boolean
    clearAlarmsRequest: boolean
    resetRequest: boolean
    shutdownRequest: boolean
    creator: string
    creation_date: Date
    modified: string
	modified_date: Date
}
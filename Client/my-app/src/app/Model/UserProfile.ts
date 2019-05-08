export enum UserProfile{
    NORMAL_USER = 0,
    SUPER_USER = 1,
    ADMIN = 2
}

export namespace UserProfile{
    export function getValueFromString(profile : string) : number{
        //return 
        //console.log("In UserProfile, profile is: " + UserProfile[profile])
        switch(profile){
            case "NORMAL_USER": return UserProfile.NORMAL_USER
            case "SUPER_USER": return UserProfile.SUPER_USER
            case "ADMIN": return UserProfile.ADMIN
            default: throw "UserProfile doesn't exist"
        }
    }
}

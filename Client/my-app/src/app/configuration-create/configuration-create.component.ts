import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

@Component({
  selector: 'app-configuration-create',
  templateUrl: './configuration-create.component.html',
  styleUrls: ['./configuration-create.component.css']
})
export class ConfigurationCreateComponent implements OnInit {


  private configName: string;
  private activationDate: Date;
  
  properties = {
    archive:{
      expiration: "",
      period: "",
      referenceDate: ""
    },
    controlConnection:{
      referenceDate: "",
      period: "",
      retryDelay: "",
      maxRetries: ""
    },
    core:{

    },
    data:{

    },
    download:{

    },
    scanning:{

    },
    server:{
      
    },
    testPlan:{
      
    },
    upload:{

    },
    voice:{

    }
  }; 

  constructor(
    private _location: Location
  ) { }

  ngOnInit() {
    
  }

  goBack(){
    this._location.back();
  }

  createConfig(){
    alert('Doing nothing yet!')
    alert(JSON.stringify(this.properties))
  }

}

export class GraphicUtils{

    constructor(

    ) { }

    private colorNum = 4
    private currentColor = this.colorNum
    private colorsCode = ["#3e95cd","#8e5ea2","#3cba9f","#c45850","#e8c3b9"]
    private newCharPoints = new Array()
/*
    chartPoints = [{  
        x: this.positions[0].obuStatus[0].date,//this.tomorrow,//new Date(),  
        y: this.positions[0].obuStatus[0].location.groundSpeed
        },{  
        x: this.positions[0].obuStatus[1].date,//this.tomorrow,//new Date().setMinutes(),  
        y: this.positions[0].obuStatus[1].location.groundSpeed
      }];
*/
    createDataSet(position, optionSelected){
        var dataset = new Dataset()
        dataset.label = position.obuName
        dataset.borderColor = this.colorsCode[this.currentColor]
        this.currentColor--
        if(this.currentColor < 0) this.currentColor = this.colorNum
        dataset.data =  this.createChartPoints(position.obuStatus, optionSelected) 
        return dataset
    }

    createChartPoints(obustatus, optionSelected){
        var chartPoints : ChartPoint[] = new Array()

        obustatus.forEach( obuStat => {
            var newCharPoint = new ChartPoint()
            newCharPoint.x = obuStat.date//new Date()
            switch(optionSelected) { 
                case "groundSpeed": { 
                    newCharPoint.y = obuStat.location.groundSpeed
                    break; 
                } 
                case "criticalAlarms": { 
                    newCharPoint.y = obuStat.alarms.critical
                    break 
                 }
                 case "majorAlarms": { 
                    newCharPoint.y = obuStat.alarms.major
                    break 
                 } 
                 case "warningAlarms": { 
                    newCharPoint.y = obuStat.alarms.warning
                    break 
                 } 
                 case "temperature": { 
                    newCharPoint.y = obuStat.temperature
                    break 
                 }
                 case "freeStorage": { 
                    newCharPoint.y = obuStat.storage.free
                    break 
                 }
                 case "usableStorage": { 
                    newCharPoint.y = obuStat.storage.usable
                    break 
                 } 
                default: { 
                    return null
                } 
             } 
            //newCharPoint.y = obuStat.location.groundSpeed//10

            chartPoints.push(newCharPoint)
        });

        /*
        var newCharPoint = new ChartPoint()
        newCharPoint.x = obustatus[0].date//new Date()
        newCharPoint.y = obustatus[0].location.groundSpeed//10
        chartPoints.push(newCharPoint)

        newCharPoint = new ChartPoint()
        newCharPoint.x = obustatus[1].date
        newCharPoint.y = obustatus[1].location.groundSpeed
        chartPoints.push(newCharPoint)
        */
        return chartPoints
    }

    getAxisYLabel(option: string){
        switch(option) { 
            case "groundSpeed": { 
               return "Ground Speed( Km/h )"
            } 
            case "criticalAlarms": { 
                return "Critical Alarms ( Number )" 
             } 
             case "majorAlarms": { 
                return "Major Alarms ( Number )"
             } 
             case "warningAlarms": { 
                return "Warning Alarms ( Number )"
             }
             case "temperature": { 
                return "Temperature ( °C )"
             }
             case "freeStorage": { 
                return "Free Storage ( Bytes )"
             }
             case "usableStorage": { 
                return "Usable Storage ( Bytes )"
             } 
            default: { 
               return null 
            } 
         }
         
                   
        
        
    }


}


/*
//GRAFICO
      this.chart = new Chart(this.chartRef.nativeElement, {
        type: 'line',
        data: {
          //labels: ["wow"], // your labels array
          datasets: [
            {
              label: "Blue!",
              //data: this.chartPoints, // Azul
              borderColor: '#00AEFF',
              fill: false
            },
            {
              label: "Red!",
              //data: this.chartPoints, // Vermelho
              borderColor: '#C45850',
              fill: false
            }

          ]
        },
        options: {
          responsive:true,
          maintainAspectRatio: false,
          legend: {
            display: true
          },
          scales: {
            xAxes: [{
              display: true,
              type: 'time',
              //
                time: { //nao faz nada
                  displayFormats: {
                    quarter: 'MMM D'
                  }
                }
                //
            }],
            yAxes: [{
              display: true
            }],
          }
        }
      });
*/

/*
chartPoints = [{  
    x: this.tomorrow,//this.positions[0].obuStatus[0].date,//new Date(),  
    y: 10
    },{  
    x: this.tomorrow,//new Date().setMinutes(),  
    y: 20
  }];
*/

class Dataset{ //Classe auxiliar
    label : string
    data
    borderColor : string
    fill : boolean = false
}


class ChartPoint{ //Classe auxiliar
    x
    y
}


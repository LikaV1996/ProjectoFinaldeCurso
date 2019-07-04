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
    createDataSet(obuName){
        var dataset = new Dataset()
        dataset.label = obuName
        dataset.borderColor = this.colorsCode[this.currentColor]
        this.currentColor--
        if(this.currentColor < 0) this.currentColor = this.colorNum
        dataset.data =  this.createChartPoints()//TODO FALTA charPoints[] 
        return dataset
    }

    createChartPoints(){
        var chartPoints : ChartPoint[] = new Array()
        var newCharPoint = new ChartPoint()
        newCharPoint.x = new Date()
        newCharPoint.y = 10
        chartPoints.push(newCharPoint)
        newCharPoint = new ChartPoint()
        newCharPoint.x = new Date()
        newCharPoint.y = 20
        chartPoints.push(newCharPoint)
        
        return chartPoints
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


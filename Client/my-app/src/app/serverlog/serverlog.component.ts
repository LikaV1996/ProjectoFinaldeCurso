import { Component, OnInit, Input } from '@angular/core';
import { ServerLogService } from '../_services/serverlog.service';
import { ServerLog } from '../Model/ServerLog';

@Component({
  selector: 'app-serverlog',
  templateUrl: './serverlog.component.html',
  styleUrls: ['./serverlog.component.css']
})
export class ServerLogComponent implements OnInit {

  constructor(
    private _serverLogService : ServerLogService
  ) { }

  @Input() private dateOrder : string = "true"
  @Input() private accessType : string = ""
  @Input() private accessor : string = ""

  private curPage : number = 1
  private totalNumberOfPages : number = this.curPage
  private pageLimit : number = 20

  private serverLogs : ServerLog[]

  ngOnInit() {
    this.getServerLogs(this.curPage) //current page = 1
  }

  getServerLogs(pageNumber : number){
    this.curPage = pageNumber
    let order : boolean = this.dateOrder && this.dateOrder == "true" ? true : false

    //debugger
    this.serverLogs = null;
    this._serverLogService.getServerLogs(order, this.curPage, this.pageLimit, this.accessor, this.accessType).subscribe(
      ServerLogObj => {
        this.serverLogs = ServerLogObj.serverLogList
        this.updateNumberOfPages(ServerLogObj.fullCount)
        
      }
    )
  }

  applyFilter(){
    this.getServerLogs(this.curPage)
  }
  
  updateNumberOfPages(fullCount : number){
    /*
    const incompletePage = (fullCount % this.pageLimit > 0) ? 1 : 0
    this.totalNumberOfPages = fullCount / this.pageLimit + incompletePage +1
    */
    this.totalNumberOfPages = Math.ceil(fullCount / this.pageLimit)
  }


  getPage(pageToGo : number){
    if(pageToGo < 1 || pageToGo > this.totalNumberOfPages)
      return;
    
    this.getServerLogs(pageToGo)
  }

  isFirstPage() : boolean {
    return this.curPage <= 1
  }

  isLastPage() : boolean {
    return this.curPage >= this.totalNumberOfPages
  }

}

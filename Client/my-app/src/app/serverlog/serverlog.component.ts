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


  private curPage : number = 1
  private totalNumberOfPages : number = this.curPage
  private pageLimit : number = 20

  @Input() private dateOrder : string = "false"
  @Input() private accessType : string = ""
  @Input() private accessor : string = ""
  @Input() private inputPage : string = this.curPage.toString()


  private serverLogs : ServerLog[]

  ngOnInit() {
    this.getServerLogs(this.curPage) //current page = 1
  }

  getServerLogs(pageNumber : number){
    this.curPage = pageNumber
    this.inputPage = pageNumber.toString()
    let order : boolean = this.dateOrder && this.dateOrder == "true" ? true : false
    this.pageLimit = parseInt(this.pageLimit.toString())
    //debugger
    this.serverLogs = null;
    this._serverLogService.getServerLogs(order, this.curPage, this.pageLimit, this.accessor, this.accessType).subscribe(
      ServerLogResp => {
        this.serverLogs = ServerLogResp.serverLogList
        this.updateNumberOfPages(ServerLogResp.fullCount)
        
      }
    )
  }

  applyFilter(){
    this.getServerLogs( 1 ) //page 1
  }
  
  updateNumberOfPages(fullCount : number){
    this.totalNumberOfPages = Math.ceil(fullCount / this.pageLimit)
  }


  getPage(pageToGo : number){
    if(pageToGo != this.curPage) {
      pageToGo = parseInt(pageToGo.toString())
      if(pageToGo < 1 || pageToGo > this.totalNumberOfPages){
        return;
      }
      
      this.getServerLogs(pageToGo)
    }
  }

  isFirstPage() : boolean {
    return this.curPage <= 1
  }

  isLastPage() : boolean {
    return this.curPage >= this.totalNumberOfPages
  }

}

import { Component, OnInit } from '@angular/core';
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

  private order : boolean
  private accessType : string
  private user : string

  private curPage : number
  private totalNumberOfPages : number
  private pageLimit : number = 20

  private serverLogs : ServerLog[]

  ngOnInit() {
    this.getServerLogs(1)
  }

  getServerLogs(pageNumber : number){
    this.curPage = pageNumber

    this.serverLogs = null;
    this._serverLogService.getServerLogs(null, this.curPage, this.pageLimit, null, null).subscribe(
      ServerLogObj => {
        this.serverLogs = ServerLogObj.serverLogList
        this.updateNumberOfPages(ServerLogObj.fullCount)
        
      }
    )
  }

  
  updateNumberOfPages(fullCount : number){
    const incompletePage = (fullCount % this.pageLimit > 0) ? 1 : 0
    this.totalNumberOfPages = fullCount / this.pageLimit + incompletePage
  }


  getPage(pageToGo : number){
    if(pageToGo < 1 || pageToGo > this.totalNumberOfPages)
      return;
    
    this.getServerLogs(pageToGo)
  }

  isFirstPage() : boolean {
    return this.curPage == 1
  }

  isLastPage() : boolean {
    return this.curPage == this.totalNumberOfPages
  }

}

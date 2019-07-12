import { Component, OnInit, Input } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import { ObuLogService } from '../_services/obulogs.service';
import { SysLog } from '../Model/SysLog';

@Component({
  selector: 'app-systemlog',
  templateUrl: './systemlog.component.html',
  styleUrls: ['./systemlog.component.css']
})
export class SystemLogComponent implements OnInit {
  constructor(
    private _obuLogService : ObuLogService,
    private route: ActivatedRoute,
  ) { }

  private obuID: number

  private curPage : number = 1
  private totalNumberOfPages : number = this.curPage
  private pageLimit : number = 20

  @Input() private dateOrder : string = "false"
  @Input() private filename : string = ""
  @Input() private inputPage : string = this.curPage.toString()

  private sysLogs : SysLog[]

  ngOnInit() {
    this.obuID = this.route.snapshot.params['id'];

    this.getSysLogs(this.curPage) //current page = 1
  }

  getSysLogs(pageNumber : number){
    this.curPage = pageNumber
    this.inputPage = pageNumber.toString()
    let order : boolean = this.dateOrder && this.dateOrder === "true" ? true : false

    this.sysLogs = null;
    this._obuLogService.getSysLogs(this.obuID, order, this.curPage, this.pageLimit, this.filename).subscribe(
      SysLogResp => {
        this.sysLogs = SysLogResp.sysLogList
        this.updateNumberOfPages(SysLogResp.fullCount)

      }
    )
  }

  applyFilter(){
    this.getSysLogs( 1 ) //page 1
  }

  updateNumberOfPages(fullCount : number){
    this.totalNumberOfPages = Math.ceil(fullCount / this.pageLimit)
  }

  
  getPage(pageToGo : number){
    if(pageToGo < 1 || pageToGo > this.totalNumberOfPages)
      return;
    
    this.getSysLogs(pageToGo)
  }

  isFirstPage() : boolean {
    return this.curPage <= 1
  }

  isLastPage() : boolean {
    return this.curPage >= this.totalNumberOfPages
  }

}

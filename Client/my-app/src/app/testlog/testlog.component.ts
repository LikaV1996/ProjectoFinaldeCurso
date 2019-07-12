import { Component, OnInit, Input } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import { ObuLogService } from '../_services/obulogs.service';
import { TestLog } from '../Model/TestLog';

@Component({
  selector: 'app-testlog',
  templateUrl: './testlog.component.html',
  styleUrls: ['./testlog.component.css']
})
export class TestLogComponent implements OnInit {

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


  private testLogs : TestLog[]

  ngOnInit() {
    this.obuID = this.route.snapshot.params['id'];

    this.getTestLogs(this.curPage) //current page = 1
  }

  getTestLogs(pageNumber : number){
    this.curPage = pageNumber
    this.inputPage = pageNumber.toString()
    let order : boolean = this.dateOrder && this.dateOrder === "true" ? true : false

    this.testLogs = null;
    this._obuLogService.getTestLogs(this.obuID, order, this.curPage, this.pageLimit, this.filename).subscribe(
      TestLogResp => {
        this.testLogs = TestLogResp.testLogList
        this.updateNumberOfPages(TestLogResp.fullCount)

        /*
        this.testLogs.forEach( log => {
          if( ! log.data) log.data = "This log as no data"
        })
        */
        
      }
    )
  }

  applyFilter(){
    this.getTestLogs( 1 ) //page 1
  }

  updateNumberOfPages(fullCount : number){
    this.totalNumberOfPages = Math.ceil(fullCount / this.pageLimit)
  }

  
  getPage(pageToGo : number){
    if(pageToGo < 1 || pageToGo > this.totalNumberOfPages)
      return;
    
    this.getTestLogs(pageToGo)
  }

  isFirstPage() : boolean {
    return this.curPage <= 1
  }

  isLastPage() : boolean {
    return this.curPage >= this.totalNumberOfPages
  }

}

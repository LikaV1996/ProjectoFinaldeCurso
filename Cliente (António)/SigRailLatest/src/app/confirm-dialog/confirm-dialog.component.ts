import { Component, OnInit, OnChanges, Input, Inject } from '@angular/core';
import { MatSnackBar, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.css']
})
export class ConfirmDialogComponent implements OnInit {
  title: string;
  text: string;
  constructor( public dialogRef: MatDialogRef<ConfirmDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data) {
  }

  ngOnInit() {
    this.title = this.data[0];
    this.text = this.data[1];
  }

  ok() {
    this.dialogRef.close('OK');
  }

  cancel() {
    this.dialogRef.close();
  }
}

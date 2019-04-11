import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { BaseLayer } from '../model/project';

@Component({
  selector: 'app-layerlistdialog',
  templateUrl: './layerlistdialog.component.html',
  styleUrls: ['./layerlistdialog.component.css']
})
export class LayerlistdialogComponent implements OnInit {
  selectedLayer: BaseLayer;
constructor(public dialogRef: MatDialogRef<LayerlistdialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data) {
  }

  ngOnInit() {
  }

  select(layer: BaseLayer) {
    this.selectedLayer = layer;
  }

  open(layer: BaseLayer) {
    this.dialogRef.close(layer);
  }

  cancel() {
    this.dialogRef.close();
  }

}

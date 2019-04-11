import { TileLayer, Icon, DivIcon, FeatureGroup, Layer } from 'leaflet';
import { ProjectService } from '../project.service';

export class Deflate extends FeatureGroup {
  constructor(options?) {
    super(options);
  }

  addLayer(layer: Layer) {
//    console.log('add layer: ' + this.getLayers().length);
    return super.addLayer(layer);
    /*var layers = layer instanceof L.FeatureGroup ? Object.getOwnPropertyNames(layer._layers) : [];
    var i = 0;
    var len = layers.length;
    if (layers.length) {
      for (i = 0, len = layers.length; i < len; i += 1) {
        this.addLayer(layer._layers[layers[i]]);
      }
    } else {
      if (this._map) {
        this.prepLayer(layer);
        this._addToMap(layer);
      } else {
        this._needsPrepping.push(layer);
      }
      this._layers[this.getLayerId(layer)] = layer;
    }*/
}
}

// TileLayer with custom http headers
export class TileLayerH extends TileLayer {
  constructor(private pService: ProjectService, url, options) {
    super(url, options);
  }
  createTile(coords, done) {
    const url = super.getTileUrl(coords);
    const img = document.createElement('img');
    this.pService.getClutterImg(url).subscribe(
    image => {
      if (image.size === 0) {// Transparent pixel
        img.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
      } else {
        img.src = URL.createObjectURL(image);
      }
      done(null, img);
    });
    return img;
  }
  addTo(map) {
    return super.addTo(map);
  }
}

// Custom Marker icon
export class IconABC extends DivIcon {
  options: any;
  defaultOptions = {
    className: 'fa-marker',
    iconSize: [48, 48],
    iconAnchor:  [0, 0],
    shadowAnchor: [0, 0],
    popupAnchor: [0, 0],
    tooltipAnchor: [16, -32],
    shadowSize: [0, 0]
  };
  innerIcon = 'fa-train';

  constructor(options?) {
    super(options);
    if (options && 'innerIcon' in options) {
      this.innerIcon = options.innerIcon;
    }
    this.options = {...this.defaultOptions, ...options};
  }

  createIcon() {
    const div = document.createElement('div');
    div.setAttribute('style', // 'border: 1px solid yellow; background-color: blue;' +
      'width: 48px; height: 48px; position: absolute;' +
      'margin-left: -24px; margin-top: -48px');
    const innerDiv = document.createElement('div'); // Marker (with hole)
    innerDiv.setAttribute('style',
      'display: block; position: absolute; ' +
//      ' top: -48px; left: -24px;' +
//      ' margin-top: -48px; margin-left: -24px;' +
      ' width: 48px; height: 48px; text-align: center');
    div.appendChild(innerDiv);
    if (this.options.selected) {
      const matIconBg = document.createElement('mat.icon');
      matIconBg.classList.add('mat-icon', 'fa', 'fa-map-marker');
      matIconBg.setAttribute('style',
        ' font-size: 52px; line-height: 48px; width: 48px; height: 48px; position: absolute;' +
        ' color: #888; z-index: -1');
      innerDiv.appendChild(matIconBg);
    }
    const matIcon = document.createElement('mat.icon');
    matIcon.classList.add('mat-icon', 'fa', 'fa-map-marker');
    matIcon.setAttribute('style',
      ' font-size: 48px; line-height: 48px; width: 48px; height: 48px;' +
      ' margin-top: 0; vertical-align: center; color: ' + this.options.color || '#FF4444');
    innerDiv.appendChild(matIcon);

    const innerDiv2 = document.createElement('div'); // White circle (covering hole)
    div.appendChild(innerDiv2);
    innerDiv2.setAttribute('style',
      'display: block; position: absolute; ' +
      ' top: 4px; left: 12px;' +
      ' width: 24px; height: 24px; text-align: center');
    const matIcon2 = document.createElement('mat.icon');
    matIcon2.classList.add('mat-icon', 'fa', 'fa-circle');
    matIcon2.setAttribute('style',
      ' font-size: 24px; line-height: 24px; width: 24px; height: 24px;' +
      ' margin: 0; vertical-align: center; color: #FFFFFF');
    innerDiv2.appendChild(matIcon2);

    const innerDiv3 = document.createElement('div'); // Inner icon
    div.appendChild(innerDiv3);
    innerDiv3.setAttribute('style',
      'display: block; position: absolute; ' +
      ' top: 8px; left: 16px;' +
      ' width: 16px; height: 16px; text-align: center');
    const matIcon3 = document.createElement('mat.icon');
    matIcon3.classList.add('mat-icon', 'fa', this.innerIcon);
    matIcon3.setAttribute('style',
      ' font-size: 16px; line-height: 16px; width: 16px; height: 16px;' +
      ' margin: 0; vertical-align: center; color: ' + this.options.color || '#FF4444');
    innerDiv3.appendChild(matIcon3);

    return div;
  }

  createIcon_fa() {
    const div = document.createElement('div');
    div.setAttribute('style', 'margin: 0; padding:0; border: 1px solid yellow');
//    <fa-icon [icon]="['fas', 'map-marker']"></fa-icon>
    const faIcon = document.createElement('fa-icon');

    // const txt = document.createTextNode('place');
    faIcon.classList.add('fas', 'fa-map-marker');
//    faIcon.setAttribute('style', 'margin: -48px 0; padding: 0; font-size: 48px; color: #459fa5');
    div.appendChild(faIcon);
    return div;
  }
}

import {
    Feature, FeatureCollection, Point,
} from 'geojson';

export class DBObj {
  id: number;
  created?: Date;
  creator?: string;
  modified?: Date;
  modifier?: string;
  href?: string;
}

export class PublishableObj extends DBObj {
  published: Date;
  publisher: string;
}

export class CoordinateSystem {
  wkid: number;
  name: string;
}

export class Project extends PublishableObj {
  state?: string;
  coordinateSystem: number;
  name: string;
  comment: string;
  properties: any;

  railwaysHref?: string;
  railwayInfrastructuresHref?: string;
  buildingGroupsHref?: string;
  cluttersHref?: string;
}

export class RailwayProperties {
  visible?: boolean;
  color?: string;
  opacity?: number;
  weight?: number;
}

export class BaseLayer  extends DBObj {
  name: string;
  coordinateSystem: number;
  state: string;
  properties: any; // RailwayProperties | string;
  error?: string;
}

export class Railway extends BaseLayer {
  railwayPoints: FeatureCollection<Point>;
}

export class BuildingPerimeter {
  type: string;
  coordinates: number[][][];
}

export class Building extends DBObj {
  name: string;
  elevation: number;
  perimeter: BuildingPerimeter; // geometry de uma Feature<Polygon>;
  properties: any;
}

export class BuildingGroup extends DBObj {
  name: string;
  coordinateSystem: number;
  buildingsHref: string;
  state: string;
  properties: any;
}

export class RailwayInfrastructure extends BaseLayer {
//  elements: FeatureCollection<Point>;
  railwayElementsHref: string;
  tunnelsHref: string;
}

export class RailwayElementClass {
  id: number;
  name: string;
}

export class RailwayElement extends DBObj {
  name: string;
//  railwayElementClass: string;
  class: string;
//  coordinateSystem: number;
  location: Feature<Point>;
  properties: any;
}

export class Tunnel extends DBObj {
  name: string;
  startLocation: Feature<Point>;
  endLocation: Feature<Point>;
  properties: any;
}

export class RGBAColor {
  red: number;
  green: number;
  blue: number;
  alpha: number;
}

export class ClutterClass {
  pixelValue: number;
  description: string;
  color: RGBAColor;
}

// export class BoundingBox {
//   upRightCorner: Point;
//   downLeftCorner: Point;
// }

export class BoundingBox {
    bbox: number[];
}

export class Clutter extends BaseLayer {
  boundingBox: BoundingBox;
  clutterTilesHref: string;
  classes: ClutterClass[];
}

export class User extends DBObj {
  username: string;
  mail: string;
  name: string;
  picture: string;
  role: string;
}

export class PropagationEnvironment extends DBObj {
  name: string;
  boundingBox: BoundingBox;
  resolution: number;
}


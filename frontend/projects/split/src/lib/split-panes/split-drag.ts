export class SplitDrag {
  constructor(
    public pixelPosition: number,
    public beforeIndex: number,
    public afterIndex: number,
    public beforePercentSize: number,
    public afterPercentSize: number,
    public beforePixelSize: number,
    public afterPixelSize: number,
  ) {
  }
}

export interface SplitDirectionService {
  fxLayout: string;

  eventToSize($event: MouseEvent): number;

  divToSize(nativeElement: any): number;
}

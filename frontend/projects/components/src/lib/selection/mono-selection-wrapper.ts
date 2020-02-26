import {SelectionModel} from '@angular/cdk/collections';

export class MonoSelectionWrapper<T> {

  readonly model: SelectionModel<T> = new SelectionModel(false);

  constructor(private matcher: (t1: T, t2: T) => boolean) {
  }

  public isSelected(t: T): boolean {
    return this.hasSelection && this.matcher(this.selection, t);
  }

  public get hasSelection(): boolean {
    return this.model.hasValue();
  }

  public set selection(t: T) {
    if (t) {
      this.model.select(t);
    } else {
      this.model.clear();
    }
  }

  public get selection(): T | null {
    return this.hasSelection ? this.model.selected[0] : null;
  }
}

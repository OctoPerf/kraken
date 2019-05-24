import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {WorkspaceComponent} from 'projects/gatling/src/app/workspace/workspace/workspace.component';

const routes: Routes = [
  {
    path: '',
    component: WorkspaceComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class WorkspaceRoutingModule {
}

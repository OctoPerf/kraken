import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./workspace/workspace.module').then(m => m.WorkspaceModule),
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
      // onSameUrlNavigation: 'reload',
      enableTracing: false,
    }
  )],
  exports: [RouterModule],
})
export class AppRoutingModule {
}

import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SecurityGuard} from 'projects/security/src/lib/security.guard';
import {StorageResolverService} from 'projects/storage/src/lib/storage-resolver.service';
import {ProjectIdGuard} from 'projects/commons/src/lib/config/project-id.guard';
import {CurrentProjectResolverService} from 'projects/commons/src/lib/current-project/current-project-resolver.service';

const routes: Routes = [
  {
    path: ':projectId',
    canActivate: [SecurityGuard, ProjectIdGuard],
    canLoad: [SecurityGuard],
    resolve: {
      nodes: StorageResolverService,
      currentProject: CurrentProjectResolverService
    },
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

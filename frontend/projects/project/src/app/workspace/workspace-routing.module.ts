import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {WelcomeComponent} from 'projects/project/src/app/project/welcome/welcome.component';
import {CreateProjectComponent} from 'projects/project/src/app/project/create-project/create-project.component';
import {WorkspaceComponent} from 'projects/project/src/app/workspace/workspace/workspace.component';
import {ProjectListResolverService} from 'projects/project/src/app/project/project-list-resolver.service';
import {SecurityGuard} from 'projects/security/src/lib/security.guard';

const routes: Routes = [
  {
    path: '',
    component: WorkspaceComponent,
    canActivate: [SecurityGuard],
    canLoad: [SecurityGuard],
    resolve: {
      projects: ProjectListResolverService
    },
    children: [
      {
        path: '',
        component: WelcomeComponent,
      },
      {
        path: 'create',
        component: CreateProjectComponent,
      },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class WorkspaceRoutingModule {
}

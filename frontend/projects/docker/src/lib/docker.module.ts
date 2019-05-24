import {ModuleWithProviders, NgModule} from '@angular/core';
import {DockerClientModule} from 'projects/docker/src/lib/docker-client/docker-client.module';
import {DockerComposeModule} from 'projects/docker/src/lib/docker-compose/docker-compose.module';
import {DOCKER_ID} from 'projects/docker/src/lib/docker-id';

@NgModule({
  imports: [
    DockerClientModule,
    DockerComposeModule,
  ],
  exports: [
    DockerClientModule,
    DockerComposeModule,
  ],
})
export class DockerModule {
  public static forRoot(id: string): ModuleWithProviders {
    return {
      ngModule: DockerModule,
      providers: [
        {
          provide: DOCKER_ID,
          useValue: id
        },
      ]
    };
  }
}

import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {DockerService} from 'projects/docker/src/lib/docker-client/docker.service';
import {DockerImage} from 'projects/docker/src/lib/entities/docker-image';
import {MatSort, MatTableDataSource} from '@angular/material';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {Subscription} from 'rxjs';
import {DELETE_ICON, INSPECT_ICON, REFRESH_ICON} from 'projects/icon/src/lib/icons';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faDownload} from '@fortawesome/free-solid-svg-icons/faDownload';
import {library} from '@fortawesome/fontawesome-svg-core';
import {ImageNameDialogComponent} from 'projects/docker/src/lib/docker-dialogs/image-name-dialog/image-name-dialog.component';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {JsonPipe} from '@angular/common';

library.add(faDownload);

@Component({
  selector: 'lib-images-table',
  templateUrl: './images-table.component.html',
  styleUrls: ['./images-table.component.scss']
})
export class ImagesTableComponent implements OnInit, OnDestroy {

  readonly pullIcon = new IconFa(faDownload, 'accent');
  readonly deleteIcon = DELETE_ICON;
  readonly refreshIcon = REFRESH_ICON;
  readonly fullIcon = INSPECT_ICON;
  readonly displayedColumns: string[] = ['name', 'tag', 'created', 'size', 'full', 'rmi'];
  loading = true;
  dataSource: MatTableDataSource<DockerImage>;

  @ViewChild(MatSort) sort: MatSort;

  private subscription: Subscription;

  constructor(private dockerService: DockerService,
              private dialogs: DialogService,
              private json: JsonPipe) {
    this.dataSource = new MatTableDataSource([]);
    this.subscription = this.dockerService.imagesSubject.subscribe((images) => this.images = images);
  }

  ngOnInit() {
    this.refresh();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  refresh() {
    this.loading = true;
    this.dockerService.images().subscribe();
  }

  full(image: DockerImage) {
    this.dialogs.inspect('Docker Image', this.json.transform(image.full), 'ADMIN_INSPECT_IMAGE');
  }

  rmi(image: DockerImage) {
    this.dialogs.delete('Docker Image', [`${image.name}:${image.tag}`], 'ADMIN_DELETE_IMAGE').subscribe(() => {
      this.dockerService.rmi(image).subscribe();
    });
  }

  pullImage() {
    this.dialogs.open(ImageNameDialogComponent, DialogSize.SIZE_MD).subscribe((name: string) => {
      this.dockerService.pull(name).subscribe();
    });
  }

  set images(images: DockerImage[]) {
    this.dataSource = new MatTableDataSource(images);
    this.dataSource.sort = this.sort;
    this.loading = false;
  }
}

import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {Host} from 'projects/runtime/src/lib/entities/host';
import {MatSort} from '@angular/material/sort';
import {Subscription} from 'rxjs';
import {REFRESH_ICON, RENAME_ICON} from 'projects/icon/src/lib/icons';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {AttachHostDialogComponent} from 'projects/runtime/src/lib/runtime-host/runtime-host-dialogs/attach-host-dialog/attach-host-dialog.component';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faLink} from '@fortawesome/free-solid-svg-icons/faLink';
import {library} from '@fortawesome/fontawesome-svg-core';
import {faUnlink} from '@fortawesome/free-solid-svg-icons/faUnlink';
import {KeyBinding, KeyBindingsService} from 'projects/tools/src/lib/key-bindings.service';
import {MonoSelectionWrapper} from 'projects/components/src/lib/selection/mono-selection-wrapper';

library.add(faLink, faUnlink);

@Component({
  selector: 'lib-hosts-table',
  templateUrl: './hosts-table.component.html',
  styleUrls: ['./hosts-table.component.scss']
})
export class HostsTableComponent implements OnInit, OnDestroy {

  readonly refreshIcon = REFRESH_ICON;
  readonly setIdIcon = RENAME_ICON;
  readonly attachIcon = new IconFa(faLink);
  readonly detachIcon = new IconFa(faUnlink);
  readonly displayedColumns: string[] = ['id', 'name', 'addresses', 'cpu', 'memory', 'owner', 'buttons'];
  readonly _selection: MonoSelectionWrapper<Host> = new MonoSelectionWrapper<Host>(this._match.bind(this));

  private keyBindings: KeyBinding[] = [];

  loading = true;
  dataSource: MatTableDataSource<Host>;

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  private subscription: Subscription;

  constructor(private dialogs: DialogService,
              private hostService: RuntimeHostService,
              private keys: KeyBindingsService) {
    this.dataSource = new MatTableDataSource([]);
    this.subscription = this.hostService.allSubject.subscribe((hosts) => this.hosts = hosts);
    this.keyBindings.push(new KeyBinding(['Enter'], this._onEnter.bind(this), 'hosts'));
    this.keyBindings.forEach(binding => {
      this.keys.add([binding]);
    });
  }

  ngOnInit() {
    this.refresh();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
    this.keyBindings.forEach(binding => this.keys.remove([binding]));
  }

  public refresh() {
    this.loading = true;
    this.hostService.all().subscribe();
  }

  public set hosts(hosts: Host[]) {
    this.dataSource = new MatTableDataSource(hosts);
    this.dataSource.sort = this.sort;
    this.loading = false;
  }

  public attach(host: Host) {
    this.dialogs.open(AttachHostDialogComponent, DialogSize.SIZE_MD, {
      title: `Attach Host '${host.name}'`,
      host,
    })
      .subscribe(attached => {
        this.hostService.attach(host, attached).subscribe();
      });
  }

  public detach(host: Host, force: boolean) {
    this.dialogs.confirm('Detach Host', 'This host will not be usable to execute tasks on Kraken. Are you sure?', force)
      .subscribe(() => {
        this.hostService.detach(host).subscribe();
      });
  }

  _onEnter($event: KeyboardEvent): boolean {
    if (this._selection.hasSelection) {
      const host = this._selection.selection;
      this.attach(host);
      return true;
    }
    return false;
  }

  _match(host1: Host, host2: Host): boolean {
    return host1.name === host2.name;
  }
}
